package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Comments implements Iterable<Comment> {
    private final List<Comment> comments;

    public Comments() {
        comments = new ArrayList<>();
    }

    @Override
    public Iterator<Comment> iterator() {
        return comments.iterator();
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public int size() {
        return comments.size();
    }
}