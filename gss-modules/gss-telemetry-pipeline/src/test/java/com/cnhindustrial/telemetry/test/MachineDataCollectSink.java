package com.cnhindustrial.telemetry.test;

import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.kafka.common.protocol.types.Field.Str;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Testing sink.
 */
// TODO temporary the generic type is String
public class MachineDataCollectSink implements SinkFunction<String> {

    private static final long serialVersionUID = 428694870703857546L;

    /**
     * must be static
     * https://ci.apache.org/projects/flink/flink-docs-stable/dev/stream/testing.html#junit-rule-miniclusterwithclientresource
     */
    private static final List<String> values = new ArrayList<>();

    @Override
    public synchronized void invoke(String value, Context context) throws Exception {
        values.add(value);
    }

    public List<String> getValues() {
        return Collections.unmodifiableList(values);
    }

    public void clear() {
        values.clear();
    }
}
