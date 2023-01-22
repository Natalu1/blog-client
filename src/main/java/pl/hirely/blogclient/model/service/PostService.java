package pl.hirely.blogclient.model.service;

import io.vavr.control.Option;
import org.springframework.stereotype.Service;
import pl.hirely.blogclient.model.client.PostClient;
import pl.hirely.blogclient.model.dto.PostCommentDto;
import pl.hirely.blogclient.model.dto.PostDto;
import pl.hirely.blogclient.model.service.error.BadRequestException;
import pl.hirely.blogclient.model.service.error.BlogConnectionException;
import pl.hirely.blogclient.model.service.error.NotFoundException;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;


@Service
public class PostService {

//    private static final Map<Integer, PostDto> POSTS = HashMap.of(
//            1, new PostDto("First post", "some random content1"),
//            2, new PostDto("Second post", "some random content2"),
//            3, new PostDto("Third", "some random content3"),
//            4, new PostDto("Next", "some random content4"),
//            5, new PostDto("Almost last", "some random content5"),
//            6, new PostDto("And the last", "some random content6"));

    private final PostClient postClient;

    public PostService(PostClient postClient) {
        this.postClient = postClient;
    }


    public Option<PostDto> getPostById(Integer postId) {
        Call<PostDto> postDtoCall = postClient.findPostById(postId);
        try {
            Response<PostDto> response = postDtoCall.execute();
            if (response.isSuccessful()) {
                return Option.of(response.body());
            } else if (response.code() == 404) {
                throw new NotFoundException();
            }
            throw new BlogConnectionException
                    (response.errorBody().toString());
        } catch (IOException e) {
            throw new BlogConnectionException
                    ("Unexpected connection problem");
//            e.printStackTrace();
//            return Option.none(); //in java would be Optional.empty()
        }

    }

    public io.vavr.collection.List<PostDto> getAllPosts() {
        Call<List<PostDto>> postsDtoCall = postClient.findAllPosts();
        try {
            Response<List<PostDto>> response = postsDtoCall.execute();
            if (response.isSuccessful()) {
                return io.vavr.collection.List.ofAll(response.body());
            }
            throw new BlogConnectionException
                    (response.errorBody().toString());
        } catch (IOException e) {
            throw new BlogConnectionException
                    ("Unexpected connection problem");
        }

    }
    public void createNewPost(PostDto postDto) {
        try {
            Response<Void> response = postClient.createPost(postDto).execute();
            if (response.code() == 400) {
                throw new BadRequestException();
            }
        } catch (IOException e) {
            throw new BlogConnectionException(e.getMessage());
        }
    }

    public void createNewComment(Long postId, PostCommentDto postCommentDto){
        try {
            Response<Void> response = postClient.createPostComment(postId, postCommentDto).execute();
            if (response.code() == 404) {
                throw new NotFoundException();
            }
        } catch (IOException e) {
            throw new BlogConnectionException(e.getMessage());
        }

    }



}
