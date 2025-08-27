package Hotel1;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class PickUp extends JFrame {
	private JPanel contentPane;
	private JTable table;
	private Choice c1;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				PickUp frame = new PickUp();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public PickUp() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(530, 200, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JLabel lblPickUpService = new JLabel("Pick Up Service");
		lblPickUpService.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPickUpService.setBounds(90, 11, 200, 25);
		contentPane.add(lblPickUpService);

		JLabel lblTypeOfCar = new JLabel("Type of Car:");
		lblTypeOfCar.setBounds(32, 97, 100, 14);
		contentPane.add(lblTypeOfCar);

		c1 = new Choice();
		try (Connection conn = DBConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT DISTINCT brand FROM driver")) {
			while (rs.next()) {
				c1.add(rs.getString("brand"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		c1.setBounds(123, 94, 150, 25);
		contentPane.add(c1);

		JButton btnDisplay = new JButton("Display");
		btnDisplay.setBounds(200, 500, 120, 30);
		btnDisplay.setBackground(Color.BLACK);
		btnDisplay.setForeground(Color.WHITE);
		contentPane.add(btnDisplay);

		JButton btnExit = new JButton("Back");
		btnExit.setBounds(420, 500, 120, 30);
		btnExit.setBackground(Color.BLACK);
		btnExit.setForeground(Color.WHITE);
		contentPane.add(btnExit);

		btnExit.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});

		btnDisplay.addActionListener(e -> displayDrivers());

		// Table
		table = new JTable();
		table.setBounds(0, 233, 800, 250);
		contentPane.add(table);

		// Column labels
		contentPane.add(createLabel("Name", 24, 208));
		contentPane.add(createLabel("Age", 165, 208));
		contentPane.add(createLabel("Gender", 264, 208));
		contentPane.add(createLabel("Company", 366, 208));
		contentPane.add(createLabel("Brand", 486, 208));
		contentPane.add(createLabel("Available", 600, 208));
		contentPane.add(createLabel("Location", 700, 208));

		getContentPane().setBackground(Color.WHITE);
	}

	private JLabel createLabel(String text, int x, int y) {
		JLabel label = new JLabel(text);
		label.setBounds(x, y, 100, 14);
		return label;
	}

	private void displayDrivers() {
		String selectedBrand = c1.getSelectedItem();
		String query = "SELECT * FROM driver WHERE brand = ?";

		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(query)) {

			ps.setString(1, selectedBrand);
			ResultSet rs = ps.executeQuery();

			DefaultTableModel model = new DefaultTableModel();
			model.setColumnIdentifiers(new String[]{
					"Name", "Age", "Gender", "Company", "Brand", "Available", "Location"
			});

			while (rs.next()) {
				model.addRow(new Object[]{
						rs.getString("name"),
						rs.getInt("age"),
						rs.getString("gender"),
						rs.getString("company"),
						rs.getString("brand"),
						rs.getString("available"),
						rs.getString("location")
				});
			}

			table.setModel(model);

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(this, "Failed to load data.");
		}
	}
}
