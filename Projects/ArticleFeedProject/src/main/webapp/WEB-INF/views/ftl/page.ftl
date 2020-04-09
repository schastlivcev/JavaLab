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
<#nested/>
<script src="${rc.getContextPath()}/static/js/jquery-3.4.1.min.js"></script>
<script src="${rc.getContextPath()}/static/js/popper.min.js"></script>
<script src="${rc.getContextPath()}/static/js/bootstrap.min.js"></script>
</body>
</html>
</#macro>

<#macro navbar active>
    <nav class="navbar sticky-top p-1 d-flex">
        <a class="navbar-brand pl-2" href="${rc.getContextPath()}/feed"><b class="lightblue mr-1" <#if active == "feed">style="color: white"</#if>>ArticleFeed</b><i class="fas fa-rss iconstl <#if active == "feed">activewhite</#if> navbrand"></i></a>
        <div class="mx-auto">
            <a class="m-2" href="${rc.getContextPath()}/user"><i class="fas fa-user iconstl <#if active == "user">activewhite</#if>"></i><small><b class="ml-1" <#if active == "user">style="color: white"</#if>>Я</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/friends"><i class="fas fa-user-friends iconstl <#if active == "friends">activewhite</#if>"></i><small><b class="ml-1" <#if active == "friends">style="color: white"</#if>>Друзья</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/groups"><i class="fas fa-book-reader iconstl <#if active == "groups">activewhite</#if>"></i><small><b class="ml-1" <#if active == "groups">style="color: white"</#if>>Группы</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/bookmarks"><i class="fas fa-bookmark iconstl <#if active == "bookmarks">activewhite</#if>"></i><small><b class="ml-1" <#if active == "bookmarks">style="color: white"</#if>>Закладки</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/search"><i class="fas fa-search iconstl <#if active == "search">activewhite</#if>"></i><small><b class="ml-1" <#if active == "search">style="color: white"</#if>>Поиск</b></small></a>
            <a class="m-2" href="${rc.getContextPath()}/signOut"><i class="fas fa-sign-out-alt iconstl <#if active == "sign_out">activewhite</#if>"></i><small><b class="ml-1" <#if active == "sign_out">style="color: white"</#if>>Выйти</b></small></a>
        </div>
    </nav>
</#macro>

<#macro article wall me type="user">
    <div class="card m-1 p-2">
        <!-- CARD HEADER ///////////////////////////////////////////////////////////////// -->
        <div class="d-flex">
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
                    <div class="d-inline-flex"><a href="${rc.getContextPath()}/group/${wall.article.group.id}"><h5 class="m-0 darkblue">${wall.article.group.name}</h5></a><a href="${rc.getContextPath()}/user/${wall.article.user.id}"><h5 class="m-0 ml-1 lightblue"> ${wall.article.user.name} ${wall.article.user.surname}</h5></a></div>
                </#if>
                <#if type == "user">
                    <a href="${rc.getContextPath()}/user/${wall.article.user.id}"><h5 class="m-0 darkblue">${wall.article.user.name} ${wall.article.user.surname}</h5></a>
                </#if>
                <p class="p-0 m-0 lightblue"><small><b>${wall.article.createdAt}</b></small></p>
            </div>

            <a href="${rc.getContextPath()}/article/${wall.id}"class="btn"><i class="fas fa-link iconstl p-0"></i></a>

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
        <#if wall.article.content??>
            <div class="postcard">
                ${wall.article.content}
            </div>
        </#if>
        <!-- ARTICLE END////////////////////////////////////////////////////////// -->
        <#if wall.article.content??>
            <hr class="mb-2 mt-2">
        </#if>
        <!-- FOOTER////////////////////////////////////////////////////////// -->
        <#if wall.article.user.id == me.id>
            <div class="d-flex">
                <button class="btn p-0 mx-auto"><i class="fas fa-times iconstl align-self-center" onclick="remove(this, ${wall.id})"></i></button>
            </div>
        <#else>
            <div class="d-flex">
                <button class="btn mr-auto"><i class="<#if wall.reply == true>fas<#else>far</#if> fa-arrow-alt-circle-left iconstl" onclick="update(this, ${wall.id}, 'reply')"></i></button>
                <!-- fas fa-arrow-alt-circle-left -->
                <button class="btn"><i class="<#if wall.bookmark == true>fas<#else>far</#if> fa-bookmark iconstl" onclick="update(this, ${wall.id}, 'bookmark')"></i></button>
                <!-- fas fa-bookmark -->
            </div>
        </#if>
    <!-- FOOTER END////////////////////////////////////////////////////// -->
    </div>

    <div class="alert alert-danger m-1 p-2 text-center pagealert" id="likeerror" role="alert">
        <h5>Не удалось добавить</h5>
    </div>
    <script src="${rc.getContextPath()}/static/js/article.js"></script>
</#macro>