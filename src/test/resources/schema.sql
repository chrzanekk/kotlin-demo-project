CREATE TABLE if not exists customer
(
    id              serial primary key,
    first_name      varchar(50),
    last_name     varchar(50),
    personal_number varchar(50)
);

CREATE TABLE if not exists credit
(
    id           serial primary key,
    credit_name  varchar(50),
    customer_id  integer,
    credit_value numeric
);

INSERT INTO customer (first_name, last_name, personal_number)
values ('John', 'Doe', '808080'),
       ('Jane', 'Smith', '818181'),
       ('Bob', 'Johnson', '828282');

INSERT INTO credit (credit_name, customer_id, credit_value)
values ('FirstCreditName', (SELECT customer.id FROM customer WHERE customer.personal_number = '808080'), 10000),
        ('SecondCreditName', (SELECT customer.id FROM customer WHERE customer.personal_number = '818181'), 20000),
        ('ThirdCreditName', (SELECT customer.id FROM customer WHERE customer.personal_number = '828282'), 30000);