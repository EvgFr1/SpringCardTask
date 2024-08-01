<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Сообщение о создании карты</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <#if status == "success">
        <div class="alert alert-success" role="alert">
            Карта успешно создана!
        </div>
    <#elseif status == "error">
        <div class="alert alert-danger" role="alert">
            Произошла ошибка при создании карты. Пожалуйста, попробуйте еще раз.
        </div>
    </#if>
    <div class="text-center mt-4">
        <a href="/" class="btn btn-primary">Вернуться на главную</a>
    </div>
</div>
</body>
</html>
