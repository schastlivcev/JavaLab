<#import "*/page.ftl" as p>
    <#if user.id == me.id>
        <#assign title = "Моя страница">
    <#else>
        <#assign title = "Пользователь ${user.id}">
    </#if>
<@p.page title="${title} - ArticleFeed">
    <#if friends?? && friends.status == "ACCEPTED">
        <@p.navbar active="friends"/>
    <#elseif user.id == me.id>
        <@p.navbar active="user"/>
    <#else>
        <@p.navbar active="search"/>
    </#if>

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
                        <#else>
                            <#-- Friends button -->
                            <#if friends?? && friends.status == "REQUESTED" && friends.userSender.id == me.id>
                                <hr class="mb-2 mt-0">
                                <form id="friendship" method="post" action="/friendship">
                                    <input type="hidden" name="friend_id" value="${user.id}">
                                    <input type="hidden" name="status" value="CANCEL">
                                    <button type="button" class="w-100 p-2 btn btnwhite" onclick="sendFriendship()"><i class="fas fa-user-times iconstl"></i></button>
                                </form>
                            <#elseif friends?? && friends.status == "REQUESTED" && friends.userRecipient.id == me.id>
                                <hr class="mb-2 mt-0">
                                <form id="friendship" method="post" action="/friendship">
                                    <input type="hidden" name="friend_id" value="${user.id}">
                                    <input type="hidden" name="status" value="ACCEPT">
                                    <button type="button" class="w-100 p-2 btn btnwhite" onclick="sendFriendship()"><i class="fas fa-user-check iconstl"></i></button>
                                </form>
                            <#elseif friends?? && friends.status == "ACCEPTED">
                                <hr class="mb-2 mt-0">
                                <form id="friendship" method="post" action="/friendship">
                                    <input type="hidden" name="friend_id" value="${user.id}">
                                    <input type="hidden" name="status" value="DELETE">
                                    <button type="button" class="w-100 p-2 btn btnwhite" onclick="sendFriendship()"><i class="fas fa-user-slash iconstl"></i></button>
                                </form>
                            <#elseif user.id != me.id>
                                <hr class="mb-2 mt-0">
                                <form id="friendship" method="post" action="/friendship">
                                    <input type="hidden" name="friend_id" value="${user.id}">
                                    <input type="hidden" name="status" value="ADD">
                                    <button type="button" class="w-100 p-2 btn btnwhite" onclick="sendFriendship()"><i class="fas fa-user-plus iconstl"></i></button>
                                </form>
                            </#if>
                        </#if>
                    </div>
                    <hr>
                    <small><p class="lightblue"><b>Страна: </b><b class="darkblue">${user.country.nameRu}</b></p></small>
                    <small><p class="lightblue"><b>Пол: </b><b class="darkblue"><#if user.isMan == true>Мужчина<#else>Женщина</#if></b></p></small>
                    <small><p class="lightblue"><b>Дата рождения: </b><b class="darkblue">${user.birthday}</b></p></small>
                    <small><p class="lightblue"><b>Дата регистрации: </b><b class="darkblue">${user.createdAt?date}</b></p></small>
                </div>
            </div>

<#--            <#if friends?? && friends.status == "REQUESTED" && friends.userSender.id == me.id>-->
<#--                <hr class="mb-2 mt-0">-->
<#--                <form id="friendship" method="post" action="/friendship">-->
<#--                    <input type="hidden" name="friend_id" value="${user.id}">-->
<#--                    <input type="hidden" name="status" value="CANCEL">-->
<#--                    <button type="button" class="w-100 p-2 btn btnwhite" onclick="sendFriendship()"><b class="lightblue">Отменить заявку</b></button>-->
<#--                </form>-->
<#--            <#elseif friends?? && friends.status == "REQUESTED" && friends.userRecipient.id == me.id>-->
<#--                <hr class="mb-2 mt-0">-->
<#--                <form id="friendship" method="post" action="/friendship">-->
<#--                    <input type="hidden" name="friend_id" value="${user.id}">-->
<#--                    <input type="hidden" name="status" value="ACCEPT">-->
<#--                    <button type="button" class="w-100 p-2 btn btnlight" onclick="sendFriendship()"><b>Принять заявку</b></button>-->
<#--                </form>-->
<#--            <#elseif friends?? && friends.status == "ACCEPTED">-->
<#--                <hr class="mb-2 mt-0">-->
<#--                <form id="friendship" method="post" action="/friendship">-->
<#--                    <input type="hidden" name="friend_id" value="${user.id}">-->
<#--                    <input type="hidden" name="status" value="DELETE">-->
<#--                    <button type="button" class="w-100 p-2 btn btnwhite" onclick="sendFriendship()"><b class="lightblue">Удалить из друзей</b></button>-->
<#--                </form>-->
<#--            <#elseif user.id != me.id>-->
<#--                <hr class="mb-2 mt-0">-->
<#--                <form id="friendship" method="post" action="/friendship">-->
<#--                    <input type="hidden" name="friend_id" value="${user.id}">-->
<#--                    <input type="hidden" name="status" value="ADD">-->
<#--                    <button type="button" class="w-100 p-2 btn btnlight" onclick="sendFriendship()"><b>Добавить в друзья</b></button>-->
<#--                </form>-->
<#--            </#if>-->

            <#if user.bio??><h5 class="activegrey mt-2 mb-0">${user.bio}</h5></#if>

            <#if user.id != me.id>
                <#if channelId??>
                    <a class="btn p-2 mt-2 w-100 btnlight" href="${rc.getContextPath()}/chat?ch=${channelId}"><b>Написать сообщение</b></a>
                <#else>
                    <a class="btn p-2 mt-2 w-100 btnlight" href="${rc.getContextPath()}/user/${user.id}/createСhat"><b>Написать сообщение</b></a>
                </#if>
            </#if>

        </div>

        <#if user.id == me.id>
            <div class="card m-1 p-2">
                <a href="${rc.getContextPath()}/add" class="mx-auto btn p-0"><i class="fas fa-plus iconstl align-self-center"></i></a>
            </div>
        </#if>

        <#list walls as wall>
            <#if user.id == me.id>
                <@p.article wall=wall me=me/>
            <#else>
                <#assign bookmarked=false>
                <#if bookmarksId?seq_contains(wall.article.id)>
                    <#assign bookmarked=true>
                </#if>
                <#assign replied=false>
                <#if repliesId?seq_contains(wall.article.id)>
                    <#assign replied=true>
                </#if>
                <@p.article wall=wall me=me replied=replied bookmarked=bookmarked/>
            </#if>
        </#list>
    </div>

    <script src="${rc.getContextPath()}/static/js/friends.js"></script>
</@p.page>