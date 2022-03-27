package com.mycompany.core.service.impl;

import java.util.*;

import com.mycompany.core.service.AudienceService;
import com.mycompany.core.service.Option;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;


import org.osgi.service.component.annotations.Modified;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.osgi.service.metatype.annotations.AttributeDefinition;

@Component(service = AudienceService.class, immediate = true)
@Designate(ocd = AudienceServiceImpl.Config.class)
public class AudienceServiceImpl implements AudienceService {

    public static final String NAME = "name";
    public static final String VALUE = "value";

    @ObjectClassDefinition(name = "Audience Service", description = "Get Configurations")
    public @interface Config {

        String DEFAULT_ROOTPATH = "etc/default";

        @AttributeDefinition(name = "Root Path")
        String root_path() default DEFAULT_ROOTPATH;
    }

    private String rootPath;

    @Override
    public List<Option> getAudienceAsOptions(SlingHttpServletRequest request) {
        List<Option> list = new ArrayList<>();
        Resource resource = request.getResourceResolver().getResource(rootPath);
        if (resource != null) {
            Map<String, String> audienceList = getAudiences(resource);
            audienceList.forEach((k, v) -> list.add(new Option(k, v)));
        }
        return list;
    }

    /**
     * Returns resource name and page title of the childNodes of a specific resource.
     *
     * @param resource
     */
    private static Map<String, String> getAudiences(Resource resource) {
        Map<String, String> audienceMap = new HashMap<>();
        Iterator<Resource> childNodes = resource.listChildren();
        while (childNodes.hasNext()) {
            Resource childResource = childNodes.next();
            ValueMap valueMap = childResource.getValueMap();
            String name = StringUtils.EMPTY;
            String value = StringUtils.EMPTY;
            if (valueMap.containsKey(NAME) && valueMap.containsKey(VALUE)) {
                name = valueMap.get(NAME, String.class);
                value = valueMap.get(VALUE, String.class);
            }
            audienceMap.put(name, value);
        }
        return audienceMap;
    }

    /**
     * {@inheritDoc}
     */
    @Activate
    @Modified
    public void activate(final Config config) {
        this.rootPath = config.root_path();
    }
}