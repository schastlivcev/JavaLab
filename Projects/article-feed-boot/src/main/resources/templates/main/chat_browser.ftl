<#import "*/page.ftl" as p>
<@p.page title="Сообщения - ArticleFeed">
    <@p.navbar active="chat"/>

    <div class="d-flex flex-column m-2 wid mx-auto">

        <input id="csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <#if error?? && error == true>
            <div class="card m-1 p-2 text-center">
                <p class="darkblue"><b>Ошибка при загрузке сообщений<br>Перезагрузите страницу</b></p>
            </div>
        <#else>
            <div id="empty_bookmarks" style="display: <#if !messages?? || messages?size == 0>block<#else>none</#if>" class="card m-1 p-2 text-center">
                <p class="darkblue"><b>У вас пока нет бесед</b></p>
            </div>
            <#if messages?? && messages?size gt 0>
                <div class="card m-1 p-2 friend">
                    <div class="d-flex justify-content-between">
                        <h4 class="darkblue"><b>Беседы</b></h4>
                        <a href="${rc.getContextPath()}/createChat" class="m-0 mt-1 p-0"><i class="fas fa-plus iconstl align-self-center"></i></a>
                    </div>
                    <#list messages as message>
                        <#list channelUsers as channelId, users>
                            <#if message.channel.id == channelId>
                                <hr class="my-1">
                                <div class="my-1">
                                    <@p.chat_preview message=message users=users me=me />
                                </div>
                            </#if>
                        </#list>
                    </#list>
                </div>
            </#if>
        </#if>
    </div>
</@p.page>