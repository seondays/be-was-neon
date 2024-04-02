package model;

public class Article {
    private static int idCode = 0;
    private final int articleID;
    private final Comments comments;
    private User author;
    private String textBody;

    public Article() {
        this.articleID = idCode++;
        comments = new Comments();
    }

    public void setContent(String textBody) {
        this.textBody = textBody;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTextBody() {
        return textBody;
    }

    public int getArticleID() {
        return articleID;
    }

    public void addComment(Comment comment) {
        comments.addComment(comment);
    }

    public Comments getComments() {
        return comments;
    }
    public int commentSize() {
        return comments.size();
    }

    @Override
    public String toString() {
        return "articleID: " + articleID + ", author: " + author.getUserId() + "\nbody: " + textBody;
    }
}
