-- 1. ROLES LOOKUP TABLE
-- Separating roles ensures data integrity (prevents typos like 'stuednt')
CREATE TABLE roles (
    role_name VARCHAR(50) PRIMARY KEY
);

INSERT INTO roles (role_name) VALUES ('student'), ('admin');


-- 2. STUDENTS TABLE (Core Entity)
-- Stores only unique student account details. Current points are calculated 
-- dynamically or updated via transactions to prevent data mismatch.
CREATE TABLE students (
    student_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL, -- For bcrypt hashes
    role VARCHAR(50) DEFAULT 'student',
    CONSTRAINT fk_student_role FOREIGN KEY (role) REFERENCES roles(role_name)
);


-- 3. REWARDS TABLE (Lookup/Catalog Entity)
-- Keeps track of available items and their fixed token costs.
CREATE TABLE rewards (
    reward_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    points_required INT NOT NULL CHECK (points_required > 0)
);


-- 4. BOTTLE DEPOSITS (Transaction Entity)
-- Tracks daily recycling history. This allows the system to easily look back 
-- 5 days to calculate and award the "5-day consecutive bonus".
CREATE TABLE bottle_deposits (
    deposit_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id INT NOT NULL,
    bottles_deposited INT NOT NULL CHECK (bottles_deposited > 0),
    points_earned INT NOT NULL CHECK (points_earned >= 0),
    deposit_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_deposit_student FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE
);


-- 5. REWARD CLAIMS (Transaction Entity)
-- Crucial for normalization! Tracks point expenditures when items are claimed.
CREATE TABLE reward_claims (
    claim_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    student_id INT NOT NULL,
    reward_id INT NOT NULL,
    points_spent INT NOT NULL CHECK (points_spent > 0),
    claim_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_claim_student FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    CONSTRAINT fk_claim_reward FOREIGN KEY (reward_id) REFERENCES rewards(reward_id)
);

INSERT INTO rewards (reward_id, name, points_required) VALUES
(1, 'Ballpen', 40),
(2, 'Folder / Envelope', 50),
(3, 'Correction Tape', 100),
(4, 'Sticky Notes', 100),
(5, 'Spiral Notebook', 150),
(6, 'Bond paper', 200),
(7, 'Yellow Pad', 200),
(8, 'Tumbler', 500),
(9, 'Handheld E fan', 500),
(10, 'SlingBag / Backpack', 1500),
(11, 'ELITE Org shirt', 2000),
(12, 'Black Shoes', 2500);

CREATE TABLE student_inventory (
    inventory_id SERIAL PRIMARY KEY,
    student_id INT UNIQUE NOT NULL REFERENCES students(student_id) ON DELETE CASCADE,
    current_points INT DEFAULT 0 CHECK (current_points >= 0)
);

