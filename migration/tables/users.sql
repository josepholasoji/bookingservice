-- Active: 1722204061621@@127.0.0.1@3306@booking

-- create table user
CREATE TABLE users(  
    id int NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT 'Primary Key',
    name VARCHAR(100) NOT NULL COMMENT 'Name of the user',
    email VARCHAR(100) NOT NULL COMMENT 'Email of the user',
    password VARCHAR(200) NOT NULL COMMENT 'Password of the user',
    role ENUM('admin', 'user') DEFAULT 'user' COMMENT 'Role of the user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
    is_active BOOLEAN DEFAULT TRUE COMMENT 'Is the user active?'
) COMMENT 'User Table';

