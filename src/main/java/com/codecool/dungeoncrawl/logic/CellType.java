package com.codecool.dungeoncrawl.logic;

public enum CellType {

    // can collide
    FLOOR("floor"),
    FLOOR2("floor2"),
    FLOOR3("floor3"),
    LAVA("lava"),
    DOOR1("doorlvl1out"),
    DOOR2("doorlvl2in"),
    DOOR3("doorlvl2out"),
    DOOR4("doorlvl3in"),

    // cannot collide
    EMPTY("empty"),
    WALL("wall"),
    WALL2("wall2"),
    WALL3("wall3"),
    TREE("tree"),
    STATUE("statue"),
    STATUE2("statue2"),
    ALTAR("altar"),
    GRAVE("grave"),

    // potions

    FULLHP("fullpot");

    // items

    // keys




    private final String tileName;

    CellType(String tileName) {
        this.tileName = tileName;
    }

    public String getTileName() {
        return tileName;
    }

}
