-- Enhanced Hotel Management System Database Setup
-- Run these queries in your MySQL database

-- 1. Create the login table with role-based authentication
CREATE TABLE IF NOT EXISTS login (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_user_role (username, role)
);

-- 2. Create enhanced room table with more details
CREATE TABLE IF NOT EXISTS room (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) UNIQUE NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    availability VARCHAR(20) DEFAULT 'Available',
    floor_number INT,
    amenities TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Create enhanced customer table with booking details (without foreign key for now)
CREATE TABLE IF NOT EXISTS customer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_type VARCHAR(50) NOT NULL,
    number VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10) NOT NULL,
    country VARCHAR(50) NOT NULL,
    phone VARCHAR(20),
    check_in_date DATE,
    check_out_date DATE,
    guests INT DEFAULT 1,
    room_number VARCHAR(10),
    status VARCHAR(20) DEFAULT 'Booked',
    deposit DECIMAL(10,2),
    special_requests TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Create employee table
CREATE TABLE IF NOT EXISTS employee (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    gender VARCHAR(10),
    job VARCHAR(50),
    salary DECIMAL(10,2),
    phone VARCHAR(20),
    email VARCHAR(100),
    hire_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Create department table
CREATE TABLE IF NOT EXISTS department (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    budget DECIMAL(12,2),
    manager VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Create drivers table
CREATE TABLE IF NOT EXISTS drivers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INT,
    gender VARCHAR(10),
    company VARCHAR(100),
    brand VARCHAR(50),
    available VARCHAR(10) DEFAULT 'Available',
    location VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 7. Insert sample data

-- Sample login users
INSERT INTO login (username, password, role) VALUES 
('admin', 'admin123', 'Admin'),
('staff1', 'staff123', 'Staff'),
('staff2', 'staff456', 'Staff'),
('customer1', 'customer123', 'Customer'),
('customer2', 'customer456', 'Customer');

-- Sample rooms
INSERT INTO room (room_number, room_type, price, availability, floor_number, amenities) VALUES 
('101', 'Standard', 100.00, 'Available', 1, 'WiFi, TV, AC'),
('102', 'Standard', 100.00, 'Available', 1, 'WiFi, TV, AC'),
('201', 'Deluxe', 150.00, 'Available', 2, 'WiFi, TV, AC, Mini Bar'),
('202', 'Deluxe', 150.00, 'Available', 2, 'WiFi, TV, AC, Mini Bar'),
('301', 'Suite', 250.00, 'Available', 3, 'WiFi, TV, AC, Mini Bar, Jacuzzi'),
('302', 'Suite', 250.00, 'Available', 3, 'WiFi, TV, AC, Mini Bar, Jacuzzi'),
('401', 'Presidential', 500.00, 'Available', 4, 'WiFi, TV, AC, Mini Bar, Jacuzzi, Butler Service'),
('402', 'Presidential', 500.00, 'Available', 4, 'WiFi, TV, AC, Mini Bar, Jacuzzi, Butler Service');

-- Sample employees
INSERT INTO employee (name, age, gender, job, salary, phone, email, hire_date) VALUES 
('John Smith', 35, 'Male', 'Manager', 5000.00, '555-0101', 'john@hotel.com', '2020-01-15'),
('Sarah Johnson', 28, 'Female', 'Receptionist', 2500.00, '555-0102', 'sarah@hotel.com', '2021-03-20'),
('Mike Wilson', 42, 'Male', 'Maintenance', 3000.00, '555-0103', 'mike@hotel.com', '2019-11-10'),
('Lisa Brown', 31, 'Female', 'Housekeeping', 2200.00, '555-0104', 'lisa@hotel.com', '2021-06-05');

-- Sample departments
INSERT INTO department (name, budget, manager) VALUES 
('Front Office', 50000.00, 'John Smith'),
('Housekeeping', 30000.00, 'Lisa Brown'),
('Maintenance', 25000.00, 'Mike Wilson'),
('Food & Beverage', 40000.00, 'Sarah Johnson');

-- Sample drivers
INSERT INTO drivers (name, age, gender, company, brand, available, location) VALUES 
('David Lee', 45, 'Male', 'Hotel Shuttle', 'Toyota Camry', 'Available', 'Hotel Lobby'),
('Maria Garcia', 38, 'Female', 'Hotel Shuttle', 'Honda Accord', 'Available', 'Hotel Lobby'),
('Robert Chen', 52, 'Male', 'Luxury Transport', 'Mercedes S-Class', 'Available', 'Hotel Lobby');

-- Sample customers (optional - for testing)
INSERT INTO customer (id_type, number, name, gender, country, phone, check_in_date, check_out_date, guests, room_number, status, deposit) VALUES 
('Passport', 'A12345678', 'Alice Johnson', 'Female', 'USA', '555-0201', '2024-01-15', '2024-01-17', 2, '101', 'Checked In', 200.00),
('Aadhar Card', '123456789012', 'Bob Wilson', 'Male', 'India', '555-0202', '2024-01-16', '2024-01-18', 1, '201', 'Checked In', 300.00);

-- Update room availability for booked rooms
UPDATE room SET availability = 'Occupied' WHERE room_number IN ('101', '201');

-- Create indexes for better performance
CREATE INDEX idx_room_availability ON room(availability);
CREATE INDEX idx_customer_status ON customer(status);
CREATE INDEX idx_customer_dates ON customer(check_in_date, check_out_date);
CREATE INDEX idx_login_role ON login(role);

-- Create views for common queries
CREATE VIEW available_rooms AS 
SELECT room_number, room_type, price, floor_number, amenities 
FROM room 
WHERE availability = 'Available';

CREATE VIEW current_bookings AS 
SELECT c.name, c.room_number, c.check_in_date, c.check_out_date, c.status, r.room_type, r.price
FROM customer c 
JOIN room r ON c.room_number = r.room_number 
WHERE c.status IN ('Checked In', 'Booked');

-- Create payments table
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    room_number VARCHAR(10) NOT NULL,
    room_type VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    tax DECIMAL(10,2) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    payment_date DATETIME NOT NULL,
    status VARCHAR(20) DEFAULT 'Completed',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create maintenance_requests table
CREATE TABLE IF NOT EXISTS maintenance_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL,
    issue_type VARCHAR(50) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending',
    requested_by VARCHAR(100) NOT NULL,
    request_date DATETIME NOT NULL,
    completion_date DATETIME NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create housekeeping_tasks table
CREATE TABLE IF NOT EXISTS housekeeping_tasks (
    id INT AUTO_INCREMENT PRIMARY KEY,
    room_number VARCHAR(10) NOT NULL,
    cleaning_type VARCHAR(50) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    assigned_to VARCHAR(100) NOT NULL,
    scheduled_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'Pending',
    special_instructions TEXT,
    notes TEXT,
    completion_date DATETIME NULL,
    created_date DATETIME NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample data for payments
INSERT INTO payments (customer_name, room_number, room_type, amount, tax, total_amount, payment_method, payment_date) VALUES
('John Smith', '101', 'Standard', 100.00, 10.00, 110.00, 'Credit Card', '2024-01-15 14:30:00'),
('Sarah Johnson', '205', 'Deluxe', 150.00, 15.00, 165.00, 'Debit Card', '2024-01-16 10:15:00'),
('Mike Wilson', '301', 'Suite', 250.00, 25.00, 275.00, 'Cash', '2024-01-17 16:45:00');

-- Insert sample data for maintenance_requests
INSERT INTO maintenance_requests (room_number, issue_type, priority, description, status, requested_by, request_date) VALUES
('101', 'Electrical', 'High', 'Light bulb needs replacement', 'Completed', 'Housekeeping Staff', '2024-01-15 09:00:00'),
('205', 'Plumbing', 'Urgent', 'Water leak in bathroom', 'In Progress', 'Guest', '2024-01-16 14:30:00'),
('301', 'HVAC', 'Medium', 'Air conditioning not working properly', 'Pending', 'Guest', '2024-01-17 11:20:00');

-- Insert sample data for housekeeping_tasks
INSERT INTO housekeeping_tasks (room_number, cleaning_type, priority, assigned_to, scheduled_date, status, special_instructions, created_date) VALUES
('101', 'Daily Cleaning', 'Medium', 'Maria Garcia', '2024-01-15', 'Completed', 'Extra attention to bathroom', '2024-01-15 08:00:00'),
('205', 'Deep Cleaning', 'High', 'Juan Rodriguez', '2024-01-16', 'In Progress', 'Guest requested early cleaning', '2024-01-16 09:00:00'),
('301', 'Check-out Cleaning', 'Urgent', 'Ana Martinez', '2024-01-17', 'Pending', 'Standard check-out cleaning', '2024-01-17 10:00:00');

-- Create additional views for reports
CREATE VIEW payment_summary AS
SELECT 
    DATE(payment_date) as payment_date,
    COUNT(*) as transactions,
    SUM(total_amount) as daily_revenue
FROM payments 
WHERE status = 'Completed'
GROUP BY DATE(payment_date)
ORDER BY payment_date DESC;

CREATE VIEW maintenance_summary AS
SELECT 
    issue_type,
    COUNT(*) as total_requests,
    SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) as completed_requests
FROM maintenance_requests
GROUP BY issue_type;

CREATE VIEW housekeeping_summary AS
SELECT 
    cleaning_type,
    COUNT(*) as total_tasks,
    SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) as completed_tasks
FROM housekeeping_tasks
GROUP BY cleaning_type;

-- Sample queries for testing

-- Check available rooms
-- SELECT * FROM available_rooms;

-- Check current bookings
-- SELECT * FROM current_bookings;

-- Check login users
-- SELECT username, role FROM login;

-- Check room statistics
-- SELECT room_type, COUNT(*) as total, SUM(CASE WHEN availability = 'Available' THEN 1 ELSE 0 END) as available FROM room GROUP BY room_type;

-- Check payment summary
-- SELECT * FROM payment_summary;

-- Check maintenance summary
-- SELECT * FROM maintenance_summary;

-- Create customer_feedback table
CREATE TABLE IF NOT EXISTS customer_feedback (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    room_number VARCHAR(10),
    email VARCHAR(100),
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    category VARCHAR(50) NOT NULL,
    feedback_text TEXT NOT NULL,
    staff_response TEXT,
    feedback_date DATETIME NOT NULL,
    response_date DATETIME NULL,
    status VARCHAR(20) DEFAULT 'New',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert sample feedback data
INSERT INTO customer_feedback (customer_name, room_number, email, rating, category, feedback_text, feedback_date, status) VALUES
('Alice Johnson', '101', 'alice@email.com', 5, 'Overall Experience', 'Excellent stay! The room was clean and staff was very friendly.', '2024-01-15 16:30:00', 'New'),
('Bob Wilson', '201', 'bob@email.com', 4, 'Service', 'Great service, but the WiFi was a bit slow. Otherwise perfect!', '2024-01-16 14:20:00', 'Responded'),
('Carol Davis', '301', 'carol@email.com', 3, 'Room Quality', 'Room was okay, but the bed was a bit hard for my preference.', '2024-01-17 10:15:00', 'New');

-- Create feedback summary view
CREATE VIEW feedback_summary AS
SELECT 
    category,
    COUNT(*) as total_feedback,
    AVG(rating) as avg_rating,
    SUM(CASE WHEN rating >= 4 THEN 1 ELSE 0 END) as satisfied_customers
FROM customer_feedback
GROUP BY category
ORDER BY avg_rating DESC;

-- Check housekeeping summary
-- SELECT * FROM housekeeping_summary;

-- Create inventory table
CREATE TABLE IF NOT EXISTS inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description TEXT,
    quantity DECIMAL(10,2) NOT NULL,
    unit VARCHAR(20) NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    reorder_level INT NOT NULL,
    max_level INT NOT NULL,
    supplier VARCHAR(100),
    status VARCHAR(20) DEFAULT 'In Stock',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create concierge_requests table
CREATE TABLE IF NOT EXISTS concierge_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    guest_name VARCHAR(100) NOT NULL,
    room_number VARCHAR(10),
    phone VARCHAR(20),
    service_type VARCHAR(50) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    preferred_date DATE NOT NULL,
    preferred_time TIME NOT NULL,
    request_details TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'New',
    staff_notes TEXT,
    request_date DATETIME NOT NULL,
    completion_date DATETIME NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create visitor_log table
CREATE TABLE IF NOT EXISTS visitor_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    visitor_name VARCHAR(100) NOT NULL,
    phone_number VARCHAR(20),
    visitor_type VARCHAR(50) NOT NULL,
    access_level VARCHAR(50) NOT NULL,
    entry_time DATETIME NOT NULL,
    expected_exit DATETIME NOT NULL,
    exit_time DATETIME NULL,
    purpose TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'Checked In',
    security_notes TEXT,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create security_alerts table
CREATE TABLE IF NOT EXISTS security_alerts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    alert_type VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'Active',
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_date DATETIME NULL
);

-- Insert sample inventory data
INSERT INTO inventory (item_name, category, description, quantity, unit, unit_price, reorder_level, max_level, supplier) VALUES
('Towels', 'Linens', 'Hotel quality towels', 500, 'Pieces', 15.00, 50, 1000, 'Textile Supplies Co.'),
('Shampoo', 'Toiletries', 'Luxury shampoo bottles', 200, 'Bottles', 2.50, 20, 500, 'Beauty Products Inc.'),
('Coffee', 'Food & Beverage', 'Premium coffee beans', 50, 'Kilos', 25.00, 10, 100, 'Coffee Distributors'),
('Cleaning Supplies', 'Cleaning Supplies', 'Multi-purpose cleaner', 100, 'Bottles', 8.00, 15, 200, 'CleanPro Supplies');

-- Insert sample concierge requests
INSERT INTO concierge_requests (guest_name, room_number, phone, service_type, priority, preferred_date, preferred_time, request_details, status) VALUES
('John Smith', '101', '555-0101', 'Restaurant Reservation', 'Medium', '2024-01-15', '19:00:00', 'Table for 2 at La Maison', 'Completed'),
('Sarah Johnson', '205', '555-0102', 'Transportation', 'High', '2024-01-16', '08:00:00', 'Airport pickup needed', 'In Progress'),
('Mike Wilson', '301', '555-0103', 'Tour Booking', 'Low', '2024-01-17', '10:00:00', 'City tour for 4 people', 'New');

-- Insert sample visitor log
INSERT INTO visitor_log (visitor_name, phone_number, visitor_type, access_level, entry_time, expected_exit, purpose, status) VALUES
('Alice Brown', '555-0201', 'Guest Visitor', 'Guest Room', '2024-01-15 14:00:00', '2024-01-15 18:00:00', 'Visiting friend in room 101', 'Checked Out'),
('Bob Davis', '555-0202', 'Service Provider', 'Public Areas', '2024-01-16 09:00:00', '2024-01-16 17:00:00', 'Maintenance work', 'Checked In'),
('Carol Evans', '555-0203', 'Delivery Person', 'Lobby Only', '2024-01-17 11:00:00', '2024-01-17 11:30:00', 'Package delivery', 'Checked Out');

-- Insert sample security alerts
INSERT INTO security_alerts (alert_type, description, status) VALUES
('Suspicious Activity', 'Unusual behavior in lobby area', 'Active'),
('Unauthorized Access', 'Attempted access to restricted area', 'Resolved'),
('Overdue Visitor', 'Visitor exceeded expected exit time', 'Active');

-- Create additional views for new systems
CREATE VIEW inventory_summary AS
SELECT 
    category,
    COUNT(*) as total_items,
    SUM(quantity * unit_price) as total_value,
    SUM(CASE WHEN quantity <= reorder_level THEN 1 ELSE 0 END) as low_stock_items
FROM inventory
GROUP BY category;

CREATE VIEW concierge_summary AS
SELECT 
    service_type,
    COUNT(*) as total_requests,
    SUM(CASE WHEN status = 'Completed' THEN 1 ELSE 0 END) as completed_requests
FROM concierge_requests
GROUP BY service_type;

CREATE VIEW visitor_summary AS
SELECT 
    visitor_type,
    COUNT(*) as total_visitors,
    SUM(CASE WHEN status = 'Checked In' THEN 1 ELSE 0 END) as currently_inside
FROM visitor_log
WHERE DATE(entry_time) = CURDATE()
GROUP BY visitor_type;

-- Check feedback summary
-- SELECT * FROM feedback_summary;

-- Check inventory summary
-- SELECT * FROM inventory_summary;

-- Check concierge summary
-- SELECT * FROM concierge_summary;

-- Check visitor summary
-- SELECT * FROM visitor_summary; 

-- Food Orders Table for FoodOrderSystem
CREATE TABLE IF NOT EXISTS food_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(100),
    room_number VARCHAR(10),
    item_name VARCHAR(100) NOT NULL,
    quantity INT DEFAULT 1,
    price DECIMAL(10,2) NOT NULL,
    order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'Ordered'
); 

-- Table to track order status history for food orders
CREATE TABLE IF NOT EXISTS food_order_status_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    changed_by VARCHAR(100),
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES food_orders(id)
); 

-- Table for staff/admin users for authentication
CREATE TABLE IF NOT EXISTS staff_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'Staff',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert a default admin user (password: admin123, hash for demo only)
INSERT IGNORE INTO staff_users (username, password_hash, role) VALUES ('admin', 'admin123', 'Admin'); 