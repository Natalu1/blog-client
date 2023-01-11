package pl.hirely.blogclient.model.client;

import pl.hirely.blogclient.model.dto.PostDto;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface PostClient {
    @GET("/blog/post/{id}")
    Call<PostDto> findPostById(@Path("id") Integer postId);

    @GET("/blog/post")
    Call<List<PostDto>>findAllPosts();
}
