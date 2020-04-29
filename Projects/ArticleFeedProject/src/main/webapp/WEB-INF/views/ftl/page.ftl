<#macro page title>
<!doctype html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">

    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/fontawesome/css/all.css">
    <link rel="stylesheet" href="${rc.getContextPath()}/static/css/feed.css" type="text/css">

    <title>${title}</title>
</head>
<body>
<script src="${rc.getContextPath()}/static/js/jquery-3.4.1.min.js"></script>
<script src="${rc.getContextPath()}/static/js/popper.min.js"></script>
<script src="${rc.getContextPath()}/static/js/bootstrap.min.js"></script>
<#nested/>
</body>
</html>
</#macro>

<#macro navbar active>
    <nav class="navbar sticky-top p-1 d-flex">
        <a class="navbar-brand pl-2" href="${rc.getContextPath()}/feed"><b class="lightblue mr-1" <#if active == "feed">style="color: white"</#if>>ArticleFeed</b><i class="fas fa-rss iconstl <#if active == "feed">activewhite</#if> navbrand"></i></a>
        <div class="mx-auto">
            <a class="m-2" href="${rc.getContextPath()}/user"><i class="fas fa-user iconstl <#if active == "user">activewhite</#if>"></i><small><b class="ml-1" <#if active == "user">style="color: white"</#if>>Я</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/friends"><i class="fas fa-user-friends iconstl <#if active == "friends">activewhite</#if>"></i><small><b class="ml-1" <#if active == "friends">style="color: white"</#if>>Друзья</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/chat"><i class="fas fa-comments iconstl <#if active == "chat">activewhite</#if>"></i><small><b class="ml-1" <#if active == "chat">style="color: white"</#if>>Сообщения</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/bookmarks"><i class="fas fa-bookmark iconstl <#if active == "bookmarks">activewhite</#if>"></i><small><b class="ml-1" <#if active == "bookmarks">style="color: white"</#if>>Закладки</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/search"><i class="fas fa-search iconstl <#if active == "search">activewhite</#if>"></i><small><b class="ml-1" <#if active == "search">style="color: white"</#if>>Поиск</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/signOut"><i class="fas fa-sign-out-alt iconstl <#if active == "sign_out">activewhite</#if>"></i><small><b class="ml-1" <#if active == "sign_out">style="color: white"</#if>>Выйти</b></small></a>
        </div>
    </nav>
</#macro>

<#macro article wall me replied=false bookmarked=false single_page=false bookmarks_page=false type="user">
    <script src="${rc.getContextPath()}/static/js/article.js"></script>

    <div class="card m-1 p-2" id="article_card">
        <!-- CARD HEADER ///////////////////////////////////////////////////////////////// -->
        <#if wall.reply == true>
            <div class="d-flex flex-column mt-1">
                <div class="d-flex">
                    <div class="imgmask">
                        <#if type == "user">
                            <#if !me.image??>
                                <img src="${rc.getContextPath()}/static/defaults/user_image.png">
                            <#else>
                            <#-- CHANGE -->
                                <img src="${wall.user.image}">
                            </#if>
                        <#-- CHANGE -->
                        <#elseif type == "group">

                        </#if>
                    </div>

                    <div class="d-flex flex-column mr-auto">
                        <#-- CHANGE -->
                        <#if type == "group">
<#--                            <div class="d-inline-flex"><a href="${rc.getContextPath()}/group/${wall.article.group.id}"><h5 class="m-0 darkblue">${wall.article.group.name}</h5></a><a href="${rc.getContextPath()}/user/${wall.article.user.id}"><h5 class="m-0 ml-1 lightblue"> ${wall.article.user.name} ${wall.article.user.surname}</h5></a></div>-->
                        </#if>
                        <#if type == "user">
                            <a href="${rc.getContextPath()}/user/${wall.user.id}"><h5 class="m-0 darkblue">${wall.user.name} ${wall.user.surname}</h5></a>
                        </#if>
                        <p class="p-0 m-0 lightblue"><small><b>${wall.createdAt}</b></small></p>
                    </div>

                    <a href="${rc.getContextPath()}/article/${wall.id}" class="btn"><i class="fas fa-link iconstl p-0"></i></a>

                </div>

        </#if>

        <div class="d-flex">
            <#if wall.reply == true>
                <p class="my-0 mx-2 align-self-center"><i class="fas fa-reply iconstl p-0"></i></p>
            </#if>

            <div class="imgmask">
                <#if type == "user">
                    <#if !wall.article.user.image??>
                        <img src="${rc.getContextPath()}/static/defaults/user_image.png">
                    <#else>
                    <#-- CHANGE -->
                        <img src="${wall.article.user.image}">
                    </#if>
                <#-- CHANGE -->
                <#elseif type == "group">

                </#if>
            </div>

            <div class="d-flex flex-column mr-auto">
                <#-- CHANGE -->
                <#if type == "group">
<#--                    <div class="d-inline-flex"><a href="${rc.getContextPath()}/group/${wall.article.group.id}"><h5 class="m-0 darkblue">${wall.article.group.name}</h5></a><a href="${rc.getContextPath()}/user/${wall.article.user.id}"><h5 class="m-0 ml-1 lightblue"> ${wall.article.user.name} ${wall.article.user.surname}</h5></a></div>-->
                </#if>
                <#if type == "user">
                    <a href="${rc.getContextPath()}/user/${wall.article.user.id}"><h5 class="m-0 darkblue">${wall.article.user.name} ${wall.article.user.surname}</h5></a>
                </#if>
                <p class="p-0 m-0 lightblue"><small><b>${wall.article.createdAt}</b></small></p>
            </div>

            <#if wall.reply == false>
                <a href="${rc.getContextPath()}/article/${wall.id}" class="btn"><i class="fas fa-link iconstl p-0"></i></a>
            </#if>

        </div>
        <!-- CARD HEADER END////////////////////////////////////////////////////////////// -->

        <!-- ARTICLE HEADING/////////////////////////////////////////////////////// -->
        <#if wall.article.heading??>
            <div class="input-group p-0">
                <h3 class="mt-0">${wall.article.heading}</h3>
            </div>
        </#if>
        <!-- ARTICLE HEADING END/////////////////////////////////////////////////////////////// -->
        <#if wall.article.heading??>
            <hr class="mb-2 mt-0">
        </#if>
        <!-- ARTICLE/////////////////////////////////////////////////////////////// -->
        <input type="hidden" name="articleId" value="${wall.article.id}" id="article_id">

        <#if wall.article.content??>
            <div class="postcard">
                ${wall.article.content}
            </div>
        </#if>
        <!-- ARTICLE END////////////////////////////////////////////////////////// -->
        <#if wall.article.content?? && !(wall.article.user.id == me.id && wall.user.id != me.id)>
            <hr class="mb-2 mt-2">
        </#if>
        <!-- FOOTER////////////////////////////////////////////////////////// -->
        <#if wall.user.id == me.id>
            <div class="d-flex">
                <button id="delete_button" class="btn p-0 mx-auto" onclick="updateWall(this, ${wall.id}, 'DELETE<#if single_page == true>_SINGLE<#elseif bookmarks_page == true>_BOOKMARK_SINGLE</#if>')"><i class="fas fa-times iconstl align-self-center"></i></button>
            </div>
        <#elseif !(wall.article.user.id == me.id && wall.user.id != me.id)>
            <div class="d-flex">
                <button id="reply_button" class="btn mr-auto" onclick="updateWall(this, ${wall.id}, <#if replied == true>'DELETE_REPLY'<#else>'REPLY'</#if>)"><i class="<#if replied == true>fas<#else>far</#if> fa-share-square iconstl"></i></button>
                <!-- fas fa-arrow-alt-circle-left -->
                <button id="bookmark_button" class="btn" onclick="updateWall(this, ${wall.id}, <#if bookmarked == true>'DELETE_BOOKMARK'<#else>'BOOKMARK'</#if>)"><i class="<#if bookmarked == true>fas<#else>far</#if> fa-bookmark iconstl"></i></button>
                <!-- fas fa-bookmark -->
            </div>
        </#if>

        <div class="alert alert-danger m-1 text-center pagealert" id="article_error" role="alert">
            <h5>Ошибка отправления, пожалуйста повторите попытку</h5>
        </div>

        <#if wall.reply == true>
            </div>
        </#if>
    <!-- FOOTER END////////////////////////////////////////////////////// -->
    </div>
</#macro>

<#macro friend_card friends me card=true>
    <#if friends.userSender.id == me.id>
        <#assign friend = friends.userRecipient>
    <#else>
        <#assign friend = friends.userSender>
    </#if>

    <#if card == true>
        <div class="card m-1 p-2 friend">
    </#if>
    <!-- CARD HEADER ///////////////////////////////////////////////////////////////// -->
    <div class="d-flex">
        <div class="imgmask">
            <#if !friend.image??>
                <img src="${rc.getContextPath()}/static/defaults/user_image.png">
            <#else>
            <#-- CHANGE -->
                <img src="${friend.image}">
            </#if>
        </div>

        <div class="d-flex flex-column w-100">
            <a href="${rc.getContextPath()}/user/${friend.id}"><b class="darkblue">${friend.name} ${friend.surname}</b></a>
            <hr class="m-0">
            <#if friend.status??><h5 class="activegrey m-0">${friend.status}</h5><#else><h5 class="activegrey m-0"/></#if>
        </div>

<#--        <#if friends.status == "REQUESTED" && friend.id == friends.userRecipient.id>-->
<#--            <hr class="mb-2 mt-0">-->
<#--            <form id="friendship" method="post" action="/friendship">-->
<#--                <input type="hidden" name="friend_id" value="${friend.id}">-->
<#--                <button type="submit" name="status" value="CANCEL" class="btn ml-2 px-0 pb-1 mb-1 btnwhite"><b class="lightblue">Отменить заявку</b></button>-->
<#--            </form>-->
<#--        <#elseif friends.status == "REQUESTED" && friend.id == friends.userSender.id>-->
<#--            <hr class="mb-2 mt-0">-->
<#--            <form id="friendship" method="post" action="/friendship">-->
<#--                <input type="hidden" name="friend_id" value="${friend.id}">-->
<#--                <button type="submit" name="status" value="ACCEPT" class="px-3 ml-2 pb-1 mb-1 btn btnlight"><b>Принять заявку</b></button>-->
<#--            </form>-->
<#--        </#if>-->

    </div>
    <!-- CARD HEADER END////////////////////////////////////////////////////////////// -->

    <#if card == true>
        </div>
    </#if>
</#macro>

<#macro user user card=true>
    <#if card == true>
        <div class="card m-1 p-2 friend">
    </#if>
    <!-- CARD HEADER ///////////////////////////////////////////////////////////////// -->
    <div class="d-flex">
        <div class="imgmask">
            <#if !user.image??>
                <img src="${rc.getContextPath()}/static/defaults/user_image.png">
            <#else>
            <#-- CHANGE -->
                <img src="${user.image}">
            </#if>
        </div>

        <div class="d-flex flex-column w-100">
            <a href="${rc.getContextPath()}/user/${user.id}"><b class="darkblue">${user.name} ${user.surname}</b></a>
            <hr class="m-0">
            <#if user.status??><h5 class="activegrey m-0">${user.status}</h5><#else><h5 class="activegrey m-0"><br></h5></#if>
        </div>

    </div>
    <!-- CARD HEADER END////////////////////////////////////////////////////////////// -->

    <#if card == true>
        </div>
    </#if>
</#macro>

<#macro message message>
    <div class="d-flex">
        <div class="imgmask">
            <#if !message.author.image??>
                <img src="${rc.getContextPath()}/static/defaults/user_image.png">
            <#else>
            <#-- CHANGE -->
                <img src="${message.author.image}">
            </#if>
        </div>

        <div class="d-flex flex-column w-100">
            <a href="${rc.getContextPath()}/user/${message.author.id}"><b class="darkblue">${message.author.name} ${message.author.surname}</b></a>
            <div class="d-inline-flex justify-content-between">
                <h5 class="m-0" style="max-width: 70%">${message.content}</h5>
                <h5 class="activegrey m-0">${message.createdAt}</h5>
            </div>
        </div>
    </div>
</#macro>

<#macro chat_preview message users me>
    <div class="d-flex">
        <#if message.channel.dialogue == true>
            <#list users as user>
                <#if user.id != me.id>
                    <#assign user_index = user?index>
                    <a href="${rc.getContextPath()}/chat?ch=${message.channel.id}" style="text-decoration: none; color: inherit">
                    <div class="imgmask">
                        <#if !user.image??>
                            <img src="${rc.getContextPath()}/static/defaults/user_image.png">
                        <#else>
                        <#-- CHANGE -->
                            <img src="${user.image}">
                        </#if>
                    </div>
                    </a>
                    <#break>
                </#if>
            </#list>
        <#else>
            <a href="${rc.getContextPath()}/chat?ch=${message.channel.id}" style="text-decoration: none; color: inherit">
            <div class="imgmask">
                <#if !message.channel.image??>
                    <img src="${rc.getContextPath()}/static/defaults/user_chat.jpg">
                <#else>
                <#-- CHANGE -->
                    <img src="${message.channel.image}">
                </#if>
            </div>
            </a>
        </#if>

        <div class="d-flex flex-column w-100">
            <#if message.channel.dialogue == true>
                <div class="d-flex">
                    <span class="activegrey"><b>Диалог с&nbsp;</b></span>
                    <a href="${rc.getContextPath()}/user/${users[user_index].id}"><b class="darkblue">${users[user_index].name} ${users[user_index].surname}</b></a>
                </div>
            <#else>
                <#if message.channel.name??>
                    <span class="darkblue">${message.channel.name}</span>
                <#else>
                    <div class="d-flex">
                        <span class="activegrey"><b>Беседа с </b></span>
                        <span><b class="darkblue">
                    </div>
                        <#list users as user>
                            <#if user.id != me.id>
                                <a href="${rc.getContextPath()}/user/${user.id}">${user.name} ${user.surname}</a>&nbsp;
                            </#if>
                        </#list>
                    </b></span>
                </#if>
            </#if>
            <a href="${rc.getContextPath()}/chat?ch=${message.channel.id}" style="text-decoration: none; color: inherit">

            <hr class="m-0">
            <div class="d-flex justify-content-between">
                <span class="m-0">${message.author.name} ${message.author.surname}: ${message.content[0..*30]}<#if message.content?length gt 30>...</#if></span>
                <span class="activegrey m-0">${message.createdAt}</span>
            </div>
            </a>
        </div>

    </div>
</#macro>