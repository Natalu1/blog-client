package pl.hirely.blogclient.model.service;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.springframework.stereotype.Service;
import pl.hirely.blogclient.model.dto.PostDto;


@Service
public class PostService {

    private static final Map<Integer, PostDto> POSTS = HashMap.of(
            1, new PostDto("First post", "some random content1"),
            2, new PostDto("Second post", "some random content2"),
            3, new PostDto("Third", "some random content3"),
            4, new PostDto("Next", "some random content4"),
            5, new PostDto("Almost last", "some random content5"),
            6, new PostDto("And the last", "some random content6"));

    public Option<PostDto> getPostById(Integer postId) {
        return POSTS.get(postId);
    }

    public List<PostDto> getAllPosts() {
        return POSTS.values().toList();
    }

}
