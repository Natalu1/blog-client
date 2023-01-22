package pl.hirely.blogclient.model.client

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.hirely.blogclient.model.dto.PostDto
import retrofit2.Response
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
    void cleanup(){
        wireMockServer.stop()
    }

    def "Name"(){
        given:
        wireMockServer.stubFor(WireMock.get(/blog/post/5)
                .willReturn(WireMock.getResponse().withStatus(200)))

        when:
        def response = postClient.findPostById(5).execute()

        then:
        response.code() == 200
    }
}
