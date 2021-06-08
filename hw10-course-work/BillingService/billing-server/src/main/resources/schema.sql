DROP TABLE IF EXISTS BILLING_ORDERS;

DROP SEQUENCE IF EXISTS billing_order_seq;

CREATE SEQUENCE billing_order_seq
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1;

CREATE TABLE BILLING_ORDERS (
                       ID SERIAL PRIMARY KEY NOT NULL,
                       ACCOUNT_ID INTEGER,
                       AMOUNT NUMERIC,
                       CURRENCY VARCHAR(3)
);
