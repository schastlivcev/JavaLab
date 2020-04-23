<#macro page title jquery=false>
    <!doctype html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport"
              content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">

        <title>${title}</title>

        <#if jquery == true>
            <script src="${rc.getContextPath()}/static/js/jquery-3.4.1.min.js"></script>
        </#if>
    </head>
    <body>
    <#nested/>
    </body>
    </html>
</#macro>

<#macro message message>
    <div>
        <div style="display: flex; justify-content: space-between">
            <p style="margin: 0"><b>${message.author.name}</b></p>
            <p style="margin:0; color: grey">${message.createdAt}</p>
        </div>
        <p style="margin: 0">${message.content}</p>
    </div>
</#macro>

<#macro room channel me>
    <div>
        <div style="display: flex; justify-content: space-between">
            <#if channel.name??>
                <h4 style="margin: 0">${channel.id}, ${channel.name}</h4>
            <#else>
                <h4 style="margin: 0">${channel.id}</h4>
            </#if>
            <a href="/chat?r=${channel.id}">Join</a>
        </div>
        <p style="margin: 0">participants:
            <#list channel.users as channel_to_user>
                <#if channel_to_user.user.id == me.id>
                    <b>${channel_to_user.user.name}</b>
                <#else>
                    ${channel_to_user.user.name}
                </#if>
            </#list>
        </p>
    </div>
</#macro>