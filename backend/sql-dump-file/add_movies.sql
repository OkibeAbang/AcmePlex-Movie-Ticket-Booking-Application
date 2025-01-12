-- Add more theatres (unchanged)
INSERT INTO theatres (name, address, num_rows, max_seats_per_row) VALUES
                                                                      ('Chinook VIP', '6455 Macleod Trail SW', 8, 12),
                                                                      ('Sunridge Cinemas', '2555 32 Street NE', 10, 15),
                                                                      ('Westhills Cinema', '165 Stewart Green SW', 12, 14),
                                                                      ('Market Mall Movies', '3625 Shaganappi Trail NW', 9, 12);

-- Add more movies with premium dates before today
INSERT INTO movies (title, synopsis, age_rating, duration, release_date_public, release_date_premium, genre, poster_file_path, featured) VALUES
                                                                                                                                             ('Inception', 'A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.', 'PG', '02:28:00', '2024-12-20', '2023-12-01', 'Action, Sci-Fi, Thriller', 'inception_grid.jpg', TRUE),

                                                                                                                                             ('Despicable Me 3', 'Gru meets his long-lost twin brother Dru, who wants to team up for one last criminal heist.', 'G', '01:30:00', '2024-11-30', '2023-11-15', 'Animation, Comedy, Family', 'despicable_me_three_grid.jpg', TRUE),

                                                                                                                                             ('Dune: Part Two', 'Paul Atreides unites with Chani and the Fremen while seeking revenge against the conspirators who destroyed his family.', 'PG', '02:45:00', '2024-12-25', '2023-12-10', 'Sci-Fi, Adventure', 'dune_two_grid.jpg', TRUE),

                                                                                                                                             ('The Batman 2', 'Batman ventures into Gotham City''s underworld when a sadistic killer leaves behind a trail of cryptic clues.', 'PG', '02:35:00', '2024-12-31', '2023-12-15', 'Action, Crime, Drama', 'batman_two_grid.jpg', FALSE),

                                                                                                                                             ('Avatar 3', 'Jake Sully and Neytiri''s peaceful life is disrupted when an ancient threat resurfaces.', 'PG', '02:50:00', '2024-12-20', '2023-12-05', 'Action, Adventure, Fantasy', 'avatar_three_grid.jpg', TRUE),

                                                                                                                                             ('John Wick 5', 'John Wick uncovers a path to defeating The High Table, but must face a new enemy with powerful alliances.', 'R', '02:15:00', '2024-11-25', '2023-11-10', 'Action, Crime, Thriller', 'john_wick_five_grid.jpg', TRUE),

                                                                                                                                             ('Oppenheimer', 'The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.', 'R', '03:00:00', '2024-12-15', '2023-12-01', 'Biography, Drama, History', 'oppenheimer_grid.jpg', TRUE),

                                                                                                                                             ('Mission: Impossible 8', 'Ethan Hunt and his IMF team embark on their most dangerous mission yet.', 'PG', '02:25:00', '2024-12-10', '2023-11-25', 'Action, Adventure, Thriller', 'mission_impossible_eight_grid.jpg', TRUE);

-- Rest remains the same
INSERT INTO theatre_showing (theatre_id, movie_id)
SELECT t.id, m.id
FROM theatres t
         CROSS JOIN movies m
WHERE t.id IN (2, 3, 4, 5)
  AND m.id > 4;

INSERT INTO showtimes (_date, movie_id) VALUES
                                            -- Inception showtimes
                                            ('2024-12-24 14:30:00', 5),
                                            ('2024-12-24 17:30:00', 5),
                                            ('2024-12-24 20:30:00', 5),

                                            -- Despicable Me 3 showtimes
                                            ('2024-12-24 13:00:00', 6),
                                            ('2024-12-24 15:30:00', 6),
                                            ('2024-12-24 18:00:00', 6),

                                            -- Other movies
                                            ('2024-12-24 14:00:00', 7),
                                            ('2024-12-24 17:00:00', 7),
                                            ('2024-12-24 20:00:00', 7);

INSERT INTO seats (showtime_id, seat, is_reserved) VALUES
                                                       (7, 'A1', TRUE),
                                                       (7, 'A2', TRUE),
                                                       (8, 'B3', TRUE),
                                                       (9, 'C4', TRUE),
                                                       (10, 'D5', TRUE);

INSERT INTO news (movie_id, poster_url) VALUES
                                            (5, 'inception_hero.jpg'),
                                            (6, 'despicable_me_three_hero.jpg'),
                                            (7, 'dune_two_hero.jpg');