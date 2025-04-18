# Sleep Monitoring API – Документация

Бэкенд-приложение на Ktor для сбора и анализа данных сна пользователя.

Базовый URL: `/api/v1`

## ⚙️ Особенности сериализации

> **Примечание:** Все значения времени сериализуются как **timestamp** (число секунд с начала эпохи):
- `Instant` → `1702944000.0` (Unix timestamp, с дробной частью)
- `LocalDate` → `1702944000.0` (полночь UTC в этот день)
- `LocalTime` → `36000.0` (количество секунд от начала суток)
- `Duration` → `3600.0` (длительность в секундах)

---

## 🔐 Аутентификация

### Регистрация

**POST** `/auth/register`

**Request:**
```json
{
  "username": "user123",
  "password": "secret"
}
```

**Response:**
```json
{
  "username": "user123",
  "token": "jwt-token-string",
  "success": true
}
```

---

### Вход

**POST** `/auth/login`

**Request:**
```json
{
  "username": "user123",
  "password": "secret"
}
```

**Response:**
```json
{
  "username": "user123",
  "token": "jwt-token-string",
  "success": true
}
```

---

### Проверка авторизации

**GET** `/auth/check`

**Response:**
```json
{
  "username": "user123",
  "success": true
}
```

---

## 👤 Профиль (требует авторизации)

### Обновить профиль

**POST** `/profile/update`

**Request:**
```json
{
  "birth_date": 915148800.0,
  "gender": "Male",
  "physical_activity": "OnceADay",
  "caffeine_consumption": "TwiceAWeek",
  "alcohol_consumption": "Never",
  "asleep_time": 82800.0,
  "awake_time": 25200.0
}
```

> `birth_date` – 01.01.1999 UTC, `asleep_time` – 23:00, `awake_time` – 07:00

**Response:**
```json
{
  "success": true,
  "message": null
}
```

---

### Получить профиль

**GET** `/profile/get`

**Response:**
```json
{
  "data": {
    "birth_date": 915148800.0,
    "gender": "Male",
    "physical_activity": "OnceADay",
    "caffeine_consumption": "TwiceAWeek",
    "alcohol_consumption": "Never",
    "asleep_time": 82800.0,
    "awake_time": 25200.0
  },
  "message": null,
  "success": true
}
```

---

## 🛌 Сон (требует авторизации)

### Загрузить данные сна

**POST** `/sleep/upload`

**Request:**
```json
[
  {
    "timestamp": 1713397200.0,
    "pulse": 62,
    "phase": "LIGHT"
  },
  {
    "timestamp": 1713400800.0,
    "pulse": 60,
    "phase": "DEEP"
  }
]
```

**Response:**
```json
{
  "success": true,
  "message": null
}
```

---

### Сформировать отчет о сне за период

**POST** `/sleep/make-report`

**Request:**
```json
{
  "from": 1713300000.0,
  "to": 1713904800.0
}
```

**Response:**
```json
{
  "data": {
    "total_sleep": 28800.0,
    "awakenings": 2,
    "avg_awake": 900.0,
    "avg_asleep": 7200.0,
    "data": [
      {
        "timestamp": 1713397200.0,
        "pulse": 62,
        "phase": "LIGHT"
      }
    ]
  },
  "message": null,
  "success": true
}
```

---

### Получить последнюю сессию сна

**GET** `/sleep/last-sleep`

**Response:**
```json
{
  "data": {
    "start_time": 1713393600.0,
    "end_time": 1713422400.0,
    "report": {
      "total_sleep": 28800.0,
      "awakenings": 1,
      "avg_awake": 600.0,
      "avg_asleep": 3600.0,
      "data": [...]
    }
  },
  "message": null,
  "success": true
}
```

---

### Рассчитать рекомендованное время сна

**POST** `/sleep/calculate-recommended-asleep-time`

**Request:**
```json
{
  "asleep_time": 82800.0,
  "awake_time": 25200.0
}
```

**Response:**
```json
{
  "data": {
    "asleep_time": 81900.0,
    "awake_time": 25200.0
  },
  "message": null,
  "success": true
}
```

---

## 📦 Форматы ответов

### DataResponse<T>
```json
{
  "data": T,
  "message": "string or null",
  "success": true
}
```

### SimpleResponse
```json
{
  "success": true/false,
  "message": "string or null"
}
```

---

## 🛡️ Авторизация

Для всех защищённых эндпоинтов (`/profile`, `/sleep/*`) необходимо добавить заголовок:

```
Authorization: Bearer <jwt-token>
```

---

Вот документация для микросервисов, подписывающихся на **KeyDB pub/sub каналы**. Она описывает, какие сообщения ожидаются по каждому каналу, кто инициирует запрос, и какой микросервис (DB или Logic) должен на него отреагировать.

---

# 📡 KeyDB: Протокол взаимодействия микросервисов

Обмен сообщениями между микросервисами происходит через **KeyDB** по модели **Request-Response** в формате JSON.

Каждое сообщение отправляется на именованный канал и содержит:
- **Request**: входные данные
- **Response**: результат обработки

---

## 🗂 Структура обёртки (если используется)

Для большинства сообщений используется следующая обёртка:

```json
{
  "username": "user123",
  "data": { ... }
}
```

---

## 📬 Каналы и обработчики

### 🔹 `get-user`
- **Кому**: `DB`
- **Тип запроса**: `String` (username)
- **Ответ**: `User`

#### Пример запроса:
```json
"user123"
```

#### Пример ответа:
```json
{
  "username": "user123",
  "password": "hashed-password"
}
```

---

### 🔹 `save-user`
- **Кому**: `DB`
- **Тип запроса**: `User`
- **Ответ**: `Boolean`

#### Пример запроса:
```json
{
  "username": "user123",
  "password": "hashed-password"
}
```

#### Пример ответа:
```json
true
```

---

### 🔹 `update-profile`
- **Кому**: `DB`
- **Тип запроса**: `UserRequest<Profile>`
- **Ответ**: `Boolean`

#### Пример запроса:
```json
{
  "username": "user123",
  "data": {
    "birth_date": 915148800.0,
    "gender": "Male",
    "physical_activity": "OnceADay",
    "caffeine_consumption": "TwiceAWeek",
    "alcohol_consumption": "Never",
    "asleep_time": 82800.0,
    "awake_time": 25200.0
  }
}
```

---

### 🔹 `get-profile`
- **Кому**: `DB`
- **Тип запроса**: `String` (username)
- **Ответ**: `Profile`

---

### 🔹 `upload-sleep`
- **Кому**: `DB`
- **Тип запроса**: `UserRequest<SleepData>`
- **Ответ**: `Boolean`

#### Пример запроса:
```json
{
  "username": "user123",
  "data": [
    {
      "timestamp": 1713397200.0,
      "pulse": 62,
      "phase": "LIGHT"
    }
  ]
}
```

---

### 🔹 `make-sleep-report`
- **Кому**: `Logic`
- **Тип запроса**: `UserRequest<Period>`
- **Ответ**: `Report`

#### Пример запроса:
```json
{
  "username": "user123",
  "data": {
    "from": 1713300000.0,
    "to": 1713904800.0
  }
}
```

---

### 🔹 `get-last-sleep`
- **Кому**: `Logic`
- **Тип запроса**: `String` (username)
- **Ответ**: `SleepSession`

---

### 🔹 `calculate-recommended-asleep-time`
- **Кому**: `Logic`
- **Тип запроса**: `UserRequest<TimePreference>`
- **Ответ**: `TimePreference`

---

## ✅ Общие правила

- **Типы данных** сериализуются через Jackson с включенной опцией `WRITE_DATES_AS_TIMESTAMPS`.
- Все даты и времена (например, `Instant`, `LocalDate`, `Duration`) передаются как **числа** — количество секунд.
- Ответ всегда ожидается синхронно (в рамках pub/sub паттерна KeyDB).

---

## Спецификация всех `data` классов в формате JSON

---

### 🔹 `User`
```json
{
  "username": "user123",
  "password": "secure-password"
}
```

---

### 🔹 `AuthRequest`
```json
{
  "username": "user123",
  "password": "secure-password"
}
```

---

### 🔹 `AuthResponse`
```json
{
  "username": "user123",
  "token": "jwt.token.here",
  "success": true
}
```

---

### 🔹 `CheckAuthResponse`
```json
{
  "username": "user123",
  "success": true
}
```

---

### 🔹 `SimpleResponse`
```json
{
  "success": true,
  "message": "Operation completed successfully"
}
```

---

### 🔹 `DataResponse<T>` (обобщённый пример)
```json
{
  "data": { /* тип данных зависит от запроса */ },
  "message": null,
  "success": true
}
```

---

### 🔹 `Profile`
```json
{
  "birth_date": 915148800.0,
  "gender": "Male",
  "physical_activity": "OnceADay",
  "caffeine_consumption": "TwiceAWeek",
  "alcohol_consumption": "Never",
  "asleep_time": 82800.0,
  "awake_time": 25200.0
}
```

---

### 🔹 `TimePreference`
```json
{
  "asleep_time": 82800.0,
  "awake_time": 25200.0
}
```

---

### 🔹 `Period`
```json
{
  "from": 1713300000.0,
  "to": 1713904800.0
}
```

---

### 🔹 `SleepDataPiece`
```json
{
  "timestamp": 1713397200.0,
  "pulse": 62,
  "phase": "LIGHT"
}
```

---

### 🔹 `SleepData` (typealias `List<SleepDataPiece>`)
```json
[
  {
    "timestamp": 1713397200.0,
    "pulse": 62,
    "phase": "DEEP"
  },
  {
    "timestamp": 1713400800.0,
    "pulse": 65,
    "phase": "REM"
  }
]
```

---

### 🔹 `Report`
```json
{
  "total_sleep": 28800.0,
  "awakenings": 2,
  "avg_awake": 600.0,
  "avg_asleep": 7200.0,
  "data": [ /* SleepData */ ]
}
```

---

### 🔹 `SleepSession`
```json
{
  "start_time": 1713393600.0,
  "end_time": 1713422400.0,
  "report": { /* Report */ }
}
```

---

### 🔹 `UserRequest<T>` (обёртка)
Пример с `Profile`:
```json
{
  "username": "user123",
  "data": {
    "birth_date": 915148800.0,
    "gender": "Female",
    "physical_activity": "Rarely",
    "caffeine_consumption": "Often",
    "alcohol_consumption": "Never",
    "asleep_time": 79200.0,
    "awake_time": 25200.0
  }
}
```

---
