package com.codecool.dungeoncrawl.logic;

public class MapSaver {

    public void saveMapToSql(GameMap map){
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Cell cell = map.getCell(x, y);
            }
        }
    }
}
