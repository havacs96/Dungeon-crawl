package com.codecool.dungeoncrawl.logic.actors;

public enum EnemyType {

    MONSTER("monster", 40, 300),
    SKELETON("skeleton", 50, 400),
    HELLBOY("hellboy", 100, 800);

    private final String enemyName;
    private final int strength;
    private final int healthPoints;

    EnemyType(String enemyName, int strength, int healthPoints) {
        this.enemyName = enemyName;
        this.strength = strength;
        this.healthPoints = healthPoints;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public int getStrength() {
        return strength;
    }

    public int getHealthPoints() {
        return healthPoints;
    }



}
