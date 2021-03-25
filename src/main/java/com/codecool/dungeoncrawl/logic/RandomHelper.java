package com.codecool.dungeoncrawl.logic;

import java.util.Random;

public class RandomHelper {

    public int getRandomValue(int max){
        Random random = new Random();
        return random.nextInt(max);
    }
}
