/* Copyright (c) pro!vision GmbH. All rights reserved. */
package com.mycompany.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mycompany.core.service.CurrenciesService;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.*;

/**
 * Servlet to handle currency data requests based on the resource type
 */
@Component(service = Servlet.class)
@SlingServletResourceTypes(
    resourceTypes = "myCompany/components/page",
    selectors = "currencydata",
    extensions = "json",
    methods = HttpConstants.METHOD_GET)
public class CurrencyConvertorServlet extends SlingSafeMethodsServlet {

  public static final String PRICE_PARAM = "price";
  public static final String CURRENCIESP_PARAM = "currencies";
  public static final String PRICE_PARAM_FORMAT = "%,.2f";
  public static final String ERROR_MESSGAE = "Invalid/Missing Parameter";

  @Reference
  private CurrenciesService currenciesService;

  @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

    Gson gson = new Gson();
    int responseStatus = HttpStatus.SC_OK;

    // validate for existence of mandatory price and currencies parameter
    String priceParam = request.getParameter(PRICE_PARAM);
    String currencyParam = request.getParameter(CURRENCIESP_PARAM);

    double gbpPrice = 0;
    List<String> currencies = new ArrayList<>();

    if (StringUtils.isBlank(priceParam) || StringUtils.equals(priceParam, "0")) {
      responseStatus = HttpStatus.SC_BAD_REQUEST;
    } else {
      gbpPrice = Double.parseDouble(priceParam);
      gbpPrice = Double.parseDouble(String.format(Locale.UK, PRICE_PARAM_FORMAT, gbpPrice));
    }

    if (StringUtils.isBlank(currencyParam)) {
      responseStatus = HttpStatus.SC_BAD_REQUEST;
    } else {
      currencies = Arrays.asList(StringUtils.split(currencyParam, ","));
    }

    //Get converted currency values based on the request
    Map<String, Map<String,String>> convertedCurrenciesMap = new HashMap<>();
    if (gbpPrice != 0 && !currencies.isEmpty()) {
      convertedCurrenciesMap = currenciesService.getCurrenciesList(request, gbpPrice, currencies);
    }

    response.setContentType(ContentType.APPLICATION_JSON.toString());
    JsonObject jsonObject = new JsonObject();
    if (responseStatus != HttpStatus.SC_OK) {
      jsonObject.addProperty("Error", ERROR_MESSGAE);
      response.getWriter().write(gson.toJson(jsonObject));
    } else {
      response.getWriter().write(gson.toJson(convertedCurrenciesMap));
    }
  }

}
