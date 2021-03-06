package com.codecool.dungeoncrawl.dao;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.model.PlayerModel;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDaoJdbc implements PlayerDao {
    private DataSource dataSource;

    public PlayerDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void add(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO player (player_name, strength, health, x, y) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, player.getPlayerName());
            statement.setInt(2, player.getStrength());
            statement.setInt(3, player.getHealth());
            statement.setInt(4, player.getX());
            statement.setInt(5, player.getY());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            player.setId(resultSet.getInt(1));
            System.out.println(resultSet.getInt(1));
        } catch (SQLException e) {
            throw new RuntimeException("Error while adding a new player",e);
        }
    }

    @Override
    public void update(PlayerModel player) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "UPDATE player SET player_name = ?, strength = ?, health = ?, x = ?, y = ? " +
                    "WHERE player_name LIKE ?";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1, player.getPlayerName());
            st.setInt(2, player.getStrength());
            st.setInt(3, player.getHealth());
            st.setInt(4, player.getX());
            st.setInt(5, player.getY());
            st.setString(6, player.getPlayerName());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating a player",e);
        }
    }

    @Override
    public PlayerModel get(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT player_name, strength, health, x, y FROM player " +
                    "WHERE id = ? ";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if (!rs.isBeforeFirst()) {
                return null;
            }
            if(!rs.next()){
                return null;
            }
            String playerName = rs.getString(1);
            int x = rs.getInt(4);
            int y = rs.getInt(5);

            PlayerModel playerModel = new PlayerModel(playerName, x, y);
            playerModel.setPlayerName(rs.getString(1));
            playerModel.setStrength(rs.getInt(2));
            playerModel.setHealth(rs.getInt(3));
            playerModel.setX(rs.getInt(4));
            playerModel.setY(rs.getInt(5));

            return playerModel;

        } catch (SQLException e) {
            throw new RuntimeException("Error while searching for a player", e);
        }
    }

    @Override
    public List<PlayerModel> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT player_name, strength, health, x, y FROM player";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            List<PlayerModel> playerModels = new ArrayList<>();
            if (!rs.isBeforeFirst()) {
                return null;
            }
            while(rs.next()){
                String playerName = rs.getString(1);
                int x = rs.getInt(4);
                int y = rs.getInt(5);

                PlayerModel playerModel = new PlayerModel(playerName, x, y);
                playerModel.setPlayerName(rs.getString(1));
                playerModel.setStrength(rs.getInt(2));
                playerModel.setHealth(rs.getInt(3));
                playerModel.setX(rs.getInt(4));
                playerModel.setY(rs.getInt(5));
                playerModels.add(playerModel);
            }
            return playerModels;

        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all players",e);
        }
    }

    public int getId(String playerName) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id FROM player " +
                    "WHERE player_name = ? ";
            PreparedStatement st = conn.prepareStatement(sql);
            st.setString(1,playerName);
            ResultSet rs = st.executeQuery();
            if (!rs.isBeforeFirst()) {
                return -1;
            }
            if(!rs.next()){
                return -1;
            }
            return rs.getInt(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error while getting id of player",e);
        }
    }
}
