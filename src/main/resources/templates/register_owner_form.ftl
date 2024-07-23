<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Регистрация клиента</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5 d-flex flex-column align-items-center">
    <h1 class="mb-4">Регистрация клиента</h1>
    <form action="/submit_owner" method="post" class="w-50 d-grid gap-2">
        <div>
            <label for="lastName" class="form-label">Фамилия:</label>
            <input type="text" id="lastName" name="lastName" class="form-control" required>
        </div>

        <div>
            <label for="firstName" class="form-label">Имя:</label>
            <input type="text" id="firstName" name="firstName" class="form-control" required>
        </div>

        <div>
            <label for="middleName" class="form-label">Отчество:</label>
            <input type="text" id="middleName" name="middleName" class="form-control" required>
        </div>

        <div>
            <label for="dateOfBirth" class="form-label">Дата рождения:</label>
            <input type="date" id="dateOfBirth" name="dateOfBirth" class="form-control" required>
        </div>

        <div>
            <label for="ownerAddress" class="form-label">Адрес:</label>
            <input type="text" id="ownerAddress" name="ownerAddress" class="form-control" required>
        </div>

        <button type="submit" class="btn btn-primary mt-4">Продолжить</button>
    </form>
</div>
</body>
</html>
