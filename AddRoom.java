package Hotel1;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class AddRoom extends JFrame implements ActionListener {

    private JPanel contentPane;
    private JTextField t_roomNumber, t_price;
    private JComboBox<String> comboAvailability, comboCleanStatus, comboBedType;
    private JButton b_add, b_back;

    public static void main(String[] args) {
        new AddRoom().setVisible(true);
    }

    public AddRoom() {
        setTitle("Add Room");
        setBounds(450, 200, 1000, 450);
        contentPane = new JPanel();
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Title
        JLabel lblTitle = new JLabel("Add Rooms");
        lblTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitle.setBounds(194, 10, 120, 22);
        contentPane.add(lblTitle);

        // Room Number
        JLabel lblRoomNo = new JLabel("Room Number");
        lblRoomNo.setForeground(Color.WHITE);
        lblRoomNo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblRoomNo.setBounds(64, 70, 120, 22);
        contentPane.add(lblRoomNo);

        t_roomNumber = new JTextField();
        t_roomNumber.setBounds(174, 70, 156, 20);
        contentPane.add(t_roomNumber);

        // Availability
        JLabel lblAvailability = new JLabel("Availability");
        lblAvailability.setForeground(Color.WHITE);
        lblAvailability.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblAvailability.setBounds(64, 110, 120, 22);
        contentPane.add(lblAvailability);

        comboAvailability = new JComboBox<>(new String[]{"Available", "Occupied"});
        comboAvailability.setBounds(174, 110, 156, 20);
        contentPane.add(comboAvailability);

        // Clean Status
        JLabel lblClean = new JLabel("Cleaning Status");
        lblClean.setForeground(Color.WHITE);
        lblClean.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblClean.setBounds(64, 150, 120, 22);
        contentPane.add(lblClean);

        comboCleanStatus = new JComboBox<>(new String[]{"Cleaned", "Dirty"});
        comboCleanStatus.setBounds(174, 150, 156, 20);
        contentPane.add(comboCleanStatus);

        // Price
        JLabel lblPrice = new JLabel("Price");
        lblPrice.setForeground(Color.WHITE);
        lblPrice.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPrice.setBounds(64, 190, 120, 22);
        contentPane.add(lblPrice);

        t_price = new JTextField();
        t_price.setBounds(174, 190, 156, 20);
        contentPane.add(t_price);

        // Bed Type
        JLabel lblBedType = new JLabel("Bed Type");
        lblBedType.setForeground(Color.WHITE);
        lblBedType.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblBedType.setBounds(64, 230, 120, 22);
        contentPane.add(lblBedType);

        comboBedType = new JComboBox<>(new String[]{"Single Bed", "Double Bed"});
        comboBedType.setBounds(174, 230, 156, 20);
        contentPane.add(comboBedType);

        // Add Button
        b_add = new JButton("Add");
        b_add.setBounds(64, 300, 111, 33);
        b_add.setBackground(Color.BLACK);
        b_add.setForeground(Color.WHITE);
        b_add.addActionListener(this);
        contentPane.add(b_add);

        // Back Button
        b_back = new JButton("Back");
        b_back.setBounds(198, 300, 111, 33);
        b_back.setBackground(Color.BLACK);
        b_back.setForeground(Color.WHITE);
        b_back.addActionListener(this);
        contentPane.add(b_back);

        // Image
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("Hotel1/Addroom.jpg"));
        Image img = icon.getImage().getScaledInstance(1970, 1080, Image.SCALE_DEFAULT);
        JLabel imgLabel = new JLabel(new ImageIcon(img));
        imgLabel.setBounds(0, 0, 1970, 1080);
        contentPane.add(imgLabel);

        contentPane.setBackground(Color.WHITE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == b_add) {
            String room = t_roomNumber.getText();
            String availability = (String) comboAvailability.getSelectedItem();
            String status = (String) comboCleanStatus.getSelectedItem();
            String price = t_price.getText();
            String bedType = (String) comboBedType.getSelectedItem();

            if (room.isEmpty() || price.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO room (room_number, availability, clean_status, price, bed_type) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, Integer.parseInt(room));
                pst.setString(2, availability);
                pst.setString(3, status);
                pst.setDouble(4, Double.parseDouble(price));
                pst.setString(5, bedType);

                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Room added successfully!");
                this.setVisible(false);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error while adding room: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid numbers for room and price.");
            }

        } else if (ae.getSource() == b_back) {
            this.setVisible(false);
        }
    }
}
