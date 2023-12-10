-- Currencies
DROP TABLE IF EXISTS currencies;
CREATE TABLE currencies
(
    id             INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    nitro_id       INT                            NOT NULL,
    is_seasonal    BOOLEAN DEFAULT false          NOT NULL,
    name           VARCHAR(255)                   NOT NULL,
    default_amount INT     DEFAULT 0              NOT NULL
);
INSERT INTO currencies (nitro_id, is_seasonal, name)
VALUES (-1, false, 'credit');
INSERT INTO currencies (nitro_id, is_seasonal, name)
VALUES (0, true, 'ducket');
INSERT INTO currencies (nitro_id, is_seasonal, name)
VALUES (5, true, 'diamond');

-- Users
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
    id                         INT AUTO_INCREMENT PRIMARY KEY                                                                  NOT NULL UNIQUE,
    username                   VARCHAR(255)                                                                                    NOT NULL UNIQUE,
    password                   TEXT                                                                                            NOT NULL,
    sso_ticket                 VARCHAR(255)                                                                                    NOT NULL UNIQUE,
    email                      VARCHAR(255)                                                                                    NOT NULL UNIQUE,
    is_email_verified          BOOLEAN                 DEFAULT false                                                           NOT NULL,
    rank_id                    INT                                                                                             NOT NULL,
    account_creation_timestamp LONG                                                                                            NOT NULL,
    last_login_timestamp       LONG                                                                                            NOT NULL,
    last_online_timestamp      LONG                                                                                            NOT NULL,
    is_online                  BOOLEAN                 DEFAULT false                                                           NOT NULL,
    motto                      VARCHAR(255)            DEFAULT 'Running on Uriel'                                              NOT NULL,
    look                       TEXT                    DEFAULT 'hr-115-42.hd-195-19.ch-3030-82.lg-275-1408.fa-1201.ca-1804-64' NOT NULL,
    gender                     ENUM ('MALE', 'FEMALE') DEFAULT 'MALE'                                                          NOT NULL,
    registration_ip            VARCHAR(255)                                                                                    NOT NULL,
    current_ip                 VARCHAR(255)                                                                                    NOT NULL,
    machine_id                 VARCHAR(255)                                                                                    NOT NULL,
    home_room_id               INT                     DEFAULT 0                                                               NOT NULL,
    FOREIGN KEY (rank_id) REFERENCES ranks (id),
    INDEX (sso_ticket, username)
);

DROP TABLE IF EXISTS user_currencies;
CREATE TABLE user_currencies
(
    id          INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    user_id     INT                            NOT NULL,
    currency_id INT                            NOT NULL,
    amount      INT                            NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (currency_id) REFERENCES currencies (id)
);

DROP TABLE IF EXISTS user_subscriptions;
CREATE TABLE user_subscriptions
(
    id                INT AUTO_INCREMENT PRIMARY KEY    NOT NULL UNIQUE,
    user_id           INT                               NOT NULL,
    subscription_type VARCHAR(255) DEFAULT 'HABBO_CLUB' NOT NULL,
    start_timestamp   LONG                              NOT NULL,
    end_timestamp     LONG                              NOT NULL,
    is_active         BOOLEAN      DEFAULT false        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

DROP TABLE IF EXISTS user_effects;
CREATE TABLE user_effects
(
    id                   INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    user_id              INT                            NOT NULL,
    effect_id            INT                            NOT NULL,
    duration             LONG                           NOT NULL,
    quantity             INT                            NOT NULL,
    activation_timestamp LONG                           NOT NULL,
    is_active            BOOLEAN DEFAULT false          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

DROP TABLE IF EXISTS user_looks;
CREATE TABLE user_looks
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT                                                                          NOT NULL,
    slot_id INT                                                                          NOT NULL,
    look    TEXT DEFAULT 'hr-115-42.hd-195-19.ch-3030-82.lg-275-1408.fa-1201.ca-1804-64' NOT NULL,
    gender  ENUM ('MALE', 'FEMALE')                                                      NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

DROP TABLE IF EXISTS user_permissions;
CREATE TABLE user_permissions
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    user_id    INT                            NOT NULL,
    permission VARCHAR(255)                   NOT NULL,
    allow      BOOLEAN DEFAULT false          NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- Ranks
DROP TABLE IF EXISTS ranks;
CREATE TABLE ranks
(
    id           INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    name         VARCHAR(255)                   NOT NULL,
    weight       INT                            NOT NULL,
    parent_id    INT,
    badge        VARCHAR(255),
    prefix       VARCHAR(255),
    prefix_color VARCHAR(255),
    FOREIGN KEY (parent_id) REFERENCES ranks (id)
);
INSERT INTO ranks (name, weight)
VALUES ('Default', 0);
INSERT INTO ranks (name, weight, parent_id)
VALUES ('Moderator', 100, 1);
INSERT INTO ranks (name, weight, parent_id)
VALUES ('Administrator', 200, 2);

DROP TABLE IF EXISTS rank_permissions;
CREATE TABLE rank_permissions
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    rank_id    INT                            NOT NULL,
    permission VARCHAR(255)                   NOT NULL,
    allow      BOOLEAN DEFAULT false          NOT NULL,
    FOREIGN KEY (rank_id) REFERENCES ranks (id)
);

-- Navigator
DROP TABLE IF EXISTS navigator_public_categories;
CREATE TABLE navigator_public_categories
(
    id         INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    name       VARCHAR(255)                   NOT NULL,
    has_image  BOOLEAN DEFAULT false          NOT NULL,
    is_visible BOOLEAN DEFAULT true           NOT NULL,
    order_num  INT     DEFAULT 0              NOT NULL
);
INSERT INTO navigator_public_categories (name, has_image, is_visible, order_num)
VALUES ('Staff Picks', false, true, 0);
INSERT INTO navigator_public_categories (name, has_image, is_visible, order_num)
VALUES ('Official Games', false, true, 0);
INSERT INTO navigator_public_categories (name, has_image, is_visible, order_num)
VALUES ('Official Fansites', false, true, 0);
INSERT INTO navigator_public_categories (name, has_image, is_visible, order_num)
VALUES ('BAW: Builders at Work', false, true, 0);
INSERT INTO navigator_public_categories (name, has_image, is_visible, order_num)
VALUES ('Room Bundles', false, true, 0);
INSERT INTO navigator_public_categories (name, has_image, is_visible, order_num)
VALUES ('Safety', false, true, 0);

DROP TABLE IF EXISTS navigator_flat_categories;
CREATE TABLE navigator_flat_categories
(
    id            INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    caption       VARCHAR(255)                   NOT NULL,
    minimum_rank  INT     DEFAULT 1              NOT NULL,
    maximum_users INT     DEFAULT 100            NOT NULL,
    is_public     BOOLEAN DEFAULT false          NOT NULL,
    allow_trading BOOLEAN DEFAULT false          NOT NULL,
    order_num     INT     DEFAULT 0              NOT NULL
);
INSERT INTO navigator_flat_categories (caption)
VALUES ('${navigator.flatcategory.global.BC}');
INSERT INTO navigator_flat_categories (caption)
VALUES ('${navigator.flatcategory.global.BUILDING}');
INSERT INTO navigator_flat_categories (caption)
VALUES ('${navigator.flatcategory.global.CHAT}');
INSERT INTO navigator_flat_categories (caption)
VALUES ('${navigator.flatcategory.global.FANSITE}');
INSERT INTO navigator_flat_categories (caption)
VALUES ('${navigator.flatcategory.global.GAMES}');
INSERT INTO navigator_flat_categories (caption)
VALUES ('${navigator.flatcategory.global.HELP}');
INSERT INTO navigator_flat_categories (caption)
VALUES ('${navigator.flatcategory.global.LIFE}');
INSERT INTO navigator_flat_categories (caption, minimum_rank, is_public)
VALUES ('${navigator.flatcategory.global.OFFICIAL}', 3, true);
INSERT INTO navigator_flat_categories (caption)
VALUES ('${navigator.flatcategory.global.PARTY}');

-- Rooms
DROP TABLE IF EXISTS rooms;
CREATE TABLE rooms
(
    id                           INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    owner_id                     INT                            NOT NULL,
    name                         VARCHAR(255)                   NOT NULL,
    description                  TEXT         DEFAULT ''        NOT NULL,
    navigator_public_category_id INT,
    navigator_flat_category_id   INT          DEFAULT 1         NOT NULL,
    tags                         TEXT(255)    DEFAULT ''        NOT NULL,
    model                        VARCHAR(255) DEFAULT 'model_a' NOT NULL,
    is_custom_model              BOOLEAN      DEFAULT false     NOT NULL,
    access_type                  VARCHAR(255) DEFAULT 'OPEN'    NOT NULL,
    password                     VARCHAR(255) DEFAULT ''        NOT NULL,
    users                        INT          DEFAULT 0         NOT NULL,
    maximum_users                INT          DEFAULT 25        NOT NULL,
    score                        INT          DEFAULT 0         NOT NULL,
    is_public                    BOOLEAN      DEFAULT false     NOT NULL,
    is_staff_picked              BOOLEAN      DEFAULT false     NOT NULL,
    creation_timestamp           LONG         DEFAULT 0         NOT NULL,
    wallpaper                    VARCHAR(255) DEFAULT '0.0'     NOT NULL,
    floor_pattern                VARCHAR(255) DEFAULT '0.0'     NOT NULL,
    landscape                    VARCHAR(255) DEFAULT '0.0'     NOT NULL,
    wall_thickness               INT          DEFAULT 0         NOT NULL,
    floor_thickness              INT          DEFAULT 0         NOT NULL,
    wall_height                  INT          DEFAULT -1        NOT NULL,
    are_walls_hidden             BOOLEAN      DEFAULT false     NOT NULL,
    are_wireds_hidden            BOOLEAN      DEFAULT false     NOT NULL,
    allow_other_pets             BOOLEAN      DEFAULT false     NOT NULL,
    allow_other_pets_to_eat      BOOLEAN      DEFAULT false     NOT NULL,
    allow_walkthrough            BOOLEAN      DEFAULT false     NOT NULL,
    allow_diagonal_movement      BOOLEAN      DEFAULT false     NOT NULL,
    chat_mode                    INT          DEFAULT 0         NOT NULL,
    chat_weight                  INT          DEFAULT 1         NOT NULL,
    chat_scrolling_speed         INT          DEFAULT 1         NOT NULL,
    chat_hearing_distance        INT          DEFAULT 50        NOT NULL,
    chat_flood_protection        INT          DEFAULT 2         NOT NULL,
    who_can_kick                 INT          DEFAULT 0         NOT NULL,
    who_can_ban                  INT          DEFAULT 0         NOT NULL,
    who_can_mute                 INT          DEFAULT 0         NOT NULL,
    trading_mode                 INT          DEFAULT 2         NOT NULL,
    roller_speed                 INT          DEFAULT 4         NOT NULL,
    is_promoted                  BOOLEAN      DEFAULT false     NOT NULL,
    is_for_sale                  BOOLEAN      DEFAULT false     NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES users (id),
    FOREIGN KEY (navigator_flat_category_id) REFERENCES navigator_flat_categories (id)
);

DROP TABLE IF EXISTS room_moodlights;
CREATE TABLE room_moodlights
(
    id                 INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    room_id            INT                            NOT NULL,
    is_enabled         BOOLEAN      DEFAULT false     NOT NULL,
    is_background_only BOOLEAN      DEFAULT false     NOT NULL,
    color              VARCHAR(255) DEFAULT '#000000' NOT NULL,
    intensity          INT          DEFAULT 255       NOT NULL,
    FOREIGN KEY (room_id) REFERENCES rooms (id)
);