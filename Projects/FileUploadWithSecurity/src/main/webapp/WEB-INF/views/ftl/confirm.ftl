<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Confirmation</title>
</head>
<body style="display: flex; justify-content: center">
<#if !status??>
    <h2 style="color: red">You have not signed in yet!</h2>
</#if>
<#if status?? && status == 'expired'>
    <h2 style="color: red">Dear ${name}, your confirmation has already expired.<br>Please register again.</h2>
</#if>
<#if status?? && status == 'verified'>
    <form action="/login" method="get">
        <h2 style="color: green; margin-bottom: 0">Congratulations, ${name}! You have just confirmed registration successfully!</h2><br>
        <h3 style="color: grey; margin-top: 0">Now you may sign in:</h3>
        <input type="submit" value="Sign In" />
    </form>
</#if>
</body>
</html>