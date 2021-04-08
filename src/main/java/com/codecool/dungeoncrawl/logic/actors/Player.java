package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.items.*;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends Actor {

    private List<Item> inventory = new ArrayList<>();
    private final String[] notWalkable = {"wall", "wall2", "wall3", "tree", "statue", "statue2",
            "empty", "grave", "altar" };
    private boolean onItem;
    private int health = 1000;
    private int strength = 100;
    private boolean onDoorDown = false;
    private boolean onDoorUp = false;
    private String name;
    private int loadedX;
    private int loadedY;


    public Player(Cell cell) {
        super(cell);
    }

    public void usePotion(String potionType) {
        List<String> potionNames = new ArrayList<>();
        for (Item item : inventory) {
            if (item instanceof Potion) {
                potionNames.add(item.getTileName());
            }
        }
        switch (potionType){
            case "w":
                if (potionNames.contains("weak healing potion")){
                    this.setHealth(this.getHealth() + PotionType.WEAK_HEALTH_POTION.getHealthPlus());
                    inventory.remove(findPotion(PotionType.WEAK_HEALTH_POTION, "weak healing potion"));
                }
                break;
            case "s":
                if (potionNames.contains("strong healing potion")){
                    this.setHealth(this.getHealth() + PotionType.STRONG_HEALTH_POTION.getHealthPlus());
                    inventory.remove(findPotion(PotionType.STRONG_HEALTH_POTION, "strong healing potion"));
                }
                break;
            case "e":
                if (potionNames.contains("extra healing potion")){
                    this.setHealth(this.getHealth() + PotionType.EXTRA_HEALTH_POTION.getHealthPlus());
                    inventory.remove(findPotion(PotionType.EXTRA_HEALTH_POTION, "extra healing potion"));
                }
                break;
        }
    }

    public Potion findPotion(PotionType type, String efficiency) {
        for (Item item : inventory) {
            if(item.getTileName().equals(efficiency)) {
                return (Potion) item;
            }
        }
        return null;
    }

    public String getTileName() {
        return "player";
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void addItemToInventoryOnLoad(Item item){inventory.add(item);}

    public void addItemToInventory() {
        inventory.add(this.getCell().getItem());
        if(this.getCell().getItem() instanceof Weapon){addStatsToPlayer();}
    }

    public void addStatsToPlayer(){
        Weapon weapon = (Weapon) this.getCell().getItem();
        if(weapon.getTileName().equals("crossbow")) {
            this.setHealth(this.getHealth() + WeaponType.CROSSBOW.getPlusHealthPoints());
            this.setStrength(this.getStrength() + WeaponType.CROSSBOW.getPlusStrength());
        } else if (weapon.getTileName().equals("axe")) {
            this.setHealth(this.getHealth() + WeaponType.AXE.getPlusHealthPoints());
            this.setStrength(this.getStrength() + WeaponType.AXE.getPlusStrength());
        } else if (weapon.getTileName().equals("sword")) {
            this.setHealth(this.getHealth() + WeaponType.SWORD.getPlusHealthPoints());
            this.setStrength(this.getStrength() + WeaponType.SWORD.getPlusStrength());
        }
    }

    public void clearInventory() {this.inventory.clear();}

    public void removeItem() {
        this.getCell().setItem(null);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOnDoorDown() {
        return onDoorDown;
    }

    public boolean isOnDoorUp() {
        return onDoorUp;
    }

    public int getLoadedX() {
        return loadedX;
    }

    public void setLoadedX(int loadedX) {
        this.loadedX = loadedX;
    }

    public int getLoadedY() {
        return loadedY;
    }

    public void setLoadedY(int loadedY) {
        this.loadedY = loadedY;
    }

    @Override
    public void move(int dx, int dy) {
        onItem = false;
        onDoorDown = false;
        onDoorUp = false;
        Cell nextCell = cell.getNeighbor(dx, dy);
        if (nextCell.getTileName().equals("doorlvl1out") || nextCell.getTileName().equals("doorlvl2out")){
            if(!hasKey(nextCell.getTileName())) {return;}
            onDoorDown = true;
            cell.setActor(null);
            return;
        }
        if (nextCell.getTileName().equals("doorlvl2in") || nextCell.getTileName().equals("doorlvl3in")){
            onDoorUp = true;
            cell.setActor(null);
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
        if (nextCell.getTileName().equals("lava")){this.setHealth(this.getHealth() - 25);}
        cell.setActor(null);
        nextCell.setActor(this);
        cell = nextCell;
    }

    public void fight(Actor player, Actor enemy) {
        Actor attacker = player;
        Actor defender = enemy;
        Actor temp;
        while (this.getHealth() > 0 && enemy.getHealth() > 0) {
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

    public boolean hasKey(String door){
        List<String> keyNames = new ArrayList<>();
        for (Item item : inventory) {
            if (item instanceof Key){
                keyNames.add(item.getTileName());
            }
        }
        if(door.equals("doorlvl1out") && keyNames.contains("bronze")){return true;}
        else if(door.equals("doorlvl2out") && keyNames.contains("silver")){return true;}
        return false;
    }
}
