package com.cnhindustrial.telemetry;

import com.cnhindustrial.telemetry.factory.GeoMesaCassandraDataStoreFactory;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.api.java.typeutils.runtime.kryo.KryoSerializer;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.metrics.Counter;
import org.apache.flink.runtime.state.FunctionInitializationContext;
import org.apache.flink.runtime.state.FunctionSnapshotContext;
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.api.operators.StreamingRuntimeContext;
import org.apache.flink.streaming.runtime.tasks.ProcessingTimeCallback;
import org.apache.flink.streaming.runtime.tasks.ProcessingTimeService;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;

import org.locationtech.geomesa.index.geotools.GeoMesaFeatureStore;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeoMesaBufferedSink extends RichSinkFunction<GeomesaFeature> implements CheckpointedFunction, ProcessingTimeCallback, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoMesaBufferedSink.class);
    private static GeoMesaCassandraDataStoreFactory dataStoreFactory = new GeoMesaCassandraDataStoreFactory();

    //Flink Counter
    private transient Counter counter;

    //Geomesa Data Store used for getting GeoMesaFeatureStore
    private transient DataStore datastore;

    //Used for saving features to DB
    private transient GeoMesaFeatureStore featureStore;

    //Used as Timer for sending partial messages
    private transient ProcessingTimeService processingTimeService;

    //Here is our batch with messages
    private List<SimpleFeature> bufferedMessages;

    //List for saving state, as described in Flink documentation
    private transient ListState<SimpleFeature> checkpointedState;

    //Time in millis for timer, triggered every 30 minutes
    private static final int INACTIVE_BUCKET_CHECK_INTERVAL = 30 * 60 * 1000;

    //How messages is stored in batch,
    //Mind that each parallel execution
    //has it own batch with this THRESHOLD
    private static final int GENERAL_THRESHOLD = 10000;

    private int thresholdPerParallelSubtag;

    public GeoMesaBufferedSink() {
        bufferedMessages = new ArrayList<>(GENERAL_THRESHOLD);
    }

    @Override
    public void invoke(GeomesaFeature value, Context context) {
        this.bufferedMessages.add(value);
        this.counter.inc();
        if(this.bufferedMessages.size() >= thresholdPerParallelSubtag){
            storeMessageToGeomesa();
            this.bufferedMessages.clear();
        }
    }

    /**
     * Create checkpoint from our list of messages
     */
    @Override
    public void snapshotState(FunctionSnapshotContext context) throws Exception {
        this.checkpointedState.clear();
        this.checkpointedState.addAll(this.bufferedMessages);
    }

    /**
     * Restore messages from checkpoint
     */
    @Override
    public void initializeState(FunctionInitializationContext context) throws Exception {
        KryoSerializer serializer = new KryoSerializer(GeomesaFeature.class, getRuntimeContext().getExecutionConfig());
        ListStateDescriptor<SimpleFeature> descriptor = new ListStateDescriptor<SimpleFeature>("buffered-elements", serializer);
        this.checkpointedState = context.getOperatorStateStore().getListState(descriptor);
        if (context.isRestored()) {
            for (SimpleFeature element : this.checkpointedState.get()) {
                this.bufferedMessages.add(element);
            }
        }
    }

    /**
     * Setup Geomesa
     */
    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        LOGGER.debug("Configuring CanPlug sink function.");
        //Initialize Flink Counter
        counter = getRuntimeContext().getMetricGroup().counter("counterBatchedMessages");

        //Initialize feature storage (Geomesa)
        Map<String, String> globalJobConfiguration = getRuntimeContext().getExecutionConfig().getGlobalJobParameters().toMap();
        Map<String, String> allParameters = new HashMap<>(globalJobConfiguration);

        allParameters.put("cassandra.contact.point", "127.0.0.1:9042");
        allParameters.put("cassandra.keyspace", "mykeyspace");
        allParameters.put("cassandra.catalog", "telemetry");
        allParameters.put("geomesa.batchwriter.maxthreads", "10");
        allParameters.put("geomesa.batchwriter.latency.millis", "60000");
        allParameters.put("geomesa.batchwriter.memory", "52428800");
        allParameters.put("geomesa.batchwriter.timeout.millis", "600000");

        datastore = dataStoreFactory.apply(allParameters);
        SimpleFeatureType feature = datastore.getSchema("telemetry");
        if (feature == null) {
            datastore.createSchema(TelemetrySimpleFeatureTypeBuilder.getFeatureType());
        }
        featureStore = (GeoMesaFeatureStore) datastore.getFeatureSource(TelemetrySimpleFeatureTypeBuilder.getFeatureType().getTypeName());

        //Initialize ProcessingTimeService
        this.processingTimeService = ((StreamingRuntimeContext) getRuntimeContext()).getProcessingTimeService();

        //Current timestamp
        final long currentProcessingTime = this.processingTimeService.getCurrentProcessingTime();

        //Set up trigger for sending partial messages
        this.processingTimeService.registerTimer(currentProcessingTime + INACTIVE_BUCKET_CHECK_INTERVAL, this);

        //Each parallel sub-task have it' own counter, need to divide general on each sub-task
        this.thresholdPerParallelSubtag = GENERAL_THRESHOLD / getRuntimeContext().getNumberOfParallelSubtasks();
    }

    /**
     * Here is logic of sending partial messages
     * And set up time trigger for next iteration
     */
    @Override
    public void onProcessingTime(long timestamp) {
        //Get current timestamp
        long currentProcessingTime = processingTimeService.getCurrentProcessingTime();

        //Sending partial messages
        sendPartialMessages();

        //Set up time trigger for next iteration
        this.processingTimeService.registerTimer(currentProcessingTime + INACTIVE_BUCKET_CHECK_INTERVAL, this);
    }

    private void sendPartialMessages() {
        storeMessageToGeomesa();
        this.bufferedMessages.clear();
    }

    private void storeMessageToGeomesa() {
        SimpleFeatureCollection collection = DataUtilities.collection(this.bufferedMessages);
        featureStore.addFeatures(collection);
    }
}
