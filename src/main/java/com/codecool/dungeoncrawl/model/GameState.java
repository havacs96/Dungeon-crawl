package com.codecool.dungeoncrawl.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;

public class GameState extends BaseModel {
    private Timestamp savedAt;
    private int currentMap;
    private String map1;
    private String map2;
    private String map3;
    private int playerID;

    public GameState(String map1, String map2, String map3, int playerID) {
        this.map1 = map1;
        this.map2 = map2;
        this.map3 = map3;
        this.playerID = playerID;
    }

    public Timestamp getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(Timestamp savedAt) {
        this.savedAt = savedAt;
    }

    public int getCurrentMap () {
        return currentMap;
    }

    public int getCurrentMap(String text) {
        String numberOnly = text.replaceAll("[^0-9]", "");
        return Integer.parseInt(numberOnly);
    }

    public void setCurrentMap(int currentMap) {
        this.currentMap = currentMap;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getMap1() {
        return map1;
    }

    public String getMap2() {
        return map2;
    }

    public String getMap3() {
        return map3;
    }
}
