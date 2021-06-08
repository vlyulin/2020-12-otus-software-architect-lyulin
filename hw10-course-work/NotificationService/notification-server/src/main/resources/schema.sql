
DROP TABLE IF EXISTS NOTIFICATION_COMMODITIES;
DROP TABLE IF EXISTS NOTIFICATION_ORDERS;

CREATE TABLE NOTIFICATION_ORDERS (
                        ID SERIAL PRIMARY KEY NOT NULL,
                        OBJECT_VERSION_NUMBER INTEGER NOT NULL DEFAULT 1,
                        IDEMPOTENCY_KEY UUID UNIQUE,
                        SHIP_DATE DATE,
                        STATUS VARCHAR(30),
                        COMPLETE BOOLEAN,
                        CURRENCY VARCHAR(3),
                        CREATED_BY int not null
);

CREATE TABLE NOTIFICATION_COMMODITIES (
                             ID SERIAL PRIMARY KEY,
                             ORDER_ID BIGSERIAL,
                             QUANTITY SMALLINT,
                             PRICE NUMERIC(8,2),
                             CONSTRAINT fk_order_id
                                 FOREIGN KEY(ORDER_ID) REFERENCES NOTIFICATION_ORDERS(ID)
);
