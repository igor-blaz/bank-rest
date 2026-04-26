package com.example.bankcards.factory.user;

import com.example.bankcards.entity.User;
import com.example.bankcards.enums.Role;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserTestFactory {
    public User makeUser() {
        return new User(1L, "Igor", "Ivanov", "Igor",23, Role.USER, "12345678910");
    }

    public User makeUser(Long id) {
        return new User(id, "Igor", "Ivanov", "Igor",23, Role.USER, "12345678910");
    }

    public List<User> makeUserList(int size) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(makeUser());
        }
        return list;
    }
}
