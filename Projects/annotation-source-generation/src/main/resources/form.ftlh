<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${form.name}</title>
</head>
<body>
<form method="${form.method}" action="${form.action}">
    <#list inputs as input>
        <input type="${input.type}" name="${input.name}" placeholder="${input.placeholder}">
    </#list>
    <#if inputs?size gt 0>
        <input type="submit">
    </#if>
</form>
</body>
</html>