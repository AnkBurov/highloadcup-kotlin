package ru.highloadcup.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UsersDto implements Serializable {

    private List<User> users = new ArrayList<>();

    public UsersDto() {
    }

    public UsersDto(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
