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