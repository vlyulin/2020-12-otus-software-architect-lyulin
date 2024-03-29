
DROP TABLE IF EXISTS COMMODITIES;
DROP TABLE IF EXISTS ORDERS;
DROP TABLE IF EXISTS USERS;

DROP SEQUENCE IF EXISTS orders_seq;
DROP SEQUENCE IF EXISTS commodities_seq;

CREATE SEQUENCE orders_seq
    MINVALUE 1
    START WITH 1
    INCREMENT BY 1;

CREATE SEQUENCE commodities_seq
     MINVALUE 1
     START WITH 1
     INCREMENT BY 1;

CREATE TABLE USERS (
   USER_ID SERIAL PRIMARY KEY NOT NULL, -- SERIAL
   LOGIN VARCHAR(30) NOT NULL,
   PASSWORD VARCHAR(150) NOT NULL,
   FIRSTNAME VARCHAR(255) NOT NULL,
   LASTNAME VARCHAR(255),
   EMAIL VARCHAR(255),
   CONSTRAINT login_unique UNIQUE (login)
);

CREATE TABLE ORDERS (
    ID SERIAL PRIMARY KEY NOT NULL, -- SERIAL
    OBJECT_VERSION_NUMBER INTEGER NOT NULL DEFAULT 1,
    IDEMPOTENCY_KEY UUID UNIQUE,
    SHIP_DATE DATE,
    STATUS VARCHAR(30),
    COMPLETE BOOLEAN,
    CREATED_BY int not null,
    CONSTRAINT fk_user_id
        FOREIGN KEY(CREATED_BY) REFERENCES USERS(USER_ID)
);

CREATE TABLE COMMODITIES (
   ID SERIAL PRIMARY KEY, -- SERIAL -- NOT NULL
   ORDER_ID BIGSERIAL, -- NOT NULL,
   QUANTITY SMALLINT,
   PRICE NUMERIC(8,2),
   CONSTRAINT fk_order_id
       FOREIGN KEY(ORDER_ID) REFERENCES ORDERS(ID)
);
