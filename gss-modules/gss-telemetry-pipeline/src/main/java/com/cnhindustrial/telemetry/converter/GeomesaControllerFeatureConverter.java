package com.cnhindustrial.telemetry.converter;

import com.cnhindustrial.telemetry.GeomesaFeature;
import com.cnhindustrial.telemetry.common.model.ControllerDto;
import com.cnhindustrial.telemetry.model.FeaturePropertiesName;

import org.apache.flink.api.common.functions.MapFunction;

public class GeomesaControllerFeatureConverter implements MapFunction<ControllerDto, GeomesaFeature>  {

    private static final long serialVersionUID = 9037490302137843519L;

    @Override
    public GeomesaFeature map(ControllerDto controllerDto) {
        GeomesaFeature geomesaFeature = GeomesaFeature.newInstance();
        geomesaFeature.setAttribute(FeaturePropertiesName.ASSET_ID, controllerDto.getId());
        geomesaFeature.setAttribute(FeaturePropertiesName.TEST, controllerDto.toString());

        // TODO set real telemetry data

        return geomesaFeature;
    }
}
