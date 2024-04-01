package model;

public class Article {
    private User author;
    private String textBody;
    private Comments comments;

    public void setContent(String textBody) {
        this.textBody = textBody;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTextBody() {
        return textBody;
    }

    @Override
    public String toString() {
        return "author: " + author.getUserId() + "\nbody: " + textBody;
    }
}
