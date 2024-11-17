create sequence promotion_id_seq start with 1 increment by 50;

create table promotions
(
    id         BIGINT        NOT NULL DEFAULT nextval('promotion_id_seq'),
    product_id BIGINT        NOT NULL,
    discount   NUMERIC(5, 2) NOT NULL,
    primary key (id),
    constraint product_id_unique unique (product_id)
);