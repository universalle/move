package com.cnhindustrial.telemetry.model;

public enum SchemaFieldDescription {

    // TODO set real telemetry fields
    ID(FeaturePropertiesName.ASSET_ID, "Integer", null),
    TEST(FeaturePropertiesName.TEST, "String", null);

    private String fieldName;
    private String type;
    private String properties;

    SchemaFieldDescription(String fieldName, String type, String properties) {
        this.fieldName = fieldName;
        this.type = type;
        this.properties = properties;
    }

    public String getDescription(){
        String basic = this.fieldName + ":" + this.type;
        if(this.properties != null){
            basic = basic + ":" + this.properties;
        }
        return basic;
    }
}
