package Hotel1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Room extends JFrame {
	private JPanel contentPane;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Room frame = new Room();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Room() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(450, 200, 1100, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Table for room data
		table = new JTable();
		table.setBounds(0, 40, 500, 400);
		contentPane.add(table);

		// Load Data Button
		JButton btnLoadData = new JButton("Load Data");
		btnLoadData.setBounds(100, 470, 120, 30);
		btnLoadData.setBackground(Color.BLACK);
		btnLoadData.setForeground(Color.WHITE);
		contentPane.add(btnLoadData);

		btnLoadData.addActionListener(e -> loadRoomData());

		// Back Button
		JButton btnBack = new JButton("Back");
		btnBack.setBounds(290, 470, 120, 30);
		btnBack.setBackground(Color.BLACK);
		btnBack.setForeground(Color.WHITE);
		contentPane.add(btnBack);

		btnBack.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});

		// Table Labels
		contentPane.add(new JLabel("Room Number")).setBounds(22, 15, 90, 14);
		contentPane.add(new JLabel("Availability")).setBounds(129, 15, 69, 14);
		contentPane.add(new JLabel("Clean Status")).setBounds(226, 15, 76, 14);
		contentPane.add(new JLabel("Price")).setBounds(340, 15, 46, 14);
		contentPane.add(new JLabel("Bed Type")).setBounds(427, 15, 76, 14);

		// Banner Image
		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Hotel1/room3.jpg"));
		Image i3 = i1.getImage().getScaledInstance(1000, 800, Image.SCALE_DEFAULT);
		ImageIcon i2 = new ImageIcon(i3);
		JLabel banner = new JLabel(i2);
		banner.setBounds(80, -150, 1920, 1090);
		add(banner);

		getContentPane().setBackground(Color.WHITE);
	}

	private void loadRoomData() {
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement("SELECT * FROM room");
			 ResultSet rs = ps.executeQuery()) {

			DefaultTableModel model = new DefaultTableModel();
			model.setColumnIdentifiers(new Object[]{
					"Room Number", "Availability", "Clean Status", "Price", "Bed Type"
			});

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getString("room_number"),
						rs.getString("availability"),
						rs.getString("status"),
						rs.getString("price"),
						rs.getString("bed_type")
				});
			}

			table.setModel(model);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading room data.");
		}
	}
}
