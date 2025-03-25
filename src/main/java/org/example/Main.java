package org.example;


import org.example.connection.DataBaseWorker;
import org.example.entity.User;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        DataBaseWorker dbWorker = new DataBaseWorker();

        User foundUser = dbWorker.getUserByLogin("user1");
        System.out.println("Найден пользователь: " + foundUser);

        User newUser = new User(
                "Vanhik",
                "password",
                "Vanhik97@yandex.ru",
                new Date()
        );
        int affectedRows = dbWorker.insertUser(newUser);
        System.out.println("Добавлено строк: " + affectedRows);

        User checkUser = dbWorker.getUserByLogin("Vanhik");
        System.out.println("Проверка добавленного пользователя: " + checkUser);

    }
}