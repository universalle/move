package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.json.BaseDeserializationSchema;
import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.common.model.TelemetryMessage;
import com.cnhindustrial.telemetry.common.model.TelemetryRecord;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

public class MessageDeserializeFunction2 extends ProcessFunction<byte[], TelemetryMessage> {

    private static final long serialVersionUID = 3843890166123424318L;

    private final OutputTag<TelemetryRecord> outputTag = new OutputTag<>("telemetry-record", TypeInformation.of(TelemetryRecord.class));
    private BaseDeserializationSchema<TelemetryDto> statusDeserializationSchema;
    private BaseDeserializationSchema<TelemetryRecord> telemetryDeserializationSchema;

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
        statusDeserializationSchema = new BaseDeserializationSchema<>(TelemetryDto.class);
        telemetryDeserializationSchema = new BaseDeserializationSchema<>(TelemetryRecord.class);
    }

    @Override
    public void processElement(byte[] bytes, Context context, Collector<TelemetryMessage> collector) throws Exception {
        String messageStart = new String(bytes, 0, 30);
        if (messageStart.startsWith("{\"messageType\":\"Status\"")) {
            // emit data to regular output
            TelemetryDto statusRecord = statusDeserializationSchema.deserialize(bytes);
            collector.collect(statusRecord);
        } else {
            // emit data to side output
            TelemetryRecord telemetryRecord = telemetryDeserializationSchema.deserialize(bytes);
            collector.collect(telemetryRecord);
        }
    }

    public OutputTag<TelemetryRecord> getSideStreamTag() {
        return outputTag;
    }
}
