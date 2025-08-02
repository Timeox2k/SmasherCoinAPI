package me.timeox2k.smasherCoinAPI.manager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import me.timeox2k.smasherCoinAPI.SmasherCoinAPI;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.sql.*;

public class DatabaseManager {
    private HikariDataSource dataSource;

    public DatabaseManager() {
        final FileConfiguration configuration = SmasherCoinAPI.getInstance().getConfig();
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(configuration.getString("MySQL.host"));
        config.setUsername(configuration.getString("MySQL.username"));
        config.setPassword(configuration.getString("MySQL.password"));
        config.setMaximumPoolSize(10);
        config.setMaxLifetime(600000);
        dataSource = new HikariDataSource(config);

        createTables();
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }

    private void createTables() {
        String sql = "CREATE TABLE IF NOT EXISTS coin_data (" + "uuid VARCHAR(36) NOT NULL PRIMARY KEY, " + "name VARCHAR(16) NOT NULL, " + "coins INT DEFAULT 0" + ");";

        try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addCoins(Player player, int coins) {
        setCoins(player, getCoins(player) + coins);
    }

    public int getCoins(Player player) {
        String sql = "SELECT coins FROM coin_data WHERE uuid = ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("coins");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

        return 0;
    }

    public void setCoins(Player player, int coins) {
        String sql = "UPDATE coin_data SET coins = ? WHERE uuid = ?";
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, coins);
            statement.setString(2, player.getUniqueId().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeCoins(Player player, int coins) {
        setCoins(player, Math.max(getCoins(player) - coins, 0));
    }

    public void createPlayer(Player player) {
        String sql = "INSERT INTO coin_data (uuid, name) VALUES (?, ?) ON DUPLICATE KEY UPDATE name = ?";

        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, player.getName());
            statement.setString(3, player.getName());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            SmasherCoinAPI.getInstance().getLogger().severe("Failed to create player: " + e.getMessage());
        }
    }

}