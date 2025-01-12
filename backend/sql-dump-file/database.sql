DROP DATABASE IF EXISTS schema1;

CREATE DATABASE schema1;

USE schema1;

-- THEATRE TABLE
CREATE TABLE
    theatres (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL UNIQUE,
        address VARCHAR(255),
        num_rows INT,
        max_seats_per_row INT
    );

INSERT INTO
    theatres (name, address, num_rows, max_seats_per_row)
VALUE
    ('Calgary Cinema', '1234 Example Street SW', 10, 10);

-- MOVIES TABLE
CREATE TABLE
    movies (
        id INT AUTO_INCREMENT PRIMARY KEY,
        title VARCHAR(255) NOT NULL UNIQUE,
        synopsis VARCHAR(500) NOT NULL,
        duration VARCHAR(8) NOT NULL, -- Format : HH-MM-SS (Changed from length to duration because length is a built-in keyword)
        age_rating VARCHAR(255) NOT NULL,
        release_date_public DATE NOT NULL,
        release_date_premium DATE NOT NULL,
        genre VARCHAR(255) NOT NULL,
        poster_file_path VARCHAR(255) NOT NULL,
        featured BOOL NOT NULL
        -- Date Format: YYYY-MM-DD
    );

INSERT INTO
    movies (title, synopsis, age_rating, duration, release_date_public, release_date_premium, genre, poster_file_path, featured)
VALUES -- Changed from pg_rating to age_rating && changed their values to reflect the program
    ('Tenet', 'Armed with only one word - Tenet - and fighting for the survival of the entire world, the Protagonist journeys through a twilight world of international espionage on a mission that will unfold in something beyond real time.', 'PG', '02:30:00', '2024-12-14', '2024-11-30',  'Action, Thriller, Science Fiction', 'tenet_grid.jpg', TRUE),
    ('Sonic the Hedgehog 3', 'Sonic, Knuckles, and Tails reunite against a powerful new adversary, Shadow, a mysterious villain with powers unlike anything they have faced before. With their abilities outmatched in every way, Team Sonic must seek out an unlikely alliance in hopes of stopping Shadow and protecting the planet.', 'PG', '01:49:00', '2024-11-01', '2024-10-19', 'Adventure, Comedy, Family, Science Fiction', 'sonic_the_hedgehog_three_grid.jpg', TRUE),
    ('Iron Man 3', 'When Tony Stark''s world is torn apart by a formidable terrorist called the Mandarin, he starts an odyssey of rebuilding and retribution.', 'PG', '02:10:00', '2013-05-03', '2013-04-20', 'Action, Adventure, Science Fiction', 'iron_man_three_grid.jpg', TRUE),
    ('Gladiator II', 'Years after witnessing the death of the revered hero Maximus at the hands of his uncle, Lucius is forced to enter the Colosseum after his home is conquered by the tyrannical Emperors who now lead Rome with an iron fist. With rage in his heart and the future of the Empire at stake, Lucius must look to his past to find strength and honor to return the glory of Rome to its people.', 'R', '02:28:00', '2024-12-15', '2024-12-01', 'Action, Adventure', 'gladiator_two_grid.jpg', TRUE);

CREATE TABLE
    theatre_showing (
        theatre_id INT NOT NULL,
        movie_id INT NOT NULL,
        PRIMARY KEY (theatre_id, movie_id),
        FOREIGN KEY (theatre_id) REFERENCES theatres (id),
        FOREIGN KEY (movie_id) REFERENCES movies (id)
    );

INSERT INTO
    theatre_showing (theatre_id, movie_id)
VALUES
    (1, 1),
    (1, 2),
    (1, 3);

-- SHOWTIMES TABLE
CREATE TABLE
    showtimes (
        id INT AUTO_INCREMENT PRIMARY KEY,
        _date DATETIME NOT NULL,
        movie_id INT NOT NULL,
        FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE
    );

INSERT INTO
    showtimes (_date, movie_id)
VALUES
    ('2024-11-24 19:30:00', 2), -- id == 1, 'Tenet'
    ('2024-12-24 19:30:00', 1), -- id == 2, 'Sonic'
    ('2024-12-24 17:17:17', 1),
    ('2024-12-24 19:30:00', 2),
    ('2024-12-03 07:30:00', 3),
    ('2024-12-03 09:30:00', 3);

-- SEATS table
CREATE TABLE
    seats (
        id INT AUTO_INCREMENT PRIMARY KEY,
        showtime_id INT NOT NULL,
        seat VARCHAR(255) NOT NULL,
        is_reserved BOOLEAN NOT NULL DEFAULT FALSE,
        FOREIGN KEY (showtime_id) REFERENCES showtimes (id) ON DELETE CASCADE
    );

INSERT INTO
    seats (showtime_id, seat, is_reserved)
VALUES
    (1, 'A2', TRUE),
    (1, 'D5', TRUE),
    (2, 'E1', TRUE),
    (2, 'L8', TRUE);

-- REGISTERED USERS table
CREATE TABLE
    users (
        id INT AUTO_INCREMENT PRIMARY KEY,
        _email VARCHAR(255) NOT NULL UNIQUE,
        _password VARCHAR(255) NOT NULL,
        fname VARCHAR(255) NOT NULL,
        lname VARCHAR(255) NOT NULL,
        credit FLOAT(6, 2) NOT NULL,
        premium BOOLEAN NOT NULL DEFAULT FALSE, -- false == ordinary user, true == registerd users
        premium_sign_up_date DATE
        -- Date Format: YYYY-MM-DD
    );

INSERT INTO
    users (_email, _password, fname, lname, credit, premium, premium_sign_up_date)
VALUES
    (
        'meyer2842@gmail.com',
        'PASSWORD1',
        'FNAME1',
        'LNAME1',
        1.20,
        True,
        '2024-12-01'
    ),
    (
        'test1@gmail.com',
        'PASSWORD2',
        'FNAME2',
        'LNAME2',
        232.05,
        FALSE,
        null
    ),
    (
        'test2@gmail.com',
        'PASSWORD3',
        'FNAME1',
        'LNAME3',
        0.00,
        TRUE,
        '2022-11-22'
    );

-- TICKETS TABLE
CREATE TABLE
    tickets (
        id INT AUTO_INCREMENT PRIMARY KEY,
        state VARCHAR(255) NOT NULL,
        seat_id INT NOT NULL,
        showtime_id INT NOT NULL,
        movie_id INT NOT NULL,
        theatre_id INT NOT NULL,
        user_id INT NOT NULL,
        FOREIGN KEY (seat_id) REFERENCES seats (id) ON DELETE CASCADE,
        FOREIGN KEY (showtime_id) REFERENCES showtimes (id) ON DELETE CASCADE,
        FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE CASCADE,
        FOREIGN KEY (theatre_id) references theatres (id) ON DELETE CASCADE,
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

-- Below code throws error if attempt to add ticket with INVALID movie_id
INSERT INTO
    tickets (state, seat_id, showtime_id, movie_id, theatre_id, user_id)
VALUES
    ('Cancelled', 1, 1, 2, 1, 2), -- id == 1
    ('Reserved', 2, 1, 2, 1, 2), -- id == 2
    ('Reserved', 3, 2, 1, 1, 1),
    ('Used', 4, 2, 1, 1, 3);

-- PAYMENT INFO methods
CREATE TABLE
    payment_methods (
        id INT AUTO_INCREMENT PRIMARY KEY,
        card_holder_name VARCHAR(255) NOT NULL,
        card_num VARCHAR(255) NOT NULL,
        -- need to implement REGEX check before adding to database in broker layer
        exp_date DATE NOT NULL,
        sec_num INT NOT NULL,
        user_id INT NOT NULL,
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
    );

INSERT INTO
    payment_methods (card_holder_name, card_num, exp_date, user_id, sec_num)
VALUES
    ('John Doe', '0001-0001-0001-0001', '0001-01-01', 1, 111),
    ('Jim Ow', '0002-0002-0002-0002', '0002-02-02', 2, 222),
    ('Jane Foe', '0003-0003-0003-0003', '0003-03-03', 3, 333);

CREATE TABLE
    payments (
        id INT AUTO_INCREMENT PRIMARY KEY,
        payment_type VARCHAR(255) NOT NULL,
        subtotal FLOAT(6, 2) NOT NULL,
        credit_used FLOAT(6, 2),
        total FLOAT(6, 2),
        user_id INT,
        payment_method_id INT,
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
        FOREIGN KEY (payment_method_id) REFERENCES payment_methods (id) ON DELETE CASCADE
);

INSERT INTO
    payments (payment_type, subtotal, credit_used, total, user_id, payment_method_id)
VALUES
    ('Premium Registration', 50.00, 0.00, 20.00, 1, 1),
    ('Premium Registration', 50.00, 0.00, 20.00, 3, 3),
    ('Ticket Cancellation', 0.00, -10.00, 0.00, 1, 1),
    ('Ticket Purchase', 10.00, 10.00, 0.00, 1, 1),
    ('Ticket Purchase', 10.00, 10.00, 0.00, 2, 2),
    ('Ticket Purchase', 10.00, 4.40, 5.60, 3, 3);

CREATE TABLE news (
    id INT AUTO_INCREMENT PRIMARY KEY,
    movie_id INT,
    poster_url VARCHAR(255) NOT NULL,
    CONSTRAINT fk_movie FOREIGN KEY (movie_id) REFERENCES movies(id)
);

INSERT INTO
    news (movie_id, poster_url)
VALUES
    (4, 'gladiator_two_hero.jpg'),
    (null, 'test_hero.jpg');


    SELECT * FROM news;
