package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InventorySystem extends JFrame {
    
    private JTextField itemNameField, quantityField, priceField, supplierField;
    private JComboBox<String> categoryBox, unitBox, statusBox;
    private JTextArea descriptionArea;
    private JTable inventoryTable;
    private JLabel totalItemsLabel, lowStockLabel, totalValueLabel;
    private JSpinner reorderLevelSpinner, maxLevelSpinner;
    
    public InventorySystem() {
        setTitle("Inventory Management System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createUI();
        loadInventory();
        updateStatistics();
    }
    
    private void createUI() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(156, 39, 176));
        headerPanel.setPreferredSize(new Dimension(1200, 60));
        
        JLabel titleLabel = new JLabel("Inventory Management System");
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
        
        // Left panel - Inventory form
        JPanel formPanel = createInventoryForm();
        mainPanel.add(formPanel);
        
        // Right panel - Inventory list
        JPanel listPanel = createInventoryList();
        mainPanel.add(listPanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 10));
        panel.setBackground(new Color(243, 229, 245));
        panel.setPreferredSize(new Dimension(1200, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        totalItemsLabel = new JLabel("Total Items: 0");
        totalItemsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalItemsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalItemsLabel.setBorder(BorderFactory.createLineBorder(new Color(103,1,100), 2));
        totalItemsLabel.setForeground(new Color(103,1,100));
        
        lowStockLabel = new JLabel("Low Stock Items: 0");
        lowStockLabel.setFont(new Font("Arial", Font.BOLD, 16));
        lowStockLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lowStockLabel.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 2));
        lowStockLabel.setForeground(Color.ORANGE);
        
        totalValueLabel = new JLabel("Total Value: $0");
        totalValueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalValueLabel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        totalValueLabel.setForeground(Color.GREEN);
        
        panel.add(totalItemsLabel);
        panel.add(lowStockLabel);
        panel.add(totalValueLabel);
        
        return panel;
    }
    
    private JPanel createInventoryForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Add/Edit Inventory Item"));
        
        // Item Details
        JPanel itemPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        itemPanel.setBorder(BorderFactory.createTitledBorder("Item Details"));
        
        itemPanel.add(new JLabel("Item Name:"));
        itemNameField = new JTextField(20);
        itemPanel.add(itemNameField);
        
        itemPanel.add(new JLabel("Category:"));
        categoryBox = new JComboBox<>(new String[]{
            "Amenities", "Cleaning Supplies", "Food & Beverage", "Office Supplies", 
            "Maintenance", "Electronics", "Furniture", "Linens", "Toiletries", "Other"
        });
        itemPanel.add(categoryBox);
        
        itemPanel.add(new JLabel("Description:"));
        descriptionArea = new JTextArea(3, 20);
        descriptionArea.setLineWrap(true);
        JScrollPane descScroll = new JScrollPane(descriptionArea);
        itemPanel.add(descScroll);
        
        itemPanel.add(new JLabel("Unit:"));
        unitBox = new JComboBox<>(new String[]{"Pieces", "Boxes", "Bottles", "Packs", "Sets", "Kilos", "Liters"});
        itemPanel.add(unitBox);
        
        panel.add(itemPanel);
        
        // Stock Details
        JPanel stockPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        stockPanel.setBorder(BorderFactory.createTitledBorder("Stock Details"));
        
        stockPanel.add(new JLabel("Current Quantity:"));
        quantityField = new JTextField(10);
        stockPanel.add(quantityField);
        
        stockPanel.add(new JLabel("Unit Price:"));
        priceField = new JTextField(10);
        stockPanel.add(priceField);
        
        stockPanel.add(new JLabel("Reorder Level:"));
        reorderLevelSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 1000, 1));
        stockPanel.add(reorderLevelSpinner);
        
        stockPanel.add(new JLabel("Max Level:"));
        maxLevelSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 10000, 10));
        stockPanel.add(maxLevelSpinner);
        
        panel.add(stockPanel);
        
        // Supplier Details
        JPanel supplierPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        supplierPanel.setBorder(BorderFactory.createTitledBorder("Supplier Details"));
        
        supplierPanel.add(new JLabel("Supplier:"));
        supplierField = new JTextField(20);
        supplierPanel.add(supplierField);
        
        supplierPanel.add(new JLabel("Status:"));
        statusBox = new JComboBox<>(new String[]{"In Stock", "Low Stock", "Out of Stock", "On Order"});
        supplierPanel.add(statusBox);
        
        panel.add(supplierPanel);
        
        return panel;
    }
    
    private JPanel createInventoryList() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Inventory List"));
        
        // Create table model
        String[] columnNames = {"ID", "Item Name", "Category", "Quantity", "Unit", "Price", "Value", "Status"};
        Object[][] data = {};
        
        inventoryTable = new JTable(data, columnNames);
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Add refresh button
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            loadInventory();
            updateStatistics();
        });
        panel.add(refreshBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton addBtn = new JButton("Add Item");
        addBtn.setBackground(new Color(34, 139, 34));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        addBtn.addActionListener(e -> addItem());
        
        JButton updateBtn = new JButton("Update Item");
        updateBtn.setBackground(new Color(70, 130, 180));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        updateBtn.addActionListener(e -> updateItem());
        
        JButton stockInBtn = new JButton("Stock In");
        stockInBtn.setBackground(new Color(255, 140, 0));
        stockInBtn.setForeground(Color.WHITE);
        stockInBtn.setFont(new Font("Arial", Font.BOLD, 14));
        stockInBtn.addActionListener(e -> stockIn());
        
        JButton stockOutBtn = new JButton("Stock Out");
        stockOutBtn.setBackground(new Color(220, 20, 60));
        stockOutBtn.setForeground(Color.WHITE);
        stockOutBtn.setFont(new Font("Arial", Font.BOLD, 14));
        stockOutBtn.addActionListener(e -> stockOut());
        
        JButton generateOrderBtn = new JButton("Generate Order");
        generateOrderBtn.setBackground(new Color(156, 39, 176));
        generateOrderBtn.setForeground(Color.WHITE);
        generateOrderBtn.setFont(new Font("Arial", Font.BOLD, 14));
        generateOrderBtn.addActionListener(e -> generateOrder());
        
        JButton clearBtn = new JButton("Clear Form");
        clearBtn.setBackground(new Color(105, 105, 105));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Arial", Font.BOLD, 14));
        clearBtn.addActionListener(e -> clearForm());
        
        JButton backBtn = new JButton("Back");
        backBtn.setBackground(new Color(64, 64, 64));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFont(new Font("Arial", Font.BOLD, 14));
        backBtn.addActionListener(e -> {
            new AdminDashboard("Admin").setVisible(true);
            setVisible(false);
        });
        
        panel.add(addBtn);
        panel.add(updateBtn);
        panel.add(stockInBtn);
        panel.add(stockOutBtn);
        panel.add(generateOrderBtn);
        panel.add(clearBtn);
        panel.add(backBtn);
        
        return panel;
    }
    
    private void loadInventory() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM inventory ORDER BY item_name";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            // Clear existing table data
            while (inventoryTable.getRowCount() > 0) {
                ((javax.swing.table.DefaultTableModel) inventoryTable.getModel()).removeRow(0);
            }
            
            // Add new data
            javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) inventoryTable.getModel();
            while (rs.next()) {
                double quantity = rs.getDouble("quantity");
                double price = rs.getDouble("unit_price");
                double value = quantity * price;
                
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("item_name"),
                    rs.getString("category"),
                    quantity,
                    rs.getString("unit"),
                    "$" + price,
                    "$" + String.format("%.2f", value),
                    rs.getString("status")
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading inventory: " + e.getMessage());
        }
    }
    
    private void updateStatistics() {
        try (Connection conn = DBConnection.getConnection()) {
            // Total items
            String totalQuery = "SELECT COUNT(*) as total FROM inventory";
            PreparedStatement totalStmt = conn.prepareStatement(totalQuery);
            ResultSet totalRs = totalStmt.executeQuery();
            
            if (totalRs.next()) {
                totalItemsLabel.setText("Total Items: " + totalRs.getInt("total"));
            }
            
            // Low stock items
            String lowStockQuery = "SELECT COUNT(*) as low_stock FROM inventory WHERE status = 'Low Stock'";
            PreparedStatement lowStockStmt = conn.prepareStatement(lowStockQuery);
            ResultSet lowStockRs = lowStockStmt.executeQuery();
            
            if (lowStockRs.next()) {
                lowStockLabel.setText("Low Stock Items: " + lowStockRs.getInt("low_stock"));
            }
            
            // Total value
            String valueQuery = "SELECT SUM(quantity * unit_price) as total_value FROM inventory";
            PreparedStatement valueStmt = conn.prepareStatement(valueQuery);
            ResultSet valueRs = valueStmt.executeQuery();
            
            if (valueRs.next()) {
                double totalValue = valueRs.getDouble("total_value");
                totalValueLabel.setText("Total Value: $" + String.format("%.2f", totalValue));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void addItem() {
        if (itemNameField.getText().isEmpty() || quantityField.getText().isEmpty() || priceField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            String insertQuery = "INSERT INTO inventory (item_name, category, description, quantity, unit, unit_price, reorder_level, max_level, supplier, status, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, itemNameField.getText());
            stmt.setString(2, (String) categoryBox.getSelectedItem());
            stmt.setString(3, descriptionArea.getText());
            stmt.setDouble(4, Double.parseDouble(quantityField.getText()));
            stmt.setString(5, (String) unitBox.getSelectedItem());
            stmt.setDouble(6, Double.parseDouble(priceField.getText()));
            stmt.setInt(7, (Integer) reorderLevelSpinner.getValue());
            stmt.setInt(8, (Integer) maxLevelSpinner.getValue());
            stmt.setString(9, supplierField.getText());
            stmt.setString(10, (String) statusBox.getSelectedItem());
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            loadInventory();
            updateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateItem() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int itemId = (Integer) inventoryTable.getValueAt(selectedRow, 0);
        
        try (Connection conn = DBConnection.getConnection()) {
            String updateQuery = "UPDATE inventory SET item_name = ?, category = ?, description = ?, quantity = ?, unit = ?, unit_price = ?, reorder_level = ?, max_level = ?, supplier = ?, status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, itemNameField.getText());
            stmt.setString(2, (String) categoryBox.getSelectedItem());
            stmt.setString(3, descriptionArea.getText());
            stmt.setDouble(4, Double.parseDouble(quantityField.getText()));
            stmt.setString(5, (String) unitBox.getSelectedItem());
            stmt.setDouble(6, Double.parseDouble(priceField.getText()));
            stmt.setInt(7, (Integer) reorderLevelSpinner.getValue());
            stmt.setInt(8, (Integer) maxLevelSpinner.getValue());
            stmt.setString(9, supplierField.getText());
            stmt.setString(10, (String) statusBox.getSelectedItem());
            stmt.setInt(11, itemId);
            
            stmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Item updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            loadInventory();
            updateStatistics();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating item: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void stockIn() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item for stock in!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity to add:");
        if (quantityStr == null || quantityStr.isEmpty()) return;
        
        try {
            double quantity = Double.parseDouble(quantityStr);
            int itemId = (Integer) inventoryTable.getValueAt(selectedRow, 0);
            
            try (Connection conn = DBConnection.getConnection()) {
                String updateQuery = "UPDATE inventory SET quantity = quantity + ? WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(updateQuery);
                stmt.setDouble(1, quantity);
                stmt.setInt(2, itemId);
                
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, quantity + " units added to stock!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                loadInventory();
                updateStatistics();
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void stockOut() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item for stock out!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String quantityStr = JOptionPane.showInputDialog(this, "Enter quantity to remove:");
        if (quantityStr == null || quantityStr.isEmpty()) return;
        
        try {
            double quantity = Double.parseDouble(quantityStr);
            int itemId = (Integer) inventoryTable.getValueAt(selectedRow, 0);
            
            try (Connection conn = DBConnection.getConnection()) {
                String updateQuery = "UPDATE inventory SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";
                PreparedStatement stmt = conn.prepareStatement(updateQuery);
                stmt.setDouble(1, quantity);
                stmt.setInt(2, itemId);
                stmt.setDouble(3, quantity);
                
                int affected = stmt.executeUpdate();
                
                if (affected > 0) {
                    JOptionPane.showMessageDialog(this, quantity + " units removed from stock!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient stock available!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                loadInventory();
                updateStatistics();
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating stock: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateOrder() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM inventory WHERE quantity <= reorder_level ORDER BY category, item_name";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            StringBuilder order = new StringBuilder();
            order.append("=== PURCHASE ORDER ===\n");
            order.append("Date: ").append(new SimpleDateFormat("yyyy-MM-dd").format(new Date())).append("\n\n");
            order.append("Items to Reorder:\n");
            order.append("================\n\n");
            
            boolean hasItems = false;
            while (rs.next()) {
                hasItems = true;
                order.append("Item: ").append(rs.getString("item_name")).append("\n");
                order.append("Category: ").append(rs.getString("category")).append("\n");
                order.append("Current Stock: ").append(rs.getDouble("quantity")).append(" ").append(rs.getString("unit")).append("\n");
                order.append("Reorder Level: ").append(rs.getInt("reorder_level")).append("\n");
                order.append("Suggested Order: ").append(rs.getInt("max_level") - rs.getDouble("quantity")).append(" ").append(rs.getString("unit")).append("\n");
                order.append("Supplier: ").append(rs.getString("supplier")).append("\n");
                order.append("Unit Price: $").append(rs.getDouble("unit_price")).append("\n");
                order.append("----------------------------------------\n");
            }
            
            if (!hasItems) {
                order.append("No items need reordering at this time.\n");
            }
            
            JTextArea orderArea = new JTextArea(order.toString());
            orderArea.setEditable(false);
            orderArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPane = new JScrollPane(orderArea);
            scrollPane.setPreferredSize(new Dimension(600, 400));
            
            JOptionPane.showMessageDialog(this, scrollPane, "Purchase Order", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error generating order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        itemNameField.setText("");
        quantityField.setText("");
        priceField.setText("");
        supplierField.setText("");
        descriptionArea.setText("");
        categoryBox.setSelectedIndex(0);
        unitBox.setSelectedIndex(0);
        statusBox.setSelectedIndex(0);
        reorderLevelSpinner.setValue(10);
        maxLevelSpinner.setValue(100);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InventorySystem().setVisible(true);
        });
    }
} 