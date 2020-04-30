<#import "*/page.ftl" as p>
<@p.page title="Поиск - ArticleFeed">
    <@p.navbar active="search"/>
    <div class="d-flex flex-column m-2 wid mx-auto">

        <input id="csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <div class="card m-1 p-2">
            <form method="get" class="input-group d-inline-flex align-content-center">
                <input type="text" class="form-control" name="q" placeholder="Имя Фамилия" <#if query??>value="${query}"</#if>>
                <div class="input-group-append">
                    <button class="btn btn-outline-primary btnlight py-1 px-3 m-0" type="submit"><b>Найти</b></button>
                </div>
            </form>
        </div>

        <#if error?? && error == "INVALID_QUERY">
            <div class="alert alert-danger m-2 p-1" role="alert">
                <h5>Некорректный запрос</h5>
            </div>
        </#if>

        <#if users?? && users?size == 0>
            <div class="card m-1 p-2 text-center">
                <p class="darkblue"><b>Нет результатов</b></p>
            </div>
        </#if>

        <#if users?? && users?size gt 0>
            <#list users as user>
                <@p.user user=user card=true/>
            </#list>
        </#if>
    </div>
</@p.page>