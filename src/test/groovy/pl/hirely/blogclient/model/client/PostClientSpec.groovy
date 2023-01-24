package pl.hirely.blogclient.model.client

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.http.Body
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import pl.hirely.blogclient.PostClient
import pl.hirely.blogclient.configuration.RetrofitTestConfiguration
import pl.hirely.blogclient.model.dto.PostCommentDto
import pl.hirely.blogclient.model.dto.PostDto
import spock.lang.Specification
import spock.lang.Subject

@SpringBootTest
@Import(RetrofitTestConfiguration)
class PostClientSpec extends Specification {
    private WireMockServer wireMockServer = new WireMockServer(
            WireMockConfiguration.options().port(2345)
    )
    @Autowired
    @Subject
    PostClient postClient

    void setup() {
        wireMockServer.start()
    }

    void cleanup() {
        wireMockServer.stop()
    }

    def "should receive json response with post"() {
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

    def "should receive json response with list of post"() {
        given:
        wireMockServer.stubFor(WireMock.get("/blog/post")
                .willReturn(WireMock.aResponse().withStatus(200)
                        .withResponseBody(blogPostBodyList())))

        when:
        def response = postClient.findAllPosts().execute()

        then:
        response.code() == 200
        response.body().title == ["test-title", "test-title1"]
        response.body().content == ["some-content", "some-content1"]

    }

    def "should receive 200 response after creating a post"() {
        given:
        def dtoToSend = new PostDto("test-title", "some-content")
        wireMockServer.stubFor(WireMock.post("/blog/post")
                .withRequestBody(WireMock.equalToJson(blogPostBody().asString()))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)))

        when:
        def response = postClient.createPost(dtoToSend).execute()

        then:
        response.code() == 200
    }

    def "should receive 200 response after creating a comment in post"() {
        given:
        def dtoToSend = new PostCommentDto("comment")
        wireMockServer.stubFor(WireMock.post("/blog/post/3/comment")
                .withRequestBody(WireMock.equalToJson(postCommentBody().asString()))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)))

        when:
        def response = postClient.createPostComment(3, dtoToSend).execute()

        then:
        response.code() == 200
    }

    private Body postCommentBody() {
        return new Body("""
            { 
            "content": "comment"}
            """)
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

//    @Configuration
//
//    static class TestPostClientConfiguration {
//
//        @Bean
//        public Retrofit retrofitClient() {
//            return new Retrofit.Builder()
//                    .baseUrl("http://localhost:2345/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .client(new OkHttpClient())
//                    .build();
//        }
//
//        @Bean
//        public PostClient postClient(Retrofit retrofit) {
//            return retrofit.create((PostClient.class));
//        }
//    }


}
