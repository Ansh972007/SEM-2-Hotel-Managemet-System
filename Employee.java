package Hotel1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class Employee extends JFrame {
	private JPanel contentPane;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Employee frame = new Employee();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Employee() throws SQLException {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(430, 200, 1000, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		table = new JTable();
		table.setBounds(0, 34, 1000, 450);
		contentPane.add(table);

		JButton btnLoadData = new JButton("Load Data");
		btnLoadData.addActionListener(e -> {
			try (Connection conn = DBConnection.getConnection();
				 PreparedStatement ps = conn.prepareStatement("SELECT * FROM employee");
				 ResultSet rs = ps.executeQuery()) {

				DefaultTableModel model = new DefaultTableModel();
				model.setColumnIdentifiers(new Object[]{"Name", "Age", "Gender", "Job", "Salary", "Phone", "Aadhar", "Gmail"});

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

			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
			}
		});
		btnLoadData.setBounds(350, 500, 120, 30);
		btnLoadData.setBackground(Color.BLACK);
		btnLoadData.setForeground(Color.WHITE);
		contentPane.add(btnLoadData);

		JButton btnExit = new JButton("Back");
		btnExit.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});
		btnExit.setBounds(510, 500, 120, 30);
		btnExit.setBackground(Color.BLACK);
		btnExit.setForeground(Color.WHITE);
		contentPane.add(btnExit);

		contentPane.add(new JLabel("Name")).setBounds(41, 11, 46, 14);
		contentPane.add(new JLabel("Age")).setBounds(159, 11, 46, 14);
		contentPane.add(new JLabel("Gender")).setBounds(273, 11, 46, 14);
		contentPane.add(new JLabel("Job")).setBounds(416, 11, 86, 14);
		contentPane.add(new JLabel("Salary")).setBounds(536, 11, 86, 14);
		contentPane.add(new JLabel("Phone")).setBounds(656, 11, 86, 14);
		contentPane.add(new JLabel("Aadhar")).setBounds(786, 11, 86, 14);
		contentPane.add(new JLabel("Gmail")).setBounds(896, 11, 86, 14);

		getContentPane().setBackground(Color.WHITE);
	}
}
