-- Наполнение словаря c MPA-рейтингами
MERGE INTO dict_mpa_rating KEY (id)
    VALUES (1, 'G'),
           (2, 'PG'),
           (3, 'PG-13'),
           (4, 'R'),
           (5, 'NC-17');

-- Наполнение словаря со статусами заявок в друзья
MERGE INTO dict_friendship_status KEY (id)
    VALUES (1, 'Подтверждённая'),
           (2, 'Неподтверждённая');

-- Наполнение словаря с жанрами фильмов
MERGE INTO dict_genre KEY (id)
    VALUES (1, 'Комедия'),
           (2, 'Драма'),
           (3, 'Мультфильм'),
           (4, 'Триллер'),
           (5, 'Документальный'),
           (6, 'Боевик');
