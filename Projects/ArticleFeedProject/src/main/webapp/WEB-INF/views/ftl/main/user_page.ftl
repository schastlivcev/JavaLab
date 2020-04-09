<#import "*/page.ftl" as p>
<@p.page title="Страница ${user.id} - ArticleFeed">
    <@p.navbar active="user"/>
<#--    <c:choose>-->
<#--        <c:when test="${friendstatus.equals('YES')}">-->
<#--            <c:import url="navbar.jsp">-->
<#--                <c:param name="active" value="friends"/>-->
<#--            </c:import>-->
<#--        </c:when>-->
<#--        <c:when test="${you != null}">-->
<#--            <c:import url="navbar.jsp">-->
<#--                <c:param name="active" value="user"/>-->
<#--            </c:import>-->
<#--        </c:when>-->
<#--        <c:otherwise>-->
<#--            <c:import url="navbar.jsp">-->
<#--                <c:param name="active" value="search"/>-->
<#--            </c:import>-->
<#--        </c:otherwise>-->
<#--    </c:choose>-->


    <div class="d-flex flex-column m-2 wid mx-auto">

        <div class="card m-1 p-2">
            <div class="d-flex">
                <div class="profilepic my-2 mr-2">
                    <#if !user.image??>
                        <img src="${rc.getContextPath()}/static/defaults/user_image.png">
                    <#else>
                        <#-- CHANGE -->
                        <img src="${user.image}">
                    </#if>
                </div>
                <div class="d-flex flex-column w-100">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h4><b>${user.name} ${user.surname}</b></h4>
                            <#if user.status??><h5 class="activegrey">${user.status}</h5></#if>
                            <#if !user.status??><h5></h5></#if>
                        </div>
                        <#if user.id == me.id>
                            <a href="${rc.getContextPath()}/edit" class="btn p-0 pr-1"><i class="fas fa-pen iconstl"></i></a>
                        </#if>
                    </div>
                    <hr>
                    <small><p class="lightblue"><b>Страна: </b><b class="darkblue">${user.country.nameRu}</b></p></small>
                    <small><p class="lightblue"><b>Пол: </b><b class="darkblue"><#if user.isMan == true>Мужчина<#else>Женщина</#if></b></p></small>
                    <small><p class="lightblue"><b>Дата рождения: </b><b class="darkblue">${user.birthday}</b></p></small>
                    <small><p class="lightblue"><b>Дата регистрации: </b><b class="darkblue">${user.createdAt?date}</b></p></small>
                </div>
            </div>

<#--            <c:if test="${friendstatus.equals('SENT')}">-->
<#--                <hr class="mb-2 mt-0">-->
<#--                <form method="get">-->
<#--                    <button type="submit" name="friend" value="REMOVE" class="w-100 p-2 btn btnwhite"><b class="lightblue">Отменить заявку</b></button>-->
<#--                </form>-->
<#--            </c:if>-->
<#--            <c:if test="${friendstatus.equals('YES')}">-->
<#--                <hr class="mb-2 mt-0">-->
<#--                <form method="get">-->
<#--                    <button type="submit" name="friend" value="REMOVE" class="w-100 p-2 btn btnwhite"><b class="lightblue">Удалить из друзей</b></button>-->
<#--                </form>-->
<#--            </c:if>-->
<#--            <c:if test="${friendstatus.equals('ACCEPT')}">-->
<#--                <hr class="mb-2 mt-0">-->
<#--                <form method="get">-->
<#--                    <button type="submit" name="friend" value="ACCEPT" class="w-100 p-2 btn btnlight"><b>Принять заявку</b></button>-->
<#--                </form>-->
<#--            </c:if>-->
<#--            <c:if test="${friendstatus.equals('ADDTOFRIENDS')}">-->
<#--                <hr class="mb-2 mt-0">-->
<#--                <form method="get">-->
<#--                    <button type="submit" name="friend" value="ADD" class="mx-auto w-100 p-2 btn btnlight"><b>Добавить в друзья</b></button>-->
<#--                </form>-->
<#--            </c:if>-->
        </div>

        <#if me??>
            <div class="card m-1 p-2">
                <a href="${rc.getContextPath()}/add" class="mx-auto btn p-0"><i class="fas fa-plus iconstl align-self-center"></i></a>
            </div>
        </#if>

<#--        <#if walls?size gt 0>-->
            <#list walls as wall>
                <@p.article wall=wall me=me/>
            </#list>
<#--        </#if>-->
</@p.page>