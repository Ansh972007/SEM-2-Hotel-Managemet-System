package Hotel1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.sql.*;

public class CheckOut extends JFrame {
	private JPanel contentPane;
	private JTextField t1;
	private Choice c1;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				CheckOut frame = new CheckOut();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public CheckOut() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(530, 200, 800, 294);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblCheckOut = new JLabel("Check Out ");
		lblCheckOut.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCheckOut.setBounds(70, 11, 140, 35);
		contentPane.add(lblCheckOut);

		JLabel lblName = new JLabel("Number :");
		lblName.setBounds(20, 85, 80, 14);
		contentPane.add(lblName);

		c1 = new Choice();
		loadCustomerNumbers();
		c1.setBounds(130, 82, 150, 20);
		contentPane.add(c1);

		JButton searchBtn = new JButton(new ImageIcon(
				new ImageIcon(ClassLoader.getSystemResource("hotel/management/system/icons/tick.png"))
						.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT)));
		searchBtn.setBounds(290, 82, 20, 20);
		contentPane.add(searchBtn);

		searchBtn.addActionListener(e -> fetchRoomNumber());

		JLabel lblRoomNumber = new JLabel("Room Number:");
		lblRoomNumber.setBounds(20, 132, 86, 20);
		contentPane.add(lblRoomNumber);

		t1 = new JTextField();
		t1.setBounds(130, 132, 150, 20);
		contentPane.add(t1);

		JButton btnCheckOut = new JButton("Check Out");
		btnCheckOut.setBounds(50, 200, 100, 25);
		btnCheckOut.setBackground(Color.BLACK);
		btnCheckOut.setForeground(Color.WHITE);
		contentPane.add(btnCheckOut);

		btnCheckOut.addActionListener(e -> processCheckout());

		JButton btnExit = new JButton("Back");
		btnExit.setBounds(160, 200, 100, 25);
		btnExit.setBackground(Color.BLACK);
		btnExit.setForeground(Color.WHITE);
		contentPane.add(btnExit);

		btnExit.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});

		// Image banner
		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Hotel1/sixth.jpg"));
		Image i3 = i1.getImage().getScaledInstance(400, 225, Image.SCALE_DEFAULT);
		JLabel l1 = new JLabel(new ImageIcon(i3));
		l1.setBounds(300, 0, 500, 225);
		add(l1);

		getContentPane().setBackground(Color.WHITE);
	}

	private void loadCustomerNumbers() {
		try (Connection conn = DBConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT number FROM customer")) {
			while (rs.next()) {
				c1.add(rs.getString("number"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fetchRoomNumber() {
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement("SELECT room_number FROM customer WHERE number = ?")) {
			ps.setString(1, c1.getSelectedItem());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				t1.setText(rs.getString("room_number"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processCheckout() {
		String number = c1.getSelectedItem();
		String room = t1.getText();

		String deleteCustomer = "DELETE FROM customer WHERE number = ?";
		String updateRoom = "UPDATE room SET availability = 'Available' WHERE room_number = ?";

		try (Connection conn = DBConnection.getConnection()) {
			PreparedStatement ps1 = conn.prepareStatement(deleteCustomer);
			ps1.setString(1, number);
			ps1.executeUpdate();

			PreparedStatement ps2 = conn.prepareStatement(updateRoom);
			ps2.setString(1, room);
			ps2.executeUpdate();

			JOptionPane.showMessageDialog(this, "Check Out Successful");
			new Reception().setVisible(true);
			setVisible(false);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error during checkout: " + e.getMessage());
		}
	}
}
