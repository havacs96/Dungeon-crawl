package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.RandomHelper;

import java.util.Arrays;

public class Enemy extends Actor {

    private final String enemyName;
    private final int strength;
    private int health;
    private EnemyType type;
    private final int directionOptions = 4;
    private final String[] notWalkable = {"wall", "wall2", "wall3", "tree", "statue",
            "statue2", "empty", "grave", "doorlvl1out", "doorlvl2out", "doorlvl2in", "doorlvl3in", "altar"};

    public Enemy(Cell cell, EnemyType type) {

        super(cell);
        this.type = type;
        this.enemyName = type.getEnemyName();
        this.strength = type.getStrength();
        this.health = type.getHealth();

    }

    @Override
    public String getTileName() {
        return enemyName;
    }

    public String getEnemyName() {
        return enemyName;
    }

    @Override
    public int getStrength() {
        return strength;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public void setHealth(int health) {
        this.health = health;
    }

    public boolean checkEnemyMove(Cell cell){
        if (!(Arrays.asList(notWalkable).contains(cell.getTileName()))
                && cell.getActor() == null && cell.getItem() == null){ return true;}
        return false;
    }

    public void changeEnemyPosition(Cell nextCell){
        if (checkEnemyMove(nextCell)){
            cell.setActor(null);
            nextCell.setActor(this);
            cell = nextCell;
        }
    }


    public void monsterMove() {
        RandomHelper random = new RandomHelper();
        int direction = random.getRandomValue(directionOptions);
        Cell nextCell;
        switch (direction) {
            case 0: //up
                nextCell = cell.getNeighbor(0, -1);
                changeEnemyPosition(nextCell);
                break;
            case 1: //down
                nextCell = cell.getNeighbor(0, 1);
                changeEnemyPosition(nextCell);
                break;
            case 2: //left
                nextCell = cell.getNeighbor(-1, 0);
                changeEnemyPosition(nextCell);
                break;
            case 3: //right
                nextCell = cell.getNeighbor(1, 0);
                changeEnemyPosition(nextCell);
                break;
        }
    }
}
