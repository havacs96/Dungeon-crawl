package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;

public class Player extends Actor {
<<<<<<< HEAD
    private String name;

=======
>>>>>>> dungeon-crawl-1-java-technorbi/master
    public Player(Cell cell) {
        super(cell);
    }

<<<<<<< HEAD
    public Player(Cell cell, String name) {
        super(cell);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

=======
>>>>>>> dungeon-crawl-1-java-technorbi/master
    public String getTileName() {
        return "player";
    }
}
