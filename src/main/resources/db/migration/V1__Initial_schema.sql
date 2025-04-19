USE ticketease;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role ENUM('ADMIN', 'SUPPORT') NOT NULL,
    reset_password_token VARCHAR(255) UNIQUE,
    reset_password_token_expiry DATETIME,
    created_at_date DATE DEFAULT (CURRENT_DATE) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE NOT NULL
    );

CREATE TABLE IF NOT EXISTS customers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    identifier_no VARCHAR(100) NOT NULL UNIQUE,
    type ENUM('COMPANY', 'INDIVIDUAL') NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(100),
    address VARCHAR(255),
    created_at_date DATE DEFAULT (CURRENT_DATE) NOT NULL,
    is_active BOOLEAN DEFAULT TRUE NOT NULL,
    CONSTRAINT chk_identifier_no CHECK (identifier_no REGEXP '^[A-Za-z0-9-]+$')
    );

CREATE TABLE IF NOT EXISTS tickets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL UNIQUE,
    status ENUM('BACKLOG', 'TO_DO', 'IN_PROGRESS', 'DONE', 'CANCELLED') NOT NULL,
    due_date DATE NOT NULL,
    assignee_id BIGINT NOT NULL,
    reporter_id BIGINT NOT NULL,
    request_by_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    created_at_date DATE DEFAULT (CURRENT_DATE) NOT NULL,
    FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (reporter_id) REFERENCES users(id) ON DELETE RESTRICT,
    FOREIGN KEY (request_by_id) REFERENCES customers(id) ON DELETE RESTRICT,
    CONSTRAINT chk_due_date CHECK (due_date >= created_at_date)
    ) ENGINE=InnoDB;

CREATE INDEX idx_tickets_assignee ON tickets(assignee_id);
CREATE INDEX idx_tickets_reporter ON tickets(reporter_id);
CREATE INDEX idx_tickets_request_by ON tickets(request_by_id);
CREATE INDEX idx_tickets_status ON tickets(status);

INSERT IGNORE INTO users (username, first_name, last_name, password, email, role) VALUES
('admin', 'Mann Joe', 'Wong', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'zet4cloud@gmail.com', 'ADMIN'),
('support1', 'Alice', 'Tan', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'W230026@e.ntu.edu.sg', 'SUPPORT'),
('support2', 'Ben', 'Lim', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'ben.lim@example.com', 'SUPPORT'),
('support3', 'Cheryl', 'Ng', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'cheryl.ng@example.com', 'SUPPORT'),
('support4', 'David', 'Lee', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'david.lee@example.com', 'SUPPORT'),
('support5', 'Eileen', 'Goh', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'eileen.goh@example.com', 'SUPPORT'),
('support6', 'Felix', 'Chua', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'felix.chua@example.com', 'SUPPORT'),
('support7', 'Grace', 'Teo', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'grace.teo@example.com', 'SUPPORT'),
('support8', 'Henry', 'Tan', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'henry.tan@example.com', 'SUPPORT'),
('support9', 'Ivy', 'Lau', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'ivy.lau@example.com', 'SUPPORT'),
('support10', 'Jason', 'Yeo', '$2a$12$nEm621PGpnKu4wgRnEML0udf.OSl4o/zl0C7gkQYZgS8ia7yw4SL2', 'jason.yeo@example.com', 'SUPPORT');

INSERT INTO customers (name, identifier_no, type, email, phone, address) VALUES
('AlphaTech Solutions', 'AT-2025-001', 'COMPANY', 'contact@alphatech.com', '+65 6123 4567', '123 Tech Park Avenue, Singapore'),
('BetaLogix Pte Ltd', 'BLX-9981', 'COMPANY', 'info@betalogix.sg', '+65 6345 6789', '456 Innovation Way, Singapore'),
('CyberNova Inc.', 'CN-333X', 'COMPANY', 'support@cybernova.com', '+65 6987 2345', '789 Cyber Lane, Singapore'),
('DeltaCore Systems', 'DCS-4512', 'COMPANY', 'sales@deltacore.io', '+65 6655 1122', '101 Core Street, Singapore'),
('EcoFusion Ltd.', 'EF-2024-B2', 'COMPANY', 'hello@ecofusion.com', '+65 6789 5566', '55 Green Hub, Singapore'),
('FusionByte Technologies', 'FB-888A', 'COMPANY', 'contact@fusionbyte.tech', '+65 6899 7733', '12 Silicon Drive, Singapore'),
('GlobalWare Corp.', 'GW-541X', 'COMPANY', 'enquiries@globalware.com', '+65 6111 0099', '888 Commerce Blvd, Singapore'),
('HexaDynamics', 'HD-1234', 'COMPANY', 'info@hexadynamics.com', '+65 6999 4433', '7 Dynamics Plaza, Singapore'),
('InnoVerse Solutions', 'IV-789B', 'COMPANY', 'hello@innoverse.co', '+65 6456 9876', '500 Innovation Tower, Singapore'),
('JadeCloud Networks', 'JC-009X', 'COMPANY', 'support@jadecloud.net', '+65 6234 1112', '300 Cloud Street, Singapore'),
('Marcus Lim', 'S1234567A', 'INDIVIDUAL', 'marcus.lim@gmail.com', '+65 9123 4567', '89 Maple Crescent, Singapore'),
('Serene Tan', 'T9876543B', 'INDIVIDUAL', 'serene.tan@yahoo.com', '+65 9765 4321', '22 Palm Grove, Singapore');

INSERT INTO tickets (code, status, due_date, assignee_id, reporter_id, request_by_id, title, description) VALUES
('TICK-2023-001', 'IN_PROGRESS', '2025-06-15', 2, 1, 1, 'Website Login Issues', 'Customers reporting inability to login to the portal, receiving 500 errors'),
('TICK-2023-002', 'TO_DO', '2025-06-20', 3, 1, 2, 'Payment Gateway Integration', 'Need to integrate new payment provider for Malaysian customers'),
('TICK-2023-003', 'DONE', '2025-06-10', 4, 2, 3, 'Database Migration', 'Completed migration from MySQL 5.7 to 8.0 for production environment'),
('TICK-2023-004', 'IN_PROGRESS', '2025-06-18', 5, 3, 4, 'Mobile App Crash', 'App crashes on iOS 16.4 when accessing profile section'),
('TICK-2023-005', 'BACKLOG', '2025-07-01', 6, 1, 5, 'UI Redesign', 'Redesign dashboard interface based on new brand guidelines'),
('TICK-2023-006', 'TO_DO', '2025-06-22', 7, 4, 6, 'API Rate Limiting', 'Implement rate limiting for public API endpoints'),
('TICK-2023-007', 'IN_PROGRESS', '2025-06-17', 8, 5, 7, 'Email Delivery Failures', 'Transactional emails not being delivered to Outlook addresses'),
('TICK-2023-008', 'DONE', '2025-06-05', 9, 6, 8, 'SSL Certificate Renewal', 'Renewed SSL certificate for production domains'),
('TICK-2023-009', 'CANCELLED', '2025-06-12', 10, 7, 9, 'Legacy System Decommission', 'Project cancelled due to compliance requirements'),
('TICK-2023-010', 'TO_DO', '2025-06-25', 2, 8, 10, 'Data Export Feature', 'Implement CSV/Excel export for customer transaction history'),
('TICK-2023-011', 'IN_PROGRESS', '2025-06-19', 3, 9, 11, 'Password Reset Issues', 'Individual user unable to reset password via email link'),
('TICK-2023-012', 'BACKLOG', '2025-07-05', 4, 10, 12, 'Two-Factor Authentication', 'Add 2FA support for all user accounts'),
('TICK-2023-013', 'DONE', '2025-06-08', 5, 1, 1, 'Server Maintenance', 'Completed scheduled maintenance for database servers'),
('TICK-2023-014', 'TO_DO', '2025-06-28', 6, 2, 2, 'Performance Optimization', 'Optimize slow queries reported in customer dashboard'),
('TICK-2023-015', 'IN_PROGRESS', '2025-06-16', 7, 3, 3, 'Third-Party API Integration', 'Integrate with new shipping provider API for logistics team');