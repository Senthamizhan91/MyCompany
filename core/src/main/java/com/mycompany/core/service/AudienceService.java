package com.mycompany.core.service;

import java.util.List;

import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Audience service interface.
 */
@FunctionalInterface
public interface AudienceService {

    /**
     * Get the list of page owners
     *
     * @param request instance of SlingHttpServletRequest.class
     * @return Option - Collection of audience names
     */
    List<Option> getAudienceAsOptions(SlingHttpServletRequest request);
}
