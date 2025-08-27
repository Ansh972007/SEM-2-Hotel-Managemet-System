package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConciergeSystem extends JFrame {
    
    private JTextField guestNameField, roomNumberField, phoneField;
    private JComboBox<String> serviceTypeBox, priorityBox, statusBox;
    private JTextArea requestArea, notesArea;
    private JTable requestsTable;
    private JLabel totalRequestsLabel, pendingRequestsLabel, completedRequestsLabel;
    private JSpinner dateSpinner, timeSpinner;
    
    public ConciergeSystem() {
        setTitle("Concierge & Guest Services System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createUI();
        loadRequests();
        updateStatistics();
    }
    
    private void createUI() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 193, 7));
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        
        JLabel titleLabel = new JLabel("Concierge & Guest Services System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.BLACK);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Statistics panel
        JPanel statsPanel = createStatisticsPanel();
        add(statsPanel, BorderLayout.CENTER);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel - Request form
        JPanel formPanel = createRequestForm();
        mainPanel.add(formPanel);
        
        // Right panel - Requests list
        JPanel listPanel = createRequestsList();
        mainPanel.add(listPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBackground(new Color(255, 248, 220));
        panel.setPreferredSize(new Dimension(1200, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        totalRequestsLabel = new JLabel("Total Requests: 0");
        totalRequestsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalRequestsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalRequestsLabel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        totalRequestsLabel.setForeground(Color.ORANGE);
        
        pendingRequestsLabel = new JLabel("Pending: 0");
        pendingRequestsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pendingRequestsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pendingRequestsLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        pendingRequestsLabel.setForeground(Color.RED);
        
        completedRequestsLabel = new JLabel("Completed: 0");
        completedRequestsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        completedRequestsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        completedRequestsLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        completedRequestsLabel.setForeground(Color.GREEN);
        
        panel.add(totalRequestsLabel);
        panel.add(pendingRequestsLabel);
        panel.add(completedRequestsLabel);
        
        return panel;
    }
    
    private JPanel createRequestForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("New Service Request"));
        
        // Guest Details
        JPanel guestPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        guestPanel.setBorder(BorderFactory.createTitledBorder("Guest Details"));
        
        guestPanel.add(new JLabel("Guest Name:"));
        guestNameField = new JTextField(20);
        guestPanel.add(guestNameField);
        
        guestPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField(10);
        guestPanel.add(roomNumberField);
        
        guestPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(15);
        guestPanel.add(phoneField);
        
        panel.add(guestPanel);
        
        // Service Details
        JPanel servicePanel = new JPanel(new GridLayout(4, 2, 10, 10));
        servicePanel.setBorder(BorderFactory.createTitledBorder("Service Details"));
        
        servicePanel.add(new JLabel("Service Type:"));
        serviceTypeBox = new JComboBox<>(new String[]{
            "Restaurant Reservation", "Transportation", "Tour Booking", "Spa Appointment",
            "Room Service", "Laundry Service", "Wake-up Call", "Special Event Planning",
            "Business Services", "Shopping Assistance", "Medical Assistance", "Other"
        });
        servicePanel.add(serviceTypeBox);
        
        servicePanel.add(new JLabel("Priority:"));
        priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "Urgent"});
        servicePanel.add(priorityBox);
        
        servicePanel.add(new JLabel("Preferred Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        servicePanel.add(dateSpinner);
        
        servicePanel.add(new JLabel("Preferred Time:"));
        timeSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        servicePanel.add(timeSpinner);
        
        panel.add(servicePanel);
        
        // Request Details
        JPanel requestPanel = new JPanel(new BorderLayout());
        requestPanel.setBorder(BorderFactory.createTitledBorder("Request Details"));
        requestArea = new JTextArea(4, 25);
        requestArea.setLineWrap(true);
        requestArea.setText("Please describe your request in detail...");
        JScrollPane requestScroll = new JScrollPane(requestArea);
        requestPanel.add(requestScroll, BorderLayout.CENTER);
        panel.add(requestPanel);
        
        // Status and Notes
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statusPanel.setBorder(BorderFactory.createTitledBorder("Status & Notes"));
        
        statusPanel.add(new JLabel("Status:"));
        statusBox = new JComboBox<>(new String[]{"New", "In Progress", "Completed", "Cancelled"});
        statusPanel.add(statusBox);
        
        statusPanel.add(new JLabel("Staff Notes:"));
        notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        statusPanel.add(notesScroll);
        
        panel.add(statusPanel);
        
        return panel;
    }
    
    private JPanel createRequestsList() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Service Requests"));
        
        // Create table model
        String[] columnNames = {"ID", "Guest", "Room", "Service", "Priority", "Date", "Status", "Notes"};
        Object[][] data = {};
        
        requestsTable = new JTable(data, columnNames);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(requestsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            loadRequests();
            updateStatistics();
        });
        panel.add(refreshBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton submitBtn = new JButton("Submit Request");
        submitBtn.setBackground(new Color(34, 139, 34));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.addActionListener(e -> submitRequest());
        
        JButton updateBtn = new JButton("Update Request");
        updateBtn.setBackground(new Color(70, 130, 180));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        updateBtn.addActionListener(e -> updateRequest());
        
        JButton completeBtn = new JButton("Mark Complete");
        completeBtn.setBackground(new Color(255, 140, 0));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        completeBtn.addActionListener(e -> markComplete());
        
        JButton restaurantBtn = new JButton("Restaurant Guide");
        restaurantBtn.setBackground(new Color(156, 39, 176));
        restaurantBtn.setForeground(Color.WHITE);
        restaurantBtn.setFont(new Font("Arial", Font.BOLD, 14));
        restaurantBtn.addActionListener(e -> showRestaurantGuide());
        
        JButton attractionsBtn = new JButton("Local Attractions");
        attractionsBtn.setBackground(new Color(0, 150, 136));
        attractionsBtn.setForeground(Color.WHITE);
        attractionsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        attractionsBtn.addActionListener(e -> showLocalAttractions());
        
        JButton clearBtn = new JButton("Clear Form");
        clearBtn.setBackground(new Color(220, 20, 60));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Arial", Font.BOLD, 14));
        clearBtn.addActionListener(e -> clearForm());
        
        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(105, 105, 105));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            new StaffDashboard("Staff").setVisible(true);
            setVisible(false);
        });
        
        panel.add(submitBtn);
        panel.add(updateBtn);
        panel.add(completeBtn);
        panel.add(restaurantBtn);
        panel.add(attractionsBtn);
        panel.add(clearBtn);
        panel.add(backBtn);
        
        return panel;
    }
    
    private void loadRequests() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM concierge_requests ORDER BY request_date DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            // Clear existing table data
            while (requestsTable.getRowCount() > 0) {
                ((javax.swing.table.DefaultTableModel) requestsTable.getModel()).removeRow(0);
            }
            
            // Add new data
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) requestsTable.getModel();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("guest_name"),
                    rs.getString("room_number"),
                    rs.getString("service_type"),
                    rs.getString("priority"),
                    rs.getDate("preferred_date"),
                    rs.getString("status"),
                    rs.getString("staff_notes")
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading requests: " + e.getMessage());
        }
    }
    
    private void updateStatistics() {
        try (Connection conn = DBConnection.getConnection()) {
            // Total requests
            String totalQuery = "SELECT COUNT(*) as total FROM concierge_requests";
            PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
            ResultSet totalRs = totalStmt.executeQuery();
            
            if (totalRs.next()) {
                totalRequestsLabel.setText("Total Requests: " + totalRs.getInt("total"));
            }
            
            // Pending requests
            String pendingQuery = "SELECT COUNT(*) as pending FROM concierge_requests WHERE status IN ('New', 'In Progress')";
            PreparedStatement pendingStmt = conn.prepareStatement(pendingQuery);
            ResultSet pendingRs = pendingStmt.executeQuery();
            
            if (pendingRs.next()) {
                pendingRequestsLabel.setText("Pending: " + pendingRs.getInt("pending"));
            }
            
            // Completed requests
            String completedQuery = "SELECT COUNT(*) as completed FROM concierge_requests WHERE status = 'Completed'";
            PreparedStatement completedStmt = conn.prepareStatement(completedQuery);
            ResultSet completedRs = completedStmt.executeQuery();
            
            if (completedRs.next()) {
                completedRequestsLabel.setText("Completed: " + completedRs.getInt("completed"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void submitRequest() {
        if (guestNameField.getText().isEmpty() || requestArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in guest name and request details!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String insertQuery = "INSERT INTO concierge_requests (guest_name, room_number, phone, service_type, priority, preferred_date, preferred_time, request_details, status, staff_notes, request_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, guestNameField.getText());
            stmt.setString(2, roomNumberField.getText());
            stmt.setString(3, phoneField.getText());
            stmt.setString(4, (String) serviceTypeBox.getSelectedItem());
            stmt.setString(5, (String) priorityBox.getSelectedItem());
            stmt.setDate(6, new java.sql.Date(((Date) dateSpinner.getValue()).getTime()));
            stmt.setTime(7, new java.sql.Time(((Date) timeSpinner.getValue()).getTime()));
            stmt.setString(8, requestArea.getText());
            stmt.setString(9, (String) statusBox.getSelectedItem());
            stmt.setString(10, notesArea.getText());
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Service request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            loadRequests();
            updateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateRequest() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int requestId = (Integer) requestsTable.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String updateQuery = "UPDATE concierge_requests SET status = ?, staff_notes = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, (String) statusBox.getSelectedItem());
            stmt.setString(2, notesArea.getText());
            stmt.setInt(3, requestId);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Request updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            loadRequests();
            updateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void markComplete() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request to mark complete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int requestId = (Integer) requestsTable.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String updateQuery = "UPDATE concierge_requests SET status = 'Completed', completion_date = NOW() WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setInt(1, requestId);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Request marked as completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            loadRequests();
            updateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error marking request complete: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showRestaurantGuide() {
        StringBuilder guide = new StringBuilder();
        guide.append("=== RESTAURANT GUIDE ===\n\n");
        guide.append("🍽️ FINE DINING:\n");
        guide.append("• La Maison - French Cuisine ($$$)\n");
        guide.append("• Ocean View - Seafood Specialties ($$$)\n");
        guide.append("• Royal Palace - International ($$$)\n\n");
        
        guide.append("🍕 CASUAL DINING:\n");
        guide.append("• Pizzeria Roma - Italian ($$)\n");
        guide.append("• Golden Dragon - Chinese ($$)\n");
        guide.append("• Taco Fiesta - Mexican ($$)\n\n");
        
        guide.append("☕ CAFES & BARS:\n");
        guide.append("• Coffee Corner - Specialty Coffee ($)\n");
        guide.append("• Sunset Bar - Cocktails & Light Bites ($$)\n");
        guide.append("• Garden Cafe - Afternoon Tea ($)\n\n");
        
        guide.append("📞 RESERVATION ASSISTANCE:\n");
        guide.append("Contact concierge for reservations and special arrangements.\n");
        
        JTextArea guideArea = new JTextArea(guide.toString());
        guideArea.setEditable(false);
        guideArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(guideArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Restaurant Guide", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showLocalAttractions() {
        StringBuilder attractions = new StringBuilder();
        attractions.append("=== LOCAL ATTRACTIONS ===\n\n");
        attractions.append("🏛️ HISTORICAL SITES:\n");
        attractions.append("• Old Town Square - 15 min walk\n");
        attractions.append("• Historical Museum - 20 min walk\n");
        attractions.append("• Cathedral of St. Mary - 10 min walk\n\n");
        
        attractions.append("🎭 ENTERTAINMENT:\n");
        attractions.append("• City Theater - Live performances\n");
        attractions.append("• Jazz Club - Live music nightly\n");
        attractions.append("• Cinema Complex - Latest movies\n\n");
        
        attractions.append("🛍️ SHOPPING:\n");
        attractions.append("• Central Mall - 5 min walk\n");
        attractions.append("• Artisan Market - Local crafts\n");
        attractions.append("• Fashion District - Designer boutiques\n\n");
        
        attractions.append("🌳 OUTDOOR ACTIVITIES:\n");
        attractions.append("• City Park - Walking trails\n");
        attractions.append("• River Cruise - Scenic boat tours\n");
        attractions.append("• Botanical Gardens - Nature walks\n\n");
        
        attractions.append("🚗 TRANSPORTATION:\n");
        attractions.append("• Hotel shuttle service available\n");
        attractions.append("• Taxi service at front desk\n");
        attractions.append("• Public transport nearby\n");
        
        JTextArea attractionsArea = new JTextArea(attractions.toString());
        attractionsArea.setEditable(false);
        attractionsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(attractionsArea);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Local Attractions", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void clearForm() {
        guestNameField.setText("");
        roomNumberField.setText("");
        phoneField.setText("");
        requestArea.setText("Please describe your request in detail...");
        notesArea.setText("");
        serviceTypeBox.setSelectedIndex(0);
        priorityBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        dateSpinner.setValue(new Date());
        timeSpinner.setValue(new Date());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ConciergeSystem().setVisible(true);
        });
    }
} 