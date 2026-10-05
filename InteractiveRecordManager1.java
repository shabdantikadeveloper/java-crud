package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class InteractiveRecordManager1 {
    private static final String URL = "jdbc:mysql://localhost:3306/mobile";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice = 0;

        do {
            System.out.println("\n==============================");
            System.out.println("    MySQL CRUD Operations     ");
            System.out.println("==============================");
            System.out.println("1. Insert Record (Create)");
            System.out.println("2. Display Records (Read)");
            System.out.println("3. Update Record (Update)");
            System.out.println("4. Delete Record (Delete)");
            System.out.println("5. Exit");
            System.out.print("Enter your choice (1-5): ");
            
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } else {
                System.out.println("Invalid input! Please enter a number.");
                scanner.next(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter City: ");
                    String city = scanner.nextLine();
                    createRecord(name, city);
                    break;

                case 2:
                    readRecords();
                    break;

                case 3:
                    System.out.print("Enter ID of the record to update: ");
                    int updateId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter New Name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter New City: ");
                    String newCity = scanner.nextLine();
                    updateRecord(updateId, newName, newCity);
                    break;

                case 4:
                    System.out.print("Enter ID of the record to delete: ");
                    int deleteId = scanner.nextInt();
                    scanner.nextLine();
                    deleteRecord(deleteId);
                    break;

                case 5:
                    System.out.println("Exiting application. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice! Please enter a number between 1 and 5.");
            }
        } while (choice != 5);

        scanner.close();
    }

    // 1. CREATE
    public static void createRecord(String name, String city) {
        String sql = "INSERT INTO record (name, city) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, city);
            pstmt.executeUpdate();
            System.out.println("=> Successfully inserted record.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 2. READ (Using your proven working display logic)
    public static void readRecords() {
        String sql = "SELECT id, name, city FROM record";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            System.out.println("\n--- Current Records in Database ---");
            boolean hasRecords = false;
            
            while (rs.next()) {
                hasRecords = true;
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String city = rs.getString("city");
                
                System.out.println("ID: " + id + " | Name: " + name + " | City: " + city);
            }
            
            if (!hasRecords) {
                System.out.println("No records found in the table.");
            }
            System.out.println("-----------------------------------\n");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 3. UPDATE
    public static void updateRecord(int id, String newName, String newCity) {
        String sql = "UPDATE record SET name = ?, city = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newName);
            pstmt.setString(2, newCity);
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            System.out.println("=> Successfully updated record.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. DELETE
    public static void deleteRecord(int id) {
        String sql = "DELETE FROM record WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("=> Successfully deleted record.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}