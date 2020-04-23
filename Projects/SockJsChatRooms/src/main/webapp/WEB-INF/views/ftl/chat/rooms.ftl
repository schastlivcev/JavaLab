<#import "*/page.ftl" as p>
<@p.page title="ChatRooms">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center;">
        <h3 style="margin: 0">ChatRooms</h3>
        <h4 style="margin: 0">Welcome ${user.name}!</h4>
        <#if channels??>
            <h4 style="margin: 0">You can <a href="/rooms/create"><b>create new room</b></a> or join rooms:</h4>

            <hr style="min-width: 50vw">
            <#list channels as channel>
                <div style="min-width: 50vw">
                    <@p.room channel=channel me=user/>
                    <hr>
                </div>
            </#list>
        <#else>
            <h4 style="margin: 0">You can only <a href="/rooms/create"><b>create new room</b></a></h4>
        </#if>
    </div>
</@p.page>