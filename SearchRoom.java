package Hotel1;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class SearchRoom extends JFrame {
	private JPanel contentPane;
	private JTable table;
	private Choice c1;
	private JCheckBox checkRoom;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				SearchRoom frame = new SearchRoom();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public SearchRoom() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(530, 200, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblSearchForRoom = new JLabel("Search For Room");
		lblSearchForRoom.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSearchForRoom.setBounds(250, 11, 186, 31);
		contentPane.add(lblSearchForRoom);

		JLabel lblRoomAvailable = new JLabel("Room Bed Type:");
		lblRoomAvailable.setBounds(50, 73, 96, 14);
		contentPane.add(lblRoomAvailable);

		c1 = new Choice();
		c1.add("Single Bed");
		c1.add("Double Bed");
		c1.setBounds(153, 70, 120, 20);
		contentPane.add(c1);

		checkRoom = new JCheckBox("Only display Available");
		checkRoom.setBounds(400, 69, 205, 23);
		checkRoom.setBackground(Color.WHITE);
		contentPane.add(checkRoom);

		table = new JTable();
		table.setBounds(0, 187, 700, 300);
		contentPane.add(table);

		// Labels above the table
		contentPane.add(new JLabel("Room Number")).setBounds(23, 162, 96, 14);
		contentPane.add(new JLabel("Availability")).setBounds(175, 162, 120, 14);
		contentPane.add(new JLabel("Clean Status")).setBounds(306, 162, 96, 14);
		contentPane.add(new JLabel("Price")).setBounds(458, 162, 46, 14);
		contentPane.add(new JLabel("Bed Type")).setBounds(580, 162, 96, 14);

		JButton btnSearch = new JButton("Search");
		btnSearch.setBounds(200, 400, 120, 30);
		btnSearch.setBackground(Color.BLACK);
		btnSearch.setForeground(Color.WHITE);
		btnSearch.addActionListener(e -> searchRooms());
		contentPane.add(btnSearch);

		JButton btnExit = new JButton("Back");
		btnExit.setBounds(380, 400, 120, 30);
		btnExit.setBackground(Color.BLACK);
		btnExit.setForeground(Color.WHITE);
		btnExit.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});
		contentPane.add(btnExit);

		getContentPane().setBackground(Color.WHITE);
	}

	private void searchRooms() {
		String bedType = c1.getSelectedItem();
		String query;

		if (checkRoom.isSelected()) {
			query = "SELECT * FROM room WHERE availability = 'Available' AND bed_type = ?";
		} else {
			query = "SELECT * FROM room WHERE bed_type = ?";
		}

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, bedType);
			ResultSet rs = ps.executeQuery();

			DefaultTableModel model = new DefaultTableModel();
			model.setColumnIdentifiers(new Object[]{"Room Number", "Availability", "Clean Status", "Price", "Bed Type"});

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

		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading rooms: " + e.getMessage());
		}
	}
}
