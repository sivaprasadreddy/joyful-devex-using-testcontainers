create sequence product_id_seq start with 1 increment by 50;

create table products
(
    id          BIGINT        NOT NULL DEFAULT nextval('product_id_seq'),
    code        VARCHAR(255)  NOT NULL,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(255),
    price       NUMERIC(5, 2) NOT NULL,
    primary key (id),
    constraint product_code_unique unique (code)
);
