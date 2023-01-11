package pl.hirely.blogclient.model.service.error;

public class BlogConnectionException extends RuntimeException {
    public BlogConnectionException(String error) {
        super(error);
    }
}
