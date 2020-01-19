package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.common.model.TelemetryMessage;
import com.cnhindustrial.telemetry.common.model.TelemetryRecord;
import com.cnhindustrial.telemetry.function.StatusKeySelector.TelemetryKey;

import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessWindowFunction2 extends ProcessWindowFunction<TelemetryMessage, TelemetryDto, TelemetryKey, TimeWindow> {

    private static final long serialVersionUID = -3583008506491871774L;

//    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessWindowFunction2.class);

    @Override
    public void process(TelemetryKey telemetryKey,
                        Context context,
                        Iterable<TelemetryMessage> elements,
                        Collector<TelemetryDto> out) throws Exception {
        TelemetryDto result = null;
        for (TelemetryMessage element : elements) {
            if (element instanceof TelemetryDto) {
//                if (result != null) {
//                    LOGGER.info("Overriding by {} status {}.", element.getTime(), result.getTime());
//                }
//                LOGGER.info("Setting status {}.", element.getTime());
                result = (TelemetryDto) element;
            }
        }

        if (result != null) {
            for (TelemetryMessage element : elements) {
                if (element instanceof TelemetryRecord) {
                    TelemetryRecord record = (TelemetryRecord) element;
                    result.getTelemetryRecords().add(record);
                }
            }
            out.collect(result);
        } else {
//            LOGGER.info("No status message for {}.", elements.iterator().next().getTime());
        }
    }
}
