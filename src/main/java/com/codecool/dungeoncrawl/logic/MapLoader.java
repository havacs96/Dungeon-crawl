package com.codecool.dungeoncrawl.logic;
import com.codecool.dungeoncrawl.logic.actors.EnemyType;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Enemy;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap() {
        InputStream is = MapLoader.class.getResourceAsStream("/map3.txt");
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
                        // Collideables
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '&':
                            cell.setType(CellType.WALL2);
                            break;
                        case '$':
                            cell.setType(CellType.WALL3);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case '_':
                            cell.setType(CellType.FLOOR2);
                            break;
                        case '|':
                            cell.setType(CellType.LAVA);
                            break;
                        case 'ß':
                            cell.setType(CellType.FLOOR3);
                            break;
                        case 'D':
                            cell.setType(CellType.DOOR1);
                            break;
                        case 'U':
                            cell.setType(CellType.DOOR2);
                            break;
                        case 'P':
                            cell.setType(CellType.DOOR3);
                            break;
                        case 'I':
                            cell.setType(CellType.DOOR4);
                            break;
                        case 'T':
                            cell.setType(CellType.TREE);
                            break;
                        case '1':
                            cell.setType(CellType.STATUE);
                            break;
                        case '3':
                            cell.setType(CellType.STATUE2);
                            break;
                        case '2':
                            cell.setType(CellType.ALTAR);
                            break;
                        // Enemies
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
                        // Player
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        // Keys
                        case 'K':
                            cell.setType(CellType.KEYLVL1);
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
