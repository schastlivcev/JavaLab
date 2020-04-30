<#import "*/page.ftl" as p>
<@p.page title="Друзья - ArticleFeed">
    <@p.navbar active="friends"/>
    <div class="d-flex flex-column m-2 wid mx-auto">

        <input id="csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <#list friends as friend>
            <#if !responsed??>
                <#if friend.status == "REQUESTED" && me.id == friend.userRecipient.id>
                    <#assign responsed = true>
                </#if>
            </#if>
            <#if !requested??>
                <#if friend.status == "REQUESTED" && me.id == friend.userSender.id>
                    <#assign requested = true>
                </#if>
            </#if>
            <#if !accepted??>
                <#if friend.status == "ACCEPTED">
                    <#assign accepted = true>
                </#if>
            </#if>
        </#list>

        <#if responsed??>
            <div class="card m-1 p-2 friend">
                <div class="input-group p-0 d-inline-flex">
                    <h4 class="mt-0 mb-2"><b class="darkblue">Заявки в друзья</b></h4>
                </div>
                <hr class="mb-1">
                <#list friends as friend>
                    <#if friend.status == "REQUESTED" && me.id != friend.userSender.id>
                        <div class="mt-1">
                            <@p.friend_card friends=friend me=me card=false/>
                        </div>
                    </#if>
                </#list>
            </div>
        </#if>

        <#if requested??>
            <div class="card m-1 p-2 friend">
                <div class="input-group p-0 d-inline-flex">
                    <h4 class="mt-0 mb-2"><b class="darkblue">Отправленные заявки</b></h4>
                </div>
                <hr class="mb-1">
                <#list friends as friend>
                    <#if friend.status == "REQUESTED" && me.id == friend.userSender.id>
                        <div class="mt-1">
                            <@p.friend_card friends=friend me=me card=false/>
                        </div>
                    </#if>
                </#list>
            </div>
        </#if>

        <#if accepted??>
            <#list friends as friend>
                <#if friend.status == "ACCEPTED">
                    <@p.friend_card friends=friend me=me />
                </#if>
            </#list>
        <#else>
            <div class="card m-1 p-2 text-center">
                <p class="darkblue"><b>У вас пока нет друзей</b></p>
            </div>
        </#if>

    </div>
</@p.page>