package com.mycompany.core.service.impl;

import com.mycompany.core.service.CurrenciesService;
import com.mycompany.core.service.Currency;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component(service = CurrenciesService.class, immediate = true)
@Designate(ocd = CurrenciesServiceImpl.Config.class)
public class CurrenciesServiceImpl implements CurrenciesService {

    @Override
    public Map<String, Map<String,String>> getCurrenciesList(SlingHttpServletRequest request,
                                                             double gbpBasePrice, List<String> currencies) {

        Map<String, Currency> currencyRatesFromJcr = getCurrencyRatesFromJcr(request);
        return getConvertedCurrenciesMap(gbpBasePrice, currencies, currencyRatesFromJcr);
    }

    /**
     * Method returns the final map which contains converted currency values
     * based on the request parameters.
     * @param gbpBasePrice
     * @param currencies
     * @param currencyRatesFromJcr
     * @return final converted Map
     * @throws ArithmeticException
     * @throws NumberFormatException
     */
    private Map<String, Map<String, String>> getConvertedCurrenciesMap(double gbpBasePrice, List<String> currencies, Map<String, Currency> currencyRatesFromJcr)
            throws ArithmeticException, NumberFormatException{

        Map<String, Map<String,String>> finalMap = new HashMap<>();
        currencies.forEach(targetCurrency -> {
            if (currencyRatesFromJcr.containsKey(targetCurrency)) {
                Currency conversionfactor = currencyRatesFromJcr.get(targetCurrency);
                double convCurrency = gbpBasePrice / conversionfactor.getConversionFactor();
                Map<String, String> convertedMap = new HashMap<>();
                convertedMap.put("price", String.format(Locale.UK, "%,.2f", convCurrency));
                convertedMap.put("name", conversionfactor.getCurrencyName());
                finalMap.put(StringUtils.upperCase(targetCurrency), convertedMap);
            }
        });
        return finalMap;
    }

    /**
     * Method to read and return a list of currencies and its conversion rates
     * against GBP stored in JCR.
     * @param request
     * @return Currency rates against GBP
     */
    private Map<String, Currency> getCurrencyRatesFromJcr(SlingHttpServletRequest request) {

        Map<String, Currency> currencyList = new HashMap<>();
        Resource rootNode = request.getResourceResolver().getResource(rootPath);
        if (rootNode != null) {
            for (Resource currencyNode : rootNode.getChildren()) {
                Currency currency = currencyNode.adaptTo(Currency.class);
                if (currency != null) {
                    currencyList.put(currencyNode.getName(), currency);
                }
            }
        }
        return currencyList;
    }

    @ObjectClassDefinition(name = "Currencies Service", description = "Get List of currencies")
    public @interface Config {

        @AttributeDefinition(name = "Root Path")
        String root_path() default "/etc/currencies";
    }

    private String rootPath;

    /**
     * {@inheritDoc}
     */
    @Activate
    @Modified
    public void activate(final Config config) {
        this.rootPath = config.root_path();
    }

}