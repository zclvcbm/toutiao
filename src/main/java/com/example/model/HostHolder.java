package com.example.model;

import org.springframework.stereotype.Component;

/**
 * Created by Admin on 2016/7/8.
 */
@Component
public class HostHolder {
    public static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
