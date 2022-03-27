package com.mycompany.core.service;

import org.apache.sling.api.SlingHttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * Currencies service interface.
 */
public interface CurrenciesService {

    /**
     * Get the Map of converted Currency values against GBP.
     */
    Map<String, Map<String,String>> getCurrenciesList(SlingHttpServletRequest request, double gbpBasePrice, List<String> currencies);
}
