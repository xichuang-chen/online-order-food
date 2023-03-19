CREATE TABLE IF NOT EXISTS orders (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         order_id VARCHAR(255),
                         payment  VARCHAR(255),
                         create_time DATETIME
    );
INSERT INTO orders (order_id, payment, create_time)
VALUES ('10001', '50', '2023-03-18 19:30:00'),
       ('10002', '39.8', '2023-03-17 19:30:00'),
       ('10003', '42', '2023-03-19 19:30:00');
