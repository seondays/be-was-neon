package model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

public class User {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private String userId;
    private String password;
    private String name;
    private String email;

    public User(String userId, String password, String name, String email) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        logger.debug(this.toString());
    }

    public User(Map<String, String> info) {
        this.userId = info.get("userId");
        this.password = info.get("password");
        this.name = nameDecoder(info.get("name"));
        this.email = info.get("email");
        logger.debug(this.toString());
    }

    private String nameDecoder(String name) {
        try {
            name = URLDecoder.decode(name,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
    }
}
