<#import "*/page.ftl" as p>
<@p.page title="Закладки - ArticleFeed">
    <@p.navbar active="bookmarks"/>

    <div class="d-flex flex-column m-2 wid mx-auto">

        <input id="csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <#if error?? && error == true>
            <div class="card m-1 p-2 text-center">
                <p class="darkblue"><b>Ошибка при загрузке закладок<br>Перезагрузите страницу</b></p>
            </div>
        <#else>
            <div id="empty_bookmarks" style="display: <#if !bookmarks?? || bookmarks?size == 0>block<#else>none</#if>" class="card m-1 p-2 text-center">
                <p class="darkblue"><b>У вас пока нет закладок</b></p>
            </div>
            <#if bookmarks?? || bookmarks?size gt 0>
                <#list bookmarks as bookmark>
                    <#assign replied=false>
                    <#if repliesId?seq_contains(bookmark.article.id)>
                        <#assign replied=true>
                    </#if>
                    <@p.article wall=bookmark me=me replied=replied bookmarked=true bookmarks_page=true />
                </#list>
            </#if>
        </#if>
    </div>
</@p.page>