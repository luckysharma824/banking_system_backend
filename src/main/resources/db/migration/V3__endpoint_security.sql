-- Create endpoint_security table and populate with current security rules

-- Table creation
CREATE TABLE IF NOT EXISTS endpoint_security (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    url_pattern VARCHAR(255) NOT NULL,
    http_method VARCHAR(10),
    priority INT NOT NULL,
    permit_all BOOLEAN NOT NULL DEFAULT FALSE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS endpoint_security_roles (
    endpoint_security_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    FOREIGN KEY (endpoint_security_id) REFERENCES endpoint_security(id) ON DELETE CASCADE
);

-- Insert security rules based on existing configuration
-- Priority order matters: more specific rules should have lower priority numbers

-- Public endpoints
INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled) 
VALUES ('/auth/login', NULL, 1, TRUE, TRUE);

INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled) 
VALUES ('/users/roles', NULL, 2, TRUE, TRUE);

INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled) 
VALUES ('/**', 'OPTIONS', 3, TRUE, TRUE);

INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled)
VALUES ('/users/permissions', NULL, 4, TRUE, TRUE);

-- User management endpoints (ADMIN only)
INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled) 
VALUES ('/users', NULL, 10, FALSE, TRUE);

INSERT INTO endpoint_security_roles (endpoint_security_id, role)
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN');

INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled)
VALUES ('/users/create', NULL, 11, FALSE, TRUE);

INSERT INTO endpoint_security_roles (endpoint_security_id, role)
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN');

-- Customer create (ADMIN, CLERK, MANAGER)
INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled)
VALUES ('/customers/create', NULL, 20, FALSE, TRUE);

INSERT INTO endpoint_security_roles (endpoint_security_id, role)
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN'),
       (LAST_INSERT_ID(), 'ROLE_CLERK'),
       (LAST_INSERT_ID(), 'ROLE_MANAGER');

-- Customer search (ADMIN, CLERK, MANAGER, CASHIER)
INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled)
VALUES ('/customers/search', NULL, 21, FALSE, TRUE);

INSERT INTO endpoint_security_roles (endpoint_security_id, role)
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN'),
       (LAST_INSERT_ID(), 'ROLE_CLERK'),
       (LAST_INSERT_ID(), 'ROLE_MANAGER'),
       (LAST_INSERT_ID(), 'ROLE_CASHIER');

-- Account balance (CASHIER can view)
INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled)
VALUES ('/accounts/balance/**', NULL, 30, FALSE, TRUE);

INSERT INTO endpoint_security_roles (endpoint_security_id, role)
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN'),
       (LAST_INSERT_ID(), 'ROLE_CLERK'),
       (LAST_INSERT_ID(), 'ROLE_MANAGER'),
       (LAST_INSERT_ID(), 'ROLE_CASHIER');

-- Accounts GET (CASHIER can view)
INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled)
VALUES ('/accounts/**', 'GET', 31, FALSE, TRUE);

INSERT INTO endpoint_security_roles (endpoint_security_id, role)
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN'),
       (LAST_INSERT_ID(), 'ROLE_CLERK'),
       (LAST_INSERT_ID(), 'ROLE_MANAGER'),
       (LAST_INSERT_ID(), 'ROLE_CASHIER');

-- Accounts all methods (ADMIN, CLERK, MANAGER)
INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled)
VALUES ('/accounts/**', NULL, 32, FALSE, TRUE);

INSERT INTO endpoint_security_roles (endpoint_security_id, role)
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN'),
       (LAST_INSERT_ID(), 'ROLE_CLERK'),
       (LAST_INSERT_ID(), 'ROLE_MANAGER');

-- Transactions (ADMIN, CASHIER)
INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled) 
VALUES ('/transactions/**', NULL, 40, FALSE, TRUE);

INSERT INTO endpoint_security_roles (endpoint_security_id, role)
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN'),
       (LAST_INSERT_ID(), 'ROLE_CASHIER');

-- Endpoint Security management (ADMIN only)
INSERT INTO endpoint_security (url_pattern, http_method, priority, permit_all, enabled) 
VALUES ('/endpoint-security/**', NULL, 5, FALSE, TRUE);

INSERT INTO endpoint_security_roles (endpoint_security_id, role)
VALUES (LAST_INSERT_ID(), 'ROLE_ADMIN');
