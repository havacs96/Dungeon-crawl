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
            System.out.println("1");
            ResultSet rs = st.executeQuery();
            System.out.println("2");
            if (!rs.isBeforeFirst()) {
                return null;
            }
            System.out.println("3");
            if(!rs.next()){
                return null;
            }
            System.out.println("4");
            Player player = new Player(null);
            System.out.println("5");
            player.setName(rs.getString(1));
            System.out.println("6");
            player.setStrength(rs.getInt(2));
            System.out.println("7");
            player.setHealth(rs.getInt(3));
            System.out.println("8");
            player.setLoadedX(rs.getInt(4));
            System.out.println("9");
            player.setLoadedY(rs.getInt(5));
            System.out.println("10");

            PlayerModel playerModel = new PlayerModel(player);
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
            while(!rs.next()){

                Player player = new Player(null);
                player.setName(rs.getString(1));
                player.setStrength(rs.getInt(2));
                player.setHealth(rs.getInt(3));
                player.setLoadedX(rs.getInt(4));
                player.setLoadedY(rs.getInt(5));

                PlayerModel playerModel = new PlayerModel(player);
                playerModel.setPlayerName(rs.getString(1));
                playerModel.setStrength(rs.getInt(2));
                playerModel.setHealth(rs.getInt(3));
                playerModel.setX(rs.getInt(4));
                playerModel.setY(rs.getInt(5));
            }
            return playerModels;

        } catch (SQLException e) {
            throw new RuntimeException("Error while reading all players",e);
        }
    }

    public int getId(String playerName) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT id FROM player " +
                    "WHERE player_name LIKE ?";
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
