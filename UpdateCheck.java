package Hotel1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class UpdateCheck extends JFrame {
	private JPanel contentPane;
	private JTextField txt_ID, txt_Room, txt_Status, txt_Date, txt_Time, txt_Payment;
	private Choice c1;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UpdateCheck frame = new UpdateCheck();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public UpdateCheck() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(500, 200, 950, 500);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUpdateCheckStatus = new JLabel("Check-In Details");
		lblUpdateCheckStatus.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUpdateCheckStatus.setBounds(124, 11, 222, 25);
		contentPane.add(lblUpdateCheckStatus);

		try {
			ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Hotel1/nine.jpg"));
			JLabel l1 = new JLabel(i1);
			l1.setBounds(450, 70, 476, 270);
			contentPane.add(l1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JLabel lblNewLabel = new JLabel("ID:");
		lblNewLabel.setBounds(25, 88, 46, 14);
		contentPane.add(lblNewLabel);

		c1 = new Choice();
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement("SELECT number FROM customer"); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				c1.add(rs.getString("number"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		c1.setBounds(248, 85, 140, 20);
		contentPane.add(c1);

		JLabel lblNewLabel_1 = new JLabel("Room Number :");
		lblNewLabel_1.setBounds(25, 129, 107, 14);
		contentPane.add(lblNewLabel_1);

		txt_ID = new JTextField();
		txt_ID.setBounds(248, 126, 140, 20);
		contentPane.add(txt_ID);

		JLabel lblNewLabel_2 = new JLabel("Name : ");
		lblNewLabel_2.setBounds(25, 174, 97, 14);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Checked-in :");
		lblNewLabel_3.setBounds(25, 216, 107, 14);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Amount Paid (Rs) : ");
		lblNewLabel_4.setBounds(25, 261, 107, 14);
		contentPane.add(lblNewLabel_4);

		JLabel lblNewLabel_5 = new JLabel("Pending Amount (Rs) : ");
		lblNewLabel_5.setBounds(25, 302, 150, 14);
		contentPane.add(lblNewLabel_5);

		txt_Status = new JTextField();
		txt_Status.setBounds(248, 171, 140, 20);
		contentPane.add(txt_Status);

		txt_Date = new JTextField();
		txt_Date.setBounds(248, 216, 140, 20);
		contentPane.add(txt_Date);

		txt_Time = new JTextField();
		txt_Time.setBounds(248, 258, 140, 20);
		contentPane.add(txt_Time);

		txt_Payment = new JTextField();
		txt_Payment.setBounds(248, 299, 140, 20);
		contentPane.add(txt_Payment);

		JButton btnCheck = new JButton("Check");
		btnCheck.setBounds(56, 378, 89, 23);
		btnCheck.setBackground(Color.BLACK);
		btnCheck.setForeground(Color.WHITE);
		contentPane.add(btnCheck);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(168, 378, 89, 23);
		btnUpdate.setBackground(Color.BLACK);
		btnUpdate.setForeground(Color.WHITE);
		contentPane.add(btnUpdate);

		JButton btnExit = new JButton("Back");
		btnExit.setBounds(281, 378, 89, 23);
		btnExit.setBackground(Color.BLACK);
		btnExit.setForeground(Color.WHITE);
		contentPane.add(btnExit);

		getContentPane().setBackground(Color.WHITE);

		// Action Listeners
		btnCheck.addActionListener(e -> {
			String number = c1.getSelectedItem();
			try (Connection conn = getConnection()) {
				PreparedStatement ps = conn.prepareStatement("SELECT * FROM customer WHERE number = ?");
				ps.setString(1, number);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					txt_ID.setText(rs.getString("room_number"));
					txt_Status.setText(rs.getString("name"));
					txt_Date.setText(rs.getString("status"));
					txt_Time.setText(rs.getString("deposit"));
				}

				// Calculate pending
				ps = conn.prepareStatement("SELECT price FROM room WHERE room_number = ?");
				ps.setString(1, txt_ID.getText());
				rs = ps.executeQuery();
				if (rs.next()) {
					int price = rs.getInt("price");
					int paid = Integer.parseInt(txt_Time.getText());
					txt_Payment.setText(String.valueOf(price - paid));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		btnUpdate.addActionListener(e -> {
			String number = c1.getSelectedItem();
			String room = txt_ID.getText();
			String name = txt_Status.getText();
			String status = txt_Date.getText();
			String deposit = txt_Time.getText();

			try (Connection conn = getConnection()) {
				String update = "UPDATE customer SET room_number=?, name=?, status=?, deposit=? WHERE number=?";
				PreparedStatement ps = conn.prepareStatement(update);
				ps.setString(1, room);
				ps.setString(2, name);
				ps.setString(3, status);
				ps.setString(4, deposit);
				ps.setString(5, number);
				ps.executeUpdate();

				JOptionPane.showMessageDialog(null, "Data Updated Successfully");
				new Reception().setVisible(true);
				setVisible(false);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		btnExit.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});
	}

	private Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost:3306/hotel"; // Replace with your DB name
		String user = "root"; // Default for XAMPP
		String password = ""; // Default is empty in XAMPP
		return DriverManager.getConnection(url, user, password);
	}
}
