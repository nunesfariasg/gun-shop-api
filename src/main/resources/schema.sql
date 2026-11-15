-- 1. Create customers table
CREATE TABLE customers (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           cpf VARCHAR(11) UNIQUE NOT NULL,
                           birth_date DATE NOT NULL,
                           cr_number VARCHAR(50) NOT NULL
);

-- 2. Create weapons table
CREATE TABLE weapons (
                         id SERIAL PRIMARY KEY,
                         model VARCHAR(100) NOT NULL,
                         brand VARCHAR(100) NOT NULL,
                         caliber VARCHAR(50) NOT NULL,
                         type VARCHAR(50) NOT NULL, -- Ex: Pistol, Rifle
                         price NUMERIC(10,2) NOT NULL,
                         stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0)
);

-- 3. Create ammunitions table
CREATE TABLE ammunitions (
                             id SERIAL PRIMARY KEY,
                             brand VARCHAR(100) NOT NULL,
                             caliber VARCHAR(50) NOT NULL,
                             quantity_per_box INTEGER NOT NULL,
                             price NUMERIC(10,2) NOT NULL,
                             stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0)
);

-- 4. Create sales table
CREATE TABLE sales (
                       id SERIAL PRIMARY KEY,
                       customer_id INTEGER NOT NULL REFERENCES customers(id),
                       sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       total_amount NUMERIC(10,2) NOT NULL DEFAULT 0.00,
                       status VARCHAR(20) NOT NULL -- Ex: PENDING, APPROVED, CANCELED
);

-- 5. Create sale_items table
CREATE TABLE sale_items (
                            id SERIAL PRIMARY KEY,
                            sale_id INTEGER NOT NULL REFERENCES sales(id) ON DELETE CASCADE,
                            weapon_id INTEGER REFERENCES weapons(id),
                            ammunition_id INTEGER REFERENCES ammunitions(id),
                            quantity INTEGER NOT NULL CHECK (quantity > 0),
                            unit_price NUMERIC(10,2) NOT NULL,

    -- Crucial restriction: An item MUST be either a Weapon or Ammunition, never both and never neither.
                            CONSTRAINT chk_exclusive_product CHECK (
                                (weapon_id IS NOT NULL AND ammunition_id IS NULL) OR
                                (weapon_id IS NULL AND ammunition_id IS NOT NULL)
                                )
);