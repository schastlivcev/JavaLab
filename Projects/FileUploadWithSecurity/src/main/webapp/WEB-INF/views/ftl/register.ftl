<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Sign Up</title>
</head>
<body style="display: flex; justify-content: center">
<form method="post">
    <h3 style="margin: 0">Sign Up</h3>
    <#if success?? && success == true>
        <h3 style="color: green">Dear ${name}, a confirmation letter has been sent on ${email}. Check it to complete signing up.</h3>
    <#else>
        <#if errors??>
            <#list errors as error>
                <h4 style="color: red; margin: 0">${error}</h4>
            </#list>
        </#if>
        <label>Email</label>
        <input type="email" name="email" <#if email??>value="${email}"</#if> /><br>

        <label>Password</label>
        <input type="password" name="password" /><br>

        <label>Repeat password</label>
        <input type="password" name="reppassword" /><br>

        <label>Name</label>
        <input type="text" name="name" <#if name??>value="${name}"</#if> /><br>

        <label>Male</label>
        <input type="radio" name="sex" value="male" <#if !sex?? || sex == "male">checked</#if> />
        <label>Female</label>
        <input type="radio" name="sex" value="female" <#if sex?? && sex == "female">checked</#if> /><br>

        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <input type="submit" value="Sign Up" />
    </#if>
</form>
</body>
</html>