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
