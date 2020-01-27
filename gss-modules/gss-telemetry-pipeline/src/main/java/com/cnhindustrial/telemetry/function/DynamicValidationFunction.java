package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.groovy.GroovyScript;
import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.pipeline.IngestPipeline;

import org.apache.flink.api.common.state.BroadcastState;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.List;

public class DynamicValidationFunction extends KeyedBroadcastProcessFunction<String, TelemetryDto, GroovyScript, TelemetryDto> {

    private static final long serialVersionUID = -7884725991052695509L;

    private final OutputTag<TelemetryDto> invalidOutputTag;

    public DynamicValidationFunction(OutputTag<TelemetryDto> invalidOutputTag) {
        this.invalidOutputTag = invalidOutputTag;
    }

    @Override
    public void processElement(TelemetryDto value, ReadOnlyContext readOnlyContext, Collector<TelemetryDto> collector) throws Exception {

        GroovyScript script = readOnlyContext.getBroadcastState(IngestPipeline.DYNAMIC_VALIDATION_DESCRIPTOR).get("scripts");

        if (valid(script, value)) {
            collector.collect(value);
        } else {
            readOnlyContext.output(invalidOutputTag, value);
        }

    }

    private boolean valid(GroovyScript groovyScript, TelemetryDto value) {

        List<Error> errors = groovyScript.evaluate(value);
        return errors.isEmpty();
    }

    @Override
    public void processBroadcastElement(GroovyScript groovyScript, Context context, Collector<TelemetryDto> collector) throws Exception {
        // store the new pattern by updating the broadcast state
        BroadcastState<String, GroovyScript> bcState = context.getBroadcastState(IngestPipeline.DYNAMIC_VALIDATION_DESCRIPTOR);
        // storing in MapState with null as VOID default value
        bcState.put("scripts", groovyScript);
    }
}
