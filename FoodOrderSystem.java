package Hotel1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;

public class FoodOrderSystem extends JFrame {
    private JTextField customerNameField, roomNumberField, quantityField;
    private JComboBox<String> foodMenu;
    private JButton addButton, placeOrderButton, clearButton, backButton, refreshButton, updateStatusButton, printBillButton, exportButton, searchButton;
    private JButton editOrderButton, cancelOrderButton, repeatOrderButton, deliveredButton, menuManageButton;
    private JButton viewTimelineButton;
    private JTable orderTable, allOrdersTable;
    private DefaultTableModel orderTableModel, allOrdersTableModel;
    private JLabel totalLabel, imageLabel, taxLabel, serviceLabel, grandTotalLabel;
    private JComboBox<String> statusBox, filterStatusBox;
    private JTextArea notesArea;
    private JTextField searchCustomerField, searchRoomField, searchDateField;
    private Map<String, Integer> foodPrices = new LinkedHashMap<>();
    private java.util.List<OrderItem> currentOrder = new ArrayList<>();
    private double taxRate = 0.05; // 5% tax
    private double serviceChargeRate = 0.10; // 10% service charge
    private String currentStaffUser = null;

    public FoodOrderSystem() {
        setTitle("Food Ordering System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Food items and prices
        foodPrices.put("Burger", 120);
        foodPrices.put("Pizza", 250);
        foodPrices.put("Pasta", 200);
        foodPrices.put("Sandwich", 90);
        foodPrices.put("Coffee", 60);
        foodPrices.put("Tea", 40);
        foodPrices.put("Momos", 100);
        foodPrices.put("Fries", 80);
        foodPrices.put("Ice Cream", 70);
        foodPrices.put("Soup", 110);
        foodPrices.put("Biryani", 180);
        foodPrices.put("Noodles", 130);
        foodPrices.put("Salad", 85);
        foodPrices.put("Juice", 75);
        foodPrices.put("Roll", 95);

        // Top panel for order input
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("New Food Order"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Customer Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        customerNameField = new JTextField(12);
        customerNameField.setToolTipText("Enter your full name");
        inputPanel.add(customerNameField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        roomNumberField = new JTextField(8);
        roomNumberField.setToolTipText("Enter your room number");
        inputPanel.add(roomNumberField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Select Food Item:"), gbc);
        gbc.gridx = 1;
        foodMenu = new JComboBox<>(foodPrices.keySet().toArray(new String[0]));
        inputPanel.add(foodMenu, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        quantityField = new JTextField("1", 4);
        quantityField.setToolTipText("Enter quantity (numbers only)");
        inputPanel.add(quantityField, gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        // Add input filter for numbers only
        quantityField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) && c != '\b') {
                    e.consume();
                }
            }
        });

        gbc.gridx = 4; gbc.gridy = 0; gbc.gridheight = 2;
        imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(100, 100));
        inputPanel.add(imageLabel, gbc);
        foodMenu.addActionListener(e -> loadImage((String) foodMenu.getSelectedItem()));
        loadImage((String) foodMenu.getSelectedItem());

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridheight = 1; gbc.gridwidth = 1;
        addButton = new JButton("Add to Order");
        inputPanel.add(addButton, gbc);
        addButton.addActionListener(e -> addToOrder());

        gbc.gridx = 1;
        clearButton = new JButton("Clear Order");
        inputPanel.add(clearButton, gbc);
        clearButton.addActionListener(e -> clearOrder());

        gbc.gridx = 2;
        placeOrderButton = new JButton("Place Order");
        inputPanel.add(placeOrderButton, gbc);
        placeOrderButton.addActionListener(e -> placeOrder());

        gbc.gridx = 3;
        backButton = new JButton("Back");
        inputPanel.add(backButton, gbc);
        backButton.addActionListener(e -> {
            new Reception();
            setVisible(false);
        });

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        inputPanel.add(new JLabel("Special Instructions / Notes:"), gbc);
        gbc.gridy = 4;
        notesArea = new JTextArea(2, 30);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);
        notesArea.setToolTipText("Add any allergy info or special instructions");
        inputPanel.add(new JScrollPane(notesArea), gbc);

        // Table for current order
        String[] orderColumns = {"Item", "Quantity", "Price", "Total"};
        orderTableModel = new DefaultTableModel(orderColumns, 0) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        orderTable = new JTable(orderTableModel);
        JScrollPane orderScroll = new JScrollPane(orderTable);
        orderScroll.setBorder(BorderFactory.createTitledBorder("Current Order"));

        // Total, tax, service, grand total
        totalLabel = new JLabel("Total: Rs. 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        taxLabel = new JLabel("Tax (5%): Rs. 0");
        taxLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        serviceLabel = new JLabel("Service (10%): Rs. 0");
        serviceLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        grandTotalLabel = new JLabel("Grand Total: Rs. 0");
        grandTotalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        printBillButton = new JButton("Print Bill");
        printBillButton.addActionListener(e -> printBill());
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(totalLabel);
        totalPanel.add(taxLabel);
        totalPanel.add(serviceLabel);
        totalPanel.add(grandTotalLabel);
        totalPanel.add(printBillButton);

        // Panel for all orders and status management
        JPanel allOrdersPanel = new JPanel(new BorderLayout(5, 5));
        allOrdersPanel.setBorder(BorderFactory.createTitledBorder("All Food Orders (Status Management)"));
        String[] allOrderCols = {"Order ID", "Customer", "Room", "Item", "Qty", "Price", "Status", "Time"};
        allOrdersTableModel = new DefaultTableModel(allOrderCols, 0) {
            public boolean isCellEditable(int row, int col) { return col == 6; }
        };
        allOrdersTable = new JTable(allOrdersTableModel);
        JScrollPane allOrdersScroll = new JScrollPane(allOrdersTable);
        
        // Search/filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Customer:"));
        searchCustomerField = new JTextField(8);
        searchPanel.add(searchCustomerField);
        searchPanel.add(new JLabel("Room:"));
        searchRoomField = new JTextField(6);
        searchPanel.add(searchRoomField);
        searchPanel.add(new JLabel("Date (yyyy-mm-dd):"));
        searchDateField = new JTextField(8);
        searchPanel.add(searchDateField);
        searchPanel.add(new JLabel("Status:"));
        filterStatusBox = new JComboBox<>(new String[]{"All", "Ordered", "Prepared", "Delivered", "Cancelled"});
        searchPanel.add(filterStatusBox);
        searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        exportButton = new JButton("Export to CSV");
        searchPanel.add(exportButton);
        menuManageButton = new JButton("Menu Management");
        searchPanel.add(menuManageButton);
        menuManageButton.addActionListener(e -> {
            if (requireStaffLogin()) showMenuManagementDialog();
        });
        allOrdersPanel.add(searchPanel, BorderLayout.NORTH);
        allOrdersPanel.add(allOrdersScroll, BorderLayout.CENTER);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBox = new JComboBox<>(new String[]{"Ordered", "Prepared", "Delivered", "Cancelled"});
        updateStatusButton = new JButton("Update Status");
        refreshButton = new JButton("Refresh");
        editOrderButton = new JButton("Edit Order");
        cancelOrderButton = new JButton("Cancel Order");
        repeatOrderButton = new JButton("Repeat Order");
        deliveredButton = new JButton("Delivered to Room");
        viewTimelineButton = new JButton("View Timeline");
        statusPanel.add(new JLabel("Set Status:"));
        statusPanel.add(statusBox);
        statusPanel.add(updateStatusButton);
        statusPanel.add(refreshButton);
        statusPanel.add(editOrderButton);
        statusPanel.add(cancelOrderButton);
        statusPanel.add(repeatOrderButton);
        statusPanel.add(deliveredButton);
        statusPanel.add(viewTimelineButton);
        allOrdersPanel.add(statusPanel, BorderLayout.SOUTH);

        updateStatusButton.addActionListener(e -> updateOrderStatus());
        refreshButton.addActionListener(e -> loadAllOrders());
        searchButton.addActionListener(e -> searchOrders());
        exportButton.addActionListener(e -> exportOrdersToCSV());
        editOrderButton.addActionListener(e -> {
            if (requireStaffLogin()) editSelectedOrder();
        });
        cancelOrderButton.addActionListener(e -> {
            if (requireStaffLogin()) cancelSelectedOrder();
        });
        repeatOrderButton.addActionListener(e -> repeatSelectedOrder());
        deliveredButton.addActionListener(e -> markDeliveredToRoom());
        viewTimelineButton.addActionListener(e -> showOrderTimeline());

        // Layout
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(orderScroll, BorderLayout.CENTER);
        leftPanel.add(totalPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, allOrdersPanel);
        splitPane.setDividerLocation(450);
        add(splitPane, BorderLayout.CENTER);

        loadAllOrders();
        setVisible(true);
    }

    private void loadImage(String foodName) {
        String path = "Hotel1/" + foodName.toLowerCase().replace(" ", "") + ".jpg";
        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            imageLabel.setIcon(null);
        }
    }

    private void addToOrder() {
        String item = (String) foodMenu.getSelectedItem();
        int price = foodPrices.get(item);
        int qty;
        try {
            qty = Integer.parseInt(quantityField.getText().trim());
            if (qty <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid quantity!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Add to current order list
        currentOrder.add(new OrderItem(item, qty, price));
        updateOrderTable();
    }

    private void updateOrderTable() {
        orderTableModel.setRowCount(0);
        int total = 0;
        for (OrderItem oi : currentOrder) {
            int rowTotal = oi.price * oi.quantity;
            orderTableModel.addRow(new Object[]{oi.item, oi.quantity, oi.price, rowTotal});
            total += rowTotal;
        }
        totalLabel.setText("Total: Rs. " + total);
        double tax = total * taxRate;
        double service = total * serviceChargeRate;
        double grand = total + tax + service;
        taxLabel.setText(String.format("Tax (5%%): Rs. %.2f", tax));
        serviceLabel.setText(String.format("Service (10%%): Rs. %.2f", service));
        grandTotalLabel.setText(String.format("Grand Total: Rs. %.2f", grand));
    }

    private void clearOrder() {
        currentOrder.clear();
        updateOrderTable();
    }

    private void placeOrder() {
        String customer = customerNameField.getText().trim();
        String room = roomNumberField.getText().trim();
        String notes = notesArea.getText().trim();
        boolean valid = true;
        if (customer.isEmpty()) {
            customerNameField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valid = false;
        } else {
            customerNameField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }
        if (room.isEmpty()) {
            roomNumberField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            valid = false;
        } else {
            roomNumberField.setBorder(UIManager.getLookAndFeel().getDefaults().getBorder("TextField.border"));
        }
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Add at least one item to your order!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!valid) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            for (OrderItem oi : currentOrder) {
                String query = "INSERT INTO food_orders (customer_name, room_number, item_name, quantity, price, status, notes) VALUES (?, ?, ?, ?, ?, 'Ordered', ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, customer);
                stmt.setString(2, room);
                stmt.setString(3, oi.item);
                stmt.setInt(4, oi.quantity);
                stmt.setInt(5, oi.price);
                stmt.setString(6, notes);
                stmt.executeUpdate();
                stmt.close();
            }
            JOptionPane.showMessageDialog(this, "Order placed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearOrder();
            loadAllOrders();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAllOrders() {
        allOrdersTableModel.setRowCount(0);
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT id, customer_name, room_number, item_name, quantity, price, status, order_time FROM food_orders ORDER BY order_time DESC, id DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            boolean newOrder = false;
            while (rs.next()) {
                allOrdersTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getInt("price"),
                        rs.getString("status"),
                        rs.getTimestamp("order_time")
                });
                if ("Ordered".equals(rs.getString("status")) && rs.getTimestamp("order_time").after(new java.sql.Timestamp(System.currentTimeMillis() - 60000))) {
                    newOrder = true;
                }
            }
            rs.close();
            stmt.close();
            if (newOrder) showOrderNotification();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchOrders() {
        allOrdersTableModel.setRowCount(0);
        String customer = searchCustomerField.getText().trim();
        String room = searchRoomField.getText().trim();
        String date = searchDateField.getText().trim();
        String status = (String) filterStatusBox.getSelectedItem();
        StringBuilder query = new StringBuilder("SELECT id, customer_name, room_number, item_name, quantity, price, status, order_time FROM food_orders WHERE 1=1");
        if (!customer.isEmpty()) query.append(" AND customer_name LIKE '%" + customer + "%'");
        if (!room.isEmpty()) query.append(" AND room_number LIKE '%" + room + "%'");
        if (!date.isEmpty()) query.append(" AND DATE(order_time) = '" + date + "'");
        if (!status.equals("All")) query.append(" AND status = '" + status + "'");
        query.append(" ORDER BY order_time DESC, id DESC");
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query.toString());
            while (rs.next()) {
                allOrdersTableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("room_number"),
                        rs.getString("item_name"),
                        rs.getInt("quantity"),
                        rs.getInt("price"),
                        rs.getString("status"),
                        rs.getTimestamp("order_time")
                });
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportOrdersToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Orders as CSV");
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter csvWriter = new FileWriter(fileChooser.getSelectedFile() + ".csv")) {
                // Write header
                for (int i = 0; i < allOrdersTableModel.getColumnCount(); i++) {
                    csvWriter.append(allOrdersTableModel.getColumnName(i));
                    if (i < allOrdersTableModel.getColumnCount() - 1) csvWriter.append(",");
                }
                csvWriter.append("\n");
                // Write rows
                for (int row = 0; row < allOrdersTableModel.getRowCount(); row++) {
                    for (int col = 0; col < allOrdersTableModel.getColumnCount(); col++) {
                        csvWriter.append(String.valueOf(allOrdersTableModel.getValueAt(row, col)));
                        if (col < allOrdersTableModel.getColumnCount() - 1) csvWriter.append(",");
                    }
                    csvWriter.append("\n");
                }
                JOptionPane.showMessageDialog(this, "Orders exported successfully!", "Export Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error exporting orders: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateOrderStatus() {
        int row = allOrdersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to update!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int orderId = (int) allOrdersTableModel.getValueAt(row, 0);
        String newStatus = (String) statusBox.getSelectedItem();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "UPDATE food_orders SET status = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
            stmt.close();
            // Log status change
            String logQuery = "INSERT INTO food_order_status_history (order_id, status, changed_by) VALUES (?, ?, ?)";
            PreparedStatement logStmt = conn.prepareStatement(logQuery);
            logStmt.setInt(1, orderId);
            logStmt.setString(2, newStatus);
            logStmt.setString(3, currentStaffUser != null ? currentStaffUser : "System");
            logStmt.executeUpdate();
            logStmt.close();
            loadAllOrders();
            JOptionPane.showMessageDialog(this, "Order status updated!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printBill() {
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in order to print!", "Print Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        StringBuilder bill = new StringBuilder();
        bill.append("HOTEL FOOD ORDER RECEIPT\n");
        bill.append("Customer: ").append(customerNameField.getText()).append("\n");
        bill.append("Room: ").append(roomNumberField.getText()).append("\n");
        bill.append("Date: ").append(new java.util.Date()).append("\n");
        bill.append("----------------------------------------\n");
        bill.append(String.format("%-15s %-8s %-8s %-8s\n", "Item", "Qty", "Price", "Total"));
        int total = 0;
        for (OrderItem oi : currentOrder) {
            int rowTotal = oi.price * oi.quantity;
            bill.append(String.format("%-15s %-8d %-8d %-8d\n", oi.item, oi.quantity, oi.price, rowTotal));
            total += rowTotal;
        }
        double tax = total * taxRate;
        double service = total * serviceChargeRate;
        double grand = total + tax + service;
        bill.append("----------------------------------------\n");
        bill.append(String.format("Total: Rs. %d\n", total));
        bill.append(String.format("Tax (5%%): Rs. %.2f\n", tax));
        bill.append(String.format("Service (10%%): Rs. %.2f\n", service));
        bill.append(String.format("Grand Total: Rs. %.2f\n", grand));
        if (!notesArea.getText().trim().isEmpty()) {
            bill.append("Notes: ").append(notesArea.getText().trim()).append("\n");
        }
        JTextArea billArea = new JTextArea(bill.toString());
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        try {
            billArea.print();
            JOptionPane.showMessageDialog(this, "Bill sent to printer!", "Print Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error printing bill: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedOrder() {
        int row = allOrdersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to edit!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int orderId = (int) allOrdersTableModel.getValueAt(row, 0);
        String status = (String) allOrdersTableModel.getValueAt(row, 6);
        if ("Delivered".equals(status) || "Cancelled".equals(status)) {
            JOptionPane.showMessageDialog(this, "Cannot edit delivered or cancelled orders!", "Edit Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String newQty = JOptionPane.showInputDialog(this, "Enter new quantity:", allOrdersTableModel.getValueAt(row, 4));
        if (newQty != null && !newQty.trim().isEmpty()) {
            try {
                int qty = Integer.parseInt(newQty.trim());
                if (qty <= 0) throw new NumberFormatException();
                try (Connection conn = DBConnection.getConnection()) {
                    String query = "UPDATE food_orders SET quantity = ? WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, qty);
                    stmt.setInt(2, orderId);
                    stmt.executeUpdate();
                    stmt.close();
                    loadAllOrders();
                    JOptionPane.showMessageDialog(this, "Order updated!", "Edit Success", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity!", "Edit Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cancelSelectedOrder() {
        int row = allOrdersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to cancel!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int orderId = (int) allOrdersTableModel.getValueAt(row, 0);
        String status = (String) allOrdersTableModel.getValueAt(row, 6);
        if ("Delivered".equals(status) || "Cancelled".equals(status)) {
            JOptionPane.showMessageDialog(this, "Cannot cancel delivered or already cancelled orders!", "Cancel Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this order?", "Confirm Cancel", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DBConnection.getConnection()) {
                String query = "UPDATE food_orders SET status = 'Cancelled' WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, orderId);
                stmt.executeUpdate();
                stmt.close();
                loadAllOrders();
                JOptionPane.showMessageDialog(this, "Order cancelled!", "Cancel Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void repeatSelectedOrder() {
        int row = allOrdersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to repeat!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String item = (String) allOrdersTableModel.getValueAt(row, 3);
        int qty = (int) allOrdersTableModel.getValueAt(row, 4);
        int price = (int) allOrdersTableModel.getValueAt(row, 5);
        currentOrder.add(new OrderItem(item, qty, price));
        updateOrderTable();
        JOptionPane.showMessageDialog(this, "Order item added to current order!", "Repeat Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void markDeliveredToRoom() {
        int row = allOrdersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to mark as delivered!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int orderId = (int) allOrdersTableModel.getValueAt(row, 0);
        String status = (String) allOrdersTableModel.getValueAt(row, 6);
        if ("Delivered".equals(status)) {
            JOptionPane.showMessageDialog(this, "Order already marked as delivered!", "Delivery Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection conn = DBConnection.getConnection()) {
            String query = "UPDATE food_orders SET status = 'Delivered', order_time = NOW() WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
            stmt.close();
            // Log status change
            String logQuery = "INSERT INTO food_order_status_history (order_id, status, changed_by) VALUES (?, ?, ?)";
            PreparedStatement logStmt = conn.prepareStatement(logQuery);
            logStmt.setInt(1, orderId);
            logStmt.setString(2, "Delivered");
            logStmt.setString(3, currentStaffUser != null ? currentStaffUser : "System");
            logStmt.executeUpdate();
            logStmt.close();
            loadAllOrders();
            JOptionPane.showMessageDialog(this, "Order marked as delivered!", "Delivery Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showMenuManagementDialog() {
        JDialog dialog = new JDialog(this, "Menu Management", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        DefaultTableModel menuModel = new DefaultTableModel(new String[]{"Item", "Price"}, 0);
        JTable menuTable = new JTable(menuModel);
        for (Map.Entry<String, Integer> entry : foodPrices.entrySet()) {
            menuModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
        JButton addBtn = new JButton("Add Item");
        JButton editBtn = new JButton("Edit Price");
        JButton removeBtn = new JButton("Remove Item");
        JPanel btnPanel = new JPanel();
        btnPanel.add(addBtn);
        btnPanel.add(editBtn);
        btnPanel.add(removeBtn);
        dialog.add(new JScrollPane(menuTable), BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        addBtn.addActionListener(e -> {
            String item = JOptionPane.showInputDialog(dialog, "Enter new food item name:");
            if (item != null && !item.trim().isEmpty()) {
                String priceStr = JOptionPane.showInputDialog(dialog, "Enter price for " + item + ":");
                try {
                    int price = Integer.parseInt(priceStr.trim());
                    foodPrices.put(item, price);
                    menuModel.addRow(new Object[]{item, price});
                    foodMenu.addItem(item);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid price!", "Menu Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        editBtn.addActionListener(e -> {
            int row = menuTable.getSelectedRow();
            if (row == -1) return;
            String item = (String) menuModel.getValueAt(row, 0);
            String priceStr = JOptionPane.showInputDialog(dialog, "Enter new price for " + item + ":", menuModel.getValueAt(row, 1));
            try {
                int price = Integer.parseInt(priceStr.trim());
                foodPrices.put(item, price);
                menuModel.setValueAt(price, row, 1);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid price!", "Menu Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        removeBtn.addActionListener(e -> {
            int row = menuTable.getSelectedRow();
            if (row == -1) return;
            String item = (String) menuModel.getValueAt(row, 0);
            foodPrices.remove(item);
            menuModel.removeRow(row);
            foodMenu.removeItem(item);
        });
        dialog.setVisible(true);
    }

    private void showOrderNotification() {
        JOptionPane.showMessageDialog(this, "New food order received!", "Order Notification", JOptionPane.INFORMATION_MESSAGE);
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("/Hotel1/notification.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            // Ignore sound error
        }
    }

    private void showOrderTimeline() {
        int row = allOrdersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an order to view timeline!", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int orderId = (int) allOrdersTableModel.getValueAt(row, 0);
        StringBuilder timeline = new StringBuilder();
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT status, changed_by, changed_at FROM food_order_status_history WHERE order_id = ? ORDER BY changed_at ASC";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            timeline.append("Status Timeline for Order #").append(orderId).append(":\n\n");
            while (rs.next()) {
                timeline.append(rs.getTimestamp("changed_at"))
                        .append(" - ")
                        .append(rs.getString("status"))
                        .append(" (by ")
                        .append(rs.getString("changed_by"))
                        .append(")\n");
            }
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            timeline.append("Error loading timeline: ").append(ex.getMessage());
        }
        JTextArea area = new JTextArea(timeline.toString());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "Order Status Timeline", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean requireStaffLogin() {
        if (currentStaffUser != null) return true;
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField userField = new JTextField();
        JPasswordField passField = new JPasswordField();
        panel.add(new JLabel("Username:"));
        panel.add(userField);
        panel.add(new JLabel("Password:"));
        panel.add(passField);
        int result = JOptionPane.showConfirmDialog(this, panel, "Staff/Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            if (checkStaffCredentials(username, password)) {
                currentStaffUser = username;
                JOptionPane.showMessageDialog(this, "Login successful!", "Login", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }

    private boolean checkStaffCredentials(String username, String password) {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM staff_users WHERE username = ? AND password_hash = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password); // In production, use hashed passwords!
            ResultSet rs = stmt.executeQuery();
            boolean ok = rs.next();
            rs.close();
            stmt.close();
            return ok;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "DB Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FoodOrderSystem::new);
    }

    // Helper class for order items
    static class OrderItem {
        String item;
        int quantity;
        int price;
        OrderItem(String item, int quantity, int price) {
            this.item = item;
            this.quantity = quantity;
            this.price = price;
        }
    }
}
