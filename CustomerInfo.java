package Hotel1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class CustomerInfo extends JFrame {
	private JPanel contentPane;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				CustomerInfo frame = new CustomerInfo();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public CustomerInfo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(530, 200, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Labels above the table
		contentPane.add(new JLabel("ID")).setBounds(31, 11, 46, 14);
		contentPane.add(new JLabel("Number")).setBounds(150, 11, 60, 14);
		contentPane.add(new JLabel("Name")).setBounds(270, 11, 60, 14);
		contentPane.add(new JLabel("Gender")).setBounds(360, 11, 60, 14);
		contentPane.add(new JLabel("Country")).setBounds(480, 11, 60, 14);
		contentPane.add(new JLabel("Room")).setBounds(600, 11, 60, 14);
		contentPane.add(new JLabel("Check-in Status")).setBounds(680, 11, 100, 14);
		contentPane.add(new JLabel("Deposit")).setBounds(800, 11, 100, 14);

		// Table
		table = new JTable();
		table.setBounds(0, 40, 900, 450);
		contentPane.add(table);

		// Load Data button
		JButton btnLoadData = new JButton("Load Data");
		btnLoadData.setBounds(300, 510, 120, 30);
		btnLoadData.setBackground(Color.BLACK);
		btnLoadData.setForeground(Color.WHITE);
		contentPane.add(btnLoadData);

		btnLoadData.addActionListener(e -> loadCustomerData());

		// Back button
		JButton btnExit = new JButton("Back");
		btnExit.setBounds(450, 510, 120, 30);
		btnExit.setBackground(Color.BLACK);
		btnExit.setForeground(Color.WHITE);
		contentPane.add(btnExit);

		btnExit.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});

		getContentPane().setBackground(Color.WHITE);
	}

	private void loadCustomerData() {
		String query = "SELECT * FROM customer";

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(query);
			 ResultSet rs = ps.executeQuery()) {

			DefaultTableModel model = new DefaultTableModel();
			model.setColumnIdentifiers(new Object[]{
					"ID", "Number", "Name", "Gender", "Country", "Room", "Check-in Status", "Deposit"
			});

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getString("id"),
						rs.getString("number"),
						rs.getString("name"),
						rs.getString("gender"),
						rs.getString("country"),
						rs.getString("room"),
						rs.getString("status"),
						rs.getString("deposit")
				});
			}

			table.setModel(model);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading customer data: " + e.getMessage());
		}
	}
}
