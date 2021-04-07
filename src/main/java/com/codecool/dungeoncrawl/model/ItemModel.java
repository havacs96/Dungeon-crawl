package com.codecool.dungeoncrawl.model;

import com.codecool.dungeoncrawl.logic.items.Item;

public class ItemModel extends BaseModel{
    int playerID;
    String itemName;
    String itemType;

    public ItemModel(Item item, int playerID) {
        this.playerID = playerID;
        this.itemName = item.getTileName();
        this.itemType = item.getType();
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
