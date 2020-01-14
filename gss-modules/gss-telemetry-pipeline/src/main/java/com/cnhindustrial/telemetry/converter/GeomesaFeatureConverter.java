package com.cnhindustrial.telemetry.converter;

import com.cnhindustrial.telemetry.GeomesaFeature;
import com.cnhindustrial.telemetry.common.model.TelemetryDto;
import com.cnhindustrial.telemetry.model.FeaturePropertiesName;
import org.apache.flink.api.common.functions.MapFunction;

public class GeomesaFeatureConverter implements MapFunction<TelemetryDto, GeomesaFeature>  {

    @Override
    public GeomesaFeature map(TelemetryDto telemetryDto) {
        GeomesaFeature geomesaFeature = GeomesaFeature.newInstance();
        geomesaFeature.setAttribute(FeaturePropertiesName.ASSET_ID, telemetryDto.getAssetId());
        geomesaFeature.setAttribute(FeaturePropertiesName.TEST, telemetryDto.toString());

        // TODO set real telemetry data

        return geomesaFeature;
    }
}
