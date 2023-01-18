package pl.hirely.blogclient.model.dto;

import java.util.List;

public class PostDto {
    private Long id;
    private String title;
    private String content;
    private List<PostCommentDto> comments;

    public List<PostCommentDto> getComments() {
        return comments;
    }

    public void setComments(List<PostCommentDto> comments) {
        this.comments = comments;
    }

    public PostDto() {
    }

    public PostDto(Long id, String title, String content, List<PostCommentDto> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.comments = comments;
    }

    public PostDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
