create sequence promotion_id_seq start with 1 increment by 50;

create table promotions
(
    id         bigint DEFAULT nextval('promotion_id_seq') not null,
    product_id bigint                                   not null,
    discount   numeric(5, 2)                            not null,
    primary key (id),
    constraint product_id_unique unique (product_id)
);