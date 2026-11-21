# Тестирование канбан-доски с Selenium (Java)

[![hexlet-check](https://github.com/mxmrbkv/qa-auto-engineer-java-project-385/actions/workflows/hexlet-check.yml/badge.svg)](https://github.com/mxmrbkv/qa-auto-engineer-java-project-385/actions)

Научитесь проектировать и поддерживать UI-тесты на Java с использованием Selenium WebDriver и JUnit.

Учебный проект Хекслета: https://ru.hexlet.io/programs/qa-auto-engineer-java
Как это должно работать: https://files.hexlet.app/a/p16xy7

## Стек

- Java 21
- JUnit 5
- Selenium WebDriver
- Gradle
- Docker

## Требования

- Java 21+
- Google Chrome
- Docker

## Установка и запуск приложения

1. Клонирование репозитория:

```bash
git clone https://github.com/mxmrbkv/qa-auto-engineer-java-project-385.git
cd qa-auto-engineer-java-project-385
```

2. Запуск контейнера с приложением:

```bash
make start
```

Приложение будет доступно по адресу [http://localhost:5173](http://localhost:5173).

> Для авторизации в приложении подходит любая комбинация логина и пароля (например, `admin` / `admin`).

3. Остановка контейнера:

```bash
make stop
```

## Использование

### Сборка и подготовка тестов

```bash
make install
```

### Запуск тестов

Запуск тестов с указанием адреса приложения через переменную окружения `APP_BASE_URL`:

```bash
APP_BASE_URL=http://localhost:5173 make test
```

По умолчанию тесты выполняются в headless-режиме (`--headless=new`). Для запуска в обычном графическом окне браузера:

```bash
HEADLESS=false APP_BASE_URL=http://localhost:5173 make test
```

---

<details>
<summary>Автоматические тесты Хекслета</summary>

Тесты запускаются на каждый коммит. За запуск отвечает файл `.github/workflows/hexlet-check.yml` — не удаляйте и не переименовывайте ни его, ни репозиторий.

</details>

## О Хекслете

[Хекслет](https://ru.hexlet.io/) — школа программирования: авторские программы обучения с практикой, поддержкой наставников и реальными проектами, которые остаются в резюме. Этот репозиторий — один из таких проектов.