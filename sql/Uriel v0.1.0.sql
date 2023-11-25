-- Miscellaneous
DROP TYPE IF EXISTS bool;
CREATE TYPE bool AS ENUM ('TRUE', 'FALSE');

-- Users
DROP TYPE IF EXISTS gender;
CREATE TYPE gender AS ENUM ('MALE', 'FEMALE');

DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id                         SERIAL PRIMARY KEY,
    username                   VARCHAR(255),
    password                   VARCHAR(255),
    sso_ticket                 VARCHAR(255),
    email                      VARCHAR(255),
    is_email_verified          bool,
    rank_id                    INTEGER,
    account_creation_timestamp INTEGER,
    last_login_timestamp       INTEGER,
    last_online_timestamp      INTEGER,
    is_online                  bool,
    motto                      VARCHAR(255),
    look                       VARCHAR(255),
    gender                     gender,
    credits                    INTEGER,
    pixels                     INTEGER,
    points                     INTEGER,
    registration_ip            VARCHAR(255),
    current_ip                 VARCHAR(255),
    machine_id                 VARCHAR(255),
    home_room_id               INTEGER
);

DROP TABLE IF EXISTS user_subscriptions;
CREATE TABLE user_subscriptions
(
    id                SERIAL PRIMARY KEY,
    user_id           INTEGER,
    subscription_type VARCHAR(255),
    start_timestamp   INTEGER,
    end_timestamp     INTEGER,
    is_active         bool
);

DROP TABLE IF EXISTS user_effects;
CREATE TABLE user_effects
(
    id                SERIAL PRIMARY KEY,
    user_id           INTEGER,
    effect_id         INTEGER,
    start_timestamp   INTEGER,
    end_timestamp     INTEGER,
    amount            INTEGER,
    is_active         bool
);
