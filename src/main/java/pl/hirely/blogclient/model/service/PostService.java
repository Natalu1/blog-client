package pl.hirely.blogclient.model.service;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;
import pl.hirely.blogclient.model.client.PostClient;
import pl.hirely.blogclient.model.dto.PostDto;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;


@Service
public class PostService {

    private static final Map<Integer, PostDto> POSTS = HashMap.of(
            1, new PostDto("First post", "some random content1"),
            2, new PostDto("Second post", "some random content2"),
            3, new PostDto("Third", "some random content3"),
            4, new PostDto("Next", "some random content4"),
            5, new PostDto("Almost last", "some random content5"),
            6, new PostDto("And the last", "some random content6"));

    private final PostClient postClient;

    public PostService(PostClient postClient) {
        this.postClient = postClient;
    }


    public Option<PostDto> getPostById(Integer postId) {
        Call<PostDto> postDtoCall = postClient.findPostById(postId);
        try {
            Response<PostDto> response = postDtoCall.execute();
            return Option.of(response.body());
        } catch (IOException e) {
            e.printStackTrace();
            return Option.none(); //in java would be Optional.empty()
        }

    }

    public List<PostDto> getAllPosts() {
        return POSTS.values().toList();
    }

}
