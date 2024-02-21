
create database form_fusion;
use form_fusion;

CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role varchar(100) NOT NULL
);

INSERT INTO user (username, password, email)
VALUES
    ('john_doe', 'hashed_password', 'john@example.com'),
    ('alice_smith', 'hashed_password', 'alice@example.com')
;



CREATE TABLE form (
    form_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    description VARCHAR(255),
    link VARCHAR(255),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

INSERT INTO form (title, description, link, user_id)
VALUES
    ('Survey 1', 'General Survey', 'survey-1', 1),
    ('Feedback Form', 'Product Feedback', 'feedback-form', 2)
;


CREATE TABLE question (
    question_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(255),
    type VARCHAR(255),
    form_id BIGINT,
    FOREIGN KEY (form_id) REFERENCES form(form_id)
);

INSERT INTO question (text, type, options, form_id)
VALUES
    ('What is your favorite color?', 'text', NULL, 1),
    ('How satisfied are you with our product?', 'multiple-choice', '["Very Satisfied", "Satisfied", "Neutral", "Dissatisfied", "Very Dissatisfied"]', 2)

;

CREATE TABLE response (
    response_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    form_id BIGINT,
    user_id BIGINT,
    timestamp TIMESTAMP,
    FOREIGN KEY (form_id) REFERENCES form(form_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);

INSERT INTO response (form_id, user_id, timestamp)
VALUES
    (1, 2, '2022-01-01 12:00:00'),
    (2, 1, '2022-01-02 14:30:00')
;

CREATE TABLE answer (
    answer_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    response_id BIGINT,
    question_id BIGINT,
    answer VARCHAR(255),
    FOREIGN KEY (response_id) REFERENCES response(response_id),
    FOREIGN KEY (question_id) REFERENCES question(question_id)
);

INSERT INTO answer (response_id, question_id, answer)
VALUES
    (1, 1, 'Blue'),
    (2, 2, 'Very Satisfied')
;
