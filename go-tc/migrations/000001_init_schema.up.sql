create table products
(
    id          bigserial     not null,
    code        varchar(255)  not null,
    name        varchar(255)  not null,
    description varchar(255),
    price       numeric(5, 2) not null,
    constraint product_code_unique unique (code)
);
