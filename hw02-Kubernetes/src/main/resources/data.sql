
insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','RU','HARD_SCIENCE_FICTION','Твердая научная фантастика','Твердая научная фантастика','Y',DATE '0001-01-01',DATE '9999-12-31');
insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','US','HARD_SCIENCE_FICTION','Hard SF','Hard science fiction','Y',DATE '0001-01-01',DATE '9999-12-31');

insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','RU','SOCIAL_SCIENCE_FICTION','Социальная фантастика','Социальная фантастика','Y',DATE '0001-01-01',DATE '9999-12-31');
insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','US','SOCIAL_SCIENCE_FICTION','Social science fiction','Social science fiction','Y',DATE '0001-01-01',DATE '9999-12-31');

insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','RU','CHRONOFANTASTIC','Хронофантастика','Хронофантастика','Y',DATE '0001-01-01',DATE '9999-12-31');
insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','US','CHRONOFANTASTIC','Chronofantastic','Chronofantastic','Y',DATE '0001-01-01',DATE '9999-12-31');

insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','RU','APOCALYPTIC','Апокалиптика','Апокалиптика','Y',DATE '0001-01-01',DATE '9999-12-31');
insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','US','APOCALYPTIC','Apocalyptic','Apocalyptic','Y',DATE '0001-01-01',DATE '9999-12-31');

insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','RU','UTOPIA','Утопии и антиутопии','Утопии и антиутопии','Y',DATE '0001-01-01',DATE '9999-12-31');
insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','US','UTOPIA','Utopia and dystopia','Utopia and dystopia','Y',DATE '0001-01-01',DATE '9999-12-31');

insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','RU','SPACE_OPERA','Космическая опера','Космическая опера','Y',DATE '0001-01-01',DATE '9999-12-31');
insert into lookup_values (lookup_type, language, lookup_code, meaning, description, enabled_flag, start_date_active, end_date_active)
values ('GENRES','US','SPACE_OPERA','Space Opera','Space Opera','Y',DATE '0001-01-01',DATE '9999-12-31');

insert into publishing_houses (publishing_house_id, name, settlement_year)
values (1, 'ИК «Столица» (Изд. группа GELEOS Publishing House)', 2010);
insert into publishing_houses (publishing_house_id, name, settlement_year)
values (2, 'PH «Stolitca» (GELEOS Publishing House)', 1990);

insert into authors (author_id, name, country, sex, date_of_birth)
values (1, 'Дойл, Артур Конан', 'EN', 'M', DATE '1859-05-22');

insert into authors (author_id, name, country, sex, date_of_birth)
values (2, 'Азимов, Айзек', 'RU', 'M', DATE '1920-01-02');

insert into authors (author_id, name, country, sex, date_of_birth)
values (3, 'Гамильтон, Эдмонд', 'US', 'M', DATE '1904-10-21');

-- Книги
/*
insert into books (name, genre, author_id, publishing_house_id, publishing_year, pages, age_limit)
values ('В ядовитом поясе', 'HARD_SCIENCE_FICTION', 1, 1, 2010, 320, '0+'); -- 1

insert into books (name, genre, author_id, publishing_house_id, publishing_year, pages, age_limit)
values ('Конец Вечности', 'CHRONOFANTASTIC', 2, 2, 1955, 247, '12+'); -- 2

insert into books (name, genre, author_id, publishing_house_id, publishing_year, pages, age_limit)
values ('Звёздные короли', 'SPACE_OPERA', 3, 2, 1947, 150, '18+'); -- 3
*/

insert into books (name, genre, author_id, publishing_house_id, publishing_year, pages, age_limit)
select d.name, d.genre, d.author_id, d.publishing_house_id, d.publishing_year, d.pages, d.age_limit
from (
	select 'В ядовитом поясе'::text as name, 'HARD_SCIENCE_FICTION'::text as genre,
			au.author_id, ph.publishing_house_id, 2010 as publishing_year, 320 as pages, '0+'::text as age_limit
	from 	publishing_houses ph,
			authors au
	where 	ph."name" = 'ИК «Столица» (Изд. группа GELEOS Publishing House)'
			and au."name" = 'Дойл, Артур Конан'
	union all
	select  'Конец Вечности'::text as name, 'CHRONOFANTASTIC'::text as genre,
			au.author_id, ph.publishing_house_id, 1955 as publishing_year, 247 as pages, '12+'::text as age_limit
	from 	publishing_houses ph,
			authors au
	where 	ph."name" = 'PH «Stolitca» (GELEOS Publishing House)'
			and au."name" = 'Азимов, Айзек'
	union all
	select  'Звёздные короли'::text as name, 'SPACE_OPERA'::text as genre,
			au.author_id, ph.publishing_house_id, 1947 as publishing_year, 150 as pages, '18+'::text as age_limit
	from 	publishing_houses ph,
			authors au
	where 	ph."name" = 'PH «Stolitca» (GELEOS Publishing House)'
			and au."name" = 'Гамильтон, Эдмонд'
) d;

-- Все пароли 12345678
insert into users (user_id, login, password, name)
values (101, 'User01', '$2y$12$9QTfCGvcHysFN8Mx/TkG9ejN8rbLdJEX3VPwkAxfH3NqiKEoHnLGy', 'User 01');

insert into users (user_id, login, password, name)
values (102, 'User02', '$2y$12$9QTfCGvcHysFN8Mx/TkG9ejN8rbLdJEX3VPwkAxfH3NqiKEoHnLGy', 'User 02');

insert into users (user_id, login, password, name)
values (103, 'Admin', '$2y$12$9QTfCGvcHysFN8Mx/TkG9ejN8rbLdJEX3VPwkAxfH3NqiKEoHnLGy', 'Admin');

insert into authorities (login, authority)
values ('User01', 'USER');

-- User 02 имеет доста и к детским книгам ROLE_USER и к очень взрослым ROLE_USER18+
insert into authorities (login, authority)
values ('User02', 'USER');
insert into authorities (login, authority)
values ('User02', 'USER18+');

-- У администратора есть все роли
insert into authorities (login, authority)
values ('Admin', 'ADMIN');
insert into authorities (login, authority)
values ('Admin', 'USER');
insert into authorities (login, authority)
values ('Admin', 'USER18+');

/*
insert into comments (book_id, comment, created_by, creation_date, last_updated_by, last_update_date)
values( 1, 'Зачетная.', 101, now(), NULL, NULL);

insert into comments (book_id, comment, created_by, creation_date, last_updated_by, last_update_date)
values( 1, 'Не читал, но осуждаю.', 102, now(), NULL, NULL);

insert into comments (book_id, comment, created_by, creation_date, last_updated_by, last_update_date)
values( 2, 'Замечательная книга', 101, now(), NULL, NULL);

insert into comments (book_id, comment, created_by, creation_date, last_updated_by, last_update_date)
values( 2, 'Можно почитать', 102, now(), NULL, NULL);

insert into comments (book_id, comment, created_by, creation_date, last_updated_by, last_update_date)
values( 3, 'Нудятина какая-то.', 102, now(), 101, now());
*/

insert into comments (book_id, comment, created_by, creation_date, last_updated_by, last_update_date)
select d.book_id, d.comment, d.created_by, d.creation_date, d.last_updated_by, d.last_update_date
from (
	select  b.book_id, 'Зачетная.'::text as comment, u.user_id as created_by,
			now() as creation_date, null::integer as last_updated_by, null::date as last_update_date
	from	books b, users u
	where   b."name" = 'В ядовитом поясе' and u.login = 'User01'
	union all
	select  b.book_id, 'Не читал, но осуждаю.'::text as comment, u.user_id as created_by,
			now() as creation_date, null::integer as last_updated_by, null::date as last_update_date
	from	books b, users u
	where   b."name" = 'В ядовитом поясе' and u.login = 'User02'
	union all
	select  b.book_id, 'Не читал, но осуждаю.'::text as comment, u.user_id as created_by,
			now() as creation_date, null::integer as last_updated_by, null::date as last_update_date
	from	books b, users u
	where   b."name" = 'Замечательная книга' and u.login = 'User01'
	union all
	select  b.book_id, 'Замечательная книга'::text as comment, u.user_id as created_by,
			now() as creation_date, null::integer as last_updated_by, null::date as last_update_date
	from	books b, users u
	where   b."name" = 'Конец Вечности' and u.login = 'User01'
	union all
	select  b.book_id, 'Можно почитать'::text as comment, u.user_id as created_by,
			now() as creation_date, null::integer as last_updated_by, null::date as last_update_date
	from	books b, users u
	where   b."name" = 'Конец Вечности' and u.login = 'User02'
	union all
	select  b.book_id, 'Нудятина какая-то.'::text as comment, u.user_id as created_by,
			now() as creation_date, u2.user_id as last_updated_by, now() as last_update_date
	from	books b, users u, users u2
	where   b."name" = 'Звёздные короли' and u.login = 'User01' and u2.login = 'User02'
) d;

------------------------------------------------------------
-- ACL Security
-- Операторы вставки пока не переделел, не хватило времени
------------------------------------------------------------

-- Пользователи и роли

INSERT INTO acl_sid (principal, sid) VALUES
(true, 'Admin'), -- 1,
(true, 'User01'), -- 2,
(true, 'User02'), -- 3,
(false, 'ROLE_ADMIN'), -- 4,
(false, 'ROLE_USER'), -- 5,
(false, 'ROLE_USER18+'); -- 6,

-- Классы
INSERT INTO acl_class (class) VALUES
('ru.otus.spring.library.docker.models.Book'),
('ru.otus.spring.library.docker.models.Comment');

-- Книги
-- Владелец у всех Администратор (acl.id = 1)
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
-- id = 1, 'В ядовитом поясе'
(1, 1, NULL, 1, false),
-- id = 2, 'Конец Вечности'
(1, 2, NULL, 1, false),
-- id = 3, 'Звёздные короли'
(1, 3, NULL, 1, false);

-- Книги
INSERT INTO acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure) VALUES
-- Книга id = 1, 'В ядовитом поясе', доступна роли ROLE_USER на чтение 0 - read permission
-- granting = 1 - разрешающее
(1, 1, 5, 1, true, true, true),
-- id = 2, 'Конец Вечности', доступна роли ROLE_USER на чтение 0 бит - read permission
(2, 1, 5, 1, true, true, true),
-- id = 3, 'Звёздные короли', доступна роли ROLE_USER18+ на чтение 0 бит - read permission
(3, 1, 6, 1, true, true, true);

-- Комментарии
-- Владелец либо User01 (acl.id = 2), либо User02 (acl.id = 3)
-- object_id_identity - идентификатор комментария
INSERT INTO acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
-- id = 1, bookId = 1 parent_object = 1, 'Зачетная.', User01 = 2
(2, 1, 1, 2, true),
-- id = 2, bookId = 1 parent_object = 1, 'Не читал, но осуждаю.', User02 = 3
(2, 2, 1, 3, true),
-- id = 3, bookId = 2 parent_object = 2, 'Замечательная книга', User01 = 2
(2, 3, 2, 2, true),
-- id = 4, bookId = 2 parent_object = 2, 'Можно почитать', User02 = 3
(2, 4, 2, 3, true),
-- id = 5, bookId = 3 parent_object = 3, 'Нудятина какая-то.', User02 = 3
(2, 5, 3, 3, true);

