package com.codecool.dungeoncrawl.logic;
import com.codecool.dungeoncrawl.logic.actors.EnemyType;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Enemy;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/map.txt");
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        // enemies
                        case 's':
                            cell.setType(CellType.FLOOR);
                            map.setEnemy(new Enemy(cell, EnemyType.SKELETON));
                            break;
                        case 'm':
                            cell.setType(CellType.FLOOR);
                            map.setEnemy(new Enemy(cell, EnemyType.MONSTER));
                            break;
                        case 'h':
                            cell.setType(CellType.FLOOR);
                            map.setEnemy(new Enemy(cell, EnemyType.HELLBOY));
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case 'D':
                            cell.setType(CellType.DOOR1);
                            break;
                        case 'T':
                            cell.setType(CellType.TREE);
                            break;
                        case '1':
                            cell.setType(CellType.STATUE);
                            break;
                        case 'K':
                            cell.setType(CellType.KEYLVL1);
                            break;
                        case 'M':
                            cell.setType(CellType.KEYLVL2);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
