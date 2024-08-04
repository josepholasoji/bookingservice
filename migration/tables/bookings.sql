-- Active: 1722204061621@@127.0.0.1@3306@booking
-- create a table of user event booking
CREATE TABLE bookings(
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary Key',
    user_id int NOT NULL COMMENT 'User ID',
    event_id int NOT NULL COMMENT 'Event ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at'
) COMMENT 'User Event Booking Table';