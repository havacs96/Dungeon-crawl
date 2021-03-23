package com.codecool.dungeoncrawl.logic;

public enum CellType {

    // can collide
    FLOOR("floor"),
    FLOOR2("floor2"),
    DOOR1("doorlvl1in"),
    DOOR2("doorlvl1out"),

    // cannot collide
    EMPTY("empty"),
    WALL("wall"),
    WALL2("wall2"),
    TREE("tree"),
    STATUE("statue"),

    // potions

    FULLHP("fullpot"),

    // items

    // keys

    KEYLVL1("key"),
    KEYLVL2("key2"),
    KEYLVL3("key2");



    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }

}
