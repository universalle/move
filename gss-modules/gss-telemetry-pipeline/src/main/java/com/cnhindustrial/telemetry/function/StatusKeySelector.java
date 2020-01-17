package com.cnhindustrial.telemetry.function;

import com.cnhindustrial.telemetry.common.model.TelemetryDto;

import org.apache.flink.api.java.functions.KeySelector;

import java.util.Objects;

public class StatusKeySelector implements KeySelector<TelemetryDto, StatusKeySelector.TelemetryKey> {

    private static final long serialVersionUID = 6981903983304465637L;

    @Override
    public TelemetryKey getKey(TelemetryDto telemetryDto) {
        return new TelemetryKey(telemetryDto.getDeviceid(), telemetryDto.getAssetId());
    }

    public static class TelemetryKey {
        private final Long deviceid;
        private final String assetId;

        public TelemetryKey(Long deviceid, String assetId) {
            this.deviceid = deviceid;
            this.assetId = assetId;
        }

        public Long getDeviceid() {
            return deviceid;
        }

        public String getAssetId() {
            return assetId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TelemetryKey that = (TelemetryKey) o;
            return Objects.equals(deviceid, that.deviceid) &&
                    Objects.equals(assetId, that.assetId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(deviceid, assetId);
        }
    }
}
