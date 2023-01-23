package pl.hirely.blogclient.model.client

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.http.Body
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import spock.lang.Specification

@SpringBootTest
class PostClientSpec extends Specification {
    private WireMockServer wireMockServer = new WireMockServer(
            WireMockConfiguration.options().port(2345)
    )
    @Autowired
    PostClient postClient

    void setup() {
        wireMockServer.start()
    }

    void cleanup() {
        wireMockServer.stop()
    }

    def "Name"() {
        given:
        wireMockServer.stubFor(WireMock.get("/blog/post/5")
                .willReturn(WireMock.aResponse().withStatus(200)
                .withResponseBody(blogPostBody())))



        when:
        def response = postClient.findPostById(5).execute()

        then:
        response.code() == 200
        response.body().title == "test-title"
        response.body().content == "some-content"
    }

    def "shouldFindAllPosts"(){
        given:
        wireMockServer.stubFor(WireMock.get("/blog/post")
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withResponseBody(blogPostBodyList())))

        when:
        def response = postClient.findAllPosts().execute()

        then:
        response.code() == 200
        response.body().title == ["test-title","test-title1"]
        response.body().content == ["some-content", "some-content1"]

    }

    private Body blogPostBodyList() {
        return new Body("""
                        [{
                            "title": "test-title",
                            "content": "some-content"
                        },
                        {
                            "title": "test-title1",
                            "content": "some-content1"
                        }]
                        """)
    }

    private Body blogPostBody() {
        return new Body("""
                        {
                            "title": "test-title",
                            "content": "some-content"
                        }
                        """)
    }

    @Configuration

    static class TestPostClientConfiguration {

        @Bean
        public Retrofit retrofitClient() {
            return new Retrofit.Builder()
                    .baseUrl("http://localhost:2345/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient())
                    .build();
        }

        @Bean
        public PostClient postClient(Retrofit retrofit) {
            return retrofit.create((PostClient.class));
        }
    }


}
