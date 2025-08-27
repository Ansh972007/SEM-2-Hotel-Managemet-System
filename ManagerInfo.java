package Hotel1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class ManagerInfo extends JFrame {
	private JPanel contentPane;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				ManagerInfo frame = new ManagerInfo();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public ManagerInfo() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(430, 200, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Table setup
		table = new JTable();
		table.setBounds(0, 34, 1000, 450);
		contentPane.add(table);

		// Labels above table
		contentPane.add(new JLabel("Name")).setBounds(41, 11, 46, 14);
		contentPane.add(new JLabel("Age")).setBounds(159, 11, 46, 14);
		contentPane.add(new JLabel("Gender")).setBounds(273, 11, 46, 14);
		contentPane.add(new JLabel("Job")).setBounds(416, 11, 86, 14);
		contentPane.add(new JLabel("Salary")).setBounds(536, 11, 86, 14);
		contentPane.add(new JLabel("Phone")).setBounds(656, 11, 86, 14);
		contentPane.add(new JLabel("Aadhar")).setBounds(786, 11, 86, 14);
		contentPane.add(new JLabel("Gmail")).setBounds(896, 11, 86, 14);

		// Load Data button
		JButton btnLoadData = new JButton("Load Data");
		btnLoadData.setBounds(350, 500, 120, 30);
		btnLoadData.setBackground(Color.BLACK);
		btnLoadData.setForeground(Color.WHITE);
		contentPane.add(btnLoadData);

		btnLoadData.addActionListener(e -> loadManagerData());

		// Back button
		JButton btnExit = new JButton("Back");
		btnExit.setBounds(510, 500, 120, 30);
		btnExit.setBackground(Color.BLACK);
		btnExit.setForeground(Color.WHITE);
		contentPane.add(btnExit);

		btnExit.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});

		getContentPane().setBackground(Color.WHITE);
	}

	private void loadManagerData() {
		String query = "SELECT * FROM employee WHERE job = 'Manager'";

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(query);
			 ResultSet rs = ps.executeQuery()) {

			DefaultTableModel model = new DefaultTableModel();
			model.setColumnIdentifiers(new Object[]{
					"Name", "Age", "Gender", "Job", "Salary", "Phone", "Aadhar", "Gmail"
			});

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getString("name"),
						rs.getInt("age"),
						rs.getString("gender"),
						rs.getString("job"),
						rs.getDouble("salary"),
						rs.getString("phone"),
						rs.getString("aadhar"),
						rs.getString("email")
				});
			}

			table.setModel(model);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading manager data.");
		}
	}
}
