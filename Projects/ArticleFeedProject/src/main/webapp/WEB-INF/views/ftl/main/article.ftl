<#import "*/page.ftl" as p>
<@p.page title="Запись ${wall.id} - ArticleFeed">
    <@p.navbar active="search"/>

    <div class="d-flex flex-column m-2 wid mx-auto">

        <#if wall.user.id == me.id>
            <@p.article wall=wall me=me single_page=true />
        <#else>
            <@p.article wall=wall me=me replied=replied bookmarked=bookmarked/>
        </#if>
    </div>
</@p.page>