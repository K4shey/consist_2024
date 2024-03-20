# Создание REST API для взаимодействия с API Gitlab.

## Цель

Создать синхронное рест апи, добавляющее комментарий в задачу 2 вашего gitlab проекта.Код должен быть покрыт компонентными тестами с использованием методик TestFirst.Нельзя использовать библиотеки, сделающие за вас всё работу, например, gitlab4j и им подобные.Был вопрос про rest assured - её тоже не нужно.Spring использовать можно.UPD: Контракт интеграционного взаимодействия должен быть зафиксирован в тестах. Про обычные UnitTest-ы тоже не забываем. И про кодопрокрытие тоже.UPD2: lombok не используем

## Использованные технологии

- Java 17
- Spring boot 3.2.3
- JUnit 5
- Maven

## Тестирование

В проекте реализовано тестирование слоя контроллера с помощью `MockMvc`, слоя сервиса с помощью `MockRestServiceServer` 

## Установка и запуск

- Выполните клонирование репозитория: `git clone https://gitlab.com/ваш_репозиторий.git`
- Откройте проект в вашей среде разработки (например, IntelliJ IDEA).
- Укажите токен авторизации пользователя в файле `application.properties`.
- Запустите приложение.
- Используйте Postman для тестирования REST API.
- POST запрос к `/api/notes`  добавляет новый комментарий к задаче. В параметре `body` в теле запроса нужно указать текст комментария к задаче.
