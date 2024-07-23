<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Статус карты</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <h2 class="mb-3 text-center">История перемещений</h2>
        <table class="table table-bordered w-75 mx-auto">
            <thead>
            <tr>
                <th scope="col">Откуда</th>
                <th scope="col">Куда</th>
            </tr>
            </thead>
            <tbody>
            <#list cardMovements as movement>
                <tr>
                    <td>${movement.fromLocation.name}</td>
                    <td>${movement.toLocation.name}</td>
                </tr>
            </#list>
            </tbody>
        </table>
</div>
</body>
</html>
