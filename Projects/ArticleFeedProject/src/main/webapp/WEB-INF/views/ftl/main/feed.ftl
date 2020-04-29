<#import "*/page.ftl" as p>
<@p.page title="Закладки - ArticleFeed">
    <@p.navbar active="feed"/>

    <div class="d-flex flex-column m-2 wid mx-auto">
        <#if error?? && error == true>
            <div class="card m-1 p-2 text-center">
                <p class="darkblue"><b>Ошибка при загрузке ленты<br>Перезагрузите страницу</b></p>
            </div>
        <#else>
            <div id="empty_bookmarks" style="display: <#if !walls?? || walls?size == 0>block<#else>none</#if>" class="card m-1 p-2 text-center">
                <p class="darkblue"><b>Лента пуста</b></p>
            </div>
            <#if walls?? || walls?size gt 0>
                <#list walls as wall>
                    <#assign bookmarked=false>
                    <#if bookmarksId?seq_contains(wall.article.id)>
                        <#assign bookmarked=true>
                    </#if>
                    <#assign replied=false>
                    <#if repliesId?seq_contains(wall.article.id)>
                        <#assign replied=true>
                    </#if>
                    <@p.article wall=wall me=me replied=replied bookmarked=bookmarked />
                </#list>
            </#if>
        </#if>
    </div>
</@p.page>