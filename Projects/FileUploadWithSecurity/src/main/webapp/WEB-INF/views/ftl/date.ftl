<!doctype html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<#macro time_from_now time>
    <#assign current_time = .now?long />
    <#assign from_now = time?datetime?long />
    <#if current_time - from_now < 30 * 86400000>
<#--    Today -->
        <#if current_time - from_now < 86400000>
<#--        Less than hour -->
            <#if current_time - from_now < 3600000>
                <#assign minutes = ((current_time - from_now) / 60000)?int />
                <#if minutes % 10 == 1>
                    ${minutes} минуту назад
                <#elseif minutes % 10 == 2 || minutes % 10 == 3 || minutes % 10 == 4>
                    ${minutes} минуты назад
                <#else>
                    ${minutes} минут назад
                </#if>
<#--        In hours -->
            <#else>
                <#assign hours = ((current_time - from_now) / 3600000)?int />
                <#if hours == 1 || hours == 21>
                    ${hours} час назад
                <#elseif hours == 2 || hours == 3 || hours == 4 || hours == 22 || hours == 23>
                    ${hours} часа назад
                <#else>
                    ${hours} часов назад
                </#if>
            </#if>
<#--            Сегодня в ${from_now?number_to_time}-->
        <#elseif current_time - from_now < 2 * 86400000>
            Вчера в ${from_now?number_to_time}
        <#elseif current_time - from_now < 3 * 86400000>
            Позавчера в ${from_now?number_to_time}
        <#else>
            <#assign days = ((current_time - from_now) / 86400000)?int />
            <#if days < 5 || (days gt 21 && days < 25)>
                ${days} дня назад в ${from_now?number_to_time}
            <#elseif days == 21>
                ${days} день назад в ${from_now?number_to_time}
            <#else>
                ${days} дней назад в ${from_now?number_to_time}
            </#if>
        </#if>
    <#else>
        ${from_now?number_to_datetime}
    </#if>
</#macro>
<h2><@time_from_now time="${time}"/></h2>
</body>
</html>