# Giveaway: Бот розыгрышей REST-приложение

Spring Boot 3.3, Java 17, Maven, PostgreSQL, Swagger. Без Lombok.

## Запуск
1. В pgAdmin нужно создать базу `giveaway`.
2. В `src/main/resources/application.yml` вписать пароль от PostgreSQL.
3. Запустить в IDEA, как проект на Maven
4. Запустить `GiveawayApplication`.
5. Выполнить `sql/test-data.sql` в pgAdmin, чтобы проверить функционал на тестовых данных.
6. Swagger: http://localhost:8080/swagger-ui/index.html

## Ключ админа
- Админ:  `admin123`.

## Проверка функционала
# prize-controller 
1. POST `api/winner/contacts` - добавить контакты пользователя
2. GET `api/notifications` - получить уведомления пользователя(его призы, рассылки)
3. GET `api/my-winnings` - посмотреть призы пользователя
# start-controller
1. POST `api/start` - кнопка начать в боте
2. GET `api/partner/today` - получить партнера дня
3. GET `api/info/how` - получить инструкцию 
4. GET `api/info/rules` - получить правила акции
# promo-controller
1. POST `api/promo` - получить промокод
# giveaway-controller
1. POST `api/giveaway/check` - проверить подписку пользователя
2. POST `api/giveway/subscribe` - имитировать подписку
3. GET `api/giveaway` - учавствовать в розыгрыше
# chance-controller
1. GET `api/chances` - посмотреть шансы пользователя в главном розыгрыше
# admin-controller
1. POST `api/draws/daily/{partnerId}` - провести розгрыш дня
2. POST `api/draws/daily/{partnerId}/confirm` - подтвердить результаты и сделать рассылку
3. POST `api/draws/daily/{partnerId}/export` - экспортировать результаты в CSV
4. POST `api/draws/final` - провести главный розыгрыш
5. POST `api/draws/final/confirm` - подтвердить результаты и сделать рассылку для главного розыгрыша
6. GET `api/stats` - получить статистику
7. GET `api/draws/daily/{partnerId}/export` - экспортировать результаты розыгрыша дня в CSV