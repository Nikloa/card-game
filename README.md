# Game API

Это простое API для управления карточной игрой и аутентификацией пользователей. Приложение написано на Kotlin с использованием Spring Boot.

## Оглавление

- [Описание](#описание)
- [Установка](#установка)
- [Использование](#использование)
    - [API для игр](#api-для-игр)
    - [API для аутентификации](#api-для-аутентификации)
- [Технологии](#технологии)

## Описание

Это RESTful API позволяет пользователям создавать игровые сессии, присоединяться к ним, начинать игры и делать ходы. Также реализована аутентификация пользователей с помощью JWT.

## Установка

1. Клонируйте репозиторий:

```
bash
git clone https://github.com/nikloa/card-game.git
cd card-game
```



2. Запустите приложение:

```
docker-compose up
```



Приложение будет доступно по адресу http://localhost:8080.

## Использование

### API для игр

- **Создание игровой сессии**


POST /api/games/new


- **Присоединиться к игровой сессии**


POST /api/games/{gameId}/join


- **Начать игровую сессию**


POST /api/games/{gameId}/start


- **Сделать ход в игре**


POST /api/games/{gameId}/turn


- **Если выпала карта Steal введите ID игрока**


POST /api/games/{gameId}/turn/{playerId}


- **Получить историю игры**


GET /api/games/{gameId}/history


### API для аутентификации

- **Регистрация пользователя**


POST /api/auth/sign-up


- **Авторизация пользователя**


POST /api/auth/sign-in


## Технологии

- Kotlin
- Spring Boot
- Spring Security
- JWT (JSON Web Tokens)
- PostgreSQL
- Liquibase
- Docker
