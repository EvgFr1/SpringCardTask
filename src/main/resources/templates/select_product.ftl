<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Выбор продукта</title>
    <link rel="stylesheet" href="/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container mt-5 d-flex flex-column align-items-center">
    <h1 class="text-center mb-4">Выберите тип продукта и платежную систему</h1>
    <form action="/create_card" method="post" class="w-50 d-grid gap-2">
        <div>
            <label for="productType" class="form-label">Тип продукта:</label>
            <select id="productType" name="productType" class="form-select" required>
                <option value="PREMIUM">Премиум</option>
                <option value="REGULAR">Обычная</option>
                <option value="SALARY">Зарплатная</option>
                <option value="CHILD">Детская</option>
            </select>
        </div>
        <div>
            <label for="paymentSystem" class="form-label">Платежная система:</label>
            <select id="paymentSystem" name="paymentSystem" class="form-select" required>
                <option value="VISA">VISA</option>
                <option value="MASTERCARD">MasterCard</option>
                <option value="MIR">MIR</option>
            </select>
        </div>
        <div>
            <label for="bankBranch" class="form-label">Банковское отделение:</label>
            <select id="bankBranch" name="bankBranch" class="form-select" required>
                <#if mainBranches??>
                    <#list mainBranches as branch>
                        <option value="${branch.id}">${branch.name}</option>
                    </#list>
                <#else>
                    <option disabled>Нет доступных отделений</option>
                </#if>
            </select>
        </div>
        <button type="submit" class="btn btn-primary mt-4">Создать карту</button>
    </form>
</div>
</body>
</html>
