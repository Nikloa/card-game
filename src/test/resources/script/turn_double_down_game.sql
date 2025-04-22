INSERT INTO cards (id, name, card_value, description, type, card_type) VALUES
(1, 'PointCard', 5, 'прибавляет 5 очков', null, 'POINT'),
(2, 'BlockCard', 1, 'блокировка хода', 'BLOCK', 'ACTION'),
(3, 'DoubleDownCard', 2, 'удваивает очки', 'DOUBLE_DOWN', 'ACTION'),
(4, 'StealCard', 5, 'отдает 5 очков игроку вытянувшему карту', 'STEAL', 'ACTION');

INSERT INTO users (id, name, login, password) VALUES
(1, 'Alex', 'Alex', '$2a$12$IWChuHSKZ72.aeNm3MCXzeYcdSi9B.YeC4szNdaBbaBjalodVhkR2'),
(2, 'Neo', 'Neo', '$2a$12$IWChuHSKZ72.aeNm3MCXzeYcdSi9B.YeC4szNdaBbaBjalodVhkR2');

INSERT INTO game_sessions (id, state) VALUES (1, 'IN_PROGRESS');

INSERT INTO players (id, user_id, game_session_id, points, turn_order, is_player_turn) VALUES
(1, 1, 1, 5, 0, true),
(2, 2, 1, 5, 1, false);

INSERT INTO deck_cards (id, card_id, game_session_id, deck_position) VALUES
(1, 1, 1, 1),
(2, 2, 1, 4),
(4, 3, 1, 4),
(3, 4, 1, 3);
