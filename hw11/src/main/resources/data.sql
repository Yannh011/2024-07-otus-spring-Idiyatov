insert into authors (full_name)
values ('Author_1'), ('Author_2'), ('Author_3');

insert into genres (name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books (title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres (book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);

insert into comments(text, book_id)
values ('CommentText_1', 1), ('CommentText_2', 1), ('CommentText_3', 2), ('CommentText_4', 2), ('CommentText_5', 3), ('CommentText_6', 3);

insert into users (username, password)
values ('user1', '$2a$12$01Il9bhsitb84WNelagD/eaDOcTTwwywTmEp19KXq4pqNLTkDqIGm'),
       ('user2', '$2a$12$/qZ2Pd5ICasx573gt5Keue3vDTxUitkXpiu8uWsHwGmskMeAJKHNm');