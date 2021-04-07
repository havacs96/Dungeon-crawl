package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.*;
import com.codecool.dungeoncrawl.model.ItemModel;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerInventoryDaoJdbc implements PlayerInventoryDao{
    private DataSource dataSource;

    public PlayerInventoryDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void add(ItemModel item) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player_inventory (player_id, item_name, item_type) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, item.getPlayerID());
            statement.setString(2, item.getItemName());
            statement.setString(3, item.getItemType());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            item.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding a new item",e);
        }
    }

    @Override
    public void remove(ItemModel item) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM player_inventory WHERE player_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, item.getPlayerID());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding a new item",e);
        }
    }

    public List<ItemModel> getAllPlayerItems(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT player_id, item_name, item_type FROM player_inventory WHERE player_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, player.getId());
            ResultSet rs = statement.executeQuery(sql);
            List<ItemModel> itemModels = new ArrayList<>();

            if (!rs.isBeforeFirst()) {
                return null;
            }

            while(!rs.next()){
                ItemModel itemModel = new ItemModel(createDifferentItems(rs.getString(3), rs.getString(2)), player.getId());
                itemModels.add(itemModel);
            }
            return itemModels;

        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all items",e);
        }
    }

    public Item createDifferentItems(String itemType, String itemName) {
        switch (itemType) {
            case "Key":
                for (KeyType keyType : KeyType.values()) {
                    if (keyType.getKeyName().equals(itemName)) {
                        return new Key(null, keyType);
                    }
                }
                break;
            case "Weapon":
                for (WeaponType weaponType : WeaponType.values()) {
                    if (weaponType.getWeaponName().equals(itemName)) {
                        return new Weapon(null, weaponType);
                    }
                }
                break;
            case "Potion":
                for (PotionType potionType : PotionType.values()) {
                    if (potionType.getPotionName().equals(itemName)) {
                        return new Potion(null, potionType);
                    }
                }
                break;
        }
        return null;
    }
}
