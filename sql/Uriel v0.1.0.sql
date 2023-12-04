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
INSERT INTO currencies (nitro_id, name)
VALUES (-1, 'credit');
INSERT INTO currencies (nitro_id, name)
VALUES (0, 'ducket');
INSERT INTO currencies (nitro_id, name)
VALUES (1, 'diamond');

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
    gender                     ENUM ('MALE', 'FEMALE') DEFAULT 'MALE'                                                             NOT NULL,
    registration_ip            VARCHAR(255)                                                                                    NOT NULL,
    current_ip                 VARCHAR(255)                                                                                    NOT NULL,
    machine_id                 VARCHAR(255)                                                                                    NOT NULL,
    home_room_id               INT                     DEFAULT 0                                                               NOT NULL
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

DROP TABLE IF EXISTS user_navigator_window_settings;
CREATE TABLE user_navigator_window_settings
(
    id                 INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    user_id            INT                            NOT NULL,
    x                  INT     DEFAULT 100            NOT NULL,
    y                  INT     DEFAULT 100            NOT NULL,
    width              INT     DEFAULT 425            NOT NULL,
    height             INT     DEFAULT 535            NOT NULL,
    is_left_panel_open BOOLEAN DEFAULT false          NOT NULL,
    results_mode       INT     DEFAULT 0              NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
);

DROP TABLE IF EXISTS user_navigator_saved_searches;
CREATE TABLE user_navigator_saved_searches
(
    id          INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    user_id     INT                            NOT NULL,
    search_code TEXT                           NOT NULL,
    filter      TEXT                           NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id)
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

-- Rooms
DROP TABLE IF EXISTS rooms;
CREATE TABLE rooms
(
    id                      INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    ownerId                 INT                            NOT NULL,
    name                    VARCHAR(255)                   NOT NULL,
    description             TEXT         DEFAULT ''        NOT NULL,
    category                INT          DEFAULT 1         NOT NULL,
    tags                    TEXT(255)    DEFAULT ''        NOT NULL,
    model                   VARCHAR(255) DEFAULT 'model_a' NOT NULL,
    is_custom_model         BOOLEAN      DEFAULT false     NOT NULL,
    access_type             VARCHAR(255) DEFAULT 'OPEN'    NOT NULL,
    password                VARCHAR(255) DEFAULT ''        NOT NULL,
    users                   INT          DEFAULT 0         NOT NULL,
    max_users               INT          DEFAULT 25        NOT NULL,
    score                   INT          DEFAULT 0         NOT NULL,
    is_public               BOOLEAN      DEFAULT false     NOT NULL,
    is_staff_picked         BOOLEAN      DEFAULT false     NOT NULL,
    creation_timestamp      LONG         DEFAULT 0         NOT NULL,
    wallpaper               VARCHAR(255) DEFAULT '0.0'     NOT NULL,
    floor_pattern           VARCHAR(255) DEFAULT '0.0'     NOT NULL,
    landscape               VARCHAR(255) DEFAULT '0.0'     NOT NULL,
    wall_thickness          INT          DEFAULT 0         NOT NULL,
    floor_thickness         INT          DEFAULT 0         NOT NULL,
    wall_height             INT          DEFAULT -1        NOT NULL,
    are_walls_hidden        BOOLEAN      DEFAULT false     NOT NULL,
    are_wireds_hidden       BOOLEAN      DEFAULT false     NOT NULL,
    allow_other_pets        BOOLEAN      DEFAULT false     NOT NULL,
    allow_other_pets_to_eat BOOLEAN      DEFAULT false     NOT NULL,
    chat_mode               INT          DEFAULT 0         NOT NULL,
    chat_weight             INT          DEFAULT 1         NOT NULL,
    chat_scrolling_speed    INT          DEFAULT 1         NOT NULL,
    chat_hearing_distance   INT          DEFAULT 50        NOT NULL,
    chat_flood_protection   INT          DEFAULT 2         NOT NULL,
    who_can_kick            INT          DEFAULT 0         NOT NULL,
    who_can_ban             INT          DEFAULT 0         NOT NULL,
    who_can_mute            INT          DEFAULT 0         NOT NULL,
    trading_mode            INT          DEFAULT 2         NOT NULL,
    roller_speed            INT          DEFAULT 4         NOT NULL,
    is_promoted             BOOLEAN      DEFAULT false     NOT NULL,
    is_for_sale             BOOLEAN      DEFAULT false     NOT NULL,
    FOREIGN KEY (ownerId) REFERENCES users (id)
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