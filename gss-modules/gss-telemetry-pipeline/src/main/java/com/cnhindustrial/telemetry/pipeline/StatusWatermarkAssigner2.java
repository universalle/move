package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.model.TelemetryMessage;

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

public class StatusWatermarkAssigner2 implements AssignerWithPunctuatedWatermarks<TelemetryMessage> {

    private static final long serialVersionUID = 7896316815733535036L;

    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(TelemetryMessage lastElement, long extractedTimestamp) {
        return null;
    }

    @Override
    public long extractTimestamp(TelemetryMessage element, long previousElementTimestamp) {
        return element.getTime().getTime();
    }
}
