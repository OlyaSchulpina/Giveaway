-- Выполни в pgAdmin ПОСЛЕ первого запуска приложения (когда таблицы уже созданы).
-- Партнёр на сегодня + 5 промокодов (мало специально, чтобы проверить «промокоды закончились»).

INSERT INTO partners (name, channel_url, giveaway_date, prize_name, winners_count, promo_expires_at)
VALUES ('Кофейня Бодрый', 'https://max.ru/test_channel', CURRENT_DATE,
        'Годовой абонемент на кофе', 2, CURRENT_DATE + 30);

INSERT INTO promos (code, partner_id)
SELECT 'PROMO' || LPAD(g::text, 4, '0'), (SELECT MAX(id) FROM partners)
FROM generate_series(1, 5) g;
