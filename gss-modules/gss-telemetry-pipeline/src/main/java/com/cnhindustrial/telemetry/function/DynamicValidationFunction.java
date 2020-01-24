package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.groovy.GroovyScript;
import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.io.IOException;
import java.util.List;

public class DynamicValidationFunction extends CoProcessFunction<TelemetryDto, GroovyScript, TelemetryDto> {

    private static final long serialVersionUID = -7884725991052695509L;

    private final OutputTag<TelemetryDto> outputTag;

    private ValueState<GroovyScript> ruleState;

    public DynamicValidationFunction(OutputTag<TelemetryDto> outputTag) {
        this.outputTag = outputTag;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        ValueStateDescriptor<GroovyScript> ruleDescriptor =
                new ValueStateDescriptor<>("validation-rule", GroovyScript.class);

        ruleState = getRuntimeContext().getState(ruleDescriptor);
    }

    @Override
    public void processElement1(TelemetryDto value, Context context, Collector<TelemetryDto> collector) throws Exception {
        if (valid(value)) {
            collector.collect(value);
        } else {
            context.output(outputTag, value);
        }

    }

    private boolean valid(TelemetryDto value) throws IOException {
        GroovyScript groovyScript = ruleState.value();
        List<Error> errors = groovyScript.evaluate(value);
        return errors.isEmpty();
    }

    @Override
    public void processElement2(GroovyScript value, Context ctx, Collector<TelemetryDto> out) throws Exception {
        ruleState.update(value);
    }
}
