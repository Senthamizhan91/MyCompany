package com.mycompany.core.service;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class)
public class Currency {

    @ValueMapValue(name = "currencyName", injectionStrategy = InjectionStrategy.OPTIONAL)
    private String currencyName;

    @ValueMapValue(name = "conversionFactor", injectionStrategy = InjectionStrategy.OPTIONAL)
    private double conversionFactor;

    public String getCurrencyName() {
        return currencyName;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }
}
