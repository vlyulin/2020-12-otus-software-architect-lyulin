
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

-- Все пароли 12345678
insert into users (user_id, login, password, name)
values (101, 'User01', '$2y$12$9QTfCGvcHysFN8Mx/TkG9ejN8rbLdJEX3VPwkAxfH3NqiKEoHnLGy', 'User 01');

insert into users (user_id, login, password, name)
values (102, 'User02', '$2y$12$9QTfCGvcHysFN8Mx/TkG9ejN8rbLdJEX3VPwkAxfH3NqiKEoHnLGy', 'User 02');

insert into users (user_id, login, password, name)
values (103, 'Admin', '$2y$12$9QTfCGvcHysFN8Mx/TkG9ejN8rbLdJEX3VPwkAxfH3NqiKEoHnLGy', 'Admin');

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
