package com.mycompany.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.ImmutableMap;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.spi.Injector;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class LinkModelTest {

    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    private Resource resource;

    private Page page;

    private PageManager pageManager;

    private LinkModel underTest;

    @BeforeEach
    void setUp() {
        assertNotNull(context.pageManager());
        context.addModelsForClasses(LinkModel.class);

    }

    @Test
    void testWhenContainaingPageIsNotPresent() {

        resource = context.create().resource("/content/mycompany/en/homepage/jcr:content/link",
                ImmutableMap.<String, Object>builder()
                        .put("sling:resourceType", "apps/mycompany/components/link")
                        .put("jcr:title", "Banner")
                        .build());
        context.currentResource(resource);

        context.registerService(Resource.class, resource);
        context.registerService(PageManager.class, context.pageManager());

        underTest = context.currentResource().adaptTo(LinkModel.class);
        String title = underTest.getTitle();
        assertNotNull(title);
        assertEquals("Banner", title);
    }

    @Test
    void testWhenContainaingPageIsPresent() {
        page = context.create().page("/content/mycompany/en/homepage", "",
                ImmutableMap.<String, Object>builder()
                        .put("jcr:title", "Homepage")
                        .build());
        context.currentPage(page);
        resource = context.create().resource(page, "link",
                ImmutableMap.<String, Object>builder()
                        .put("sling:resourceType", "apps/mycompany/components/link")
                        .put("jcr:title", "Banner")
                        .build());
        context.currentResource(resource);

        context.registerService(Resource.class, resource);
        context.registerService(PageManager.class, context.pageManager());

        underTest = context.currentResource().adaptTo(LinkModel.class);
        String title = underTest.getTitle();
        assertNotNull(title);
        assertEquals("Homepage", title);
    }

    @Test
    void testWhenNavTitleIsPresent() {
        page = context.create().page("/content/mycompany/en/homepage", "",
                ImmutableMap.<String, Object>builder()
                        .put("jcr:title", "Homepage")
                        .put("navTitle", "HomePage Nav Title")
                        .build());
        context.currentPage(page);
        resource = context.create().resource(page, "link",
                ImmutableMap.<String, Object>builder()
                        .put("sling:resourceType", "apps/mycompany/components/link")
                        .put("jcr:title", "Banner")
                        .build());
        context.currentResource(resource);

        context.registerService(Resource.class, resource);
        context.registerService(PageManager.class, context.pageManager());

        underTest = context.currentResource().adaptTo(LinkModel.class);
        String title = underTest.getTitle();
        assertNotNull(title);
        assertEquals("HomePage Nav Title", title);
    }

    @Test
    void testGetLinkWhenContainingPageIsNotPresent() {

        resource = context.create().resource("/content/mycompany/en/homepage/jcr:content/link",
                ImmutableMap.<String, Object>builder()
                        .put("sling:resourceType", "apps/mycompany/components/link")
                        .put("jcr:title", "Banner")
                        .build());
        context.currentResource(resource);

        context.registerService(Resource.class, resource);
        context.registerService(PageManager.class, context.pageManager());

        underTest = context.currentResource().adaptTo(LinkModel.class);
        String link = underTest.getLink();
        assertNotNull(link);
        assertEquals("/content/mycompany/en/homepage/jcr:content/link", link);
    }

    @Test
    void testGetLinkWhenContainingPageIsPresent() {
        page = context.create().page("/content/mycompany/en/homepage", "",
                ImmutableMap.<String, Object>builder()
                        .put("jcr:title", "Homepage")
                        .build());
        resource = context.create().resource("/content/mycompany/en/homepage/jcr:content/link",
                ImmutableMap.<String, Object>builder()
                        .put("sling:resourceType", "apps/mycompany/components/link")
                        .put("jcr:title", "Banner")
                        .build());
        context.currentResource(resource);

        context.registerService(Resource.class, resource);
        context.registerService(PageManager.class, context.pageManager());

        underTest = context.currentResource().adaptTo(LinkModel.class);
        String link = underTest.getLink();
        assertNotNull(link);
        assertEquals("/content/mycompany/en/homepage/jcr:content/link.html", link);
    }
}