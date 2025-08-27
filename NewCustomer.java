package Hotel1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class NewCustomer extends JFrame {
	private JPanel contentPane;
	private JTextField t1, t2, t3, t5, t6, t7, t8;
	private JComboBox<String> comboBox;
	private JRadioButton r1, r2;
	private Choice c1;
	private JLabel totalDaysLabel, totalAmountLabel;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				NewCustomer frame = new NewCustomer();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public NewCustomer() {
		setBounds(530, 200, 900, 650);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Title
		JLabel lblName = new JLabel("NEW CUSTOMER BOOKING FORM");
		lblName.setBackground(Color.BLACK);
		lblName.setForeground(Color.WHITE);
		lblName.setFont(new Font("Arial", Font.BOLD, 20));
		lblName.setBounds(118, 11, 400, 53);
		contentPane.add(lblName);

		// ID Type
		JLabel lblId = new JLabel("ID Type :");
		lblId.setBackground(Color.BLACK);
		lblId.setForeground(Color.WHITE);
		lblId.setBounds(35, 76, 200, 14);
		contentPane.add(lblId);

		comboBox = new JComboBox<>(new String[]{"Passport", "Aadhar Card", "Voter Id", "Driving license"});
		comboBox.setBounds(271, 73, 150, 20);
		contentPane.add(comboBox);

		// ID Number
		JLabel l2 = new JLabel("ID Number :");
		l2.setBackground(Color.BLACK);
		l2.setForeground(Color.WHITE);
		l2.setBounds(35, 111, 200, 14);
		contentPane.add(l2);

		t1 = new JTextField();
		t1.setBounds(271, 111, 150, 20);
		contentPane.add(t1);

		// Name
		JLabel lblName_1 = new JLabel("Name :");
		lblName_1.setBackground(Color.BLACK);
		lblName_1.setForeground(Color.WHITE);
		lblName_1.setBounds(35, 151, 200, 14);
		contentPane.add(lblName_1);

		t2 = new JTextField();
		t2.setBounds(271, 151, 150, 20);
		contentPane.add(t2);

		// Gender
		JLabel lblGender = new JLabel("Gender :");
		lblGender.setBackground(Color.BLACK);
		lblGender.setForeground(Color.WHITE);
		lblGender.setBounds(35, 191, 200, 14);
		contentPane.add(lblGender);

		r1 = new JRadioButton("Male");
		r1.setFont(new Font("Arial", Font.BOLD, 14));
		r1.setForeground(Color.WHITE);
		r1.setBackground(Color.BLACK);
		r1.setBounds(271, 191, 80, 20);
		contentPane.add(r1);

		r2 = new JRadioButton("Female");
		r2.setFont(new Font("Arial", Font.BOLD, 14));
		r2.setForeground(Color.WHITE);
		r2.setBackground(Color.BLACK);
		r2.setBounds(350, 191, 100, 20);
		contentPane.add(r2);

		ButtonGroup genderGroup = new ButtonGroup();
		genderGroup.add(r1);
		genderGroup.add(r2);

		// Country
		JLabel lblCountry = new JLabel("Country :");
		lblCountry.setForeground(Color.WHITE);
		lblCountry.setBounds(35, 231, 200, 14);
		contentPane.add(lblCountry);

		t3 = new JTextField();
		t3.setBounds(271, 231, 150, 20);
		contentPane.add(t3);

		// Phone Number
		JLabel lblPhone = new JLabel("Phone Number :");
		lblPhone.setForeground(Color.WHITE);
		lblPhone.setBounds(35, 271, 200, 14);
		contentPane.add(lblPhone);

		t7 = new JTextField();
		t7.setBounds(271, 271, 150, 20);
		contentPane.add(t7);

		// Check-in Date
		JLabel lblCheckIn = new JLabel("Check-in Date :");
		lblCheckIn.setForeground(Color.WHITE);
		lblCheckIn.setBounds(35, 311, 200, 14);
		contentPane.add(lblCheckIn);

		t5 = new JTextField();
		t5.setBounds(271, 311, 150, 20);
		t5.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		contentPane.add(t5);

		// Check-out Date
		JLabel lblCheckOut = new JLabel("Check-out Date :");
		lblCheckOut.setForeground(Color.WHITE);
		lblCheckOut.setBounds(35, 351, 200, 14);
		contentPane.add(lblCheckOut);

		t8 = new JTextField();
		t8.setBounds(271, 351, 150, 20);
		// Set default check-out date to tomorrow
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		t8.setText(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		contentPane.add(t8);

		// Number of Guests
		JLabel lblGuests = new JLabel("Number of Guests :");
		lblGuests.setForeground(Color.WHITE);
		lblGuests.setBounds(35, 391, 200, 14);
		contentPane.add(lblGuests);

		JTextField guestsField = new JTextField();
		guestsField.setBounds(271, 391, 150, 20);
		guestsField.setText("1");
		contentPane.add(guestsField);

		// Room Selection
		JLabel lblReserveRoomNumber = new JLabel("Allocated Room Number :");
		lblReserveRoomNumber.setForeground(Color.WHITE);
		lblReserveRoomNumber.setBounds(35, 431, 200, 14);
		contentPane.add(lblReserveRoomNumber);

		c1 = new Choice();
		loadRoomNumbers();
		c1.setBounds(271, 431, 150, 20);
		contentPane.add(c1);

		// Total Days
		totalDaysLabel = new JLabel("Total Days: 1");
		totalDaysLabel.setForeground(Color.YELLOW);
		totalDaysLabel.setFont(new Font("Arial", Font.BOLD, 14));
		totalDaysLabel.setBounds(35, 471, 200, 20);
		contentPane.add(totalDaysLabel);

		// Total Amount
		totalAmountLabel = new JLabel("Total Amount: $0");
		totalAmountLabel.setForeground(Color.YELLOW);
		totalAmountLabel.setFont(new Font("Arial", Font.BOLD, 14));
		totalAmountLabel.setBounds(35, 501, 200, 20);
		contentPane.add(totalAmountLabel);

		// Deposit
		JLabel lblDeposite = new JLabel("Deposit :");
		lblDeposite.setForeground(Color.WHITE);
		lblDeposite.setBounds(35, 541, 200, 14);
		contentPane.add(lblDeposite);

		t6 = new JTextField();
		t6.setBounds(271, 541, 150, 20);
		contentPane.add(t6);

		// Add listeners for date calculation
		t5.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calculateTotalDays();
			}
		});

		t8.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				calculateTotalDays();
			}
		});

		c1.addItemListener(e -> calculateTotalAmount());

		// Buttons
		JButton btnAdd = new JButton("Add Customer");
		btnAdd.setBounds(100, 580, 120, 30);
		btnAdd.setBackground(Color.BLACK);
		btnAdd.setForeground(Color.WHITE);
		contentPane.add(btnAdd);

		btnAdd.addActionListener(e -> addCustomer(guestsField.getText()));

		JButton btnBack = new JButton("Back");
		btnBack.setBounds(260, 580, 120, 30);
		btnBack.setBackground(Color.BLACK);
		btnBack.setForeground(Color.WHITE);
		contentPane.add(btnBack);

		btnBack.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});

		// Image on right side
		try {
			ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("Hotel1/Addcustomer.jpg"));
			Image i3 = i1.getImage().getScaledInstance(1920, 1080, Image.SCALE_DEFAULT);
			JLabel l1 = new JLabel(new ImageIcon(i3));
			l1.setBounds(0, 0, 1700, 900);
			add(l1);
		} catch (Exception e) {
			// If image not found, add a placeholder
			JLabel placeholder = new JLabel("Hotel Booking");
			placeholder.setBounds(450, 100, 400, 400);
			placeholder.setHorizontalAlignment(SwingConstants.CENTER);
			placeholder.setFont(new Font("Arial", Font.BOLD, 24));
			placeholder.setForeground(Color.GRAY);
			placeholder.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			add(placeholder);
		}

		getContentPane().setBackground(Color.WHITE);
		
		// Calculate initial values
		calculateTotalDays();
	}

	private void loadRoomNumbers() {
		try (Connection conn = DBConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT room_number, room_type, price FROM room WHERE availability = 'Available' ORDER BY room_number")) {
			while (rs.next()) {
				String roomInfo = rs.getString("room_number") + " (" + rs.getString("room_type") + " - $" + rs.getString("price") + ")";
				c1.add(roomInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void calculateTotalDays() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date checkIn = sdf.parse(t5.getText());
			Date checkOut = sdf.parse(t8.getText());
			
			long diffInMillies = checkOut.getTime() - checkIn.getTime();
			long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
			
			if (diffInDays < 0) {
				totalDaysLabel.setText("Total Days: Invalid dates");
				totalDaysLabel.setForeground(Color.RED);
			} else {
				totalDaysLabel.setText("Total Days: " + diffInDays);
				totalDaysLabel.setForeground(Color.YELLOW);
				calculateTotalAmount();
			}
		} catch (Exception e) {
			totalDaysLabel.setText("Total Days: Invalid format");
			totalDaysLabel.setForeground(Color.RED);
		}
	}

	private void calculateTotalAmount() {
		try {
			String selectedRoom = c1.getSelectedItem();
			if (selectedRoom != null && selectedRoom.contains("$")) {
				String priceStr = selectedRoom.substring(selectedRoom.lastIndexOf("$") + 1, selectedRoom.lastIndexOf(")"));
				double pricePerNight = Double.parseDouble(priceStr);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date checkIn = sdf.parse(t5.getText());
				Date checkOut = sdf.parse(t8.getText());
				
				long diffInMillies = checkOut.getTime() - checkIn.getTime();
				long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
				
				if (diffInDays > 0) {
					double totalAmount = pricePerNight * diffInDays;
					totalAmountLabel.setText("Total Amount: $" + String.format("%.2f", totalAmount));
					t6.setText(String.format("%.2f", totalAmount));
				}
			}
		} catch (Exception e) {
			totalAmountLabel.setText("Total Amount: Error");
		}
	}

	private void addCustomer(String guests) {
		String idType = (String) comboBox.getSelectedItem();
		String number = t1.getText();
		String name = t2.getText();
		String gender = r1.isSelected() ? "Male" : r2.isSelected() ? "Female" : "";
		String country = t3.getText();
		String phone = t7.getText();
		String checkIn = t5.getText();
		String checkOut = t8.getText();
		String room = c1.getSelectedItem();
		String deposit = t6.getText();

		// Validation
		if (number.isEmpty() || name.isEmpty() || gender.isEmpty() || country.isEmpty() || 
			phone.isEmpty() || checkIn.isEmpty() || checkOut.isEmpty() || deposit.isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields must be filled!", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Date validation
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date checkInDate = sdf.parse(checkIn);
			Date checkOutDate = sdf.parse(checkOut);
			
			if (checkOutDate.before(checkInDate) || checkOutDate.equals(checkInDate)) {
				JOptionPane.showMessageDialog(this, "Check-out date must be after check-in date!", "Validation Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Invalid date format! Use YYYY-MM-DD", "Validation Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Extract room number from selection
		String roomNumber = room.substring(0, room.indexOf(" "));

		try (Connection conn = DBConnection.getConnection()) {
			// Check if room is still available
			String checkQuery = "SELECT availability FROM room WHERE room_number = ?";
			PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
			checkStmt.setString(1, roomNumber);
			ResultSet checkRs = checkStmt.executeQuery();
			
			if (checkRs.next() && !"Available".equals(checkRs.getString("availability"))) {
				JOptionPane.showMessageDialog(this, "Room " + roomNumber + " is no longer available!", "Room Unavailable", JOptionPane.WARNING_MESSAGE);
				loadRoomNumbers(); // Refresh room list
				return;
			}

			// Insert customer
			String insert = "INSERT INTO customer (id_type, number, name, gender, country, phone, " +
						   "check_in_date, check_out_date, guests, room_number, status, deposit) " +
						   "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Checked In', ?)";
			PreparedStatement ps = conn.prepareStatement(insert);
			ps.setString(1, idType);
			ps.setString(2, number);
			ps.setString(3, name);
			ps.setString(4, gender);
			ps.setString(5, country);
			ps.setString(6, phone);
			ps.setString(7, checkIn);
			ps.setString(8, checkOut);
			ps.setString(9, guests);
			ps.setString(10, roomNumber);
			ps.setString(11, deposit);
			
			ps.executeUpdate();

			// Update room availability
			String updateRoom = "UPDATE room SET availability = 'Occupied' WHERE room_number = ?";
			PreparedStatement updateStmt = conn.prepareStatement(updateRoom);
			updateStmt.setString(1, roomNumber);
			updateStmt.executeUpdate();

			JOptionPane.showMessageDialog(this, 
				"Customer added successfully!\nRoom " + roomNumber + " has been allocated.", 
				"Success", JOptionPane.INFORMATION_MESSAGE);

			// Clear form
			clearForm();
			loadRoomNumbers();

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error adding customer: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void clearForm() {
		t1.setText("");
		t2.setText("");
		t3.setText("");
		t7.setText("");
		t6.setText("");
		r1.setSelected(false);
		r2.setSelected(false);
		comboBox.setSelectedIndex(0);
		
		// Reset dates
		t5.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		t8.setText(new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()));
		
		calculateTotalDays();
	}
}
