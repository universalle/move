package com.cnhindustrial.telemetry.pipeline;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.apache.flink.streaming.api.functions.AssignerWithPunctuatedWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import javax.annotation.Nullable;

public class StatusWatermarkAssigner implements AssignerWithPunctuatedWatermarks<TelemetryDto> {

    private static final long serialVersionUID = 7896316815733535036L;

    @Override
    public long extractTimestamp(TelemetryDto statusDto, long previousElementTimestamp) {
        return statusDto.getTime().getTime();
    }

    @Nullable
    @Override
    public Watermark checkAndGetNextWatermark(TelemetryDto statusDto, long extractedTimestamp) {
        return null;
    }
}
