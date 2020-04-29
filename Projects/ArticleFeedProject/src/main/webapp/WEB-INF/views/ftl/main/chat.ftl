<#import "*/page.ftl" as p>
<@p.page title="Сообщения - ArticleFeed">
    <@p.navbar active="user"/>
    <div class="d-flex flex-column m-2 wid mx-auto">

        <div class="card mt-1 p-2 d-flex justify-content-between" style="height: 92vh">

            <h4 class="mt-0 mb-2 activegrey">Диалог с <a href="${rc.getContextPath()}/user/${user.id}"><b style="color: black">${user.name} ${user.surname}</b></a></h4>
            <hr class="mb-2 mt-0">

            <div id="chat_window" class="my-2" style="overflow-y: scroll; word-break: break-word; display: flex; flex: 1 1 0; flex-flow: column">
                <#if !messages?? || messages?size == 0>
                    <h4 class="mb-2" id="chat_empty">У вас пока нет сообщений</h4>
                <#else>
                    <#list messages as message>
                        <div id="chat_message" class="m-2">
                            <@p.message message=message/>
                        </div>
                    </#list>
                </#if>
            </div>

            <form class="input-group d-inline-flex align-self-end">
                <input tabindex="1" type="text" class="form-control" id="message_content" name="content" placeholder="Сообщение">
                <input type="hidden" value="${pageId}" id="page_id">
                <div class="input-group-append">
                    <button tabindex="2" class="btn btn-outline-primary btnlight py-1 px-3 m-0" type="button" onclick="sendMessage()"><b>Отправить</b></button>
                </div>
            </form>
        </div>

    </div>

    <script src="${rc.getContextPath()}/static/js/chat.js"></script>
</@p.page>