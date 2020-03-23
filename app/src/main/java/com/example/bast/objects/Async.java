package com.example.bast.objects;

public class Async {
    public static void task(Runnable r) {
        new Thread(r).start();
    }
}
