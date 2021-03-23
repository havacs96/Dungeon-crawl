package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Enemy extends Actor {

    private final String enemyName;
    private final int strength;
    private final int healthPoints;
    private EnemyType type;

    public Enemy(Cell cell, EnemyType type) {

        super(cell);
        this.type = type;
        this.enemyName = type.getEnemyName();
        this.strength = type.getStrength();
        this.healthPoints = type.getHealthPoints();

    }

    @Override
    public String getTileName() {
        return enemyName;
    }
}
