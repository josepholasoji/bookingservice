-- Active: 1722204061621@@127.0.0.1@3306@booking
CREATE TABLE events(  
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary Key',
    name VARCHAR(100) NOT NULL COMMENT 'Name of the event',
    description TEXT NOT NULL COMMENT 'Description of the event',
    capacity INT NOT NULL COMMENT 'Capacity of the event',
    start_date DATE NOT NULL COMMENT 'Start date of the event',
    category ENUM('Concert', 'Conference', 'Game') NOT NULL COMMENT 'Category of the event',
    end_date DATE NOT NULL COMMENT 'End date of the event',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at'
) COMMENT 'Event Table';