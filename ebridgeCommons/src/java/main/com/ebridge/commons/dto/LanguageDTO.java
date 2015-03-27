package com.ebridge.commons.dto;

/**
 * @author david@tekeshe.com
 */

public class LanguageDTO {

    private final String systemValue;
    private final String normalizedValue;
    private final String classOfService;
    private final Integer index;
    private String unitOfMeasure;

    public LanguageDTO(
                String systemValue,
                String normalizedValue,
                String classOfService,
                Integer index,
                String unitOfMeasure ) {

        this.systemValue = systemValue;
        this.normalizedValue = normalizedValue;
        this.classOfService = classOfService;
        this.index = index;
    }

    public String getSystemValue() {
        return systemValue;
    }

    public String getNormalizedValue() {
        return normalizedValue;
    }

    public String getClassOfService() {
        return classOfService;
    }

    public Integer getIndex() {
        return index;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String toString() {
        return "{ systemValue : " + systemValue + ", normalizedValue : " + normalizedValue + ", index : " + index +"}";
    }
}


