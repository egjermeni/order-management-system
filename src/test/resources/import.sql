INSERT INTO users (username, password, company, first_name, last_name) VALUES ('user1', 'pass1', 'company 1', 'first name 1', 'last name 1');
INSERT INTO users (username, password, company, first_name, last_name) VALUES ('user2', 'pass2', 'company 2', 'first name 2', 'last name 2');

INSERT INTO products (name, current_price, total_value, status) VALUES ('product 1', '60.50', 1000000.00, 'open');
INSERT INTO products (name, current_price, total_value, status) VALUES ('product 2', '44.20', 2000000.00, 'open');
INSERT INTO products (name, current_price, total_value, status) VALUES ('product 3', '10.80', 3000000.00, 'closed');
INSERT INTO products (name, current_price, total_value, status) VALUES ('product 4', '100.60', 5000000.00, 'open');
INSERT INTO products (name, current_price, total_value, status) VALUES ('product 5', '80.10', 4000000.00, 'suspended');

INSERT INTO orders (user_id, product_id, order_type, price, quantity) VALUES (1, 2, 'buy', 44.20, 1);
INSERT INTO orders (user_id, product_id, order_type, price, quantity) VALUES (1, 4, 'buy', 100.60, 1);
INSERT INTO orders (user_id, product_id, order_type, price, quantity) VALUES (2, 1, 'buy', 60.50, 1);
INSERT INTO orders (user_id, product_id, order_type, price, quantity) VALUES (2, 4, 'buy', 100.60, 1);
