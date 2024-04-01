package db;

import java.util.ArrayList;
import java.util.List;
import model.Article;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, List<Article>> articles = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static void addArticle(String userId, Article article) {
        articles.computeIfAbsent(userId, k -> new ArrayList<>());
        articles.get(userId).add(article);
    }

    // todo : 최신 글이 아래로 나오는 문제 해결
    public static List<Article> findArticle(String userId) {
        if (articles.get(userId) != null) {
            return articles.get(userId);
        }
        return new ArrayList<>();
    }
}
