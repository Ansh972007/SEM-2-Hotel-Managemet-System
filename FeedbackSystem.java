package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedbackSystem extends JFrame {
    
    private JTextField customerNameField, roomNumberField, emailField;
    private JComboBox<String> ratingBox, categoryBox;
    private JTextArea feedbackArea, responseArea;
    private JTable feedbackTable;
    private JLabel averageRatingLabel, totalFeedbackLabel, satisfactionLabel;
    
    public FeedbackSystem() {
        setTitle("Customer Feedback System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createUI();
        loadFeedback();
        updateStatistics();
    }
    
    private void createUI() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(255, 69, 0));
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        
        JLabel titleLabel = new JLabel("Customer Feedback System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Statistics panel
        JPanel statsPanel = createStatisticsPanel();
        add(statsPanel, BorderLayout.CENTER);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel - Feedback form
        JPanel formPanel = createFeedbackForm();
        mainPanel.add(formPanel);
        
        // Right panel - Feedback list
        JPanel listPanel = createFeedbackList();
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
        
        averageRatingLabel = new JLabel("Average Rating: 0.0");
        averageRatingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        averageRatingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        averageRatingLabel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        averageRatingLabel.setForeground(Color.ORANGE);
        
        totalFeedbackLabel = new JLabel("Total Feedback: 0");
        totalFeedbackLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalFeedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalFeedbackLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        totalFeedbackLabel.setForeground(Color.BLUE);
        
        satisfactionLabel = new JLabel("Satisfaction: 0%");
        satisfactionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        satisfactionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        satisfactionLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        satisfactionLabel.setForeground(Color.GREEN);
        
        panel.add(averageRatingLabel);
        panel.add(totalFeedbackLabel);
        panel.add(satisfactionLabel);
        
        return panel;
    }
    
    private JPanel createFeedbackForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Submit Feedback"));
        
        // Customer Name
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField(20);
        namePanel.add(customerNameField);
        panel.add(namePanel);
        
        // Room Number
        JPanel roomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roomPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField(10);
        roomPanel.add(roomNumberField);
        panel.add(roomPanel);
        
        // Email
        JPanel emailPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        emailPanel.add(new JLabel("Email:"));
        emailField = new JTextField(25);
        emailPanel.add(emailField);
        panel.add(emailPanel);
        
        // Rating
        JPanel ratingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        ratingPanel.add(new JLabel("Rating:"));
        ratingBox = new JComboBox<>(new String[]{"5 - Excellent", "4 - Very Good", "3 - Good", "2 - Fair", "1 - Poor"});
        ratingPanel.add(ratingBox);
        panel.add(ratingPanel);
        
        // Category
        JPanel categoryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        categoryPanel.add(new JLabel("Category:"));
        categoryBox = new JComboBox<>(new String[]{
            "Overall Experience", "Room Quality", "Service", "Cleanliness", 
            "Food & Beverage", "Staff Friendliness", "Value for Money", "Location", "Other"
        });
        categoryPanel.add(categoryBox);
        panel.add(categoryPanel);
        
        // Feedback
        JPanel feedbackPanel = new JPanel(new BorderLayout());
        feedbackPanel.add(new JLabel("Feedback:"), BorderLayout.NORTH);
        feedbackArea = new JTextArea(4, 25);
        feedbackArea.setLineWrap(true);
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);
        feedbackPanel.add(feedbackScroll, BorderLayout.CENTER);
        panel.add(feedbackPanel);
        
        // Response (for staff/admin)
        JPanel responsePanel = new JPanel(new BorderLayout());
        responsePanel.add(new JLabel("Staff Response:"), BorderLayout.NORTH);
        responseArea = new JTextArea(3, 25);
        responseArea.setLineWrap(true);
        JScrollPane responseScroll = new JScrollPane(responseArea);
        responsePanel.add(responseScroll, BorderLayout.CENTER);
        panel.add(responsePanel);
        
        return panel;
    }
    
    private JPanel createFeedbackList() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Feedback List"));
        
        // Create table model
        String[] columnNames = {"ID", "Customer", "Room", "Rating", "Category", "Date", "Status"};
        Object[][] data = {};
        
        feedbackTable = new JTable(data, columnNames);
        feedbackTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feedbackTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(feedbackTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            loadFeedback();
            updateStatistics();
        });
        panel.add(refreshBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton submitBtn = new JButton("Submit Feedback");
        submitBtn.setBackground(new Color(34, 139, 34));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.addActionListener(e -> submitFeedback());
        
        JButton respondBtn = new JButton("Respond to Feedback");
        respondBtn.setBackground(new Color(70, 130, 180));
        respondBtn.setForeground(Color.WHITE);
        respondBtn.setFont(new Font("Arial", Font.BOLD, 14));
        respondBtn.addActionListener(e -> respondToFeedback());
        
        JButton viewBtn = new JButton("View Details");
        viewBtn.setBackground(new Color(255, 140, 0));
        viewBtn.setForeground(Color.WHITE);
        viewBtn.setFont(new Font("Arial", Font.BOLD, 14));
        viewBtn.addActionListener(e -> viewFeedbackDetails());
        
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
        
        panel.add(submitBtn);
        panel.add(respondBtn);
        panel.add(viewBtn);
        panel.add(clearBtn);
        panel.add(backBtn);
        
        return panel;
    }
    
    private void loadFeedback() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM customer_feedback ORDER BY feedback_date DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            // Clear existing table data
            while (feedbackTable.getRowCount() > 0) {
                ((javax.swing.table.DefaultTableModel) feedbackTable.getModel()).removeRow(0);
            }
            
            // Add new data
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) feedbackTable.getModel();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("customer_name"),
                    rs.getString("room_number"),
                    rs.getInt("rating") + " stars",
                    rs.getString("category"),
                    rs.getDate("feedback_date"),
                    rs.getString("status")
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading feedback: " + e.getMessage());
        }
    }
    
    private void updateStatistics() {
        try (Connection conn = DBConnection.getConnection()) {
            // Average rating
            String avgQuery = "SELECT AVG(rating) as avg_rating FROM customer_feedback";
            PreparedStatement avgStmt = conn.prepareStatement(avgQuery);
            ResultSet avgRs = avgStmt.executeQuery();
            
            if (avgRs.next()) {
                double avgRating = avgRs.getDouble("avg_rating");
                averageRatingLabel.setText(String.format("Average Rating: %.1f", avgRating));
            }
            
            // Total feedback
            String totalQuery = "SELECT COUNT(*) as total FROM customer_feedback";
            PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
            ResultSet totalRs = totalStmt.executeQuery();
            
            if (totalRs.next()) {
                int total = totalRs.getInt("total");
                totalFeedbackLabel.setText("Total Feedback: " + total);
            }
            
            // Satisfaction rate (4+ stars)
            String satisfactionQuery = "SELECT COUNT(*) as satisfied FROM customer_feedback WHERE rating >= 4";
            PreparedStatement satisfactionStmt = conn.prepareStatement(satisfactionQuery);
            ResultSet satisfactionRs = satisfactionStmt.executeQuery();
            
            if (satisfactionRs.next()) {
                int satisfied = satisfactionRs.getInt("satisfied");
                int total = Integer.parseInt(totalFeedbackLabel.getText().split(": ")[1]);
                double satisfactionRate = total > 0 ? (double) satisfied / total * 100 : 0;
                satisfactionLabel.setText(String.format("Satisfaction: %.1f%%", satisfactionRate));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void submitFeedback() {
        if (customerNameField.getText().isEmpty() || feedbackArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in customer name and feedback!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String insertQuery = "INSERT INTO customer_feedback (customer_name, room_number, email, rating, category, feedback_text, feedback_date, status) VALUES (?, ?, ?, ?, ?, ?, NOW(), 'New')";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, customerNameField.getText());
            stmt.setString(2, roomNumberField.getText());
            stmt.setString(3, emailField.getText());
            stmt.setInt(4, 5 - ratingBox.getSelectedIndex()); // Convert dropdown index to rating
            stmt.setString(5, (String) categoryBox.getSelectedItem());
            stmt.setString(6, feedbackArea.getText());
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Feedback submitted successfully! Thank you for your input.", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            loadFeedback();
            updateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error submitting feedback: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void respondToFeedback() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a feedback to respond to!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (responseArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a response!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int feedbackId = (Integer) feedbackTable.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String updateQuery = "UPDATE customer_feedback SET staff_response = ?, status = 'Responded', response_date = NOW() WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, responseArea.getText());
            stmt.setInt(2, feedbackId);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Response submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            responseArea.setText("");
            loadFeedback();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error responding to feedback: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void viewFeedbackDetails() {
        int selectedRow = feedbackTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a feedback to view!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int feedbackId = (Integer) feedbackTable.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM customer_feedback WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, feedbackId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                StringBuilder details = new StringBuilder();
                details.append("=== FEEDBACK DETAILS ===\n\n");
                details.append("Customer: ").append(rs.getString("customer_name")).append("\n");
                details.append("Room: ").append(rs.getString("room_number")).append("\n");
                details.append("Email: ").append(rs.getString("email")).append("\n");
                details.append("Rating: ").append(rs.getInt("rating")).append(" stars\n");
                details.append("Category: ").append(rs.getString("category")).append("\n");
                details.append("Date: ").append(rs.getDate("feedback_date")).append("\n");
                details.append("Status: ").append(rs.getString("status")).append("\n\n");
                details.append("Feedback:\n").append(rs.getString("feedback_text")).append("\n\n");
                
                if (rs.getString("staff_response") != null) {
                    details.append("Staff Response:\n").append(rs.getString("staff_response")).append("\n");
                }
                
                JTextArea detailsArea = new JTextArea(details.toString());
                detailsArea.setEditable(false);
                detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                
                JScrollPane scrollPane = new JScrollPane(detailsArea);
                scrollPane.setPreferredSize(new Dimension(500, 400));
                
                JOptionPane.showMessageDialog(this, scrollPane, "Feedback Details", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error viewing feedback details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        customerNameField.setText("");
        roomNumberField.setText("");
        emailField.setText("");
        feedbackArea.setText("");
        responseArea.setText("");
        ratingBox.setSelectedIndex(0);
        categoryBox.setSelectedIndex(0);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FeedbackSystem().setVisible(true);
        });
    }
} 