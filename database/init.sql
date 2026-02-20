-- 1. Locations Table
CREATE TABLE IF NOT EXISTS locations (
    id UUID PRIMARY KEY, 
    name VARCHAR(255),
    type VARCHAR(50), -- Enum stored as String
    city VARCHAR(255)
);

-- 2. Products Table
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    name VARCHAR(255),
    min_temperature DOUBLE PRECISION,
    max_temperature DOUBLE PRECISION
);

-- 3. Storage Units Table
CREATE TABLE IF NOT EXISTS storage_units (
    id UUID PRIMARY KEY,
    location_id UUID REFERENCES locations(id),
    min_temperature DOUBLE PRECISION,
    max_temperature DOUBLE PRECISION,
    capacity INTEGER
);

-- 4. Demands Table
CREATE TABLE IF NOT EXISTS demands (
    id UUID PRIMARY KEY,
    location_id UUID REFERENCES locations(id),
    product_id UUID REFERENCES products(id),
    date VARCHAR(50),
    min_quantity INTEGER,
    max_quantity INTEGER
);

-- 5. Routes Table
CREATE TABLE IF NOT EXISTS routes (
    id UUID PRIMARY KEY,
    from_location_id UUID REFERENCES locations(id),
    to_location_id UUID REFERENCES locations(id),
    capacity INTEGER,
    min_shipment INTEGER
);