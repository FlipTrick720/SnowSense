-- Ski Resorts Table
CREATE TABLE IF NOT EXISTS ski_resort (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    latitude DECIMAL(10, 7) NOT NULL,
    longitude DECIMAL(10, 7) NOT NULL,
    elevation INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Weather Data Table
CREATE TABLE IF NOT EXISTS weather_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ski_resort_id BIGINT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    temperature DECIMAL(5, 2),
    wind_speed DECIMAL(5, 2),
    wind_direction INT,
    precipitation DECIMAL(8, 2),
    snowfall DECIMAL(8, 2),
    snow_depth DECIMAL(8, 2),
    cloud_cover INT,
    visibility DECIMAL(10, 2),
    freezing_level INT,
    weather_code INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ski_resort_id) REFERENCES ski_resort(id)
);

CREATE INDEX idx_weather_resort_time ON weather_data(ski_resort_id, timestamp);

-- Avalanche Data Table
CREATE TABLE IF NOT EXISTS avalanche_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bulletin_id VARCHAR(255) UNIQUE NOT NULL,
    publication_time TIMESTAMP NOT NULL,
    valid_time_start TIMESTAMP NOT NULL,
    valid_time_end TIMESTAMP NOT NULL,
    unscheduled BOOLEAN,
    region_codes VARCHAR(1000),
    danger_level VARCHAR(20),
    danger_level_afternoon VARCHAR(20),
    elevation_lower VARCHAR(50),
    elevation_upper VARCHAR(50),
    aspects VARCHAR(100),
    problem_types VARCHAR(200),
    highlights VARCHAR(2000),
    comment VARCHAR(5000),
    avalanche_activity VARCHAR(5000),
    snowpack_structure VARCHAR(5000),
    travel_advisory VARCHAR(5000),
    tendency_type VARCHAR(20),
    wx_synopsis VARCHAR(2000),
    raw_data TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_avalanche_bulletin ON avalanche_data(bulletin_id);
CREATE INDEX idx_avalanche_valid_time ON avalanche_data(valid_time_start, valid_time_end);
CREATE INDEX idx_avalanche_danger ON avalanche_data(danger_level);
