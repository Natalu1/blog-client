package pl.hirely.blogclient.model.dto;

public class PostCommentDto {
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PostCommentDto(String content) {
        this.content = content;
    }

    public PostCommentDto() {
    }
}
