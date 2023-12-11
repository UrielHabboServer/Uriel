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
INSERT INTO rank_permissions (rank_id, permission, allow)
VALUES (1, 'uriel.rooms.can_create_rooms', true);

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
    id                  INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    caption             VARCHAR(255)                   NOT NULL,
    minimum_rank_weight INT     DEFAULT 0              NOT NULL,
    maximum_users       INT     DEFAULT 100            NOT NULL,
    is_public           BOOLEAN DEFAULT false          NOT NULL,
    allow_trading       BOOLEAN DEFAULT false          NOT NULL,
    order_num           INT     DEFAULT 0              NOT NULL
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
INSERT INTO navigator_flat_categories (caption, minimum_rank_weight, is_public)
VALUES ('${navigator.flatcategory.global.OFFICIAL}', 200, true);
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
    tags                         TEXT         DEFAULT ''        NOT NULL,
    model_id                     INT          DEFAULT 1         NOT NULL,
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
    FOREIGN KEY (navigator_flat_category_id) REFERENCES navigator_flat_categories (id),
    FOREIGN KEY (model_id) REFERENCES room_models (id)
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

DROP TABLE IF EXISTS room_models;
CREATE TABLE room_models
(
    id             INT AUTO_INCREMENT PRIMARY KEY NOT NULL UNIQUE,
    name           VARCHAR(255)                   NOT NULL,
    heightmap      TEXT    DEFAULT ''             NOT NULL,
    is_club_only   BOOLEAN DEFAULT false          NOT NULL,
    is_custom      BOOLEAN DEFAULT false          NOT NULL,
    door_x         INT     DEFAULT 0              NOT NULL,
    door_y         INT     DEFAULT 0              NOT NULL,
    door_direction INT     DEFAULT 0              NOT NULL,
    INDEX (name)
);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_a', 'xxxxxxxxxxxx
xxxx00000000
xxxx00000000
xxxx00000000
xxxx00000000
xxx000000000
xxxx00000000
xxxx00000000
xxxx00000000
xxxx00000000
xxxx00000000
xxxx00000000
xxxx00000000
xxxx00000000
xxxxxxxxxxxx
xxxxxxxxxxxx', 3, 5, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_b', 'xxxxxxxxxxxx
xxxxx0000000
xxxxx0000000
xxxxx0000000
xxxxx0000000
000000000000
x00000000000
x00000000000
x00000000000
x00000000000
x00000000000
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx', 0, 5, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_c', 'xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxx000000x
xxxxx000000x
xxxx0000000x
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx', 4, 7, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_d', 'xxxxxxxxxxxx
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxx0000000x
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxxx000000x
xxxxxxxxxxxx', 4, 7, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_e', 'xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xx0000000000
xx0000000000
x00000000000
xx0000000000
xx0000000000
xx0000000000
xx0000000000
xx0000000000
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx', 1, 5, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_f', 'xxxxxxxxxxxx
xxxxxxx0000x
xxxxxxx0000x
xxx00000000x
xxx00000000x
xx000000000x
xxx00000000x
x0000000000x
x0000000000x
x0000000000x
x0000000000x
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx', 2, 5, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_g', 'xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxx00000
xxxxxxx00000
xxxxxxx00000
xx1111000000
xx1111000000
x11111000000
xx1111000000
xx1111000000
xxxxxxx00000
xxxxxxx00000
xxxxxxx00000
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx', true, 1, 7, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_h', 'xxxxxxxxxxxx
xxxxxxxxxxxx\n
xxxxx111111x
xxxxx111111x
xxxx1111111x
xxxxx111111x
xxxxx111111x
xxxxx000000x
xxxxx000000x
xxx00000000x
xxx00000000x
xxx00000000x
xxx00000000x
xxxxxxxxxxxx
xxxxxxxxxxxx
xxxxxxxxxxxx', true, 4, 4, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_i', 'xxxxxxxxxxxxxxxxx
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
00000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
x0000000000000000
xxxxxxxxxxxxxxxxx', 0, 10, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_j', 'xxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxx0000000000
xxxxxxxxxxx0000000000
xxxxxxxxxxx0000000000
xxxxxxxxxxx0000000000
xxxxxxxxxxx0000000000
xxxxxxxxxxx0000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
000000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x0000000000xxxxxxxxxx
x0000000000xxxxxxxxxx
x0000000000xxxxxxxxxx
x0000000000xxxxxxxxxx
x0000000000xxxxxxxxxx
x0000000000xxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxx', 0, 10, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_k', 'xxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxx00000000
xxxxxxxxxxxxxxxxx00000000
xxxxxxxxxxxxxxxxx00000000
xxxxxxxxxxxxxxxxx00000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
x000000000000000000000000
x000000000000000000000000
x000000000000000000000000
x000000000000000000000000
0000000000000000000000000
x000000000000000000000000
x000000000000000000000000
x000000000000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxxxxxxxxxxxxxxxxxx', 0, 13, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_l', 'xxxxxxxxxxxxxxxxxxxxx
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000xxxx00000000
x00000000xxxx00000000
x00000000xxxx00000000
x00000000xxxx00000000
x00000000xxxx00000000
x00000000xxxx00000000
x00000000xxxx00000000
000000000xxxx00000000
x00000000xxxx00000000
x00000000xxxx00000000
x00000000xxxx00000000
x00000000xxxx00000000
xxxxxxxxxxxxxxxxxxxxx', 0, 16, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_m', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
x0000000000000000000000000000
x0000000000000000000000000000
x0000000000000000000000000000
x0000000000000000000000000000
00000000000000000000000000000
x0000000000000000000000000000
x0000000000000000000000000000
x0000000000000000000000000000
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxx00000000xxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0, 15, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_n', 'xxxxxxxxxxxxxxxxxxxxx
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x000000xxxxxxxx000000
x000000x000000x000000
x000000x000000x000000
x000000x000000x000000
x000000x000000x000000
x000000x000000x000000
x000000x000000x000000
x000000xxxxxxxx000000
x00000000000000000000
000000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
x00000000000000000000
xxxxxxxxxxxxxxxxxxxxx', 0, 16, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_o', 'xxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxx11111111xxxx
xxxxxxxxxxxxx11111111xxxx
xxxxxxxxxxxxx11111111xxxx
xxxxxxxxxxxxx11111111xxxx
xxxxxxxxxxxxx11111111xxxx
xxxxxxxxxxxxx11111111xxxx
xxxxxxxxxxxxx11111111xxxx
xxxxxxxxxxxxx00000000xxxx
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
x111111100000000000000000
x111111100000000000000000
x111111100000000000000000
1111111100000000000000000
x111111100000000000000000
x111111100000000000000000
x111111100000000000000000
x111111100000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxx0000000000000000
xxxxxxxxxxxxxxxxxxxxxxxxx', true, 0, 18, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_p', 'xxxxxxxxxxxxxxxxxxx
xxxxxxx222222222222
xxxxxxx222222222222
xxxxxxx222222222222
xxxxxxx222222222222
xxxxxxx222222222222
xxxxxxx222222222222
xxxxxxx22222222xxxx
xxxxxxx11111111xxxx
x222221111111111111
x222221111111111111
x222221111111111111
x222221111111111111
x222221111111111111
x222221111111111111
x222221111111111111
x222221111111111111
x2222xx11111111xxxx
x2222xx00000000xxxx
x2222xx000000000000
x2222xx000000000000
x2222xx000000000000
x2222xx000000000000
22222xx000000000000
x2222xx000000000000
xxxxxxxxxxxxxxxxxxx', true, 0, 23, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_q', 'xxxxxxxxxxxxxxxxxxx
xxxxxxxxxxx22222222
xxxxxxxxxxx22222222
xxxxxxxxxxx22222222
xxxxxxxxxx222222222
xxxxxxxxxxx22222222
xxxxxxxxxxx22222222
x222222222222222222
x222222222222222222
x222222222222222222
x222222222222222222
x222222222222222222
x222222222222222222
x2222xxxxxxxxxxxxxx
x2222xxxxxxxxxxxxxx
x2222211111xx000000
x222221111110000000
x222221111110000000
x2222211111xx000000
xx22xxx1111xxxxxxxx
xx11xxx1111xxxxxxxx
x1111xx1111xx000000
x1111xx111110000000
x1111xx111110000000
x1111xx1111xx000000
xxxxxxxxxxxxxxxxxxx', true, 10, 4, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_r', 'xxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxx33333333333333
xxxxxxxxxxx33333333333333
xxxxxxxxxxx33333333333333
xxxxxxxxxx333333333333333
xxxxxxxxxxx33333333333333
xxxxxxxxxxx33333333333333
xxxxxxx333333333333333333
xxxxxxx333333333333333333
xxxxxxx333333333333333333
xxxxxxx333333333333333333
xxxxxxx333333333333333333
xxxxxxx333333333333333333
x4444433333xxxxxxxxxxxxxx
x4444433333xxxxxxxxxxxxxx
x44444333333222xx000000xx
x44444333333222xx000000xx
xxx44xxxxxxxx22xx000000xx
xxx33xxxxxxxx11xx000000xx
xxx33322222211110000000xx
xxx33322222211110000000xx
xxxxxxxxxxxxxxxxx000000xx
xxxxxxxxxxxxxxxxx000000xx
xxxxxxxxxxxxxxxxx000000xx
xxxxxxxxxxxxxxxxx000000xx
xxxxxxxxxxxxxxxxxxxxxxxxx', true, 10, 4, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_s', 'xxxxxx
x00000
x00000
000000
x00000
x00000
x00000
x00000', 0, 3, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_t', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
x222222222222222222222222222x
x222222222222222222222222222x
2222222222222222222222222222x
x222222222222222222222222222x
x2222xxxxxx222222xxxxxxx2222x
x2222xxxxxx111111xxxxxxx2222x
x2222xx111111111111111xx2222x
x2222xx111111111111111xx2222x
x2222xx11xxx1111xxxx11xx2222x
x2222xx11xxx0000xxxx11xx2222x
x22222111x00000000xx11xx2222x
x22222111x00000000xx11xx2222x
x22222111x00000000xx11xx2222x
x22222111x00000000xx11xx2222x
x22222111x00000000xx11xx2222x
x22222111x00000000xx11xx2222x
x2222xx11xxxxxxxxxxx11xx2222x
x2222xx11xxxxxxxxxxx11xx2222x
x2222xx111111111111111xx2222x
x2222xx111111111111111xx2222x
x2222xxxxxxxxxxxxxxxxxxx2222x
x2222xxxxxxxxxxxxxxxxxxx2222x
x222222222222222222222222222x
x222222222222222222222222222x
x222222222222222222222222222x
x222222222222222222222222222x
xxxxxxxxxxxxxxxxxxxxxxxxxxxxx', true, 0, 3, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_u', 'xxxxxxxxxxxxxxxxxxxxxxxx
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
11111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
x1111100000000000000000x
xxxxxxxxxxxxxxxxxxxxxxxx', true, 0, 17, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_v', 'xxxxxxxxxxxxxxxxxxxx
x222221111111111111x
x222221111111111111x
2222221111111111111x
x222221111111111111x
x222221111111111111x
x222221111111111111x
xxxxxxxx1111xxxxxxxx
xxxxxxxx0000xxxxxxxx
x000000x0000x000000x
x000000x0000x000000x
x00000000000x000000x
x00000000000x000000x
x000000000000000000x
x000000000000000000x
xxxxxxxx00000000000x
x000000x00000000000x
x000000x0000xxxxxxxx
x00000000000x000000x
x00000000000x000000x
x00000000000x000000x
x00000000000x000000x
xxxxxxxx0000x000000x
x000000x0000x000000x
x000000x0000x000000x
x000000000000000000x
x000000000000000000x
x000000000000000000x
x000000000000000000x
xxxxxxxxxxxxxxxxxxxx', true, 0, 3, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_w', 'xxxxxxxxxxxxxxxxxxxxxxxxxxx
x2222xx1111111111xx11111111
x2222xx1111111111xx11111111
222222111111111111111111111
x22222111111111111111111111
x22222111111111111111111111
x22222111111111111111111111
x2222xx1111111111xx11111111
x2222xx1111111111xx11111111
x2222xx1111111111xxxx1111xx
x2222xx1111111111xxxx0000xx
xxxxxxx1111111111xx00000000
xxxxxxx1111111111xx00000000
x22222111111111111000000000
x22222111111111111000000000
x22222111111111111000000000
x22222111111111111000000000
x2222xx1111111111xx00000000
x2222xx1111111111xx00000000
x2222xxxx1111xxxxxxxxxxxxxx
x2222xxxx0000xxxxxxxxxxxxxx
x2222x0000000000xxxxxxxxxxx
x2222x0000000000xxxxxxxxxxx
x2222x0000000000xxxxxxxxxxx
x2222x0000000000xxxxxxxxxxx
x2222x0000000000xxxxxxxxxxx
x2222x0000000000xxxxxxxxxxx', true, 0, 3, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_x', 'xxxxxxxxxxxxxxxxxxxx
x000000000000000000x
x000000000000000000x
x000000000000000000x
x000000000000000000x
x000000000000000000x
x000000000000000000x
xxx00xxx0000xxx00xxx
x000000x0000x000000x
x000000x0000x000000x
x000000x0000x000000x
x000000x0000x000000x
0000000x0000x000000x
x000000x0000x000000x
x000000x0000x000000x
x000000x0000x000000x
x000000x0000x000000x
x000000x0000x000000x
x000000xxxxxx000000x
x000000000000000000x
x000000000000000000x
x000000000000000000x
x000000000000000000x
x000000000000000000x
x000000000000000000x
xxxxxxxxxxxxxxxxxxxx', true, 0, 12, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_y', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxx
x00000000xx0000000000xx0000x
x00000000xx0000000000xx0000x
000000000xx0000000000xx0000x
x00000000xx0000000000xx0000x
x00000000xx0000xx0000xx0000x
x00000000xx0000xx0000xx0000x
x00000000xx0000xx0000000000x
x00000000xx0000xx0000000000x
xxxxx0000xx0000xx0000000000x
xxxxx0000xx0000xx0000000000x
xxxxx0000xx0000xxxxxxxxxxxxx
xxxxx0000xx0000xxxxxxxxxxxxx
x00000000xx0000000000000000x
x00000000xx0000000000000000x
x00000000xx0000000000000000x
x00000000xx0000000000000000x
x0000xxxxxxxxxxxxxxxxxx0000x
x0000xxxxxxxxxxxxxxxxxx0000x
x00000000000000000000000000x
x00000000000000000000000000x
x00000000000000000000000000x
x00000000000000000000000000x
xxxxxxxxxxxxxxxxxxxxxxxxxxxx', true, 0, 3, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_z', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxx00000000000000000000
xxxxxxxxxxx00000000000000000000
xxxxxxxxxxx00000000000000000000
x00000000xx00000000000000000000
x00000000xx00000000000000000000
x00000000xx00000000000000000000
x00000000xx00000000000000000000
x00000000xx00000000000000000000
000000000xx00000000000000000000
x00000000xx00000000000000000000
x00000000xx00000000000000000000
x00000000xx00000000000000000000
x00000000xx00000000000000000000
x00000000xx00000000000000000000
x00000000xx00000000000000000000
xxxxxxxxxxx00000000000000000000
xxxxxxxxxxx00000000000000000000
xxxxxxxxxxx00000000000000000000
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', true, 0, 9, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_0', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
x00000000xx00000000xx00000000xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
x00000000xx00000000xx00000000xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
x00000000xx00000000xx00000000xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000
000000000xx00000000xx00000000xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000
x00000000xx00000000xx00000000xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000
x00000000xx00000000xx00000000xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx0000
x00000000xx00000000xx00000000xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
x00000000xx00000000xx00000000xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', true, 0, 4, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_1', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xeeeeeeeeeeeeeeeedcba9888888888888
xeeeeeeeeeeeeeeeexxxxxx88888888888
xeeeeeeeeeeeeeeeexxxxxx88888888888
xeeeeeeeeeeeeeeeexxxxxx88888888888
xeeeeeeeeeeeeeeeexxxxxx88888888888
xdxxxxxxxxxxxxxxxxxxxxx88888888888
xcxxxxxxxxxxxxxxxxxxxxx88888888888
xbxxxxxxxxxxxxxxxxxxxxx88888888888
xaxxxxxxxxxxxxxxxxxxxxx88888888888
aaaaaaaaaaaaaaaaaxxxxxxxxxxxxxxxxx
xaaaaaaaaaaaaaaaaxxxxxxxxxxxxxxxxx
xaaaaaaaaaaaaaaaaxxxxxxxxxxxxxxxxx
xaaaaaaaaaaaaaaaaxxxx6666666666666
xaaaaaaaaaaaaaaaaxxxx6666666666666
xaaaaaaaaaaaaaaaaxxxx6666666666666
xaaaaaaaaaaaaaaaaxxxx6666666666666
xaaaaaaaaaaaaaaaaxxxx6666666666666
xaaaaaaaaaaaaaaaa98766666666666666
xaaaaaaaaaaaaaaaaxxxxxxxxxxxx5xxxx
xaaaaaaaaaaaaaaaaxxxxxxxxxxxx4xxxx
xaaaaaaaaaaaaaaaaxxxxxxxxxxxx3xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xaaaaaaaaaaaaaaaaxxx3333333333xxxx
xxxxxxxxxxxxxxxx9xxx3333333333xxxx
xxxxxxxxxxxxxxxx8xxx3333333333xxxx
xxxxxxxxxxxxxxxx7xxx3333333333xxxx
xxx777777777xxxx6xxx3333333333xxxx
xxx777777777xxxx5xxxxxxxxxxxxxxxxx
xxx777777777xxxx4xxxxxxxxxxxxxxxxx
xxx777777777xxxx3xxxxxxxxxxxxxxxxx
xxx777777777xxxx2xxxxxxxxxxxxxxxxx
xfffffffffxxxxxx1xxxxxxxxxxxxxxxxx
xfffffffffxxxxxx111111111111111111
xfffffffffxxxxxx111111111111111111
xfffffffffxxxxxx111111111111111111
xfffffffffxxxxxx111111111111111111
xfffffffffxxxxxx111111111111111111
xfffffffffxxxxxx111111111111111111
xxxxxxxxxxxxxxxx111111111111111111
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0, 10, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_2', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xjjjjjjjjjjjjjx0000xxxxxxxxxx
xjjjjjjjjjjjjjx0000xxxxxxxxxx
xjjjjjjjjjjjjjx0000xxxxxxxxxx
xjjjjjjjjjjjjjx0000xxxxxxxxxx
xjjjjjjjjjjjjjx0000xxxxxxxxxx
xjjjjjjjjjjjjjx0000xxxxxxxxxx
xjjjjjjjjjjjjjx0000xxxxxxxxxx
xjjjjjjjjjjjjjx0000xxxxxxxxxx
xxxxxxxxxxxxiix0000xxxxxxxxxx
xxxxxxxxxxxxhhx0000xxxxxxxxxx
xxxxxxxxxxxxggx0000xxxxxxxxxx
xxxxxxxxxxxxffx0000xxxxxxxxxx
xxxxxxxxxxxxeex0000xxxxxxxxxx
xeeeeeeeeeeeeex0000xxxxxxxxxx
eeeeeeeeeeeeeex0000xxxxxxxxxx
xeeeeeeeeeeeeex0000xxxxxxxxxx
xeeeeeeeeeeeeex0000xxxxxxxxxx
xeeeeeeeeeeeeex0000xxxxxxxxxx
xeeeeeeeeeeeeex0000xxxxxxxxxx
xeeeeeeeeeeeeex0000xxxxxxxxxx
xeeeeeeeeeeeeex0000xxxxxxxxxx
xeeeeeeeeeeeeex0000xxxxxxxxxx
xeeeeeeeeeeeeex0000xxxxxxxxxx
xxxxxxxxxxxxddx00000000000000
xxxxxxxxxxxxccx00000000000000
xxxxxxxxxxxxbbx00000000000000
xxxxxxxxxxxxaax00000000000000
xaaaaaaaaaaaaax00000000000000
xaaaaaaaaaaaaax00000000000000
xaaaaaaaaaaaaax00000000000000
xaaaaaaaaaaaaax00000000000000
xaaaaaaaaaaaaax00000000000000
xaaaaaaaaaaaaax00000000000000
xaaaaaaaaaaaaax00000000000000
xaaaaaaaaaaaaax00000000000000
xaaaaaaaaaaaaax00000000000000
xaaaaaaaaaaaaax00000000000000
xxxxxxxxxxxx99x0000xxxxxxxxxx
xxxxxxxxxxxx88x0000xxxxxxxxxx
xxxxxxxxxxxx77x0000xxxxxxxxxx
xxxxxxxxxxxx66x0000xxxxxxxxxx
xxxxxxxxxxxx55x0000xxxxxxxxxx
xxxxxxxxxxxx44x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
x4444444444444x0000xxxxxxxxxx
xxxxxxxxxxxx33x0000xxxxxxxxxx
xxxxxxxxxxxx22x0000xxxxxxxxxx
xxxxxxxxxxxx11x0000xxxxxxxxxx
xxxxxxxxxxxx00x0000xxxxxxxxxx
x000000000000000000xxxxxxxxxx
x000000000000000000xxxxxxxxxx
x000000000000000000xxxxxxxxxx
x000000000000000000xxxxxxxxxx
x000000000000000000xxxxxxxxxx
x000000000000000000xxxxxxxxxx
x000000000000000000xxxxxxxxxx
x000000000000000000xxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0, 15, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_3', 'XXXXXXXXXXXXXXXXX
XXX0000000000000X
XXX0000000000000X
XXX0000000000000X
XXX0000000000000X
XXX0000000000000X
XXX0000000000000X
X000000000000000X
X000000000000000X
X000000000000000X
0000000000000000X
X000000000000000X
X000000000000000X
X000000000000000X
XXXXXXXXXXXXXXXXX', true, 0, 10, 2);
INSERT INTO room_models (name, heightmap, is_club_only, door_x, door_y, door_direction)
VALUES ('model_4', 'xxxxxxxxxxxxxxxxxxxxxxx
xXXXXXXXX9999999999999x
xXXXXXXXX9999999999999x
xXXXXXXXX9999999999999x
xXXXXXXXX9999999999999x
x00000000XXXXXXX999999x
x00000000XXXXXXX999999x
x00000000XXXXXXX999999x
x00000000XXXXXXX999999x
x000000000000000999999x
x000000000000000999999x
x000000000000000999999x
0000000000000000999999x
x000000000000000XXXXXXx
x000000000000000XXXXXXx
x000000000000000XXXXXXx
x000000000000000XXXXXXx
x000000000000000XXXXXXx
x000000000000000XXXXXXx
xxxxxxxxxxxxxxxxxxxxxxx', true, 0, 12, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_5', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
000000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
x00000000000000000000000000000000x
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0, 10, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_6', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
x222222222x000000000000000000000000xxxx
x222222222x000000000000000000000000xxxx
x222222222x000000000000000000000000xxxx
x222222222x000000000000000000000000xxxx
x222222222x000000000000000000000000xxxx
x222222222x000000000000000000000000xxxx
x222222222x000000000000000000000000xxxx
x222222222x000000000000000000000000xxxx
x222222222x00000000xxxxxxxx00000000xxxx
x11xxxxxxxx00000000xxxxxxxx00000000xxxx
x00x000000000000000xxxxxxxx00000000xxxx
x00x000000000000000xxxxxxxx00000000xxxx
x000000000000000000xxxxxxxx00000000xxxx
x000000000000000000xxxxxxxx00000000xxxx
0000000000000000000xxxxxxxx00000000xxxx
x000000000000000000xxxxxxxx00000000xxxx
x00x000000000000000xxxxxxxx00000000xxxx
x00x000000000000000xxxxxxxx00000000xxxx
x00xxxxxxxxxxxxxxxxxxxxxxxx00000000xxxx
x00xxxxxxxxxxxxxxxxxxxxxxxx00000000xxxx
x00x0000000000000000000000000000000xxxx
x00x0000000000000000000000000000000xxxx
x0000000000000000000000000000000000xxxx
x0000000000000000000000000000000000xxxx
x0000000000000000000000000000000000xxxx
x0000000000000000000000000000000000xxxx
x00x0000000000000000000000000000000xxxx
x00x0000000000000000000000000000000xxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0, 15, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_7', 'xxxxxxxxxxxxxxxxxxxxxxxxx
x222222xx00000000xxxxxxxx
x222222xx00000000xxxxxxxx
x2222221000000000xxxxxxxx
x2222221000000000xxxxxxxx
x222222xx00000000xxxxxxxx
x222222xx00000000xxxxxxxx
x222222xxxxxxxxxxxxxxxxxx
x222222xkkkkkkxxiiiiiiiix
x222222xkkkkkkxxiiiiiiiix
x222222xkkkkkkjiiiiiiiiix
x222222xkkkkkkjiiiiiiiiix
x222222xkkkkkkxxiiiiiiiix
xxx11xxxkkkkkkxxiiiiiiiix
xxx00xxxkkkkkkxxxxxxxxxxx
x000000xkkkkkkxxxxxxxxxxx
x000000xkkkkkkxxxxxxxxxxx
0000000xkkkkkkxxxxxxxxxxx
x000000xkkkkkkxxxxxxxxxxx
x000000xkkkkkkxxxxxxxxxxx
x000000xxxjjxxxxxxxxxxxxx
x000000xxxiixxxxxxxxxxxxx
x000000xiiiiiixxxxxxxxxxx
xxxxxxxxiiiiiixxxxxxxxxxx
xxxxxxxxiiiiiixxxxxxxxxxx
xxxxxxxxiiiiiixxxxxxxxxxx
xxxxxxxxiiiiiixxxxxxxxxxx
xxxxxxxxiiiiiixxxxxxxxxxx
xxxxxxxxiiiiiixxxxxxxxxxx
xxxxxxxxiiiiiixxxxxxxxxxx', 0, 17, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_8', 'xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
x5555555555555555555555555xxxxxxxxx
x5555555555555555555555555xxxxxxxxx
x5555555555555555555555555xxxxxxxxx
x5555555555555555555555555xxxxxxxxx
x5555555555555555555555555xxxxxxxxx
x5555555555555555555555555xxxxxxxxx
x5555555555xxxxxxxxxxxxxxxxxxxxxxxx
x55555555554321000000000000000000xx
x55555555554321000000000000000000xx
x5555555555xxxxx00000000000000000xx
x555555x44x0000000000000000000000xx
x555555x33x0000000000000000000000xx
x555555x22x0000000000000000000000xx
x555555x11x0000000000000000000000xx
5555555x00x0000000000000000000000xx
x555555x0000000000000000000000000xx
x555555x0000000000000000000000000xx
x555555x0000000000000000000000000xx
x555555x0000000000000000000000000xx
x555555x0000000000000000000000000xx
x555555x0000000000000000000000000xx
x555555x0000000000000000000000000xx
x555555x0000000000000000000000000xx
x555555x0000000000000000000000000xx
x555555x0000000000000000000000000xx
xxxxxxxx0000000000000000000000000xx
xxxxxxxx0000000000000000000000000xx
xxxxxxxx0000000000000000000000000xx
xxxxxxxx0000000000000000000000000xx
xxxxxxxx0000000000000000000000000xx
xxxxxxxx0000000000000000000000000xx
xxxxxxxx0000000000000000000000000xx
xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx', 0, 15, 2);
INSERT INTO room_models (name, heightmap, door_x, door_y, door_direction)
VALUES ('model_9', 'xxxxxxxxxxxxxxxxxxxxxxxx
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
00000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
x0000000000000000000000x
xxxxxxxxxxxxxxxxxxxxxxxx', 0, 17, 2);