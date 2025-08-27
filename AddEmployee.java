package Hotel1;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AddEmployee extends JFrame {

    JTextField tfName, tfAge, tfSalary, tfPhone, tfAadhar, tfEmail;
    JComboBox<String> jobBox;
    JRadioButton rbMale, rbFemale;

    public AddEmployee() {
        setTitle("Add Employee Details");
        setLayout(null);
        setSize(900, 600);
        setLocation(530, 200);
        getContentPane().setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("ADD EMPLOYEE DETAILS");
        lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 30));
        lblTitle.setForeground(Color.BLUE);
        lblTitle.setBounds(450, 24, 442, 35);
        add(lblTitle);

        // Form Fields
        JLabel lblName = new JLabel("NAME:");
        lblName.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblName.setBounds(60, 30, 150, 27);
        add(lblName);

        tfName = new JTextField();
        tfName.setBounds(200, 30, 150, 27);
        add(tfName);

        JLabel lblAge = new JLabel("AGE:");
        lblAge.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblAge.setBounds(60, 80, 150, 27);
        add(lblAge);

        tfAge = new JTextField();
        tfAge.setBounds(200, 80, 150, 27);
        add(tfAge);

        JLabel lblGender = new JLabel("GENDER:");
        lblGender.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblGender.setBounds(60, 120, 150, 27);
        add(lblGender);

        rbMale = new JRadioButton("MALE");
        rbMale.setBackground(Color.WHITE);
        rbMale.setBounds(200, 120, 70, 27);
        add(rbMale);

        rbFemale = new JRadioButton("FEMALE");
        rbFemale.setBackground(Color.WHITE);
        rbFemale.setBounds(280, 120, 100, 27);
        add(rbFemale);

        ButtonGroup bg = new ButtonGroup();
        bg.add(rbMale);
        bg.add(rbFemale);

        JLabel lblJob = new JLabel("JOB:");
        lblJob.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblJob.setBounds(60, 170, 150, 27);
        add(lblJob);

        String[] jobs = {"Front Desk Clerks", "Porters", "Housekeeping", "Kitchen Staff", "Room Service", "Waiter/Waitress", "Manager", "Accountant", "Chef"};
        jobBox = new JComboBox<>(jobs);
        jobBox.setBounds(200, 170, 150, 30);
        jobBox.setBackground(Color.WHITE);
        add(jobBox);

        JLabel lblSalary = new JLabel("SALARY:");
        lblSalary.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblSalary.setBounds(60, 220, 150, 27);
        add(lblSalary);

        tfSalary = new JTextField();
        tfSalary.setBounds(200, 220, 150, 27);
        add(tfSalary);

        JLabel lblPhone = new JLabel("PHONE:");
        lblPhone.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblPhone.setBounds(60, 270, 150, 27);
        add(lblPhone);

        tfPhone = new JTextField();
        tfPhone.setBounds(200, 270, 150, 27);
        add(tfPhone);

        JLabel lblAadhar = new JLabel("AADHAR:");
        lblAadhar.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblAadhar.setBounds(60, 320, 150, 27);
        add(lblAadhar);

        tfAadhar = new JTextField();
        tfAadhar.setBounds(200, 320, 150, 27);
        add(tfAadhar);

        JLabel lblEmail = new JLabel("EMAIL:");
        lblEmail.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblEmail.setBounds(60, 370, 150, 27);
        add(lblEmail);

        tfEmail = new JTextField();
        tfEmail.setBounds(200, 370, 150, 27);
        add(tfEmail);

        // SAVE Button
        JButton btnSave = new JButton("SAVE");
        btnSave.setBounds(200, 420, 150, 30);
        btnSave.setBackground(Color.BLACK);
        btnSave.setForeground(Color.WHITE);
        add(btnSave);

        // Image
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Hotel1/ADDemp.jpg"));
        Image i3 = i1.getImage().getScaledInstance(1920, 1080, Image.SCALE_DEFAULT);
        JLabel lblImage = new JLabel(new ImageIcon(i3));
        lblImage.setBounds(0, 0, 1600, 950);
        add(lblImage);

        // Action
        btnSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String name = tfName.getText();
                String age = tfAge.getText();
                String gender = rbMale.isSelected() ? "Male" : rbFemale.isSelected() ? "Female" : "";
                String job = (String) jobBox.getSelectedItem();
                String salary = tfSalary.getText();
                String phone = tfPhone.getText();
                String aadhar = tfAadhar.getText();
                String email = tfEmail.getText();

                if (name.isEmpty() || age.isEmpty() || gender.isEmpty() || salary.isEmpty() || phone.isEmpty() || aadhar.isEmpty() || email.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please fill all fields!");
                    return;
                }

                try (Connection conn = DBConnection.getConnection()) {
                    String query = "INSERT INTO employee (name, age, gender, job, salary, phone, aadhar, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement ps = conn.prepareStatement(query);
                    ps.setString(1, name);
                    ps.setInt(2, Integer.parseInt(age));
                    ps.setString(3, gender);
                    ps.setString(4, job);
                    ps.setDouble(5, Double.parseDouble(salary));
                    ps.setString(6, phone);
                    ps.setString(7, aadhar);
                    ps.setString(8, email);

                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Employee Added Successfully!");
                    setVisible(false);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new AddEmployee();
    }
}
