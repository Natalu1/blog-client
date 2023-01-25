package pl.hirely.blogclient.controller;

import io.vavr.collection.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.hirely.blogclient.model.dto.PostCommentDto;
import pl.hirely.blogclient.model.dto.PostDto;
import pl.hirely.blogclient.model.service.PostService;
import pl.hirely.blogclient.model.service.error.BadRequestException;
import pl.hirely.blogclient.model.service.error.BlogConnectionException;
import pl.hirely.blogclient.model.service.error.NotFoundException;


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
        model.addAttribute("newComment", new PostCommentDto());
        return "single-post";//nazva faila html
    }

    private String getNotFoundPage() {
        return "not-found";
    }

    @GetMapping
    public String getAllPosts(Model model) {
        List<PostDto> posts = postService.getAllPosts();
        PostDto newPostForm = new PostDto();
        model.addAttribute("posts", posts);
        model.addAttribute("newPost", newPostForm);
        return "all-posts";
    }

    @PostMapping
    public String createPost(@ModelAttribute PostDto postForm) {
        postService.createNewPost(postForm);
        return "redirect:/post";
    }

    @PostMapping("/{id}/comment")
    public String createNewComment(@PathVariable("id") Long postId,
                                 PostCommentDto commentDto) {
        postService.createNewComment(postId, commentDto);
        return "redirect:/post";
    }


    @ControllerAdvice
    static class ErrorHandler {
        @ExceptionHandler(BlogConnectionException.class)
        public String handleConnectionError(BlogConnectionException e) {
            return "connection-error";//nazva html file
        }

        @ExceptionHandler(NotFoundException.class)
        @ResponseStatus(value= HttpStatus.NOT_FOUND)
        public String handConnectionError(NotFoundException e) {
            return "not-found";
        }

        @ExceptionHandler(BadRequestException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)

        public String handleBadRequestError(BadRequestException e) {
            return "bad-request";
        }
    }
}
