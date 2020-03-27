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
<script src="${rc.getContextPath()}/static/js/jquery.js"></script>
<script>
    function sendFile() {
        let formData = new FormData($('form#data'));
        $.ajax({
            type: "POST",
            url: "/files",
            data: formData,
            processData: false,
            contentType: false
        })
            .done(function (response) {
                alert(response)
            })
            .fail(function () {
                alert('Ошибка отправления запроса. Пожалуйста, попробуйте снова.')
            });
        return false;
    }
</script>
<div>
    <h2 style="color: green">Добро пожаловать, ${name}! Теперь вы можете загружать файлы на наш сервер!</h2>
    <h3>Выберите файл, который хотите выгрузить на сервер и нажмите кнопку загрузки. Ссылка на доступ к файлу будет отправлена на вашу почту.</h3>
    <form id="data" method="post" enctype="multipart/form-data">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <input type="file" id="file" name="file" placeholder="Имя файла..."/>
        <button onclick="sendFile()">Загрузить файл</button>
    </form>
</div>
</body>
</html>