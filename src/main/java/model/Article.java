package model;

public class Article {
    private static int idCode = 0;
    private final int articleID;
    private User author;
    private String textBody;
    private Comments comments;

    public Article() {
        this.articleID = idCode++;
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
    @Override
    public String toString() {
        return "articleID: " + articleID + ", author: " + author.getUserId() + "\nbody: " + textBody;
    }
}
