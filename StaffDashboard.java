package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class StaffDashboard extends JFrame {
    
    private String staffUsername;
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JPanel statsPanel;
    
    public StaffDashboard(String username) {
        this.staffUsername = username;
        initializeUI();
        loadStatistics();
    }
    
    private void initializeUI() {
        setTitle("Staff Dashboard - Hotel Management System");
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
        
        // Create staff function buttons
        createStaffButtons();
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Statistics Panel
        statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(0, 100, 0));
        header.setPreferredSize(new Dimension(1200, 80));
        
        welcomeLabel = new JLabel("Welcome, " + staffUsername + " (Staff Member)");
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
    
    private void createStaffButtons() {
        // Customer Check-in
        JPanel checkInPanel = createFunctionPanel("Customer Check-in", "Register new customers", 
            new Color(34, 139, 34), e -> {
                try {
                    new NewCustomer().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Customer Check-in: " + ex.getMessage());
                }
            });
        mainPanel.add(checkInPanel);
        
        // Room Management
        JPanel roomPanel = createFunctionPanel("Room Operations", "View and update room status", 
            new Color(70, 130, 180), e -> {
                try {
                    new Room().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Room Operations: " + ex.getMessage());
                }
            });
        mainPanel.add(roomPanel);
        
        // Customer Information
        JPanel customerPanel = createFunctionPanel("Customer Information", "View customer details", 
            new Color(138, 43, 226), e -> {
                try {
                    new CustomerInfo().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Customer Information: " + ex.getMessage());
                }
            });
        mainPanel.add(customerPanel);
        
        // Check Out
        JPanel checkOutPanel = createFunctionPanel("Check Out", "Process customer check-out", 
            new Color(255, 140, 0), e -> {
                try {
                    new CheckOut().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Check Out: " + ex.getMessage());
                }
            });
        mainPanel.add(checkOutPanel);
        
        // Update Room Status
        JPanel updateRoomPanel = createFunctionPanel("Update Room Status", "Change room availability", 
            new Color(220, 20, 60), e -> {
                try {
                    new UpdateRoom().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Update Room Status: " + ex.getMessage());
                }
            });
        mainPanel.add(updateRoomPanel);
        
        // Payment Management
        JPanel paymentPanel = createFunctionPanel("Payment Management", "Process payments and invoices", 
            new Color(25, 25, 112), e -> {
                try {
                    new PaymentSystem().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Payment System: " + ex.getMessage());
                }
            });
        mainPanel.add(paymentPanel);
        
        // Maintenance System
        JPanel maintenancePanel = createFunctionPanel("Maintenance System", "Handle maintenance requests", 
            new Color(139, 69, 19), e -> {
                try {
                    new MaintenanceSystem().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Maintenance System: " + ex.getMessage());
                }
            });
        mainPanel.add(maintenancePanel);
        
        // Housekeeping System
        JPanel housekeepingPanel = createFunctionPanel("Housekeeping", "Manage cleaning tasks", 
            new Color(0, 128, 0), e -> {
                try {
                    new HousekeepingSystem().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Housekeeping System: " + ex.getMessage());
                }
            });
        mainPanel.add(housekeepingPanel);
        
        // Concierge System
        JPanel conciergePanel = createFunctionPanel("Concierge Services", "Handle guest requests and services", 
            new Color(255, 193, 7), e -> {
                try {
                    new ConciergeSystem().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Concierge System: " + ex.getMessage());
                }
            });
        mainPanel.add(conciergePanel);
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
        stats.setBackground(new Color(0, 100, 0));
        stats.setPreferredSize(new Dimension(1200, 100));
        stats.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // These will be updated with actual data
        stats.add(createStatCard("Available Rooms", "0", Color.GREEN));
        stats.add(createStatCard("Occupied Rooms", "0", Color.ORANGE));
        stats.add(createStatCard("Today's Check-ins", "0", Color.WHITE));
        stats.add(createStatCard("Today's Check-outs", "0", Color.WHITE));
        
        return stats;
    }
    
    private JPanel createStatCard(String title, String value, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(0, 100, 0));
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
            String roomQuery = "SELECT COUNT(*) as total, " +
                             "SUM(CASE WHEN availability = 'Available' THEN 1 ELSE 0 END) as available, " +
                             "SUM(CASE WHEN availability = 'Occupied' THEN 1 ELSE 0 END) as occupied " +
                             "FROM room";
            PreparedStatement roomStmt = conn.prepareStatement(roomQuery);
            ResultSet roomRs = roomStmt.executeQuery();
            
            if (roomRs.next()) {
                updateStatCard(0, "Available Rooms", String.valueOf(roomRs.getInt("available")));
                updateStatCard(1, "Occupied Rooms", String.valueOf(roomRs.getInt("occupied")));
            }
            
            // Load today's check-ins (simplified - you might want to add date tracking)
            String checkInQuery = "SELECT COUNT(*) as total FROM customer WHERE status = 'Checked In'";
            PreparedStatement checkInStmt = conn.prepareStatement(checkInQuery);
            ResultSet checkInRs = checkInStmt.executeQuery();
            
            if (checkInRs.next()) {
                updateStatCard(2, "Today's Check-ins", String.valueOf(checkInRs.getInt("total")));
            }
            
            // Load today's check-outs (simplified)
            String checkOutQuery = "SELECT COUNT(*) as total FROM customer WHERE status = 'Checked Out'";
            PreparedStatement checkOutStmt = conn.prepareStatement(checkOutQuery);
            ResultSet checkOutRs = checkOutStmt.executeQuery();
            
            if (checkOutRs.next()) {
                updateStatCard(3, "Today's Check-outs", String.valueOf(checkOutRs.getInt("total")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading statistics: " + e.getMessage());
        }
    }
    
    private void updateStatCard(int index, String title, String value) {
        try {
            JPanel statsPanel = (JPanel) this.statsPanel.getComponent(index);
            JLabel valueLabel = (JLabel) statsPanel.getComponent(1);
            valueLabel.setText(value);
        } catch (Exception e) {
            // Handle any errors in updating stat cards
            System.err.println("Error updating stat card: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StaffDashboard("Staff").setVisible(true);
        });
    }
} 