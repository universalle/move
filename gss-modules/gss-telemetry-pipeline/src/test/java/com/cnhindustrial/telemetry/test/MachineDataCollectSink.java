package com.cnhindustrial.telemetry.test;

import com.cnhindustrial.telemetry.GeomesaFeature;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Testing sink.
 */
public class MachineDataCollectSink implements SinkFunction<GeomesaFeature> {

    private static final long serialVersionUID = 428694870703857546L;

    /**
     * must be static
     * https://ci.apache.org/projects/flink/flink-docs-stable/dev/stream/testing.html#junit-rule-miniclusterwithclientresource
     */
    private static final List<GeomesaFeature> values = new ArrayList<>();

    @Override
    public synchronized void invoke(GeomesaFeature value, Context context) throws Exception {
        values.add(value);
    }

    public List<GeomesaFeature> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void clear() {
        values.clear();
    }
}
