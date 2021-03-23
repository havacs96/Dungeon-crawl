package com.codecool.dungeoncrawl.logic;

public enum CellType {

    // can collide
    FLOOR("floor"),
    DOOR1("caveentrance"),

    // cannot collide
    EMPTY("empty"),
    WALL("wall"),
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
