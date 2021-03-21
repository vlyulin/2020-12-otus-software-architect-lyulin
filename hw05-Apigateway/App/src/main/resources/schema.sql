DROP TABLE IF EXISTS COMMENTS;
DROP TABLE IF EXISTS BOOKS;
DROP TABLE IF EXISTS AUTHORS;
DROP TABLE IF EXISTS PUBLISHING_HOUSES;
DROP TABLE IF EXISTS LOOKUP_VALUES;
DROP TABLE IF EXISTS AUTHORITIES;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS acl_entry;
DROP TABLE IF EXISTS acl_object_identity;
DROP TABLE IF EXISTS acl_class;
DROP TABLE IF EXISTS acl_sid;

CREATE TABLE USERS (
                       USER_ID bigserial not null primary key,
                       LOGIN VARCHAR(30) NOT NULL,
                       PASSWORD VARCHAR(150) NOT NULL,
                       FIRSTNAME VARCHAR(255) NOT NULL,
                       LASTNAME VARCHAR(255),
                       EMAIL VARCHAR(255),
                       CONSTRAINT login_unique UNIQUE (login)
);