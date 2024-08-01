<!DOCTYPE html>
<html>
<head>
    <title>Главная страница оператора</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body>
<header>
    <h1>Добро пожаловать, ${username}!</h1>
    <p>Вы находитесь в отделении: ${currentBranch.name}</p>
    <nav>
        <a href="/profile">Профиль</a>
        <a href="/logout">Выйти</a>
    </nav>
</header>

<main class="container mt-4">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Номер карты</th>
            <th>Тип продукта</th>
            <th>Платежная система</th>
            <th>Текущая локация</th>
            <th>Статус</th>
            <th>Действия</th>
        </tr>
        </thead>
        <tbody>
        <#list cards as card>
            <tr>
                <td>${card.maskPanNumber}</td>
                <td>${card.productType}</td>
                <td>${card.paymentSystem}</td>
                <td>${card.currentLocation.name}</td>
                <td>${card.status}</td>
                <td>
                    <form action="/move_card" method="get" style="display:inline;">
                        <input type="hidden" name="cardId" value="${card.id}">
                        <button type="submit" class="btn btn-primary btn-sm">Переместить карту</button>
                    </form>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</main>

<footer class="mt-4">
    <p>&copy; 2024 Моя карта</p>
</footer>
</body>
</html>
