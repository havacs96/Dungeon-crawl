package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Player;

import com.codecool.dungeoncrawl.logic.actors.Enemy;
import com.codecool.dungeoncrawl.logic.actors.EnemyType;
import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private Player player;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell getDoorDown() {
        for (Cell[] cell : cells) {
            for (Cell doorCell : cell) {
                if (doorCell.getType() == CellType.DOOR2 || doorCell.getType() == CellType.DOOR4) {
                    return doorCell;
                }
            }
        }
        return null;
    }

    public Cell getDoorUp() {
        for (Cell[] cell : cells) {
            for (Cell doorCell : cell) {
                if (doorCell.getType() == CellType.DOOR1 || doorCell.getType() == CellType.DOOR3) {
                    return doorCell;
                }
            }
        }
        return null;
    }

    public List<Enemy> getEnemiesOnCurrentMap(){
        List<Enemy> enemies = new ArrayList<>();
        for (Cell[] cell : cells) {
            for (Cell enemyCell : cell) {
                if (enemyCell.getActor() instanceof Enemy){
                    enemies.add(enemyCell.getEnemy());
                }
            }
        }
        return enemies;
    }

}
