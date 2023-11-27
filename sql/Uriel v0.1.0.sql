-- Users
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id                         INT AUTO_INCREMENT PRIMARY KEY,
    username                   VARCHAR(255),
    password                   VARCHAR(255),
    sso_ticket                 VARCHAR(255),
    email                      VARCHAR(255),
    is_email_verified          ENUM('TRUE', 'FALSE'),
    rank_id                    INT,
    account_creation_timestamp INT,
    last_login_timestamp       INT,
    last_online_timestamp      INT,
    is_online                  ENUM('TRUE', 'FALSE'),
    motto                      VARCHAR(255),
    look                       VARCHAR(255) DEFAULT 'hr-115-42.hd-195-19.ch-3030-82.lg-275-1408.fa-1201.ca-1804-64',
    gender                     ENUM('MALE', 'FEMALE'),
    credits                    INT,
    pixels                     INT,
    points                     INT,
    registration_ip            VARCHAR(255),
    current_ip                 VARCHAR(255),
    machine_id                 VARCHAR(255),
    home_room_id               INT
);

DROP TABLE IF EXISTS user_subscriptions;
CREATE TABLE user_subscriptions
(
    id                INT AUTO_INCREMENT PRIMARY KEY,
    user_id           INT,
    subscription_type VARCHAR(255),
    start_timestamp   INT,
    end_timestamp     INT,
    is_active         ENUM('TRUE', 'FALSE')
);

DROP TABLE IF EXISTS user_effects;
CREATE TABLE user_effects
(
    id                   INT AUTO_INCREMENT PRIMARY KEY,
    user_id              INT,
    effect_id            INT,
    duration             INT,
    quantity             INT,
    activation_timestamp INT,
    is_active            ENUM('TRUE', 'FALSE')
);