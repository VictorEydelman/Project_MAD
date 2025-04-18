# external device

## Описание
Микросервис для имитации работы носимого устройства (умных часов), который:
- Генерирует данные о пульсе и фазах сна
- Определяет фазу сна на основе ЧСС
- Отдаёт данные по REST API

## Параметры
- Порт: 8909 (можно изменить в docker-compose.yml)
- Внутренний порт приложения: 8909

## API Endpoints
Проверка работы
`GET /health`

Пример ответа: `{
"status": "OK",
"message": "Service is running"
}`

Получение данных `GET /data?lastSync=<timestamp>` (lastSync (опционально) - временная метка в ISO-формате (например, 2025-04-13T10:00:00Z))

Пример ответа: ```{
"data": [
{
"timestamp": "2025-04-13T22:00:00Z",
"heartRate": 65,
"sleepPhase": "AWAKE"
}
]
}```
