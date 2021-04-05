package com.codecool.dungeoncrawl.logic.actors;

public enum EnemyType {

    MONSTER("monster", 10, 300),
    SKELETON("skeleton", 20, 400),
    HELLBOY("hellboy", 30, 800),
    WIZZARD("wizzard", 40, 400), //40
    KNIGHT("knight", 80, 800), //80
    MOLTEN("molten", 160, 1600); // 160

    private final String enemyName;
    private final int strength;
    private int health;

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
