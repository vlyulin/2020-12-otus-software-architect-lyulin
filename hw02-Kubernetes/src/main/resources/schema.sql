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

CREATE TABLE LOOKUP_VALUES(
    LOOKUP_TYPE VARCHAR(30),
    LANGUAGE CHAR(2),
    LOOKUP_CODE VARCHAR(30),
    MEANING VARCHAR(80),
    DESCRIPTION VARCHAR(240),
    ENABLED_FLAG CHAR(1),
    START_DATE_ACTIVE DATE,
    END_DATE_ACTIVE DATE,
    UNIQUE(LOOKUP_TYPE,LANGUAGE,LOOKUP_CODE)
);

CREATE TABLE AUTHORS(
    AUTHOR_ID bigserial not null primary key,
    NAME VARCHAR(255) NOT NULL,
    COUNTRY CHAR(2),
    SEX CHAR(1),
    DATE_OF_BIRTH DATE
);

CREATE TABLE PUBLISHING_HOUSES (
    PUBLISHING_HOUSE_ID bigserial not null primary key,
    NAME VARCHAR(255) NOT NULL,
    SETTLEMENT_YEAR SMALLINT
);

CREATE TABLE BOOKS (
    BOOK_ID bigserial not null primary key,
    NAME VARCHAR(255),
    GENRE VARCHAR(30) NOT NULL,
    AUTHOR_ID BIGINT,
    PUBLISHING_HOUSE_ID BIGINT,
    PUBLISHING_YEAR SMALLINT,
    PAGES SMALLINT,
    AGE_LIMIT VARCHAR(3),
    FOREIGN KEY(AUTHOR_ID) REFERENCES AUTHORS(AUTHOR_ID),
    FOREIGN KEY(PUBLISHING_HOUSE_ID) REFERENCES PUBLISHING_HOUSES(PUBLISHING_HOUSE_ID)
);

CREATE TABLE USERS (
    USER_ID bigserial not null primary key,
    LOGIN VARCHAR(30) NOT NULL,
    PASSWORD VARCHAR(150) NOT NULL,
    NAME VARCHAR(255) NOT NULL,
    CONSTRAINT login_unique UNIQUE (login)
);

CREATE TABLE AUTHORITIES (
    LOGIN VARCHAR(30) NOT NULL,
    AUTHORITY VARCHAR(50) NOT NULL,
    CONSTRAINT FK_AUTHORITIES_USERS FOREIGN KEY(LOGIN) REFERENCES USERS(LOGIN)
);
CREATE UNIQUE INDEX IX_AUTH_LOGIN ON AUTHORITIES (LOGIN,AUTHORITY);

CREATE TABLE COMMENTS (
    COMMENT_ID bigserial not null primary key,
    BOOK_ID BIGINT NOT NULL,
    COMMENT VARCHAR(4000),
    CREATED_BY BIGINT,
    CREATION_DATE DATE NOT NULL,
    LAST_UPDATED_BY BIGINT,
    LAST_UPDATE_DATE DATE
);

ALTER TABLE COMMENTS
ADD CONSTRAINT book_id_fk FOREIGN KEY (BOOK_ID) REFERENCES BOOKS (BOOK_ID);
ALTER TABLE COMMENTS
ADD CONSTRAINT created_by_fk FOREIGN KEY (CREATED_BY) REFERENCES USERS (USER_ID);
ALTER TABLE COMMENTS
ADD CONSTRAINT kast_updated_by_fk FOREIGN KEY (LAST_UPDATED_BY) REFERENCES USERS (USER_ID);

--
-- ACL Security
--
create table acl_sid(
	id bigserial not null primary key,
	principal boolean not null,
	sid varchar(100) not null,
	constraint unique_uk_1 unique(sid,principal)
);

create table acl_class(
	id bigserial not null primary key,
	class varchar(100) not null,
	constraint unique_uk_2 unique(class)
);

create table acl_object_identity(
	id bigserial primary key,
	object_id_class bigint not null,
	object_id_identity varchar(36), -- bigint not null, # bug
	parent_object bigint,
	owner_sid bigint,
	entries_inheriting boolean not null,
	constraint unique_uk_3 unique(object_id_class,object_id_identity),
	constraint foreign_fk_1 foreign key(parent_object)references acl_object_identity(id),
	constraint foreign_fk_2 foreign key(object_id_class)references acl_class(id),
	constraint foreign_fk_3 foreign key(owner_sid)references acl_sid(id)
);

create table acl_entry(
	id bigserial primary key,
	acl_object_identity bigint not null,
	ace_order int not null,
	sid bigint not null,
	mask integer not null,
	granting boolean not null,
	audit_success boolean not null,
	audit_failure boolean not null,
	constraint unique_uk_4 unique(acl_object_identity,ace_order),
	constraint foreign_fk_4 foreign key(acl_object_identity) references acl_object_identity(id),
	constraint foreign_fk_5 foreign key(sid) references acl_sid(id)
);
