package pl.hirely.blogclient.model.client;


import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitClientConfiguration {

    @Bean
    public Retrofit retrofitClient() {
        return new Retrofit.Builder()
                .baseUrl("http://localhost:8080/")
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Bean
    public PostClient postClient(Retrofit retrofit){
        return retrofit.create((PostClient.class));
    }
}
