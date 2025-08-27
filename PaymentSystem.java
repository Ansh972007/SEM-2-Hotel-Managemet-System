package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentSystem extends JFrame {
    
    private JTextField customerNameField, roomNumberField, amountField, cardNumberField;
    private JComboBox<String> paymentMethodBox, roomTypeBox;
    private JTextArea invoiceArea;
    private JLabel totalAmountLabel, taxLabel, finalAmountLabel;
    private double baseAmount = 0.0;
    private double taxRate = 0.10; // 10% tax
    
    public PaymentSystem() {
        setTitle("Payment Management System");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        createUI();
        loadRooms();
    }
    
    private void createUI() {
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(25, 25, 112));
        headerPanel.setPreferredSize(new Dimension(800, 60));
        
        JLabel titleLabel = new JLabel("Payment Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Left panel - Payment form
        JPanel formPanel = createPaymentForm();
        mainPanel.add(formPanel);
        
        // Right panel - Invoice preview
        JPanel invoicePanel = createInvoicePanel();
        mainPanel.add(invoicePanel);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Bottom panel - Buttons
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createPaymentForm() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Payment Details"));
        
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
        
        // Room Type
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typePanel.add(new JLabel("Room Type:"));
        roomTypeBox = new JComboBox<>(new String[]{"Standard", "Deluxe", "Suite", "Presidential"});
        roomTypeBox.addActionListener(e -> calculateAmount());
        typePanel.add(roomTypeBox);
        panel.add(typePanel);
        
        // Number of Days
        JPanel daysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        daysPanel.add(new JLabel("Number of Days:"));
        JSpinner daysSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        daysSpinner.addChangeListener(e -> calculateAmount());
        daysPanel.add(daysSpinner);
        panel.add(daysPanel);
        
        // Payment Method
        JPanel methodPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        methodPanel.add(new JLabel("Payment Method:"));
        paymentMethodBox = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "Cash", "Bank Transfer", "Digital Wallet"});
        methodPanel.add(paymentMethodBox);
        panel.add(methodPanel);
        
        // Card Number (for card payments)
        JPanel cardPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardPanel.add(new JLabel("Card Number:"));
        cardNumberField = new JTextField(20);
        cardPanel.add(cardNumberField);
        panel.add(cardPanel);
        
        // Amount display
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        amountPanel.add(new JLabel("Base Amount:"));
        totalAmountLabel = new JLabel("$0.00");
        totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        amountPanel.add(totalAmountLabel);
        panel.add(amountPanel);
        
        // Tax display
        JPanel taxPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        taxPanel.add(new JLabel("Tax (10%):"));
        taxLabel = new JLabel("$0.00");
        taxLabel.setFont(new Font("Arial", Font.BOLD, 16));
        taxPanel.add(taxLabel);
        panel.add(taxPanel);
        
        // Final amount
        JPanel finalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        finalPanel.add(new JLabel("Final Amount:"));
        finalAmountLabel = new JLabel("$0.00");
        finalAmountLabel.setFont(new Font("Arial", Font.BOLD, 18));
        finalAmountLabel.setForeground(Color.RED);
        finalPanel.add(finalAmountLabel);
        panel.add(finalPanel);
        
        return panel;
    }
    
    private JPanel createInvoicePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Invoice Preview"));
        
        invoiceArea = new JTextArea();
        invoiceArea.setEditable(false);
        invoiceArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(invoiceArea);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JButton processBtn = new JButton("Process Payment");
        processBtn.setBackground(new Color(34, 139, 34));
        processBtn.setForeground(Color.WHITE);
        processBtn.setFont(new Font("Arial", Font.BOLD, 14));
        processBtn.addActionListener(e -> processPayment());
        
        JButton generateBtn = new JButton("Generate Invoice");
        generateBtn.setBackground(new Color(70, 130, 180));
        generateBtn.setForeground(Color.WHITE);
        generateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        generateBtn.addActionListener(e -> generateInvoice());
        
        JButton clearBtn = new JButton("Clear");
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
        
        panel.add(processBtn);
        panel.add(generateBtn);
        panel.add(clearBtn);
        panel.add(backBtn);
        
        return panel;
    }
    
    private void loadRooms() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT room_number, room_type, price FROM room WHERE availability = 'Available' ORDER BY room_number";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                // You can populate a dropdown with available rooms if needed
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void calculateAmount() {
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
        
        // For demo purposes, assume 1 day
        baseAmount = pricePerNight;
        double tax = baseAmount * taxRate;
        double finalAmount = baseAmount + tax;
        
        totalAmountLabel.setText(String.format("$%.2f", baseAmount));
        taxLabel.setText(String.format("$%.2f", tax));
        finalAmountLabel.setText(String.format("$%.2f", finalAmount));
        
        updateInvoicePreview();
    }
    
    private void updateInvoicePreview() {
        StringBuilder invoice = new StringBuilder();
        invoice.append("=== HOTEL INVOICE ===\n\n");
        invoice.append("Date: ").append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())).append("\n");
        invoice.append("Invoice #: INV-").append(System.currentTimeMillis()).append("\n\n");
        
        if (!customerNameField.getText().isEmpty()) {
            invoice.append("Customer: ").append(customerNameField.getText()).append("\n");
        }
        if (!roomNumberField.getText().isEmpty()) {
            invoice.append("Room: ").append(roomNumberField.getText()).append("\n");
        }
        invoice.append("Room Type: ").append(roomTypeBox.getSelectedItem()).append("\n");
        invoice.append("Payment Method: ").append(paymentMethodBox.getSelectedItem()).append("\n\n");
        
        invoice.append("=== CHARGES ===\n");
        invoice.append(String.format("Base Amount: $%.2f\n", baseAmount));
        invoice.append(String.format("Tax (10%%): $%.2f\n", baseAmount * taxRate));
        invoice.append(String.format("Total: $%.2f\n", baseAmount * (1 + taxRate)));
        
        invoiceArea.setText(invoice.toString());
    }
    
    private void processPayment() {
        if (customerNameField.getText().isEmpty() || roomNumberField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection conn = DBConnection.getConnection()) {
            // Insert payment record
            String insertQuery = "INSERT INTO payments (customer_name, room_number, room_type, amount, tax, total_amount, payment_method, payment_date, status) VALUES (?, ?, ?, ?, ?, ?, ?, NOW(), 'Completed')";
            PreparedStatement stmt = conn.prepareStatement(insertQuery);
            stmt.setString(1, customerNameField.getText());
            stmt.setString(2, roomNumberField.getText());
            stmt.setString(3, (String) roomTypeBox.getSelectedItem());
            stmt.setDouble(4, baseAmount);
            stmt.setDouble(5, baseAmount * taxRate);
            stmt.setDouble(6, baseAmount * (1 + taxRate));
            stmt.setString(7, (String) paymentMethodBox.getSelectedItem());
            
            stmt.executeUpdate();
            
            // Update room status
            String updateRoomQuery = "UPDATE room SET availability = 'Occupied' WHERE room_number = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateRoomQuery);
            updateStmt.setString(1, roomNumberField.getText());
            updateStmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, 
                "Payment processed successfully!\nAmount: $" + String.format("%.2f", baseAmount * (1 + taxRate)), 
                "Payment Success", JOptionPane.INFORMATION_MESSAGE);
            
            clearForm();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error processing payment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void generateInvoice() {
        if (customerNameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter customer name first!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Create a printable invoice
        JTextArea printArea = new JTextArea(invoiceArea.getText());
        printArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        try {
            printArea.print();
            JOptionPane.showMessageDialog(this, "Invoice sent to printer!", "Print Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error printing invoice: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        customerNameField.setText("");
        roomNumberField.setText("");
        cardNumberField.setText("");
        roomTypeBox.setSelectedIndex(0);
        paymentMethodBox.setSelectedIndex(0);
        invoiceArea.setText("");
        calculateAmount();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new PaymentSystem().setVisible(true);
        });
    }
} 