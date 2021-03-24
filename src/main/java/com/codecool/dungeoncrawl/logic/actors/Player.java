package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.Item;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends Actor {

    private List<Item> inventory = new ArrayList<>();
    private final String[] notWalkable = {"wall", "tree", "statue", "empty"};
    private boolean onItem;
    private int health = 1000;
    private int strength = 100;
    private boolean onDoor = false;
    /*private boolean hasBronzeKey = false;
    private boolean hasSilverKey = false;
    private boolean hasGoldKey = false;*/


    public Player(Cell cell) {
        super(cell);
    }

    public String getTileName() {
        return "player";
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItemToInventory() {
        inventory.add(this.getCell().getItem());
    }

    public boolean isOnItem() {
        return onItem;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isOnDoor() {
        return onDoor;
    }

    @Override
    public void move(int dx, int dy) {
        onItem = false;
        onDoor = false;
        Cell nextCell = cell.getNeighbor(dx, dy);
        System.out.println(nextCell.getTileName());
        if (nextCell.getTileName().equals("doorlvl1in") || nextCell.getTileName().equals("doorlvl2in")){
            onDoor = true;
            //TODO hasKey()
            return;
        }
        if (nextCell.getItem() != null) {
            onItem = true;
        }
        else if (Arrays.asList(notWalkable).contains(nextCell.getTileName())) {
            return;
        } else if (nextCell.getActor() != null) {
            fight(cell.getActor(), nextCell.getActor());
        }
        cell.setActor(null);
        nextCell.setActor(this);
        cell.setItem(null);
        cell = nextCell;
    }

    public void fight(Actor player, Actor enemy) {
        Actor attacker = player;
        Actor defender = enemy;
        Actor temp;
        while (getHealth() > 0 && enemy.getHealth() > 0) {
            attack(attacker, defender);
            temp = attacker;
            attacker = defender;
            defender = temp;
        }
        if (0 >= player.getHealth()){
            System.exit(0);
        }
        if (attacker instanceof Player) {
            player.setHealth(attacker.getHealth());
            player.setStrength(attacker.getStrength()+10);
        } else {
            player.setHealth(defender.getHealth());
            player.setStrength(defender.getStrength()+10);
        }
    }

    public void attack(Actor attacker, Actor defender) {
        int attStrength = attacker.getStrength();
        int defHealth = defender.getHealth();
        defender.setHealth(defHealth-attStrength);
    }
}
