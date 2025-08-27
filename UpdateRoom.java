package Hotel1;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

public class UpdateRoom extends JFrame {

	private JPanel contentPane;
	private JTextField txt_ID, txt_Ava, txt_Status, txt_Room;
	private Choice c1;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				UpdateRoom frame = new UpdateRoom();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public UpdateRoom() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(530, 200, 1000, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		try {
			ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Hotel1/seventh.jpg"));
			Image i3 = i1.getImage().getScaledInstance(550, 250, Image.SCALE_DEFAULT);
			JLabel l1 = new JLabel(new ImageIcon(i3));
			l1.setBounds(400, 80, 600, 250);
			add(l1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		JLabel lblUpdateRoomStatus = new JLabel("Update Room Status");
		lblUpdateRoomStatus.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUpdateRoomStatus.setBounds(85, 11, 206, 34);
		contentPane.add(lblUpdateRoomStatus);

		JLabel lblNewLabel = new JLabel("Guest ID:");
		lblNewLabel.setBounds(27, 87, 90, 14);
		contentPane.add(lblNewLabel);

		c1 = new Choice();
		try (Connection conn = getConnection();
			 PreparedStatement ps = conn.prepareStatement("SELECT number FROM customer");
			 ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {
				c1.add(rs.getString("number"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		c1.setBounds(160, 84, 140, 20);
		contentPane.add(c1);

		JLabel lblRoomId = new JLabel("Room Number:");
		lblRoomId.setBounds(27, 133, 100, 14);
		contentPane.add(lblRoomId);

		txt_Room = new JTextField();
		txt_Room.setBounds(160, 130, 140, 20);
		contentPane.add(txt_Room);

		JLabel lblAvailability = new JLabel("Availability:");
		lblAvailability.setBounds(27, 187, 90, 14);
		contentPane.add(lblAvailability);

		txt_Ava = new JTextField();
		txt_Ava.setBounds(160, 184, 140, 20);
		contentPane.add(txt_Ava);

		JLabel lblCleanStatus = new JLabel("Clean Status:");
		lblCleanStatus.setBounds(27, 240, 90, 14);
		contentPane.add(lblCleanStatus);

		txt_Status = new JTextField();
		txt_Status.setBounds(160, 237, 140, 20);
		contentPane.add(txt_Status);

		JButton btnCheck = new JButton("Check");
		btnCheck.setBounds(120, 315, 89, 23);
		btnCheck.setBackground(Color.BLACK);
		btnCheck.setForeground(Color.WHITE);
		contentPane.add(btnCheck);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setBounds(60, 355, 89, 23);
		btnUpdate.setBackground(Color.BLACK);
		btnUpdate.setForeground(Color.WHITE);
		contentPane.add(btnUpdate);

		JButton btnExit = new JButton("Back");
		btnExit.setBounds(180, 355, 89, 23);
		btnExit.setBackground(Color.BLACK);
		btnExit.setForeground(Color.WHITE);
		contentPane.add(btnExit);

		getContentPane().setBackground(Color.WHITE);

		// Button Actions
		btnCheck.addActionListener(e -> {
			String guestId = c1.getSelectedItem();
			try (Connection conn = getConnection()) {
				PreparedStatement ps = conn.prepareStatement("SELECT room_number FROM customer WHERE number = ?");
				ps.setString(1, guestId);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					txt_Room.setText(rs.getString("room_number"));
				}

				String room = txt_Room.getText();
				ps = conn.prepareStatement("SELECT * FROM room WHERE room_number = ?");
				ps.setString(1, room);
				rs = ps.executeQuery();
				if (rs.next()) {
					txt_Ava.setText(rs.getString("availability"));
					txt_Status.setText(rs.getString("clean_status"));
				}

			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		btnUpdate.addActionListener(e -> {
			String room = txt_Room.getText();
			String status = txt_Status.getText();
			String availability = txt_Ava.getText();

			try (Connection conn = getConnection()) {
				PreparedStatement ps = conn.prepareStatement(
						"UPDATE room SET availability = ?, clean_status = ? WHERE room_number = ?");
				ps.setString(1, availability);
				ps.setString(2, status);
				ps.setString(3, room);
				ps.executeUpdate();
				JOptionPane.showMessageDialog(null, "Room Updated Successfully");
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
		String url = "jdbc:mysql://localhost:3306/hotel";  // Replace "hotel" with your DB name
		String user = "root";                               // Default user in XAMPP
		String pass = "";                                   // Default password is empty
		return DriverManager.getConnection(url, user, pass);
	}
}
