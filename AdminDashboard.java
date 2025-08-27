package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminDashboard extends JFrame {
    
    private String adminUsername;
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JPanel statsPanel;
    
    public AdminDashboard(String username) {
        this.adminUsername = username;
        initializeUI();
        loadStatistics();
    }
    
    private void initializeUI() {
        setTitle("Admin Dashboard - Hotel Management System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main Content Panel
        mainPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create admin function buttons
        createAdminButtons();
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Statistics Panel
        statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 25, 112));
        header.setPreferredSize(new Dimension(1200, 80));
        
        welcomeLabel = new JLabel("Welcome, " + adminUsername + " (Administrator)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        header.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        logoutBtn.setBackground(new Color(220, 20, 60));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to logout?", "Logout", 
                JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                new Login().setVisible(true);
                setVisible(false);
            }
        });
        header.add(logoutBtn, BorderLayout.EAST);
        
        return header;
    }
    
    private void createAdminButtons() {
        // Employee Management
        JPanel empPanel = createFunctionPanel("Employee Management", "Manage hotel employees", 
            new Color(70, 130, 180), e -> {
                new AddEmployee().setVisible(true);
                setVisible(false);
            });
        mainPanel.add(empPanel);
        
        // Room Management
        JPanel roomPanel = createFunctionPanel("Room Management", "Add and manage rooms", 
            new Color(34, 139, 34), e -> {
                new AddRoom().setVisible(true);
                setVisible(false);
            });
        mainPanel.add(roomPanel);
        
        // Driver Management
        JPanel driverPanel = createFunctionPanel("Driver Management", "Manage hotel drivers", 
            new Color(255, 140, 0), e -> {
                new AddDrivers().setVisible(true);
                setVisible(false);
            });
        mainPanel.add(driverPanel);
        
        // Customer Management
        JPanel customerPanel = createFunctionPanel("Customer Management", "View all customers", 
            new Color(138, 43, 226), e -> {
                new CustomerInfo().setVisible(true);
                setVisible(false);
            });
        mainPanel.add(customerPanel);
        
        // Employee Information
        JPanel empInfoPanel = createFunctionPanel("Employee Information", "View employee details", 
            new Color(220, 20, 60), e -> {
                    try {
                        new Employee().setVisible(true);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    setVisible(false);
            });
        mainPanel.add(empInfoPanel);
        
        // System Reports
        JPanel reportsPanel = createFunctionPanel("System Reports", "Generate comprehensive reports", 
            new Color(105, 105, 105), e -> {
                try {
                    new ReportsSystem().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Reports System: " + ex.getMessage());
                }
            });
        mainPanel.add(reportsPanel);
        
        // Inventory Management
        JPanel inventoryPanel = createFunctionPanel("Inventory Management", "Manage hotel supplies and stock", 
            new Color(156, 39, 176), e -> {
                try {
                    new InventorySystem().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Inventory System: " + ex.getMessage());
                }
            });
        mainPanel.add(inventoryPanel);
        
        // Security System
        JPanel securityPanel = createFunctionPanel("Security & Access Control", "Manage security and visitor access", 
            new Color(33, 33, 33), e -> {
                try {
                    new SecuritySystem().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Security System: " + ex.getMessage());
                }
            });
        mainPanel.add(securityPanel);
    }
    
    private JPanel createFunctionPanel(String title, String description, Color color, ActionListener action) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(color);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.GRAY);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JButton actionBtn = new JButton("Open");
        actionBtn.setFont(new Font("Arial", Font.BOLD, 14));
        actionBtn.setBackground(color);
        actionBtn.setForeground(Color.WHITE);
        actionBtn.setFocusPainted(false);
        actionBtn.addActionListener(action);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(descLabel, BorderLayout.CENTER);
        panel.add(actionBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel stats = new JPanel(new GridLayout(1, 4, 10, 10));
        stats.setBackground(new Color(25, 25, 112));
        stats.setPreferredSize(new Dimension(1200, 100));
        stats.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // These will be updated with actual data
        stats.add(createStatCard("Total Rooms", "0", Color.WHITE));
        stats.add(createStatCard("Available Rooms", "0", Color.GREEN));
        stats.add(createStatCard("Total Customers", "0", Color.WHITE));
        stats.add(createStatCard("Total Employees", "0", Color.WHITE));
        
        return stats;
    }
    
    private JPanel createStatCard(String title, String value, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(25, 25, 112));
        card.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(valueColor);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void loadStatistics() {
        try (Connection conn = DBConnection.getConnection()) {
            // Load room statistics
            String roomQuery = "SELECT COUNT(*) as total, SUM(CASE WHEN availability = 'Available' THEN 1 ELSE 0 END) as available FROM room";
            PreparedStatement roomStmt = conn.prepareStatement(roomQuery);
            ResultSet roomRs = roomStmt.executeQuery();
            
            if (roomRs.next()) {
                updateStatCard(0, "Total Rooms", String.valueOf(roomRs.getInt("total")));
                updateStatCard(1, "Available Rooms", String.valueOf(roomRs.getInt("available")));
            }
            
            // Load customer statistics
            String customerQuery = "SELECT COUNT(*) as total FROM customer";
            PreparedStatement customerStmt = conn.prepareStatement(customerQuery);
            ResultSet customerRs = customerStmt.executeQuery();
            
            if (customerRs.next()) {
                updateStatCard(2, "Total Customers", String.valueOf(customerRs.getInt("total")));
            }
            
            // Load employee statistics
            String empQuery = "SELECT COUNT(*) as total FROM employee";
            PreparedStatement empStmt = conn.prepareStatement(empQuery);
            ResultSet empRs = empStmt.executeQuery();
            
            if (empRs.next()) {
                updateStatCard(3, "Total Employees", String.valueOf(empRs.getInt("total")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading statistics: " + e.getMessage());
        }
    }
    
    private void updateStatCard(int index, String title, String value) {
        JPanel statsPanel = (JPanel) this.statsPanel.getComponent(index);
        JLabel valueLabel = (JLabel) statsPanel.getComponent(1);
        valueLabel.setText(value);
    }
    
    private void showReportsDialog() {
        String[] options = {"Room Occupancy Report", "Customer Report", "Employee Report", "Revenue Report"};
        int choice = JOptionPane.showOptionDialog(this, 
            "Select a report to generate:", "System Reports", 
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, 
            null, options, options[0]);
        
        if (choice >= 0) {
            generateReport(choice);
        }
    }
    
    private void generateReport(int reportType) {
        try (Connection conn = DBConnection.getConnection()) {
            StringBuilder report = new StringBuilder();
            report.append("=== HOTEL MANAGEMENT SYSTEM REPORT ===\n\n");
            
            switch (reportType) {
                case 0: // Room Occupancy
                    report.append("ROOM OCCUPANCY REPORT\n");
                    report.append("======================\n");
                    String roomQuery = "SELECT room_number, availability, price FROM room ORDER BY room_number";
                    PreparedStatement roomStmt = conn.prepareStatement(roomQuery);
                    ResultSet roomRs = roomStmt.executeQuery();
                    
                    while (roomRs.next()) {
                        report.append("Room ").append(roomRs.getString("room_number"))
                              .append(": ").append(roomRs.getString("availability"))
                              .append(" - $").append(roomRs.getString("price")).append("\n");
                    }
                    break;
                    
                case 1: // Customer Report
                    report.append("CUSTOMER REPORT\n");
                    report.append("===============\n");
                    String customerQuery = "SELECT name, room_number, status, deposit FROM customer ORDER BY name";
                    PreparedStatement customerStmt = conn.prepareStatement(customerQuery);
                    ResultSet customerRs = customerStmt.executeQuery();
                    
                    while (customerRs.next()) {
                        report.append(customerRs.getString("name"))
                              .append(" - Room ").append(customerRs.getString("room_number"))
                              .append(" - ").append(customerRs.getString("status"))
                              .append(" - $").append(customerRs.getString("deposit")).append("\n");
                    }
                    break;
                    
                case 2: // Employee Report
                    report.append("EMPLOYEE REPORT\n");
                    report.append("================\n");
                    String empQuery = "SELECT name, age, gender, job, salary FROM employee ORDER BY name";
                    PreparedStatement empStmt = conn.prepareStatement(empQuery);
                    ResultSet empRs = empStmt.executeQuery();
                    
                    while (empRs.next()) {
                        report.append(empRs.getString("name"))
                              .append(" - ").append(empRs.getString("job"))
                              .append(" - $").append(empRs.getString("salary")).append("\n");
                    }
                    break;
                    
                case 3: // Revenue Report
                    report.append("REVENUE REPORT\n");
                    report.append("==============\n");
                    String revenueQuery = "SELECT SUM(deposit) as total_revenue FROM customer";
                    PreparedStatement revenueStmt = conn.prepareStatement(revenueQuery);
                    ResultSet revenueRs = revenueStmt.executeQuery();
                    
                    if (revenueRs.next()) {
                        report.append("Total Revenue: $").append(revenueRs.getString("total_revenue")).append("\n");
                    }
                    break;
            }
            
            // Show report in a dialog
            JTextArea textArea = new JTextArea(report.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Report", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard("Admin").setVisible(true);
        });
    }
} 