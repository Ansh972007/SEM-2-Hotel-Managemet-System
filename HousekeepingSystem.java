package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HousekeepingSystem extends JFrame {
    
    private JTextField roomNumberField, assignedToField;
    private JComboBox<String> cleaningTypeBox, statusBox, priorityBox;
    private JTextArea notesArea, specialInstructionsArea;
    private JTable tasksTable;
    private JSpinner dateSpinner;
    private JLabel totalTasksLabel, completedTasksLabel, pendingTasksLabel;
    
    public HousekeepingSystem() {
        setTitle("Housekeeping Management System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createUI();
        loadHousekeepingTasks();
        updateStatistics();
    }
    
    private void createUI() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(0, 128, 0));
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        
        JLabel titleLabel = new JLabel("Housekeeping Management System");
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
        
        // Left panel - Task form
        JPanel formPanel = createTaskForm();
        mainPanel.add(formPanel);
        
        // Right panel - Tasks list
        JPanel listPanel = createTasksList();
        mainPanel.add(listPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setPreferredSize(new Dimension(1200, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        totalTasksLabel = new JLabel("Total Tasks: 0");
        totalTasksLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalTasksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalTasksLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        
        completedTasksLabel = new JLabel("Completed: 0");
        completedTasksLabel.setFont(new Font("Arial", Font.BOLD, 16));
        completedTasksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        completedTasksLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        completedTasksLabel.setForeground(Color.GREEN);
        
        pendingTasksLabel = new JLabel("Pending: 0");
        pendingTasksLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pendingTasksLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pendingTasksLabel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        pendingTasksLabel.setForeground(Color.ORANGE);
        
        panel.add(totalTasksLabel);
        panel.add(completedTasksLabel);
        panel.add(pendingTasksLabel);
        
        return panel;
    }
    
    private JPanel createTaskForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("New Cleaning Task"));
        
        // Room Number
        JPanel roomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roomPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField(10);
        roomPanel.add(roomNumberField);
        panel.add(roomPanel);
        
        // Cleaning Type
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Cleaning Type:"));
        cleaningTypeBox = new JComboBox<>(new String[]{
            "Daily Cleaning", "Deep Cleaning", "Check-out Cleaning", "Turnover Cleaning",
            "Carpet Cleaning", "Window Cleaning", "Bathroom Deep Clean", "Kitchen Cleaning"
        });
        typePanel.add(cleaningTypeBox);
        panel.add(typePanel);
        
        // Priority
        JPanel priorityPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        priorityPanel.add(new JLabel("Priority:"));
        priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "Urgent"});
        priorityPanel.add(priorityBox);
        panel.add(priorityPanel);
        
        // Assigned To
        JPanel assignedPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        assignedPanel.add(new JLabel("Assigned To:"));
        assignedToField = new JTextField(15);
        assignedPanel.add(assignedToField);
        panel.add(assignedPanel);
        
        // Date
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Scheduled Date:"));
        dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        datePanel.add(dateSpinner);
        panel.add(datePanel);
        
        // Status
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.add(new JLabel("Status:"));
        statusBox = new JComboBox<>(new String[]{"Pending", "In Progress", "Completed", "Cancelled"});
        statusPanel.add(statusBox);
        panel.add(statusPanel);
        
        // Special Instructions
        JPanel instructionsPanel = new JPanel(new BorderLayout());
        instructionsPanel.add(new JLabel("Special Instructions:"), BorderLayout.NORTH);
        specialInstructionsArea = new JTextArea(3, 20);
        specialInstructionsArea.setLineWrap(true);
        JScrollPane instructionsScroll = new JScrollPane(specialInstructionsArea);
        instructionsPanel.add(instructionsScroll, BorderLayout.CENTER);
        panel.add(instructionsPanel);
        
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
    
    private JPanel createTasksList() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Housekeeping Tasks"));
        
        // Create table model
        String[] columnNames = {"ID", "Room", "Type", "Priority", "Status", "Assigned To", "Date", "Notes"};
        Object[][] data = {};
        
        tasksTable = new JTable(data, columnNames);
        tasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tasksTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(tasksTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            loadHousekeepingTasks();
            updateStatistics();
        });
        panel.add(refreshBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton submitBtn = new JButton("Create Task");
        submitBtn.setBackground(new Color(34, 139, 34));
        submitBtn.setForeground(Color.WHITE);
        submitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        submitBtn.addActionListener(e -> createTask());
        
        JButton updateBtn = new JButton("Update Task");
        updateBtn.setBackground(new Color(70, 130, 180));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        updateBtn.addActionListener(e -> updateTask());
        
        JButton completeBtn = new JButton("Mark Complete");
        completeBtn.setBackground(new Color(255, 140, 0));
        completeBtn.setForeground(Color.WHITE);
        completeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        completeBtn.addActionListener(e -> markComplete());
        
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
        panel.add(clearBtn);
        panel.add(backBtn);
        
        return panel;
    }
    
    private void loadHousekeepingTasks() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM housekeeping_tasks ORDER BY scheduled_date DESC";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            // Clear existing table data
            while (tasksTable.getRowCount() > 0) {
                ((javax.swing.table.DefaultTableModel) tasksTable.getModel()).removeRow(0);
            }
            
            // Add new data
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) tasksTable.getModel();
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getString("cleaning_type"),
                    rs.getString("priority"),
                    rs.getString("status"),
                    rs.getString("assigned_to"),
                    rs.getDate("scheduled_date"),
                    rs.getString("notes")
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading housekeeping tasks: " + e.getMessage());
        }
    }
    
    private void updateStatistics() {
        try (Connection conn = DBConnection.getConnection()) {
            String totalQuery = "SELECT COUNT(*) as total FROM housekeeping_tasks";
            String completedQuery = "SELECT COUNT(*) as completed FROM housekeeping_tasks WHERE status = 'Completed'";
            String pendingQuery = "SELECT COUNT(*) as pending FROM housekeeping_tasks WHERE status IN ('Pending', 'In Progress')";
            
            PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
            PreparedStatement completedStmt = conn.prepareStatement(completedQuery);
            PreparedStatement pendingStmt = conn.prepareStatement(pendingQuery);
            
            ResultSet totalRs = totalStmt.executeQuery();
            ResultSet completedRs = completedStmt.executeQuery();
            ResultSet pendingRs = pendingStmt.executeQuery();
            
            if (totalRs.next()) {
                totalTasksLabel.setText("Total Tasks: " + totalRs.getInt("total"));
            }
            if (completedRs.next()) {
                completedTasksLabel.setText("Completed: " + completedRs.getInt("completed"));
            }
            if (pendingRs.next()) {
                pendingTasksLabel.setText("Pending: " + pendingRs.getInt("pending"));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void createTask() {
        if (roomNumberField.getText().isEmpty() || assignedToField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in room number and assigned staff!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String insertQuery = "INSERT INTO housekeeping_tasks (room_number, cleaning_type, priority, assigned_to, scheduled_date, status, special_instructions, notes, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, roomNumberField.getText());
            stmt.setString(2, (String) cleaningTypeBox.getSelectedItem());
            stmt.setString(3, (String) priorityBox.getSelectedItem());
            stmt.setString(4, assignedToField.getText());
            stmt.setDate(5, new java.sql.Date(((Date) dateSpinner.getValue()).getTime()));
            stmt.setString(6, (String) statusBox.getSelectedItem());
            stmt.setString(7, specialInstructionsArea.getText());
            stmt.setString(8, notesArea.getText());
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Housekeeping task created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            loadHousekeepingTasks();
            updateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating task: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateTask() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int taskId = (Integer) tasksTable.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String updateQuery = "UPDATE housekeeping_tasks SET status = ?, notes = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, (String) statusBox.getSelectedItem());
            stmt.setString(2, notesArea.getText());
            stmt.setInt(3, taskId);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Task updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            loadHousekeepingTasks();
            updateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating task: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void markComplete() {
        int selectedRow = tasksTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a task to mark complete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int taskId = (Integer) tasksTable.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String updateQuery = "UPDATE housekeeping_tasks SET status = 'Completed', completion_date = NOW() WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setInt(1, taskId);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Task marked as completed!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            loadHousekeepingTasks();
            updateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error marking task complete: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        roomNumberField.setText("");
        assignedToField.setText("");
        specialInstructionsArea.setText("");
        notesArea.setText("");
        cleaningTypeBox.setSelectedIndex(0);
        priorityBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        dateSpinner.setValue(new Date());
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HousekeepingSystem().setVisible(true);
        });
    }
} 