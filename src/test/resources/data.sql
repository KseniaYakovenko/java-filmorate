merge into GENRES (GENRE_ID, GENRE) values (1, 'TEST_Комедия');
merge into GENRES (GENRE_ID, GENRE) values (2, 'TEST_Драма');
merge into GENRES (GENRE_ID, GENRE) values (3, 'TEST_Мультфильм');
merge into GENRES (GENRE_ID, GENRE) values (4, 'TEST_Триллер');
merge into GENRES (GENRE_ID, GENRE) values (5, 'TEST_Документальный');
merge into GENRES (GENRE_ID, GENRE) values (6, 'TEST_Боевик');

merge into MPA (MPA_ID, MPA) values (1, 'TEST_G');
merge into MPA (MPA_ID, MPA) values (2, 'TEST_PG');
merge into MPA (MPA_ID, MPA) values (3, 'TEST_PG-13');
merge into MPA (MPA_ID, MPA) values (4, 'TEST_R');
merge into MPA (MPA_ID, MPA) values (5, 'TEST_NC-17');

INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION)
VALUES ('TEST_1_FILM_NAME', 'TEST_1_FILM_DESCRIPTION', '2001-01-01', 111),
       ('TEST_2_FILM_NAME', 'TEST_2_FILM_DESCRIPTION', '2002-02-02', 222);

INSERT INTO USERS (E_MAIL, LOGIN, USER_NAME, BIRTHDAY)
VALUES ('TEST_1_USER_E_MAIL', 'TEST_1_USER_LOGIN', 'TEST_1_USER_NAME', '2001-01-01'),
       ('TEST_2_USER_E_MAIL', 'TEST_2_USER_LOGIN', 'TEST_2_USER_NAME', '2002-02-02');