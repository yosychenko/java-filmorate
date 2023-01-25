CREATE TABLE film
(
    id            INT          NOT NULL,
    mpa_rating_id INT          NOT NULL,
    name          text         NOT NULL,
    description   VARCHAR(200) NOT NULL,
    release_date  DATE         NOT NULL,
    duration      INT          NOT NULL,
    CONSTRAINT pk_film PRIMARY KEY (id)
);

CREATE TABLE user
(
    id       INT  NOT NULL,
    email    text NOT NULL,
    login    text NOT NULL,
    name     text NULL,
    birthday DATE NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE dict_genre
(
    id    INT  NOT NULL,
    genre text NOT NULL,
    CONSTRAINT pk_dict_genre PRIMARY KEY (id)
);

CREATE TABLE dict_mpa_rating
(
    id     INT  NOT NULL,
    rating text NOT NULL,
    CONSTRAINT pk_dict_mpa_rating PRIMARY KEY (id)
);

CREATE TABLE dict_friendship_status
(
    id     INT  NOT NULL,
    status text NOT NULL,
    CONSTRAINT pk_dict_friendship_status PRIMARY KEY (id)
);

CREATE TABLE mtm_film_genre
(
    film_id  INT NOT NULL,
    genre_id INT NOT NULL,
    CONSTRAINT pk_mtm_film_genre PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE mtm_user_user_friendship
(
    user_1_id            INT NOT NULL,
    user_2_id            INT NOT NULL,
    friendship_status_id INT NOT NULL,
    CONSTRAINT pk_mtm_user_user_friendship PRIMARY KEY (user_1_id, user_2_id)
);

CREATE TABLE mtm_user_film_likes
(
    user_id INT NOT NULL,
    film_id INT NOT NULL,
    CONSTRAINT pk_mtm_user_film_likes PRIMARY KEY (user_id, film_id)
);

ALTER TABLE film
    ADD CONSTRAINT fk_film_mpa_rating_id FOREIGN KEY (mpa_rating_id)
        REFERENCES dict_mpa_rating (id);

ALTER TABLE mtm_film_genre
    ADD CONSTRAINT fk_mtm_film_genre_film_id FOREIGN KEY (film_id)
        REFERENCES film (id);

ALTER TABLE mtm_film_genre
    ADD CONSTRAINT fk_mtm_film_genre_genre_id FOREIGN KEY (genre_id)
        REFERENCES dict_genre (id);

ALTER TABLE mtm_user_user_friendship
    ADD CONSTRAINT fk_mtm_user_user_friendship_user_1_id FOREIGN KEY (user_1_id)
        REFERENCES user (id);

ALTER TABLE mtm_user_user_friendship
    ADD CONSTRAINT fk_mtm_user_user_friendship_user_2_id FOREIGN KEY (user_2_id)
        REFERENCES user (id);

ALTER TABLE mtm_user_user_friendship
    ADD CONSTRAINT chk_user_ids_not_equal CHECK (user_1_id != user_2_id);

ALTER TABLE mtm_user_user_friendship
    ADD CONSTRAINT fk_mtm_user_user_friendship_friendship_status_id FOREIGN KEY (friendship_status_id)
        REFERENCES dict_friendship_status (id);

ALTER TABLE mtm_user_film_likes
    ADD CONSTRAINT fk_mtm_user_film_likes_user_id FOREIGN KEY (user_id)
        REFERENCES user (id);

ALTER TABLE mtm_user_film_likes
    ADD CONSTRAINT fk_mtm_user_film_likes_film_id FOREIGN KEY (film_id)
        REFERENCES film (id);
