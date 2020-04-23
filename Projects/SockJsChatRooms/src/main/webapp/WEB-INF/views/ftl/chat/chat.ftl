<#import "*/page.ftl" as p>
<@p.page title="Room ${channel.id} - ChatRooms" jquery=true>
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center;">
        <h3 style="margin: 0">ChatRooms</h3>

        <div style="min-width: 50vw">
            <h4 style="margin: 0">Room ${channel.id}<#if channel.name??>, ${channel.name}</#if></h4>

            <p style="margin: 0">Participants:
            <#list channel.users as channel_to_user>
                <#if channel_to_user.user.id == user.id>
                    <b>${channel_to_user.user.name}</b>
                <#else>
                    ${channel_to_user.user.name}
                </#if>
            </#list>
            </p>

            <hr>

            <div id="chat_window" style="height:50vh; overflow-y: scroll; display: flex; flex: 1 1 0; flex-flow: column">
                <#if !messages?? || messages?size == 0>
                    <h4 style="margin: 0" id="chat_empty">У вас пока нет сообщений</h4>
                <#else>
                    <#list messages as message>
                        <div id="chat_message">
                            <@p.message message=message/>
                        </div>
                        <hr>
                    </#list>
                </#if>
            </div>

            <hr>

            <form style="display: flex; justify-content: space-between">
                <input tabindex="1" type="text" id="message_content" name="content" placeholder="Your message" style="width: 100%">
                <input type="hidden" value="${channel.id}" id="channel_id">
                <input type="hidden" value="${user.id}" id="user_id">
                <div>
                    <button tabindex="2" type="button" onclick="sendMessage()"><b>Отправить</b></button>
                </div>
            </form>
        </div>

    </div>

    <script src="${rc.getContextPath()}/static/js/sockjs.js"></script>
    <script src="${rc.getContextPath()}/static/js/chat.js"></script>
</@p.page>