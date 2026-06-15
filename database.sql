-- Create database
CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

-- Table: admins (for authentication)
CREATE TABLE IF NOT EXISTS admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table: products (for CRUD operations)
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL CHECK (price > 0),
    quantity INT DEFAULT 0 CHECK (quantity >= 0),
    category VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert sample admin (email: admin@example.com  /  password: admin123)
INSERT INTO admins (first_name, last_name, email, password)
VALUES ('Admin', 'User', 'admin@example.com', '$2y$10$1RmkgR2au5asHOVj/G/70.R/WkS8D0YR4hQCXuHBs7KJBdDfkTGrS');

-- Insert sample products
INSERT INTO products (name, description, price, quantity, category) VALUES
('Laptop Pro', 'High-performance laptop with 16GB RAM', 999.99, 10, 'Electronics'),
('Wireless Mouse', 'Ergonomic wireless mouse', 25.99, 50, 'Electronics'),
('Cotton T-Shirt', '100% premium cotton t-shirt', 19.99, 100, 'Clothing'),
('Programming Book', 'Learn PHP & MySQL', 39.99, 30, 'Books'),
('Coffee Mug', 'Ceramic coffee mug with lid', 12.99, 75, 'Home');

-- Verify data
SELECT * FROM admins;
SELECT * FROM products;
