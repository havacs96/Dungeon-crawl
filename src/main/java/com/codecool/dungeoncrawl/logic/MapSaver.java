package com.codecool.dungeoncrawl.logic;
import com.codecool.dungeoncrawl.logic.actors.Player;


public class MapSaver {
    private String mapText = new String("");

    public String SaveMap(GameMap map) {
        int width = map.getWidth();
        int height = map.getHeight();
        mapText += String.valueOf(width) + " " + String.valueOf(height) + "\n";

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = map.getCell(x, y);
                switch (cell.getTileName()) {
                    // Collideables
                    case "empty":
                        mapText += " ";
                        break;
                    case "floor":
                        if(!addStringToMapText(cell)){
                            mapText += ".";
                        }
                        break;
                    case "floor2":
                        if(!addStringToMapText(cell)){
                            mapText += "_";
                        }
                        break;
                    case "floor3":
                        if(!addStringToMapText(cell)){
                            mapText += "ß";
                        }
                        break;
                    case "lava":
                        if(!addStringToMapText(cell)){
                            mapText += "|";
                        }
                        break;
                    case "doorlvl1out":
                        mapText += "D";
                        break;
                    case "doorlvl2in":
                        mapText += "U";
                        break;
                    case "doorlvl2out":
                        mapText += "P";
                        break;
                    case "doorlvl3in":
                        mapText += "I";
                        break;
                    case "wall":
                        mapText += "#";
                        break;
                    case "wall2":
                        mapText += "&";
                        break;
                    case "wall3":
                        mapText += "$";
                        break;
                    case "tree":
                        mapText += "T";
                        break;
                    case "statue":
                        mapText += "1";
                        break;
                    case "statue2":
                        mapText += "3";
                        break;
                    case "altar":
                        mapText += "2";
                        break;
                    case "grave":
                        mapText += "4";
                        break;
                    default:
                        throw new RuntimeException("Unrecognized character: ");
                }
            }
            mapText += "\n";
        }
        return mapText;
    }

    public boolean addStringToMapText(Cell cell){
        if (cell.getActor() != null) {
            mapText += checkActor(cell);
            return true;
        } else if (cell.getItem() != null) {
            mapText += checkItem(cell);
            return true;
        }
        return false;
    }


    public String checkActor(Cell cell) {
        if (cell.getActor() instanceof Player) {
            return "@";
        } else {
            switch (cell.getActor().getTileName()) {
                case "monster":
                    return "m";
                case "skeleton":
                    return "s";
                case "hellboy":
                    return "h";
                case "wizzard":
                    return "x";
                case "knight":
                    return "y";
                case "molten":
                    return "z";
            }
        }
        return "";
    }

    public String checkItem(Cell cell) {
        if (cell.getItem() != null) {
            switch (cell.getItem().getTileName()) {
                case "bronze":
                    return "B";
                case "silver":
                    return "i";
                case "strong healing potion":
                    return "b";
                case "weak healing potion":
                    return "6";
                case "extra healing potion":
                    return "q";
                case "crossbow":
                    return "9";
                case "sword":
                    return "Y";
                case "axe":
                    return "X";
            }
        }
        return "";
    }
}