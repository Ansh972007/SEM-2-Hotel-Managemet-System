package Hotel1;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class Department extends JFrame {
	private JPanel contentPane;
	private JTable table;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Department frame = new Department();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Department() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(600, 200, 700, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblDepartment = new JLabel("Department");
		lblDepartment.setBounds(145, 11, 105, 14);
		contentPane.add(lblDepartment);

		JLabel lblBudget = new JLabel("Budget");
		lblBudget.setBounds(431, 11, 75, 14);
		contentPane.add(lblBudget);

		table = new JTable();
		table.setBounds(0, 40, 700, 350);
		contentPane.add(table);

		JButton btnLoad = new JButton("Load Data");
		btnLoad.setBounds(170, 410, 120, 30);
		btnLoad.setBackground(Color.BLACK);
		btnLoad.setForeground(Color.WHITE);
		contentPane.add(btnLoad);

		btnLoad.addActionListener(e -> loadDepartmentData());

		JButton btnBack = new JButton("Back");
		btnBack.setBounds(400, 410, 120, 30);
		btnBack.setBackground(Color.BLACK);
		btnBack.setForeground(Color.WHITE);
		contentPane.add(btnBack);

		btnBack.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});

		getContentPane().setBackground(Color.WHITE);
	}

	private void loadDepartmentData() {
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement("SELECT * FROM department");
			 ResultSet rs = ps.executeQuery()) {

			DefaultTableModel model = new DefaultTableModel();
			model.setColumnIdentifiers(new Object[]{"Department", "Budget"});

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getString("department_name"),
						rs.getDouble("budget")
				});
			}

			table.setModel(model);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error loading department data.");
		}
	}
}
