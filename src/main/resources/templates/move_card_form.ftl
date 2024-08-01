<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Доставка и получение карты</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5">
    <h2 class="text-center mb-2">Доставка и получение карты</h2>
    <h3 class="text-center mb-4">Получить карту можно только в дочернем отделении!</h3>
    <div class="row">
        <div class="col-md-6">
            <div class="text-start mb-4">
                <#if createdCard??>
                    <h4 class="mb-3">Информация о карте</h4>
                    <p>Номер карты: ${createdCard.getMaskPanNumber()}</p>
                    <p>Владелец: ${createdCard.owner.firstName} ${createdCard.owner.lastName}</p>
                    <p>Адрес владельца: ${createdCard.owner.ownerAddress}</p>
                    <p>Дата рождения владельца: ${createdCard.owner.dateOfBirth}</p>
                    <#if createdCard.currentLocation??>
                        <p>Текущая локация: ${createdCard.currentLocation.name}</p>
                    <#else>
                        <p>Карта выдана клиенту</p>
                    </#if>
                </#if>
            </div>
        </div>
        <div class="col-md-6">
            <div class="d-flex flex-column align-items-start gap-2">
                <form action="/move_card" method="post" class="w-100 d-grid gap-2">
                    <input type="hidden" name="_method" value="put">
                    <input type="hidden" name="cardId" value="${createdCard.id?c}">
                    <div class="mb-3">
                        <label for="toBranchId" class="form-label">Выберите отделение:</label>
                        <select id="toBranchId" name="toBranchId" class="form-select" <#if createdCard.status == 'RECEIVED'>disabled</#if>>
                            <#list allBranches as branch>
                                <#if createdCard.currentLocation?? && branch.id != createdCard.currentLocation.id>
                                    <option value="${branch.id}">${branch.name}</option>
                                </#if>
                            </#list>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary" <#if createdCard.status == 'RECEIVED'>disabled</#if>>Переместить карту</button>
                </form>
                <form action="/receive_card" method="post" class="w-100 d-grid gap-2">
                    <input type="hidden" name="_method" value="put">
                    <input type="hidden" name="cardId" value="${createdCard.id?c}">
                    <button type="submit" class="btn btn-success" <#if (createdCard.currentLocation?? && createdCard.currentLocation.mainBranch) || createdCard.status == 'RECEIVED'>disabled</#if>>Получить карту</button>
                </form>
                <form action="/" method="get" class="w-100 d-grid gap-2">
                    <button type="submit" class="btn btn-secondary">Вернуться на главную</button>
                </form>
            </div>
        </div>
    </div>
    <#if successMessage??>
        <div class="alert alert-success mt-4" role="alert">
            ${successMessage}
        </div>
    </#if>
</div>
</body>
</html>
