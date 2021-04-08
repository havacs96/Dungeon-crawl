package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.ItemModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import java.util.List;

public interface PlayerInventoryDao {
        void add(ItemModel item);
        void remove(ItemModel item);
        List<ItemModel> getAllPlayerItems(PlayerModel player, int id);
}
