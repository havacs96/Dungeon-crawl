package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapSaver;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.ItemModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDatabaseManager {
    private PlayerDao playerDao;
    private GameStateDao gameStateDao;
    private PlayerInventoryDao playerInventoryDao;

    public void setup() throws SQLException {
        DataSource dataSource = connect();
        playerDao = new PlayerDaoJdbc(dataSource);
        gameStateDao = new GameStateDaoJdbc(dataSource);
        playerInventoryDao = new PlayerInventoryDaoJdbc(dataSource);

    }

    public void save(Player player, List<GameMap> gameMaps) {
        PlayerModel playerModel = new PlayerModel(player);
        playerDao.add(playerModel);
        saveInventory(player.getInventory(), playerModel);
        saveGameState(gameMaps,playerModel);
    }

    public void saveInventory(List<Item> items, PlayerModel playerModel) {
        for (Item item : items) {
            ItemModel itemModel = new ItemModel(item, playerModel.getId());
            playerInventoryDao.add(itemModel);
        }
    }

    public void saveGameState (List<GameMap> gameMaps, PlayerModel playerModel) {
        List<String> stringMaps = new ArrayList<>();
        for (GameMap gameMap : gameMaps) {
            MapSaver mapSaver = new MapSaver();
            stringMaps.add(mapSaver.SaveMap(gameMap));
        }
        GameState gameState = new GameState(stringMaps.get(0), stringMaps.get(1), stringMaps.get(2), playerModel.getId());
        gameStateDao.add(gameState);
    }

    private DataSource connect() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        String dbName = System.getenv("PSQL_DB_NAME");
        String user = System.getenv("PSQL_USERNAME");
        String password = System.getenv("PSQL_PASSWORD");

        dataSource.setDatabaseName(dbName);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        System.out.println("Trying to connect");
        dataSource.getConnection().close();
        System.out.println("Connection ok.");

        return dataSource;
    }

    public boolean isPlayerExistsInDb(String playerName) {
        return playerDao.getId(playerName) != -1;
    }
}
