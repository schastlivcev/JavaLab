<#import "*/page.ftl" as p>
<@p.page title="Закладки - ArticleFeed">
    <@p.navbar active="feed"/>

    <div class="d-flex flex-column m-2 wid mx-auto">

        <input id="csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <script type="text/javascript" src="${rc.getContextPath()}/static/js/rss2json.js"></script>
        <script type="text/javascript" src="${rc.getContextPath()}/static/js/rss.js"></script>

        <div id="google_news_feed" class="card m-1 p-2">
            <div class="d-flex justify-content-between">
                <h4 class="darkblue mb-1"><b>Последние новости</b></h4>
                <h6 class="activegrey m-0 mt-1">при поддержке rss2json и Лента.ру</h6>
            </div>
            <p id="news_load" class="lightblue m-0 text-center"><b>Загрузка...</b></p>
            <div class="alert alert-danger my-1 p-2 text-center pagealert" id="news_load_error" role="alert">
                <h5 class="m-0">Не удалось загрузить. Сервер агрегатора не отвечает.</h5>
            </div>
            <div class="alert alert-danger my-1 p-2 text-center pagealert" id="news_server_error" role="alert">
                <h5 class="m-0">Не удалось загрузить. Ошибка на сервере агрегатора.</h5>
            </div>
        </div>

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