INSERT INTO banks(id, title)
VALUES (1, 'Bank of America'),
       (2, 'Russian Bank'),
       (3, 'Sweden Bank');

INSERT INTO users(id, name, last_name, user_type, start_work, end_work)
VALUES (1, 'Maxim', 'Yarosh', 'C', null, null),
       (2, 'Kate', 'Yarmoshyk', 'C', null, null);

INSERT INTO accounts(id, number, cash, user_id, bank_id)
VALUES (5, '65781921192', 535.78,1,1),
       (6,'975402983', 15023.92, 1,2),
       (7, '803213320', 983.44,2,1),
       (8, '998208312', 68293.90, 2,2);