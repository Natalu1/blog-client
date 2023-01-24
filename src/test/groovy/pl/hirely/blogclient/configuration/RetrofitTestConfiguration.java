package pl.hirely.blogclient.configuration;

import okhttp3.OkHttpClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@TestConfiguration
public class RetrofitTestConfiguration {

    @Bean
    @Primary
    Retrofit retrofitTestClient(){
        return new Retrofit.Builder()
                .baseUrl("http://localhost:2345")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient())
                .build();
    }


}
