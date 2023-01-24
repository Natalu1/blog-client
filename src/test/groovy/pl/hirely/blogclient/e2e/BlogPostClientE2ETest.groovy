package pl.hirely.blogclient.e2e

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.http.Body
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.http.HttpStatusCode
import pl.hirely.blogclient.configuration.RetrofitTestConfiguration
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(RetrofitTestConfiguration)
class BlogPostClientE2ETest extends Specification {
    private WireMockServer wireMockServer = new WireMockServer(
            WireMockConfiguration.options().port(2345)
    )
private TestRestTemplate restTemplate = new TestRestTemplate()

    void setup() {
        wireMockServer.start()
    }

    void cleanup() {
        wireMockServer.stop()
    }

    def "should return 200 with correct html body when requesting single post"(){
        given:
        wireMockServer.stubFor(WireMock.get("/blog/post/5")
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withResponseBody(blogPostBody())))

        when:
        def response = restTemplate.getForEntity("http://localhost:8081/post/5", String.class)

        then:
        response.statusCode == HttpStatusCode.valueOf(200)
        response.getBody().contains("Post found")
        response.getBody().contains("test-title")
        response.getBody().contains("some-content")

    }
    private Body blogPostBody() {
        return new Body("""
                        {
                            "title": "test-title",
                            "content": "some-content"
                        }
                        """)
    }
}
