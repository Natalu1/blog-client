package pl.hirely.blogclient.controller;

import io.vavr.collection.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hirely.blogclient.model.dto.PostDto;
import pl.hirely.blogclient.model.service.PostService;
import pl.hirely.blogclient.model.service.error.BlogConnectionException;


@Controller
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/{id}")
    public String getPostById(@PathVariable("id") Integer postId, Model model) {
        return postService.getPostById(postId)
                .map(post -> getSinglePostPage(post, model))
                .getOrElse(getNotFoundPage());
    }

    private String getSinglePostPage(PostDto postDto, Model model) {
        model.addAttribute("post", postDto);//nazva zmenoj, kotoraja v html vyzyva title i content
        return "single-post";//nazva faila html
    }

    private String getNotFoundPage() {
        return "not-found";
    }

    @GetMapping
    public String getAllPosts(Model model) {
        List<PostDto> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "all-posts";
    }

    @ControllerAdvice
    static class ErrorHandler {
        @ExceptionHandler(BlogConnectionException.class)
        public String handleConnectionError(BlogConnectionException e){
            return "connection-error";//nazva html file
        }
    }
}
