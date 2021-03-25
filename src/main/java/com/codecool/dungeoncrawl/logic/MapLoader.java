package com.codecool.dungeoncrawl.logic;
import com.codecool.dungeoncrawl.logic.actors.EnemyType;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.Enemy;
import com.codecool.dungeoncrawl.logic.items.*;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(String mapT) {
        InputStream is = MapLoader.class.getResourceAsStream(mapT);
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
                        case '4':
                            cell.setType(CellType.GRAVE);
                            break;
                        // Enemies
                        case 's':
                            cell.setType(CellType.FLOOR2);
                            new Enemy(cell, EnemyType.SKELETON);
                            break;
                        case 'm':
                            cell.setType(CellType.FLOOR);
                            new Enemy(cell, EnemyType.MONSTER);
                            break;
                        case 'h':
                            cell.setType(CellType.FLOOR3);
                            new Enemy(cell, EnemyType.HELLBOY);
                            break;
                        case 'x':
                            cell.setType(CellType.FLOOR);
                            new Enemy(cell, EnemyType.WIZZARD);
                            break;
                        case 'y':
                            cell.setType(CellType.FLOOR2);
                            new Enemy(cell, EnemyType.KNIGHT);
                            break;
                        case 'z':
                            cell.setType(CellType.FLOOR3);
                            new Enemy(cell, EnemyType.MOLTEN);
                            break;
                        // Player
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        // Keys
                        case 'B':
                            cell.setType(CellType.FLOOR);
                            new Key(cell, KeyType.BRONZE_KEY);
                            break;
                        case 'i':
                            cell.setType(CellType.FLOOR2);
                            new Key(cell, KeyType.SILVER_KEY);
                            break;
                        case 'b':
                            cell.setType(CellType.FLOOR);
                            new Potion(cell, PotionType.STRONG_HEALTH_POTION);
                            break;
                        case '6':
                            cell.setType(CellType.FLOOR);
                            new Potion(cell, PotionType.WEAK_HEALTH_POTION);
                            break;
                        case '9':
                            cell.setType(CellType.FLOOR);
                            new Weapon(cell, WeaponType.CROSSBOW);
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
