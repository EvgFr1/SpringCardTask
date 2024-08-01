<!DOCTYPE html>
<html>
<head>
    <title>Мои карты</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
    <h1 class="text-center">Мои карты</h1>
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
                <td>
                    <#if card.currentLocation??>
                        <p>${card.currentLocation.name}</p>
                    <#else>
                        <p>Карта получена</p>
                    </#if>
                </td>
                <td>${card.status}</td>
                <td>
                    <form action="/receive_card_from_myCards" method="post" style="display:inline;">
                        <input type="hidden" name="_method" value="put">
                        <input type="hidden" name="cardId" value="${card.id}">
                        <button type="submit" class="btn btn-success btn-sm"
                                <#if (card.currentLocation?? && card.endPoint.name != card.currentLocation.name) || (!card.currentLocation??) || (card.status == "RECEIVED") >disabled</#if>>Получить карту</button>
                    </form>
                </td>
            </tr>
        </#list>
        </tbody>
    </table>
</div>
</body>
</html>
