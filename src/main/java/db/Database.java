package db;

import model.Article;
import model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Map<String, User> users = new HashMap<>();
    private static Map<Integer, Article> articles = new HashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static void addArticle(Article article) {
        articles.put(article.getArticleID(), article);
    }

    // todo : 최신 글이 아래로 나오는 문제 해결
    public static Collection<Article> findAllArticle() {
        return articles.values();
    }

    public static Article findArticle(int articleId){
        return articles.get(articleId);
    }
}
