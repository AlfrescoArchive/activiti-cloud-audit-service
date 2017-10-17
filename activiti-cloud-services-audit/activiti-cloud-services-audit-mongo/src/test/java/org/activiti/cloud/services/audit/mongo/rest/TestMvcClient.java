package org.activiti.cloud.services.audit.mongo.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.LinkDiscoverer;
import org.springframework.hateoas.LinkDiscoverers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

public class TestMvcClient {

    public static MediaType DEFAULT_MEDIA_TYPE = org.springframework.hateoas.MediaTypes.HAL_JSON;

    private final MockMvc mvc;
    private final LinkDiscoverers discoverers;
    private String basePath = "/";

    /**
     * Creates a new {@link TestMvcClient} for the given {@link MockMvc} and {@link LinkDiscoverers}.
     * 
     * @param mvc must not be {@literal null}.
     * @param discoverers must not be {@literal null}.
     */
    public TestMvcClient(MockMvc mvc, LinkDiscoverers discoverers) {

        assertThat(mvc).isNotNull();
        assertThat(discoverers).isNotNull();

        this.mvc = mvc;
        this.discoverers = discoverers;
    }

    /**
     * Set base path uri. Default value is "/"
     * 
     * @param uri basePath 
     * @return TestMvcClient instance
     */
    public TestMvcClient setBasePath(String uri) {
        this.basePath = uri;

        return this;
    }

    /**
     * Folow Link with a specific Accept header (media type).
     *
     * @param link
     * @param accept
     * @return
     * @throws Exception
     */
    public ResultActions follow(Link link, MediaType accept) throws Exception {
        return follow(link.expand().getHref(), accept);
    }

    /**
     * Follow URL supplied as a string with a specific Accept header.
     * 
     * @param href
     * @param accept
     * @return
     * @throws Exception
     */
    public ResultActions follow(String href, MediaType accept) throws Exception {
        return mvc.perform(get(href).header(HttpHeaders.ACCEPT, accept.toString()));
    }

    /**
     * Discover list of URIs associated with a rel, starting at the root node ("/")
     *
     * @param rel
     * @return
     * @throws Exception
     */
    public List<Link> discover(String rel) throws Exception {
        return discover(new Link(basePath), rel);
    }

    /**
     * Discover single URI associated with a rel, starting at the root node ("/")
     * 
     * @param rel
     * @return
     * @throws Exception
     */
    public Link discoverUnique(String rel) throws Exception {

        List<Link> discover = discover(rel);
        assertThat(discover).hasSize(1);
        return discover.get(0);
    }

    /**
     * Given a URI (root), discover the URIs for a given rel.
     *
     * @param root - URI to start from
     * @param rel - name of the relationship to seek links
     * @return list of {@link org.springframework.hateoas.Link Link} objects associated with the rel
     * @throws Exception
     */
    public List<Link> discover(Link root, String rel) throws Exception {

        MockHttpServletResponse response = mvc.perform(get(root.expand().getHref()).accept(DEFAULT_MEDIA_TYPE)).//
                                              andExpect(status().isOk()).//
                                              andExpect(hasLinkWithRel(rel)).//
                                              andReturn().getResponse();

        String s = response.getContentAsString();
        return getDiscoverer(response).findLinksWithRel(rel, s);
    }

    /**
     * Given a URI (root), discover the unique URI for a given rel. NOTE: Assumes there is only one URI
     *
     * @param root the link to the resource to access.
     * @param rel the link relation to discover in the response.
     * @param mediaType the {@link MediaType} to request.
     * @return {@link org.springframework.hateoas.Link Link} tied to a given rel
     * @throws Exception
     */
    public Link discoverUnique(Link root, String rel, MediaType mediaType) throws Exception {

        MockHttpServletResponse response = mvc
                                              .perform(get(root.expand().getHref())//
                                                                                   .accept(mediaType))
                                              .andExpect(status().isOk())//
                                              .andExpect(hasLinkWithRel(rel))//
                                              .andReturn()
                                              .getResponse();

        return assertHasLinkWithRel(rel, response);
    }

    /**
     * For a given servlet response, verify that the provided rel exists in its hypermedia. If so, return the URI link.
     *
     * @param rel
     * @param response
     * @return {@link org.springframework.hateoas.Link} of the rel found in the response
     * @throws Exception
     */
    public Link assertHasLinkWithRel(String rel, MockHttpServletResponse response) throws Exception {

        String content = response.getContentAsString();
        Link link = getDiscoverer(response).findLinkWithRel(rel, content);

        assertThat(link)
            .describedAs("Expected to find link with rel " + rel + " but found none in " + content + "!")
            .isNotNull();

        return link;
    }

    /**
     * MockMvc matcher used to verify existence of rel with URI link
     *
     * @param rel
     * @return
     */
    public ResultMatcher hasLinkWithRel(final String rel) {

        return new ResultMatcher() {

            @Override
            public void match(MvcResult result) throws Exception {

                MockHttpServletResponse response = result.getResponse();
                String s = response.getContentAsString();

                assertThat(getDiscoverer(response).findLinkWithRel(rel, s))
                           .describedAs("Expected to find link with rel " + rel + " but found none in " + s)
                           .isNotNull();
            }
        };
    }

    /**
     * Using the servlet response's content type, find the corresponding link discoverer.
     *
     * @param response
     * @return {@link org.springframework.hateoas.LinkDiscoverer}
     */
    public LinkDiscoverer getDiscoverer(MockHttpServletResponse response) {

        String contentType = response.getContentType();
        LinkDiscoverer linkDiscovererFor = discoverers.getLinkDiscovererFor(contentType);

        assertThat(linkDiscovererFor)
            .describedAs("Did not find a LinkDiscoverer for returned media type " + contentType + "!")
            .isNotNull();

        return linkDiscovererFor;
    }
}
