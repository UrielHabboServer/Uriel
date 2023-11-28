-- Users
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id                         INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    username                   VARCHAR(255) NOT NULL UNIQUE,
    password                   TEXT NOT NULL,
    sso_ticket                 VARCHAR(255) NOT NULL UNIQUE,
    email                      VARCHAR(255) NOT NULL UNIQUE,
    is_email_verified          BOOLEAN DEFAULT false NOT NULL NOT NULL,
    rank_id                    INT NOT NULL,
    account_creation_timestamp INT NOT NULL,
    last_login_timestamp       INT NOT NULL,
    last_online_timestamp      INT NOT NULL,
    is_online                  BOOLEAN DEFAULT false NOT NULL,
    motto                      VARCHAR(255) DEFAULT 'Running on Uriel' NOT NULL,
    look                       TEXT DEFAULT 'hr-115-42.hd-195-19.ch-3030-82.lg-275-1408.fa-1201.ca-1804-64' NOT NULL,
    gender                     ENUM('MALE', 'FEMALE') DEFAULT 'MALE' NOT NULL,
    credits                    INT DEFAULT 0 NOT NULL,
    pixels                     INT DEFAULT 0 NOT NULL,
    points                     INT DEFAULT 0 NOT NULL,
    registration_ip            VARCHAR(255) NOT NULL,
    current_ip                 VARCHAR(255) NOT NULL,
    machine_id                 VARCHAR(255) NOT NULL,
    home_room_id               INT DEFAULT 0 NOT NULL
);

DROP TABLE IF EXISTS user_subscriptions;
CREATE TABLE user_subscriptions
(
    id                INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    user_id           INT NOT NULL,
    subscription_type VARCHAR(255) DEFAULT 'HABBO_CLUB' NOT NULL,
    start_timestamp   INT NOT NULL,
    end_timestamp     INT NOT NULL,
    is_active         BOOLEAN DEFAULT false NOT NULL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

DROP TABLE IF EXISTS user_effects;
CREATE TABLE user_effects
(
    id                   INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    user_id              INT NOT NULL,
    effect_id            INT NOT NULL,
    duration             INT NOT NULL,
    quantity             INT NOT NULL,
    activation_timestamp INT NOT NULL,
    is_active            BOOLEAN DEFAULT false NOT NULL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

DROP TABLE IF EXISTS user_looks;
CREATE TABLE user_looks
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    slot_id INT NOT NULL,
    look    TEXT DEFAULT 'hr-115-42.hd-195-19.ch-3030-82.lg-275-1408.fa-1201.ca-1804-64' NOT NULL,
    gender  BOOLEAN DEFAULT false NOT NULL NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);