<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Регистрация</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5 d-flex flex-column align-items-center">
    <div class="text-center mb-4">
        <h1>Регистрация</h1>
        <p>Пожалуйста, заполните форму для регистрации.</p>
    </div>
    <form action="/register" method="post" class="w-50">
        <div class="form-group mb-3">
            <label for="username">Имя пользователя:</label>
            <input type="text" id="username" name="username" class="form-control" required>
        </div>
        <div class="form-group mb-3">
            <label for="password">Пароль:</label>
            <input type="password" id="password" name="password" class="form-control" required>
        </div>
        <div class="form-group mb-3">
            <label for="passwordConfirm">Подтвердите пароль:</label>
            <input type="password" id="passwordConfirm" name="passwordConfirm" class="form-control" required>
        </div>
        <div class="form-group mb-3">
            <input type="submit" value="Зарегистрироваться" class="btn btn-primary w-100">
        </div>
    </form>
    <p><a href="/login">Вход</a></p>
    <#if error??>
        <div class="alert alert-danger w-50 mt-3" role="alert">${error}</div>
    </#if>
</div>
</body>
</html>
