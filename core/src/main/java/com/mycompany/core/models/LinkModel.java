package com.mycompany.core.models;

import static org.apache.sling.models.annotations.DefaultInjectionStrategy.OPTIONAL;
import static com.day.cq.commons.jcr.JcrConstants.JCR_TITLE;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;


@Model(adaptables = Resource.class, defaultInjectionStrategy = OPTIONAL)
public class LinkModel {

    private static final String HTML_EXTENSION = ".html";

    @Inject
    @Named(JCR_TITLE)
    private String title;

    private String link;

    @Inject
    private Resource resource;

    @Inject
    private PageManager pageManager;

    /**
     * Post construct init method.
     */
    @PostConstruct
    public void init() {
        appendExtensionToLink();
    }
	
	public String getLink() {
		return link;
	}
	
	public String getTitle() {
        // I have changed link to resource
        Page page = pageManager.getContainingPage(resource);

        if (page == null) {
            return title;
        }

        return getPageTitle(page);
    }

    private String getPageTitle(Page page) {
        String navigationTitle = page.getNavigationTitle();
        
        if (navigationTitle != null) {
            return navigationTitle;
        }
        
        return page.getTitle();
    }
	
    private void appendExtensionToLink() {
        link = resource.getPath();
        Page page = pageManager.getContainingPage(link);
        if (page != null) {
            link = link + HTML_EXTENSION;
        }
    }
}
