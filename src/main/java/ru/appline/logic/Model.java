package ru.appline.logic;

import lombok.Getter;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Model implements Serializable {

    private static final Model instance = new Model();
    private final Map<Integer, User> model;

    public static Model getInstance() {
        return instance;
    }

    private Model() {
        model = new HashMap<>();
        model.put(1, new User("Ivan", "Ivanov", 100000));
        model.put(2, new User("Anton", "Osipov", 50000));
        model.put(3, new User("Zoya", "Andreeva", 75000));
    }

    public void add(User user) {
        model.put(Collections.max(model.keySet()) + 1, user);
    }
}
