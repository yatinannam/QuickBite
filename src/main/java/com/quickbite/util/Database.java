package com.quickbite.util;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.quickbite.controllers.MenuItem;

public class Database {
    private static Database instance;
    private Connection conn;
    private final String DB_URL = "jdbc:sqlite:db/quickbite.db";

    private Database() {
    }

    public static synchronized Database getInstance() {
        if (instance == null)
            instance = new Database();
        return instance;
    }

    public void init() {
        try {
            // Create db directory if missing
            java.nio.file.Path path = java.nio.file.Paths.get("db");
            if (!java.nio.file.Files.exists(path)) {
                java.nio.file.Files.createDirectories(path);
            }

            conn = DriverManager.getConnection(DB_URL);
            createUsersTable();
            createMenuTable();
            seedAdminUser();
            seedMenuItems();
            System.out.println("Database connected and ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ------------------- USER TABLE -------------------

    private void createUsersTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "displayname TEXT" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private void seedAdminUser() throws SQLException {
        String check = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setString(1, "admin");
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                String hashed = BCrypt.hashpw("admin123", BCrypt.gensalt(12));
                String insert = "INSERT INTO users (username, password, displayname) VALUES (?, ?, ?)";
                try (PreparedStatement ins = conn.prepareStatement(insert)) {
                    ins.setString(1, "admin");
                    ins.setString(2, hashed);
                    ins.setString(3, "Administrator");
                    ins.executeUpdate();
                }
                System.out.println("Default admin user created: admin / admin123");
            }
        }
    }

    public boolean validateLogin(String username, String password) {
        String sql = "SELECT password FROM users WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String hashed = rs.getString("password");
                return BCrypt.checkpw(password, hashed);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ------------------- MENU TABLE -------------------

    private void createMenuTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS menu (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "price REAL NOT NULL, " +
                "description TEXT)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    private void seedMenuItems() throws SQLException {
        String checkSql = "SELECT COUNT(*) AS count FROM menu";
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(checkSql)) {
            if (rs.next() && rs.getInt("count") == 0) {
                String insert = """
                            INSERT INTO menu (name, price, description) VALUES
                            -- Starters
                            ('Garlic Bread', 90.0, 'Crispy garlic bread with herbs'),
                            ('Tomato Soup', 70.0, 'Fresh tomato soup with cream'),
                            ('Veg Spring Rolls', 100.0, 'Crispy rolls stuffed with veggies'),

                            -- Main Course
                            ('Paneer Butter Masala', 180.0, 'Rich paneer curry with butter and spices'),
                            ('Chicken Biryani', 220.0, 'Fragrant rice with tender chicken'),
                            ('Veg Fried Rice', 140.0, 'Basmati rice with sautéed vegetables'),
                            ('Cheese Burger', 130.0, 'Grilled patty, cheese, lettuce, and sauce'),
                            ('Margherita Pizza', 180.0, 'Classic pizza with mozzarella and basil'),

                            -- Beverages
                            ('Cold Coffee', 90.0, 'Chilled coffee with cream'),
                            ('Lemon Iced Tea', 70.0, 'Refreshing citrus iced tea'),
                            ('Orange Juice', 80.0, 'Freshly squeezed orange juice'),

                            -- Desserts
                            ('Chocolate Brownie', 120.0, 'Served warm with vanilla ice cream'),
                            ('Gulab Jamun', 90.0, 'Soft sweet dumplings in syrup'),
                            ('Ice Cream Sundae', 130.0, 'Scoops topped with nuts and chocolate syrup')
                        """;
                stmt.execute(insert);
                System.out.println("Expanded restaurant menu added.");
            }
        }
    }

    public List<MenuItem> getMenuItems() {
        List<MenuItem> items = new ArrayList<>();
        String query = "SELECT name, price, description FROM menu";
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                items.add(new MenuItem(
                        rs.getString("name"),
                        rs.getDouble("price"),
                        rs.getString("description")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    // ------------------- CLOSE -------------------

    public void close() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException ignored) {
        }
    }
}
