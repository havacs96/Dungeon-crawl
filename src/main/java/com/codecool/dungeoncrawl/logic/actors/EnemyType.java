package com.codecool.dungeoncrawl.logic.actors;

public enum EnemyType {

    MONSTER("monster", 10, 300),
    SKELETON("skeleton", 20, 400),
    HELLBOY("hellboy", 30, 800);

    private final String enemyName;
    private final int strength;
    private final int health;

    EnemyType(String enemyName, int strength, int health) {
        this.enemyName = enemyName;
        this.strength = strength;
        this.health = health;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public int getStrength() {
        return strength;
    }

    public int getHealth() {
        return health;
    }



}
