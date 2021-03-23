package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles2.png", 539 * 6, 538 * 6, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        // Simple
        tileMap.put("empty", new Tile(0, 0)); // ' '
        tileMap.put("wall", new Tile(6, 19)); // #
        tileMap.put("wall2", new Tile(44, 18)); // &
        tileMap.put("floor", new Tile(12, 9)); // .
        tileMap.put("floor2", new Tile(49, 6)); // _
        tileMap.put("doorlvl1in", new Tile(36, 10)); // D
        tileMap.put("doorlvl1out", new Tile(38, 10)); // U
        tileMap.put("doorlvl2in", new Tile(46, 10)); // P
        tileMap.put("doorlvl2out", new Tile(46, 10)); // O
        tileMap.put("doorlvl3in", new Tile(46, 10)); // I
        tileMap.put("tree", new Tile(13, 13)); // T
        tileMap.put("statue", new Tile(27, 13)); // 1
        // Player
        tileMap.put("player", new Tile(19, 68)); // @
        // Enemies
        tileMap.put("monster", new Tile(20, 60)); // M
        tileMap.put("skeleton", new Tile(17, 74)); // S
        tileMap.put("hellboy", new Tile(24, 65)); // H
        tileMap.put("ghost", new Tile(24, 65)); // G
        // Keys
        tileMap.put("bronze", new Tile(55, 40)); // K
        tileMap.put("silver", new Tile(56, 40)); // K
        tileMap.put("gold", new Tile(56, 40)); // K
        // Items
        tileMap.put("crossbow", new Tile(16, 49)); // c
        tileMap.put("sword", new Tile(15, 48)); // s
        tileMap.put("axe", new Tile(29, 48)); // a
        // Potions
        tileMap.put("smallhealth", new Tile(23, 42)); // p
        tileMap.put("mediumhealth", new Tile(23, 43)); // m
        tileMap.put("bighealth", new Tile(23, 44));  // b
        // TODO bosses
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}
