package pl.hirely.blogclient.model.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.hirely.blogclient.model.dto.PostCommentDto;
import pl.hirely.blogclient.model.dto.PostDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;
import java.util.concurrent.Callable;

public interface PostClient {
    @GET("/blog/post/{id}")
    Call<PostDto> findPostById(@Path("id") Integer postId);

    @GET("/blog/post")
    Call<List<PostDto>>findAllPosts();

    @POST("/blog/post")
    Call<Void> createPost(@Body PostDto postDto);

    @POST("blog/post/{postId}/comment")
Call<Void> createPostComment(@Path("postId") Long postId,
                @Body PostCommentDto postCommentDto);
}
