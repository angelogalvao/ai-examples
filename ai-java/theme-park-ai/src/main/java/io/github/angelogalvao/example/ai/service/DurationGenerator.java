package io.github.angelogalvao.example.ai.service;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Random;

@ApplicationScoped
public class DurationGenerator {

    private static Random r = new Random();

    public long randomDurantion() {
        return r.nextLong(0, 90);
    }
}
