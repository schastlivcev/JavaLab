<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Sign In</title>
</head>
<body style="display: flex; justify-content: center">
<form method="post">
    <h3 style="margin: 0">Sign In</h3>
    <#if errors??>
        <#list errors as error>
            <h4 style="color: red; margin: 0">${error}</h4>
        </#list>
    </#if>
    <label>Email</label>
    <input type="email" name="email" <#if email??>value="${email}"</#if> /><br>

    <label>Password</label>
    <input type="password" name="password" /><br>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    <input type="submit" value="Sign In" />
</form>
</body>
</html>