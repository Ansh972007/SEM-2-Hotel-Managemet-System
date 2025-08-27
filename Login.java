package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame implements ActionListener {

    JLabel l1, l2, l3, lRole, lAction, lConfirmPass;
    JTextField t1;
    JPasswordField t2, t3;
    JButton b1, b2, b3;
    JComboBox<String> roleBox;
    JComboBox<String> actionBox;
    JPanel signUpPanel, loginPanel;
    CardLayout cardLayout;
    JPanel mainPanel;

    public Login() {
        super("Hotel Management System - Login");
        setLayout(new BorderLayout());

        // Initialize card layout for switching between login and signup
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create panels
        createLoginPanel();
        createSignUpPanel();
        
        mainPanel.add(loginPanel, "LOGIN");
        mainPanel.add(signUpPanel, "SIGNUP");
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Add action listener to action box
        actionBox.addActionListener(e -> {
            if (actionBox.getSelectedItem().equals("Sign Up")) {
                cardLayout.show(mainPanel, "SIGNUP");
            } else {
                cardLayout.show(mainPanel, "LOGIN");
            }
        });

        getContentPane().setBackground(new Color(240, 248, 255));
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void createLoginPanel() {
        loginPanel = new JPanel();
        loginPanel.setLayout(null);
        loginPanel.setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("Login to Hotel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(200, 20, 300, 30);
        loginPanel.add(titleLabel);

        // Role selection
        lRole = new JLabel("Select Role:");
        lRole.setBounds(50, 70, 100, 25);
        lRole.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(lRole);
        
        roleBox = new JComboBox<>(new String[]{"Customer", "Staff", "Admin"});
        roleBox.setBounds(160, 70, 200, 25);
        loginPanel.add(roleBox);

        // Action selection
        lAction = new JLabel("Action:");
        lAction.setBounds(50, 110, 100, 25);
        lAction.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(lAction);
        
        actionBox = new JComboBox<>(new String[]{"Login", "Sign Up"});
        actionBox.setBounds(160, 110, 200, 25);
        loginPanel.add(actionBox);

        // Username
        l1 = new JLabel("Username:");
        l1.setBounds(50, 150, 100, 25);
        l1.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(l1);

        t1 = new JTextField();
        t1.setBounds(160, 150, 200, 25);
        loginPanel.add(t1);

        // Password
        l2 = new JLabel("Password:");
        l2.setBounds(50, 190, 100, 25);
        l2.setFont(new Font("Arial", Font.BOLD, 12));
        loginPanel.add(l2);

        t2 = new JPasswordField();
        t2.setBounds(160, 190, 200, 25);
        loginPanel.add(t2);

        // Buttons
        b1 = new JButton("Login");
        b1.setBounds(160, 230, 90, 30);
        b1.setFont(new Font("Arial", Font.BOLD, 12));
        b1.setBackground(new Color(70, 130, 180));
        b1.setForeground(Color.WHITE);
        b1.setFocusPainted(false);
        b1.addActionListener(this);
        loginPanel.add(b1);

        b2 = new JButton("Clear");
        b2.setBounds(270, 230, 90, 30);
        b2.setFont(new Font("Arial", Font.BOLD, 12));
        b2.setBackground(new Color(220, 20, 60));
        b2.setForeground(Color.WHITE);
        b2.setFocusPainted(false);
        b2.addActionListener(this);
        loginPanel.add(b2);

        // Add image
        try {
            ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Hotel1/Avtar.jpg"));
            Image i2 = i1.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(i2));
            imageLabel.setBounds(400, 70, 150, 150);
            loginPanel.add(imageLabel);
        } catch (Exception e) {
            // If image not found, add a placeholder
            JLabel placeholder = new JLabel("Hotel Logo");
            placeholder.setBounds(400, 70, 150, 150);
            placeholder.setHorizontalAlignment(SwingConstants.CENTER);
            placeholder.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            loginPanel.add(placeholder);
        }
    }

    private void createSignUpPanel() {
        signUpPanel = new JPanel();
        signUpPanel.setLayout(null);
        signUpPanel.setBackground(new Color(240, 248, 255));

        // Title
        JLabel titleLabel = new JLabel("Sign Up for Hotel Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setBounds(180, 20, 350, 30);
        signUpPanel.add(titleLabel);

        // Role selection
        JLabel roleLabel = new JLabel("Select Role:");
        roleLabel.setBounds(50, 70, 100, 25);
        roleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        signUpPanel.add(roleLabel);
        
        JComboBox<String> signUpRoleBox = new JComboBox<>(new String[]{"Customer", "Staff", "Admin"});
        signUpRoleBox.setBounds(160, 70, 200, 25);
        signUpPanel.add(signUpRoleBox);

        // Username
        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 110, 100, 25);
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        signUpPanel.add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(160, 110, 200, 25);
        signUpPanel.add(userField);

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 150, 100, 25);
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        signUpPanel.add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(160, 150, 200, 25);
        signUpPanel.add(passField);

        // Confirm Password
        lConfirmPass = new JLabel("Confirm Password:");
        lConfirmPass.setBounds(50, 190, 120, 25);
        lConfirmPass.setFont(new Font("Arial", Font.BOLD, 12));
        signUpPanel.add(lConfirmPass);

        t3 = new JPasswordField();
        t3.setBounds(160, 190, 200, 25);
        signUpPanel.add(t3);

        // Buttons
        JButton signUpBtn = new JButton("Sign Up");
        signUpBtn.setBounds(160, 230, 90, 30);
        signUpBtn.setFont(new Font("Arial", Font.BOLD, 12));
        signUpBtn.setBackground(new Color(34, 139, 34));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setFocusPainted(false);
        signUpBtn.addActionListener(e -> handleSignUp(signUpRoleBox, userField, passField));
        signUpPanel.add(signUpBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(270, 230, 90, 30);
        clearBtn.setFont(new Font("Arial", Font.BOLD, 12));
        clearBtn.setBackground(new Color(220, 20, 60));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFocusPainted(false);
        clearBtn.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
            t3.setText("");
        });
        signUpPanel.add(clearBtn);

        // Back to login button
        JButton backBtn = new JButton("Back to Login");
        backBtn.setBounds(160, 270, 200, 30);
        backBtn.setFont(new Font("Arial", Font.BOLD, 12));
        backBtn.setBackground(new Color(105, 105, 105));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            actionBox.setSelectedItem("Login");
            cardLayout.show(mainPanel, "LOGIN");
        });
        signUpPanel.add(backBtn);
    }

    private void handleSignUp(JComboBox<String> roleBox, JTextField userField, JPasswordField passField) {
        String role = (String) roleBox.getSelectedItem();
        String username = userField.getText().trim();
        String password = String.valueOf(passField.getPassword());
        String confirmPassword = String.valueOf(t3.getPassword());

        // Input validation
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (username.length() < 3) {
            JOptionPane.showMessageDialog(this, "Username must be at least 3 characters long!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (password.length() < 6) {
            JOptionPane.showMessageDialog(this, "Password must be at least 6 characters long!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            t3.setText("");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            // Check if user already exists
            String checkQuery = "SELECT * FROM login WHERE username = ? AND role = ?";
            PreparedStatement checkPst = conn.prepareStatement(checkQuery);
            checkPst.setString(1, username);
            checkPst.setString(2, role);
            ResultSet checkRs = checkPst.executeQuery();
            
            if (checkRs.next()) {
                JOptionPane.showMessageDialog(this, 
                    "User ID already signed up for this role. Please login instead.", 
                    "User Exists", JOptionPane.WARNING_MESSAGE);
                userField.setText("");
                passField.setText("");
                t3.setText("");
            } else {
                // Insert new user
                String insertQuery = "INSERT INTO login (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement insertPst = conn.prepareStatement(insertQuery);
                insertPst.setString(1, username);
                insertPst.setString(2, password);
                insertPst.setString(3, role);
                insertPst.executeUpdate();
                
                JOptionPane.showMessageDialog(this, 
                    "Sign up successful! You can now login.", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear fields and switch to login
                userField.setText("");
                passField.setText("");
                t3.setText("");
                actionBox.setSelectedItem("Login");
                cardLayout.show(mainPanel, "LOGIN");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == b1) {
            handleLogin();
        } else if (ae.getSource() == b2) {
            clearFields();
        }
    }

    private void handleLogin() {
        String role = (String) roleBox.getSelectedItem();
        String username = t1.getText().trim();
        String password = String.valueOf(t2.getPassword());

        // Input validation
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Username and password are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM login WHERE username = ? AND password = ? AND role = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);
            pst.setString(3, role);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                JOptionPane.showMessageDialog(this, 
                    "Login successful! Welcome " + username + " (" + role + ")", 
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Redirect to appropriate dashboard based on role
                switch (role) {
                    case "Admin":
                        new AdminDashboard(username).setVisible(true);
                        break;
                    case "Staff":
                        new StaffDashboard(username).setVisible(true);
                        break;
                    case "Customer":
                        new CustomerDashboard(username).setVisible(true);
                        break;
                    default:
                        new Dashboard().setVisible(true);
                        break;
                }
                setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Invalid username, password, or role combination!", 
                    "Login Failed", JOptionPane.ERROR_MESSAGE);
                clearFields();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Database error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        t1.setText("");
        t2.setText("");
        t1.requestFocus();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new Login());
    }
}
