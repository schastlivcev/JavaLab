<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Upload files</title>
</head>
<body style="display: flex; justify-content: center">
<#if status?? && status == "success">
    <h2 style="color: green">Загрузка файла на сервер прошла успешно</h2>
<#else>
    <h2 style="color: red">Ошибка загрузки файла на сервере</h2>
</#if>
</body>
</html>