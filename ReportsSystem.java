package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class ReportsSystem extends JFrame {
    
    private JComboBox<String> reportTypeBox;
    private JTable reportTable;
    private JTextArea reportArea;
    private JLabel totalRevenueLabel, occupancyRateLabel, averageRatingLabel;
    private JSpinner startDateSpinner, endDateSpinner;
    
    public ReportsSystem() {
        setTitle("Reports and Analytics System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createUI();
        loadDashboardStats();
    }
    
    private void createUI() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(75, 0, 130));
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        
        JLabel titleLabel = new JLabel("Reports and Analytics System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Dashboard stats panel
        JPanel statsPanel = createDashboardStats();
        add(statsPanel, BorderLayout.CENTER);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel - Report controls
        JPanel controlsPanel = createControlsPanel();
        mainPanel.add(controlsPanel);
        
        // Right panel - Report display
        JPanel displayPanel = createDisplayPanel();
        mainPanel.add(displayPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createDashboardStats() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBackground(new Color(240, 248, 255));
        panel.setPreferredSize(new Dimension(1200, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        totalRevenueLabel = new JLabel("Total Revenue: $0");
        totalRevenueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalRevenueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalRevenueLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        totalRevenueLabel.setForeground(Color.GREEN);
        
        occupancyRateLabel = new JLabel("Occupancy Rate: 0%");
        occupancyRateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        occupancyRateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        occupancyRateLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        occupancyRateLabel.setForeground(Color.BLUE);
        
        averageRatingLabel = new JLabel("Average Rating: 0.0");
        averageRatingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        averageRatingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        averageRatingLabel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        averageRatingLabel.setForeground(Color.ORANGE);
        
        panel.add(totalRevenueLabel);
        panel.add(occupancyRateLabel);
        panel.add(averageRatingLabel);
        
        return panel;
    }
    
    private JPanel createControlsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Report Controls"));
        
        // Report Type
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Report Type:"));
        reportTypeBox = new JComboBox<>(new String[]{
            "Revenue Report", "Occupancy Report", "Customer Report", "Room Status Report",
            "Payment Report", "Maintenance Report", "Housekeeping Report", "Employee Performance"
        });
        typePanel.add(reportTypeBox);
        panel.add(typePanel);
        
        // Date Range
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        datePanel.add(new JLabel("Start Date:"));
        startDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startDateSpinner, "yyyy-MM-dd");
        startDateSpinner.setEditor(startEditor);
        datePanel.add(startDateSpinner);
        panel.add(datePanel);
        
        JPanel endDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        endDatePanel.add(new JLabel("End Date:"));
        endDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endDateSpinner, "yyyy-MM-dd");
        endDateSpinner.setEditor(endEditor);
        endDatePanel.add(endDateSpinner);
        panel.add(endDatePanel);
        
        // Generate button
        JButton generateBtn = new JButton("Generate Report");
        generateBtn.setBackground(new Color(34, 139, 34));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        generateBtn.addActionListener(e -> generateReport());
        panel.add(generateBtn);
        
        return panel;
    }
    
    private JPanel createDisplayPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Report Display"));
        
        // Create tabbed pane for different display modes
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Table view
        String[] columnNames = {"Column 1", "Column 2", "Column 3", "Column 4"};
        Object[][] data = {};
        reportTable = new JTable(data, columnNames);
        JScrollPane tableScroll = new JScrollPane(reportTable);
        tabbedPane.addTab("Table View", tableScroll);
        
        // Text view
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane textScroll = new JScrollPane(reportArea);
        tabbedPane.addTab("Text View", textScroll);
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton exportBtn = new JButton("Export to CSV");
        exportBtn.setBackground(new Color(70, 130, 180));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setFont(new Font("Arial", Font.BOLD, 14));
        exportBtn.addActionListener(e -> exportToCSV());
        
        JButton printBtn = new JButton("Print Report");
        printBtn.setBackground(new Color(255, 140, 0));
        printBtn.setForeground(Color.WHITE);
        printBtn.setFont(new Font("Arial", Font.BOLD, 14));
        printBtn.addActionListener(e -> printReport());
        
        JButton refreshBtn = new JButton("Refresh Dashboard");
        refreshBtn.setBackground(new Color(34, 139, 34));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 14));
        refreshBtn.addActionListener(e -> loadDashboardStats());
        
        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(105, 105, 105));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            new AdminDashboard("Admin").setVisible(true);
            setVisible(false);
        });
        
        panel.add(exportBtn);
        panel.add(printBtn);
        panel.add(refreshBtn);
        panel.add(backBtn);
        
        return panel;
    }
    
    private void loadDashboardStats() {
        try (Connection conn = DBConnection.getConnection()) {
            // Total Revenue
            String revenueQuery = "SELECT SUM(total_amount) as total FROM payments WHERE status = 'Completed'";
            PreparedStatement revenueStmt = conn.prepareStatement(revenueQuery);
            ResultSet revenueRs = revenueStmt.executeQuery();
            
            if (revenueRs.next()) {
                double totalRevenue = revenueRs.getDouble("total");
                totalRevenueLabel.setText(String.format("Total Revenue: $%.2f", totalRevenue));
            }
            
            // Occupancy Rate
            String occupancyQuery = "SELECT " +
                "COUNT(*) as total_rooms, " +
                "SUM(CASE WHEN availability = 'Occupied' THEN 1 ELSE 0 END) as occupied_rooms " +
                "FROM room";
            PreparedStatement occupancyStmt = conn.prepareStatement(occupancyQuery);
            ResultSet occupancyRs = occupancyStmt.executeQuery();
            
            if (occupancyRs.next()) {
                int totalRooms = occupancyRs.getInt("total_rooms");
                int occupiedRooms = occupancyRs.getInt("occupied_rooms");
                double occupancyRate = totalRooms > 0 ? (double) occupiedRooms / totalRooms * 100 : 0;
                occupancyRateLabel.setText(String.format("Occupancy Rate: %.1f%%", occupancyRate));
            }
            
            // Average Rating (placeholder - you can add rating system)
            averageRatingLabel.setText("Average Rating: 4.5");
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading dashboard stats: " + e.getMessage());
        }
    }
    
    private void generateReport() {
        String reportType = (String) reportTypeBox.getSelectedItem();
        Date startDate = (Date) startDateSpinner.getValue();
        Date endDate = (Date) endDateSpinner.getValue();
        
        try (Connection conn = DBConnection.getConnection()) {
            switch (reportType) {
                case "Revenue Report":
                    generateRevenueReport(conn, startDate, endDate);
                    break;
                case "Occupancy Report":
                    generateOccupancyReport(conn, startDate, endDate);
                    break;
                case "Customer Report":
                    generateCustomerReport(conn, startDate, endDate);
                    break;
                case "Room Status Report":
                    generateRoomStatusReport(conn);
                    break;
                case "Payment Report":
                    generatePaymentReport(conn, startDate, endDate);
                    break;
                case "Maintenance Report":
                    generateMaintenanceReport(conn, startDate, endDate);
                    break;
                case "Housekeeping Report":
                    generateHousekeepingReport(conn, startDate, endDate);
                    break;
                case "Employee Performance":
                    generateEmployeeReport(conn, startDate, endDate);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }
    
    private void generateRevenueReport(Connection conn, Date startDate, Date endDate) throws SQLException {
        String query = "SELECT DATE(payment_date) as date, SUM(total_amount) as daily_revenue, COUNT(*) as transactions " +
                      "FROM payments WHERE payment_date BETWEEN ? AND ? GROUP BY DATE(payment_date) ORDER BY date";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setDate(1, new java.sql.Date(startDate.getTime()));
        stmt.setDate(2, new java.sql.Date(endDate.getTime()));
        
        ResultSet rs = stmt.executeQuery();
        
        // Update table
        String[] columnNames = {"Date", "Daily Revenue", "Transactions"};
        Vector<Vector<Object>> data = new Vector<>();
        
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getDate("date"));
            row.add(String.format("$%.2f", rs.getDouble("daily_revenue")));
            row.add(rs.getInt("transactions"));
            data.add(row);
        }
        
        updateTable(columnNames, data);
        
        // Update text area
        StringBuilder report = new StringBuilder();
        report.append("=== REVENUE REPORT ===\n");
        report.append("Period: ").append(new SimpleDateFormat("yyyy-MM-dd").format(startDate))
              .append(" to ").append(new SimpleDateFormat("yyyy-MM-dd").format(endDate)).append("\n\n");
        
        double totalRevenue = 0;
        int totalTransactions = 0;
        
        rs.beforeFirst();
        while (rs.next()) {
            report.append(String.format("%s: $%.2f (%d transactions)\n", 
                rs.getDate("date"), rs.getDouble("daily_revenue"), rs.getInt("transactions")));
            totalRevenue += rs.getDouble("daily_revenue");
            totalTransactions += rs.getInt("transactions");
        }
        
        report.append("\n=== SUMMARY ===\n");
        report.append(String.format("Total Revenue: $%.2f\n", totalRevenue));
        report.append(String.format("Total Transactions: %d\n", totalTransactions));
        report.append(String.format("Average Daily Revenue: $%.2f\n", totalRevenue / Math.max(1, data.size())));
        
        reportArea.setText(report.toString());
    }
    
    private void generateOccupancyReport(Connection conn, Date startDate, Date endDate) throws SQLException {
        String query = "SELECT room_number, room_type, availability, price FROM room ORDER BY room_number";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        String[] columnNames = {"Room Number", "Room Type", "Availability", "Price"};
        Vector<Vector<Object>> data = new Vector<>();
        
        int totalRooms = 0;
        int occupiedRooms = 0;
        double totalValue = 0;
        
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString("room_number"));
            row.add(rs.getString("room_type"));
            row.add(rs.getString("availability"));
            row.add(String.format("$%.2f", rs.getDouble("price")));
            data.add(row);
            
            totalRooms++;
            if ("Occupied".equals(rs.getString("availability"))) {
                occupiedRooms++;
            }
            totalValue += rs.getDouble("price");
        }
        
        updateTable(columnNames, data);
        
        StringBuilder report = new StringBuilder();
        report.append("=== OCCUPANCY REPORT ===\n\n");
        report.append(String.format("Total Rooms: %d\n", totalRooms));
        report.append(String.format("Occupied Rooms: %d\n", occupiedRooms));
        report.append(String.format("Available Rooms: %d\n", totalRooms - occupiedRooms));
        report.append(String.format("Occupancy Rate: %.1f%%\n", (double) occupiedRooms / totalRooms * 100));
        report.append(String.format("Total Room Value: $%.2f\n", totalValue));
        
        reportArea.setText(report.toString());
    }
    
    private void generateCustomerReport(Connection conn, Date startDate, Date endDate) throws SQLException {
        String query = "SELECT * FROM customer ORDER BY check_in_date DESC";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        String[] columnNames = {"ID", "Name", "Phone", "Room", "Check-in", "Status"};
        Vector<Vector<Object>> data = new Vector<>();
        
        int totalCustomers = 0;
        int checkedIn = 0;
        int checkedOut = 0;
        
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getInt("id"));
            row.add(rs.getString("name"));
            row.add(rs.getString("phone"));
            row.add(rs.getString("room_number"));
            row.add(rs.getDate("check_in_date"));
            row.add(rs.getString("status"));
            data.add(row);
            
            totalCustomers++;
            if ("Checked In".equals(rs.getString("status"))) {
                checkedIn++;
            } else if ("Checked Out".equals(rs.getString("status"))) {
                checkedOut++;
            }
        }
        
        updateTable(columnNames, data);
        
        StringBuilder report = new StringBuilder();
        report.append("=== CUSTOMER REPORT ===\n\n");
        report.append(String.format("Total Customers: %d\n", totalCustomers));
        report.append(String.format("Currently Checked In: %d\n", checkedIn));
        report.append(String.format("Checked Out: %d\n", checkedOut));
        report.append(String.format("Active Rate: %.1f%%\n", (double) checkedIn / totalCustomers * 100));
        
        reportArea.setText(report.toString());
    }
    
    private void generateRoomStatusReport(Connection conn) throws SQLException {
        String query = "SELECT room_type, COUNT(*) as total, " +
                      "SUM(CASE WHEN availability = 'Available' THEN 1 ELSE 0 END) as available, " +
                      "SUM(CASE WHEN availability = 'Occupied' THEN 1 ELSE 0 END) as occupied " +
                      "FROM room GROUP BY room_type";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        String[] columnNames = {"Room Type", "Total", "Available", "Occupied", "Occupancy %"};
        Vector<Vector<Object>> data = new Vector<>();
        
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            int total = rs.getInt("total");
            int available = rs.getInt("available");
            int occupied = rs.getInt("occupied");
            
            row.add(rs.getString("room_type"));
            row.add(total);
            row.add(available);
            row.add(occupied);
            row.add(String.format("%.1f%%", (double) occupied / total * 100));
            data.add(row);
        }
        
        updateTable(columnNames, data);
        
        StringBuilder report = new StringBuilder();
        report.append("=== ROOM STATUS REPORT ===\n\n");
        report.append("Room type breakdown with occupancy rates.\n");
        
        reportArea.setText(report.toString());
    }
    
    private void generatePaymentReport(Connection conn, Date startDate, Date endDate) throws SQLException {
        String query = "SELECT payment_method, COUNT(*) as count, SUM(total_amount) as total " +
                      "FROM payments WHERE payment_date BETWEEN ? AND ? GROUP BY payment_method";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setDate(1, new java.sql.Date(startDate.getTime()));
        stmt.setDate(2, new java.sql.Date(endDate.getTime()));
        
        ResultSet rs = stmt.executeQuery();
        
        String[] columnNames = {"Payment Method", "Transactions", "Total Amount", "Average"};
        Vector<Vector<Object>> data = new Vector<>();
        
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            int count = rs.getInt("count");
            double total = rs.getDouble("total");
            
            row.add(rs.getString("payment_method"));
            row.add(count);
            row.add(String.format("$%.2f", total));
            row.add(String.format("$%.2f", total / count));
            data.add(row);
        }
        
        updateTable(columnNames, data);
        
        StringBuilder report = new StringBuilder();
        report.append("=== PAYMENT REPORT ===\n\n");
        report.append("Payment method analysis for the selected period.\n");
        
        reportArea.setText(report.toString());
    }
    
    private void generateMaintenanceReport(Connection conn, Date startDate, Date endDate) throws SQLException {
        String query = "SELECT issue_type, COUNT(*) as count, " +
                      "SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) as completed " +
                      "FROM maintenance_requests WHERE request_date BETWEEN ? AND ? GROUP BY issue_type";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setDate(1, new java.sql.Date(startDate.getTime()));
        stmt.setDate(2, new java.sql.Date(endDate.getTime()));
        
        ResultSet rs = stmt.executeQuery();
        
        String[] columnNames = {"Issue Type", "Total Requests", "Completed", "Completion Rate"};
        Vector<Vector<Object>> data = new Vector<>();
        
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            int total = rs.getInt("count");
            int completed = rs.getInt("completed");
            
            row.add(rs.getString("issue_type"));
            row.add(total);
            row.add(completed);
            row.add(String.format("%.1f%%", (double) completed / total * 100));
            data.add(row);
        }
        
        updateTable(columnNames, data);
        
        StringBuilder report = new StringBuilder();
        report.append("=== MAINTENANCE REPORT ===\n\n");
        report.append("Maintenance request analysis by issue type.\n");
        
        reportArea.setText(report.toString());
    }
    
    private void generateHousekeepingReport(Connection conn, Date startDate, Date endDate) throws SQLException {
        String query = "SELECT cleaning_type, COUNT(*) as count, " +
                      "SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) as completed " +
                      "FROM housekeeping_tasks WHERE scheduled_date BETWEEN ? AND ? GROUP BY cleaning_type";
        
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setDate(1, new java.sql.Date(startDate.getTime()));
        stmt.setDate(2, new java.sql.Date(endDate.getTime()));
        
        ResultSet rs = stmt.executeQuery();
        
        String[] columnNames = {"Cleaning Type", "Total Tasks", "Completed", "Completion Rate"};
        Vector<Vector<Object>> data = new Vector<>();
        
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            int total = rs.getInt("count");
            int completed = rs.getInt("completed");
            
            row.add(rs.getString("cleaning_type"));
            row.add(total);
            row.add(completed);
            row.add(String.format("%.1f%%", (double) completed / total * 100));
            data.add(row);
        }
        
        updateTable(columnNames, data);
        
        StringBuilder report = new StringBuilder();
        report.append("=== HOUSEKEEPING REPORT ===\n\n");
        report.append("Housekeeping task analysis by cleaning type.\n");
        
        reportArea.setText(report.toString());
    }
    
    private void generateEmployeeReport(Connection conn, Date startDate, Date endDate) throws SQLException {
        String query = "SELECT name, department, salary FROM employee ORDER BY department, name";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        String[] columnNames = {"Name", "Department", "Salary"};
        Vector<Vector<Object>> data = new Vector<>();
        
        while (rs.next()) {
            Vector<Object> row = new Vector<>();
            row.add(rs.getString("name"));
            row.add(rs.getString("department"));
            row.add(String.format("$%.2f", rs.getDouble("salary")));
            data.add(row);
        }
        
        updateTable(columnNames, data);
        
        StringBuilder report = new StringBuilder();
        report.append("=== EMPLOYEE PERFORMANCE REPORT ===\n\n");
        report.append("Employee information and department breakdown.\n");
        
        reportArea.setText(report.toString());
    }
    
    private void updateTable(String[] columnNames, Vector<Vector<Object>> data) {
        javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(data, new Vector<String>() {{
            for (String col : columnNames) add(col);
        }});
        reportTable.setModel(model);
    }
    
    private void exportToCSV() {
        JOptionPane.showMessageDialog(this, "CSV export functionality would be implemented here.", "Export", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void printReport() {
        try {
            reportArea.print();
            JOptionPane.showMessageDialog(this, "Report sent to printer!", "Print Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error printing report: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ReportsSystem().setVisible(true);
        });
    }
} 