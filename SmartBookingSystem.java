package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class SmartBookingSystem extends JFrame {
    
    private JTextField customerNameField, emailField, phoneField;
    private JComboBox<String> roomTypeBox, guestsBox, budgetBox;
    private JSpinner checkInSpinner, checkOutSpinner;
    private JTextArea preferencesArea, recommendationsArea;
    private JTable availableRoomsTable;
    private JLabel totalPriceLabel, discountLabel, finalPriceLabel;
    private double basePrice = 0.0;
    private double discount = 0.0;
    
    public SmartBookingSystem() {
        setTitle("Smart Booking System - AI Recommendations");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createUI();
        loadAvailableRooms();
    }
    
    private void createUI() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 150, 136));
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        
        JLabel titleLabel = new JLabel("Smart Booking System - AI Recommendations");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel - Booking form
        JPanel formPanel = createBookingForm();
        mainPanel.add(formPanel);
        
        // Right panel - AI Recommendations
        JPanel recommendationsPanel = createRecommendationsPanel();
        mainPanel.add(recommendationsPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createBookingForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Smart Booking Form"));
        
        // Customer Details
        JPanel customerPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        customerPanel.setBorder(BorderFactory.createTitledBorder("Customer Details"));
        
        customerPanel.add(new JLabel("Name:"));
        customerNameField = new JTextField(20);
        customerPanel.add(customerNameField);
        
        customerPanel.add(new JLabel("Email:"));
        emailField = new JTextField(20);
        customerPanel.add(emailField);
        
        customerPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField(20);
        customerPanel.add(phoneField);
        
        panel.add(customerPanel);
        
        // Booking Details
        JPanel bookingPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        bookingPanel.setBorder(BorderFactory.createTitledBorder("Booking Details"));
        
        bookingPanel.add(new JLabel("Check-in Date:"));
        checkInSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkInEditor = new JSpinner.DateEditor(checkInSpinner, "yyyy-MM-dd");
        checkInSpinner.setEditor(checkInEditor);
        checkInSpinner.addChangeListener(e -> calculatePrice());
        bookingPanel.add(checkInSpinner);
        
        bookingPanel.add(new JLabel("Check-out Date:"));
        checkOutSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor checkOutEditor = new JSpinner.DateEditor(checkOutSpinner, "yyyy-MM-dd");
        checkOutSpinner.setEditor(checkOutEditor);
        checkOutSpinner.addChangeListener(e -> calculatePrice());
        bookingPanel.add(checkOutSpinner);
        
        bookingPanel.add(new JLabel("Room Type:"));
        roomTypeBox = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite", "Presidential"});
        roomTypeBox.addActionListener(e -> calculatePrice());
        bookingPanel.add(roomTypeBox);
        
        bookingPanel.add(new JLabel("Number of Guests:"));
        guestsBox = new JComboBox<>(new String[]{"1", "2", "3", "4", "5+"});
        guestsBox.addActionListener(e -> calculatePrice());
        bookingPanel.add(guestsBox);
        
        panel.add(bookingPanel);
        
        // Preferences
        JPanel preferencesPanel = new JPanel(new BorderLayout());
        preferencesPanel.setBorder(BorderFactory.createTitledBorder("Preferences & Special Requests"));
        preferencesArea = new JTextArea(4, 25);
        preferencesArea.setLineWrap(true);
        preferencesArea.setText("Enter your preferences (e.g., high floor, quiet room, near elevator, etc.)");
        JScrollPane preferencesScroll = new JScrollPane(preferencesArea);
        preferencesPanel.add(preferencesScroll, BorderLayout.CENTER);
        panel.add(preferencesPanel);
        
        // Budget
        JPanel budgetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        budgetPanel.add(new JLabel("Budget Range:"));
        budgetBox = new JComboBox<>(new String[]{"Economy ($50-100)", "Standard ($100-200)", "Premium ($200-400)", "Luxury ($400+)"});
        budgetPanel.add(budgetBox);
        panel.add(budgetPanel);
        
        // Price Display
        JPanel pricePanel = new JPanel(new GridLayout(3, 2, 10, 10));
        pricePanel.setBorder(BorderFactory.createTitledBorder("Pricing"));
        
        pricePanel.add(new JLabel("Base Price:"));
        totalPriceLabel = new JLabel("$0.00");
        totalPriceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pricePanel.add(totalPriceLabel);
        
        pricePanel.add(new JLabel("Discount:"));
        discountLabel = new JLabel("$0.00");
        discountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        discountLabel.setForeground(Color.GREEN);
        pricePanel.add(discountLabel);
        
        pricePanel.add(new JLabel("Final Price:"));
        finalPriceLabel = new JLabel("$0.00");
        finalPriceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        finalPriceLabel.setForeground(Color.RED);
        pricePanel.add(finalPriceLabel);
        
        panel.add(pricePanel);
        
        return panel;
    }
    
    private JPanel createRecommendationsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("AI Recommendations"));
        
        // Recommendations area
        recommendationsArea = new JTextArea();
        recommendationsArea.setEditable(false);
        recommendationsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane recommendationsScroll = new JScrollPane(recommendationsArea);
        panel.add(recommendationsScroll, BorderLayout.CENTER);
        
        // Available rooms table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Available Rooms"));
        
        String[] columnNames = {"Room", "Type", "Price", "Floor", "Features", "Recommendation"};
        Object[][] data = {};
        
        availableRoomsTable = new JTable(data, columnNames);
        availableRoomsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane tableScroll = new JScrollPane(availableRoomsTable);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        panel.add(tablePanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton recommendBtn = new JButton("Get AI Recommendations");
        recommendBtn.setBackground(new Color(0, 150, 136));
        recommendBtn.setForeground(Color.WHITE);
        recommendBtn.setFont(new Font("Arial", Font.BOLD, 14));
        recommendBtn.addActionListener(e -> generateRecommendations());
        
        JButton bookBtn = new JButton("Book Selected Room");
        bookBtn.setBackground(new Color(34, 139, 34));
        bookBtn.setForeground(Color.WHITE);
        bookBtn.setFont(new Font("Arial", Font.BOLD, 14));
        bookBtn.addActionListener(e -> bookSelectedRoom());
        
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
            new CustomerDashboard("Customer").setVisible(true);
            setVisible(false);
        });
        
        panel.add(recommendBtn);
        panel.add(bookBtn);
        panel.add(clearBtn);
        panel.add(backBtn);
        
        return panel;
    }
    
    private void loadAvailableRooms() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT room_number, room_type, price, floor_number, amenities FROM room WHERE availability = 'Available' ORDER BY room_number";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            // Clear existing table data
            while (availableRoomsTable.getRowCount() > 0) {
                ((javax.swing.table.DefaultTableModel) availableRoomsTable.getModel()).removeRow(0);
            }
            
            // Add new data
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) availableRoomsTable.getModel();
            while (rs.next()) {
                Object[] row = {
                    rs.getString("room_number"),
                    rs.getString("room_type"),
                    "$" + rs.getDouble("price"),
                    rs.getInt("floor_number"),
                    rs.getString("amenities"),
                    "Click for recommendation"
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading available rooms: " + e.getMessage());
        }
    }
    
    private void calculatePrice() {
        String roomType = (String) roomTypeBox.getSelectedItem();
        double pricePerNight = 0.0;
        
        switch (roomType) {
            case "Standard":
                pricePerNight = 100.0;
                break;
            case "Deluxe":
                pricePerNight = 150.0;
                break;
            case "Suite":
                pricePerNight = 250.0;
                break;
            case "Presidential":
                pricePerNight = 500.0;
                break;
        }
        
        // Calculate number of nights
        Date checkIn = (Date) checkInSpinner.getValue();
        Date checkOut = (Date) checkOutSpinner.getValue();
        long nights = 1;
        
        if (checkIn != null && checkOut != null) {
            nights = Math.max(1, (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24));
        }
        
        basePrice = pricePerNight * nights;
        
        // Apply discounts based on length of stay
        if (nights >= 7) {
            discount = basePrice * 0.15; // 15% discount for week+ stays
        } else if (nights >= 3) {
            discount = basePrice * 0.10; // 10% discount for 3+ nights
        } else {
            discount = 0.0;
        }
        
        double finalPrice = basePrice - discount;
        
        totalPriceLabel.setText(String.format("$%.2f", basePrice));
        discountLabel.setText(String.format("$%.2f", discount));
        finalPriceLabel.setText(String.format("$%.2f", finalPrice));
    }
    
    private void generateRecommendations() {
        if (customerNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder recommendations = new StringBuilder();
        recommendations.append("=== AI RECOMMENDATIONS ===\n\n");
        
        // Analyze preferences
        String preferences = preferencesArea.getText().toLowerCase();
        String budget = (String) budgetBox.getSelectedItem();
        String roomType = (String) roomTypeBox.getSelectedItem();
        int guests = Integer.parseInt((String) guestsBox.getSelectedItem());
        
        recommendations.append("Based on your preferences:\n");
        recommendations.append("- Room Type: ").append(roomType).append("\n");
        recommendations.append("- Guests: ").append(guests).append("\n");
        recommendations.append("- Budget: ").append(budget).append("\n\n");
        
        // Smart recommendations
        if (preferences.contains("quiet")) {
            recommendations.append("🎯 QUIET ROOM RECOMMENDATION:\n");
            recommendations.append("• Request rooms away from elevators and main corridors\n");
            recommendations.append("• Higher floors tend to be quieter\n");
            recommendations.append("• End rooms have less foot traffic\n\n");
        }
        
        if (preferences.contains("view") || preferences.contains("scenic")) {
            recommendations.append("🌅 VIEW RECOMMENDATION:\n");
            recommendations.append("• Higher floors offer better views\n");
            recommendations.append("• Corner rooms often have multiple windows\n");
            recommendations.append("• Request rooms facing the city or garden\n\n");
        }
        
        if (guests > 2) {
            recommendations.append("👥 GROUP RECOMMENDATION:\n");
            recommendations.append("• Consider connecting rooms for larger groups\n");
            recommendations.append("• Suites offer more space and amenities\n");
            recommendations.append("• Request extra bedding if needed\n\n");
        }
        
        // Seasonal recommendations
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        
        if (month >= 5 && month <= 8) {
            recommendations.append("☀️ SUMMER RECOMMENDATION:\n");
            recommendations.append("• Rooms with AC are essential\n");
            recommendations.append("• Consider rooms with balconies\n");
            recommendations.append("• Pool view rooms are popular\n\n");
        } else if (month >= 11 || month <= 2) {
            recommendations.append("❄️ WINTER RECOMMENDATION:\n");
            recommendations.append("• Rooms with heating are important\n");
            recommendations.append("• Higher floors are warmer\n");
            recommendations.append("• Request extra blankets if needed\n\n");
        }
        
        // Budget optimization
        if (budget.contains("Economy")) {
            recommendations.append("💰 BUDGET OPTIMIZATION:\n");
            recommendations.append("• Standard rooms offer best value\n");
            recommendations.append("• Longer stays get better discounts\n");
            recommendations.append("• Consider off-peak dates for better rates\n\n");
        }
        
        // Room availability analysis
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT room_type, COUNT(*) as available FROM room WHERE availability = 'Available' GROUP BY room_type";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            recommendations.append("📊 AVAILABILITY ANALYSIS:\n");
            while (rs.next()) {
                recommendations.append("• ").append(rs.getString("room_type"))
                              .append(": ").append(rs.getInt("available"))
                              .append(" rooms available\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        recommendationsArea.setText(recommendations.toString());
    }
    
    private void bookSelectedRoom() {
        int selectedRow = availableRoomsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to book!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (customerNameField.getText().isEmpty() || emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in customer details!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String roomNumber = (String) availableRoomsTable.getValueAt(selectedRow, 0);
        String roomType = (String) availableRoomsTable.getValueAt(selectedRow, 1);
        
        try (Connection conn = DBConnection.getConnection()) {
            // Insert booking
            String insertQuery = "INSERT INTO customer (id_type, number, name, gender, country, phone, check_in_date, check_out_date, guests, room_number, status, deposit, special_requests) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, "Booking");
            stmt.setString(2, "BK" + System.currentTimeMillis());
            stmt.setString(3, customerNameField.getText());
            stmt.setString(4, "Not Specified");
            stmt.setString(5, "Not Specified");
            stmt.setString(6, phoneField.getText());
            stmt.setDate(7, new java.sql.Date(((Date) checkInSpinner.getValue()).getTime()));
            stmt.setDate(8, new java.sql.Date(((Date) checkOutSpinner.getValue()).getTime()));
            stmt.setInt(9, Integer.parseInt((String) guestsBox.getSelectedItem()));
            stmt.setString(10, roomNumber);
            stmt.setString(11, "Booked");
            stmt.setDouble(12, Double.parseDouble(finalPriceLabel.getText().substring(1)));
            stmt.setString(13, preferencesArea.getText());
            
            stmt.executeUpdate();
            
            // Update room availability
            String updateQuery = "UPDATE room SET availability = 'Occupied' WHERE room_number = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
            updateStmt.setString(1, roomNumber);
            updateStmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, 
                "Room " + roomNumber + " booked successfully!\n" +
                "Customer: " + customerNameField.getText() + "\n" +
                "Total: " + finalPriceLabel.getText(), 
                "Booking Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            loadAvailableRooms();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error booking room: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        customerNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        preferencesArea.setText("Enter your preferences (e.g., high floor, quiet room, near elevator, etc.)");
        roomTypeBox.setSelectedIndex(0);
        guestsBox.setSelectedIndex(0);
        budgetBox.setSelectedIndex(0);
        checkInSpinner.setValue(new Date());
        checkOutSpinner.setValue(new Date());
        recommendationsArea.setText("");
        calculatePrice();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SmartBookingSystem().setVisible(true);
        });
    }
} 