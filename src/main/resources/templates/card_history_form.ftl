<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Просмотр статуса карты</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5 d-flex flex-column align-items-center">
    <h1 class="mb-4">Просмотр статуса карты</h1>
    <form action="/view_card_history" method="get" class="w-50 d-grid gap-2">
        <div class="mb-3">
            <label for="cardNumber" class="form-label">Введите номер карты:</label>
            <input type="text" id="cardNumber" name="cardNumber" class="form-control" required>
        </div>
        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
            <button type="submit" formaction="/view_card_history" class="btn btn-primary">Посмотреть историю перемещений</button>
            <button type="submit" formaction="/view_card_status_and_history" class="btn btn-success">Посмотреть статус и историю перемещений</button>
        </div>
    </form>
</div>
</body>
</html>
