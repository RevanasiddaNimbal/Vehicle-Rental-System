--Vehicle Table
CREATE TABLE vehicles (
    id VARCHAR(50) PRIMARY KEY,
    brand VARCHAR(100) NOT NULL,
    vehicle_type VARCHAR(30) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price_per_day DECIMAL(10,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    engine_capacity INTEGER,
    seating_capacity INTEGER,
    fuel_type VARCHAR(30),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE vehicles
    ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE;

ALTER TABLE vehicles
    ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP NULL;

CREATE INDEX IF NOT EXISTS idx_vehicles_active ON vehicles(active);

--Admin table
CREATE TABLE admins (
    id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE admins ADD COLUMN is_super_admin BOOLEAN DEFAULT FALSE;

 --Adding admins query
 INSERT INTO admins (username, email, password)
 VALUES ('Revanasidda Nimbal', 'admin123@gmail.com', 'password');

--vehicle owner table
CREATE TABLE vehicle_owners (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE
);
-- adding owner id column
ALTER TABLE vehicles
ADD COLUMN owner_id VARCHAR(20) NOT NULL;

-- mapping owner id with owner table
ALTER TABLE vehicles
ADD CONSTRAINT fk_vehicle_owner
FOREIGN KEY (owner_id)
REFERENCES vehicle_owners(id)
ON DELETE CASCADE;

-- Customer table
CREATE TABLE customers (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    driving_license_number VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
-- Rental table
CREATE TABLE rentals (
    id SERIAL PRIMARY KEY,
    customer_id INT NOT NULL,
    vehicle_id VARCHAR(50) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    pickup_time TIME NOT NULL,
    return_time TIME NOT NULL,
    days INT DEFAULT 0,
    base_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(10,2) NOT NULL,
    weekend_charge DECIMAL(10,2) DEFAULT 0,
    discount DECIMAL(10,2) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'BOOKED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE rentals ADD COLUMN security_deposit DOUBLE PRECISION DEFAULT 0;

-- penalties table
CREATE TABLE penalties (
    id VARCHAR(50) PRIMARY KEY,
    rental_id INT NOT NULL,
    vehicle_id VARCHAR(50) NOT NULL,
    customer_id VARCHAR(50) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    type VARCHAR(50) NOT NULL,
    reason VARCHAR(100) NOT NULL,
    issued_date DATE NOT NULL
);

--Cancellation record table
CREATE TABLE cancellations (
    id VARCHAR(50) PRIMARY KEY,
    customer_id VARCHAR(50) NOT NULL,
    owner_id VARCHAR(50) NOT NULL,
    rental_id INT NOT NULL,
    cancellation_time TIMESTAMP NOT NULL,
    reason TEXT,
    refund_amount DECIMAL(10,2) DEFAULT 0
);

ALTER TABLE cancellations
ADD COLUMN vehicle_id VARCHAR(50) NOT NULL;

-- wallet table
CREATE TABLE wallets (
    wallet_id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL UNIQUE,
    balance NUMERIC(15, 2) NOT NULL CHECK (balance >= 0)
);

--wallet credential table
CREATE TABLE wallet_credentials (
    wallet_id VARCHAR(50) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
);

--map with wallet and wallet credentials
ALTER TABLE wallet_credentials ADD CONSTRAINT fk_wallet
FOREIGN KEY (wallet_id) REFERENCES wallets(wallet_id)
ON DELETE CASCADE

-- otp table
CREATE TABLE otps (
    email VARCHAR(150) PRIMARY KEY,
    code VARCHAR(10) NOT NULL,
    expiry_time BIGINT NOT NULL
);

CREATE TABLE transactions (
    transaction_id VARCHAR(50) PRIMARY KEY,
    source_wallet_id VARCHAR(50) NOT NULL,
    destination_wallet_id VARCHAR(50) NOT NULL,
    amount NUMERIC(10, 2) NOT NULL,
    type VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL,
    reference_id VARCHAR(50),
    description TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
);
