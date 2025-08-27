package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomerDashboard extends JFrame {
    
    private String customerUsername;
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JPanel statsPanel;
    
    public CustomerDashboard(String username) {
        this.customerUsername = username;
        initializeUI();
        loadCustomerData();
    }
    
    private void initializeUI() {
        setTitle("Customer Dashboard - Hotel Management System");
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
        
        // Create customer function buttons
        createCustomerButtons();
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Statistics Panel
        statsPanel = createStatsPanel();
        add(statsPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(75, 0, 130));
        header.setPreferredSize(new Dimension(1200, 80));
        
        welcomeLabel = new JLabel("Welcome, " + customerUsername + " (Customer)");
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
    
    private void createCustomerButtons() {
        // Smart Room Booking
        JPanel bookPanel = createFunctionPanel("Smart Room Booking", "AI-powered booking with recommendations", 
            new Color(0, 150, 136), e -> {
                try {
                    new SmartBookingSystem().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Smart Booking System: " + ex.getMessage());
                }
            });
        mainPanel.add(bookPanel);
        
        // View My Bookings
        JPanel bookingsPanel = createFunctionPanel("My Bookings", "View your current bookings", 
            new Color(70, 130, 180), e -> {
                showMyBookings();
            });
        mainPanel.add(bookingsPanel);
        
        // Room Search
        JPanel searchPanel = createFunctionPanel("Search Rooms", "Find available rooms", 
            new Color(138, 43, 226), e -> {
                showRoomSearch();
            });
        mainPanel.add(searchPanel);
        
        // Hotel Services
        JPanel servicesPanel = createFunctionPanel("Hotel Services", "View available services", 
            new Color(255, 140, 0), e -> {
                showHotelServices();
            });
        mainPanel.add(servicesPanel);
        
        // Food Order
        JPanel foodPanel = createFunctionPanel("Food Order", "Order food to your room", 
            new Color(220, 20, 60), e -> {
                new FoodOrderSystem().setVisible(true);
                setVisible(false);
            });
        mainPanel.add(foodPanel);
        
        // Feedback System
        JPanel feedbackPanel = createFunctionPanel("Feedback", "Submit feedback and ratings", 
            new Color(255, 69, 0), e -> {
                try {
                    new FeedbackSystem().setVisible(true);
                    setVisible(false);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error opening Feedback System: " + ex.getMessage());
                }
            });
        mainPanel.add(feedbackPanel);
        
        // Contact Support
        JPanel supportPanel = createFunctionPanel("Contact Support", "Get help and support", 
            new Color(105, 105, 105), e -> {
                showContactSupport();
            });
        mainPanel.add(supportPanel);
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
        stats.setBackground(new Color(75, 0, 130));
        stats.setPreferredSize(new Dimension(1200, 100));
        stats.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // These will be updated with actual data
        stats.add(createStatCard("My Bookings", "0", Color.WHITE));
        stats.add(createStatCard("Active Bookings", "0", Color.GREEN));
        stats.add(createStatCard("Total Spent", "$0", Color.WHITE));
        stats.add(createStatCard("Available Rooms", "0", Color.YELLOW));
        
        return stats;
    }
    
    private JPanel createStatCard(String title, String value, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(new Color(75, 0, 130));
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
    
    private void loadCustomerData() {
        try (Connection conn = DBConnection.getConnection()) {
            // Load customer's booking statistics
            String bookingQuery = "SELECT COUNT(*) as total_bookings, " +
                                "SUM(CASE WHEN status = 'Checked In' THEN 1 ELSE 0 END) as active_bookings, " +
                                "SUM(deposit) as total_spent " +
                                "FROM customer WHERE name = ?";
            PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery);
            bookingStmt.setString(1, customerUsername);
            ResultSet bookingRs = bookingStmt.executeQuery();
            
            if (bookingRs.next()) {
                updateStatCard(0, "My Bookings", String.valueOf(bookingRs.getInt("total_bookings")));
                updateStatCard(1, "Active Bookings", String.valueOf(bookingRs.getInt("active_bookings")));
                updateStatCard(2, "Total Spent", "$" + (bookingRs.getString("total_spent") != null ? bookingRs.getString("total_spent") : "0"));
            }
            
            // Load available rooms count
            String roomQuery = "SELECT COUNT(*) as available FROM room WHERE availability = 'Available'";
            PreparedStatement roomStmt = conn.prepareStatement(roomQuery);
            ResultSet roomRs = roomStmt.executeQuery();
            
            if (roomRs.next()) {
                updateStatCard(3, "Available Rooms", String.valueOf(roomRs.getInt("available")));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading customer data: " + e.getMessage());
        }
    }
    
    private void updateStatCard(int index, String title, String value) {
        JPanel statsPanel = (JPanel) this.statsPanel.getComponent(index);
        JLabel valueLabel = (JLabel) statsPanel.getComponent(1);
        valueLabel.setText(value);
    }
    
    private void showRoomBookingDialog() {
        JDialog bookingDialog = new JDialog(this, "Book a Room", true);
        bookingDialog.setSize(500, 400);
        bookingDialog.setLocationRelativeTo(this);
        bookingDialog.setLayout(new BorderLayout());
        
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Form fields
        JTextField nameField = new JTextField(customerUsername);
        nameField.setEditable(false);
        
        JComboBox<String> roomTypeBox = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite", "Presidential"});
        JTextField checkInField = new JTextField();
        JTextField checkOutField = new JTextField();
        JTextField guestsField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextArea specialRequests = new JTextArea(3, 20);
        specialRequests.setLineWrap(true);
        
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Room Type:"));
        formPanel.add(roomTypeBox);
        formPanel.add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        formPanel.add(checkInField);
        formPanel.add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        formPanel.add(checkOutField);
        formPanel.add(new JLabel("Number of Guests:"));
        formPanel.add(guestsField);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Special Requests:"));
        formPanel.add(new JScrollPane(specialRequests));
        
        JPanel buttonPanel = new JPanel();
        JButton bookBtn = new JButton("Book Room");
        JButton cancelBtn = new JButton("Cancel");
        
        bookBtn.addActionListener(e -> {
            // Validate and process booking
            if (validateBookingForm(checkInField.getText(), checkOutField.getText(), guestsField.getText(), phoneField.getText())) {
                processBooking(nameField.getText(), (String) roomTypeBox.getSelectedItem(), 
                             checkInField.getText(), checkOutField.getText(), 
                             guestsField.getText(), phoneField.getText(), specialRequests.getText());
                bookingDialog.dispose();
            }
        });
        
        cancelBtn.addActionListener(e -> bookingDialog.dispose());
        
        buttonPanel.add(bookBtn);
        buttonPanel.add(cancelBtn);
        
        bookingDialog.add(formPanel, BorderLayout.CENTER);
        bookingDialog.add(buttonPanel, BorderLayout.SOUTH);
        
        bookingDialog.setVisible(true);
    }
    
    private boolean validateBookingForm(String checkIn, String checkOut, String guests, String phone) {
        if (checkIn.isEmpty() || checkOut.isEmpty() || guests.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Simple date validation
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date checkInDate = sdf.parse(checkIn);
            Date checkOutDate = sdf.parse(checkOut);
            
            if (checkOutDate.before(checkInDate) || checkOutDate.equals(checkInDate)) {
                JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void processBooking(String name, String roomType, String checkIn, String checkOut, 
                              String guests, String phone, String specialRequests) {
        try (Connection conn = DBConnection.getConnection()) {
            // Find available room
            String roomQuery = "SELECT room_number, price FROM room WHERE availability = 'Available' AND room_type = ? LIMIT 1";
            PreparedStatement roomStmt = conn.prepareStatement(roomQuery);
            roomStmt.setString(1, roomType);
            ResultSet roomRs = roomStmt.executeQuery();
            
            if (roomRs.next()) {
                String roomNumber = roomRs.getString("room_number");
                String price = roomRs.getString("price");
                
                // Insert booking
                String insertQuery = "INSERT INTO customer (name, room_number, check_in_date, check_out_date, " +
                                   "guests, phone, special_requests, status, deposit) VALUES (?, ?, ?, ?, ?, ?, ?, 'Booked', ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setString(1, name);
                insertStmt.setString(2, roomNumber);
                insertStmt.setString(3, checkIn);
                insertStmt.setString(4, checkOut);
                insertStmt.setString(5, guests);
                insertStmt.setString(6, phone);
                insertStmt.setString(7, specialRequests);
                insertStmt.setString(8, price);
                
                insertStmt.executeUpdate();
                
                // Update room availability
                String updateRoomQuery = "UPDATE room SET availability = 'Booked' WHERE room_number = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateRoomQuery);
                updateStmt.setString(1, roomNumber);
                updateStmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, 
                    "Booking successful! Room " + roomNumber + " has been reserved for you.", 
                    "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
                
                loadCustomerData(); // Refresh statistics
                
            } else {
                JOptionPane.showMessageDialog(this, 
                    "No available rooms of type " + roomType + " found.", 
                    "No Availability", JOptionPane.WARNING_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing booking: " + e.getMessage());
        }
    }
    
    private void showMyBookings() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM customer WHERE name = ? ORDER BY check_in_date DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, customerUsername);
            ResultSet rs = stmt.executeQuery();
            
            StringBuilder bookings = new StringBuilder();
            bookings.append("=== MY BOOKINGS ===\n\n");
            
            boolean hasBookings = false;
            while (rs.next()) {
                hasBookings = true;
                bookings.append("Room: ").append(rs.getString("room_number")).append("\n");
                bookings.append("Check-in: ").append(rs.getString("check_in_date")).append("\n");
                bookings.append("Check-out: ").append(rs.getString("check_out_date")).append("\n");
                bookings.append("Status: ").append(rs.getString("status")).append("\n");
                bookings.append("Deposit: $").append(rs.getString("deposit")).append("\n");
                bookings.append("------------------------\n");
            }
            
            if (!hasBookings) {
                bookings.append("No bookings found.");
            }
            
            JTextArea textArea = new JTextArea(bookings.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "My Bookings", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading bookings: " + e.getMessage());
        }
    }
    
    private void showRoomSearch() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT room_number, room_type, price, availability FROM room WHERE availability = 'Available' ORDER BY price";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            StringBuilder rooms = new StringBuilder();
            rooms.append("=== AVAILABLE ROOMS ===\n\n");
            
            boolean hasRooms = false;
            while (rs.next()) {
                hasRooms = true;
                rooms.append("Room ").append(rs.getString("room_number")).append("\n");
                rooms.append("Type: ").append(rs.getString("room_type")).append("\n");
                rooms.append("Price: $").append(rs.getString("price")).append("/night\n");
                rooms.append("Status: ").append(rs.getString("availability")).append("\n");
                rooms.append("------------------------\n");
            }
            
            if (!hasRooms) {
                rooms.append("No available rooms at the moment.");
            }
            
            JTextArea textArea = new JTextArea(rooms.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Available Rooms", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching rooms: " + e.getMessage());
        }
    }
    
    private void showHotelServices() {
        String services = """
            === HOTEL SERVICES ===
            
            🏊‍♂️ Swimming Pool
            💆‍♀️ Spa & Wellness Center
            🏋️‍♂️ Fitness Center
            🍽️ Fine Dining Restaurant
            🍹 Bar & Lounge
            🚗 Valet Parking
            🧺 Laundry Service
            🚁 Airport Shuttle
            🎾 Tennis Court
            🏌️‍♂️ Golf Course
            🎭 Conference Rooms
            🎉 Event Planning
            
            For more information, please contact the front desk.
            """;
        
        JTextArea textArea = new JTextArea(services);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Hotel Services", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showContactSupport() {
        String support = """
            === CONTACT SUPPORT ===
            
            📞 Front Desk: +1-555-0123
            📧 Email: support@hotel.com
            🕒 24/7 Customer Service
            
            Emergency Contact: +1-555-9999
            
            We're here to help you 24/7!
            """;
        
        JTextArea textArea = new JTextArea(support);
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JOptionPane.showMessageDialog(this, textArea, "Contact Support", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CustomerDashboard("Customer").setVisible(true);
        });
    }
} 