create table IF NOT EXISTS USERS
(
    USER_ID   BIGINT PRIMARY KEY AUTO_INCREMENT,
    E_MAIL    VARCHAR(255) NOT NULL,
    LOGIN     VARCHAR(255) NOT NULL,
    USER_NAME VARCHAR(255) NOT NULL,
    BIRTHDAY  DATE NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (user_id)
);


create table IF NOT EXISTS FILMS
(
    FILM_ID      BIGINT PRIMARY KEY AUTO_INCREMENT,
    NAME         VARCHAR(255) NOT NULL,
    DESCRIPTION  VARCHAR(255) NOT NULL,
    RELEASE_DATE DATE NOT NULL,
    DURATION     INT NOT NULL
);

create table IF NOT EXISTS GENRES
(
    GENRE_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    GENRE    VARCHAR(255) NOT NULL
);

create table IF NOT EXISTS MPA
(
    MPA_ID BIGINT PRIMARY KEY AUTO_INCREMENT,
    MPA    VARCHAR(255) NOT NULL
);

create table IF NOT EXISTS FILMS_MPA
(
    FILM_ID BIGINT NOT NULL,
    MPA_ID  BIGINT NOT NULL,
    constraint "FILMS_MPA_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "FILMS_MPA_MPA_MPA_ID_fk"
        foreign key (MPA_ID) references MPA
);

create table IF NOT EXISTS FILMS_GENRES
(
    FILM_ID  BIGINT NOT NULL,
    GENRE_ID BIGINT NOT NULL,
    constraint "FILMS_GENRES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS,
    constraint "FILMS_GENRES_GENRES_GENRE_ID_fk"
        foreign key (GENRE_ID) references GENRES
);

create table IF NOT EXISTS FRIENDS
(
    USER_ID   BIGINT NOT NULL,
    FRIEND_ID BIGINT NOT NULL,
    constraint "USER_FRIEND_USERS_USER_ID_USER_ID_fk"
        foreign key (USER_ID) references USERS (USER_ID),
    constraint "USER_FRIEND_USERS_FRIEND_ID_USER_ID_fk"
        foreign key (FRIEND_ID) references USERS (USER_ID)
);

create table IF NOT EXISTS USER_FILM_LIKES
(
    USER_ID  BIGINT NOT NULL,
    FILM_ID  BIGINT NOT NULL,
    constraint "USER_FILM_LIKES_USERS_USER_ID_fk"
        foreign key (USER_ID) references USERS(USER_ID),
    constraint "USER_FILM_LIKES_FILMS_FILM_ID_fk"
        foreign key (FILM_ID) references FILMS(FILM_ID)
);






