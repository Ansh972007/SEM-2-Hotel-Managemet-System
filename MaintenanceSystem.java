package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaintenanceSystem extends JFrame {
    
    private JTextField roomNumberField, requestByField, priorityField;
    private JComboBox<String> issueTypeBox, statusBox, priorityBox;
    private JTextArea descriptionArea, notesArea;
    private JTable requestsTable;
    private DefaultListModel<String> requestsListModel;
    private JList<String> requestsList;
    
    public MaintenanceSystem() {
        setTitle("Maintenance Request System");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createUI();
        loadMaintenanceRequests();
    }
    
    private void createUI() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(139, 69, 19));
        headerPanel.setPreferredSize(new Dimension(1000, 60));
        
        JLabel titleLabel = new JLabel("Maintenance Request System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
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
    
    private JPanel createRequestForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("New Maintenance Request"));
        
        // Room Number
        JPanel roomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roomPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField(10);
        roomPanel.add(roomNumberField);
        panel.add(roomPanel);
        
        // Issue Type
        JPanel issuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        issuePanel.add(new JLabel("Issue Type:"));
        issueTypeBox = new JComboBox<>(new String[]{
            "Electrical", "Plumbing", "HVAC", "Furniture", "Appliances", 
            "Internet/WiFi", "TV/Entertainment", "Cleaning", "Pest Control", "Other"
        });
        issuePanel.add(issueTypeBox);
        panel.add(issuePanel);
        
        // Priority
        JPanel priorityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        priorityPanel.add(new JLabel("Priority:"));
        priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "Urgent"});
        priorityPanel.add(priorityBox);
        panel.add(priorityPanel);
        
        // Requested By
        JPanel requestByPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        requestByPanel.add(new JLabel("Requested By:"));
        requestByField = new JTextField(15);
        requestByPanel.add(requestByField);
        panel.add(requestByPanel);
        
        // Description
        JPanel descPanel = new JPanel(new BorderLayout());
        descPanel.add(new JLabel("Description:"), BorderLayout.NORTH);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        descPanel.add(descScroll, BorderLayout.CENTER);
        panel.add(descPanel);
        
        // Status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Status:"));
        statusBox = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed", "Cancelled"});
        statusPanel.add(statusBox);
        panel.add(statusPanel);
        
        // Notes
        JPanel notesPanel = new JPanel(new BorderLayout());
        notesPanel.add(new JLabel("Notes:"), BorderLayout.NORTH);
        notesArea = new JTextArea(3, 20);
        notesArea.setLineWrap(true);
        JScrollPane notesScroll = new JScrollPane(notesArea);
        notesPanel.add(notesScroll, BorderLayout.CENTER);
        panel.add(notesPanel);
        
        return panel;
    }
    
    private JPanel createRequestsList() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Maintenance Requests"));
        
        // Create table model
        String[] columnNames = {"ID", "Room", "Issue", "Priority", "Status", "Date", "Requested By"};
        Object[][] data = {};
        
        requestsTable = new JTable(data, columnNames);
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        requestsTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(requestsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadMaintenanceRequests());
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
        panel.add(clearBtn);
        panel.add(backBtn);
        
        return panel;
    }
    
    private void loadMaintenanceRequests() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM maintenance_requests ORDER BY request_date DESC";
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
                    rs.getString("room_number"),
                    rs.getString("issue_type"),
                    rs.getString("priority"),
                    rs.getString("status"),
                    rs.getDate("request_date"),
                    rs.getString("requested_by")
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading maintenance requests: " + e.getMessage());
        }
    }
    
    private void submitRequest() {
        if (roomNumberField.getText().isEmpty() || descriptionArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in room number and description!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String insertQuery = "INSERT INTO maintenance_requests (room_number, issue_type, priority, description, status, requested_by, request_date, notes) VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, roomNumberField.getText());
            stmt.setString(2, (String) issueTypeBox.getSelectedItem());
            stmt.setString(3, (String) priorityBox.getSelectedItem());
            stmt.setString(4, descriptionArea.getText());
            stmt.setString(5, (String) statusBox.getSelectedItem());
            stmt.setString(6, requestByField.getText());
            stmt.setString(7, notesArea.getText());
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Maintenance request submitted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            loadMaintenanceRequests();
            
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
            String updateQuery = "UPDATE maintenance_requests SET status = ?, notes = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, (String) statusBox.getSelectedItem());
            stmt.setString(2, notesArea.getText());
            stmt.setInt(3, requestId);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Request updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            loadMaintenanceRequests();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating request: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        roomNumberField.setText("");
        requestByField.setText("");
        descriptionArea.setText("");
        notesArea.setText("");
        issueTypeBox.setSelectedIndex(0);
        priorityBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MaintenanceSystem().setVisible(true);
        });
    }
} 