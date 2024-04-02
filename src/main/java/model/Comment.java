package model;

public class Comment {
    private User author;
    private String commentBody;
    private int articleId;

    public Comment(User author, String commentBody, int articleId) {
        this.author = author;
        this.commentBody = commentBody;
        this.articleId = articleId;
    }

    public String getCommentBody() {
        return commentBody;
    }
}