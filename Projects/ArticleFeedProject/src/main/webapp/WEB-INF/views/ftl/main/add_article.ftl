<#import "*/page.ftl" as p>
<@p.page title="Добавить пост - ArticleFeed">
<@p.navbar active="user"/>
    <div class="d-flex flex-column m-2 wid mx-auto">

        <div class="card m-1 p-2">
            <!-- CARD HEADER ///////////////////////////////////////////////////////////////// -->
            <div class="d-flex">
                <div class="imgmask">
                    <#if type == "user">
                        <#if !user.image??>
                            <img src="${rc.getContextPath()}/static/defaults/user_image.png">
                        <#else>
                            <#-- CHANGE -->
                            <img src="${user.image}">
                        </#if>
                        <#-- CHANGE -->
                    <#elseif type == "group">

                    </#if>
                </div>

                <div class="d-flex flex-column mr-auto">
                    <#if type == "group">
                        <div class="d-inline-flex"><h5 class="m-0 darkblue">${group.name}</h5><h5 class="m-0 ml-1 lightblue"> ${user.name} ${user.surname}</h5></div>
                    </#if>
                    <#if type == "user">
                        <h5 class="m-0 darkblue">${user.name} ${user.surname}</h5>
                    </#if>
                    <p class="p-0 m-0 lightblue"><small><b>${.now?datetime}</b></small></p>
                </div>

            </div>
            <!-- CARD HEADER END////////////////////////////////////////////////////////////// -->

            <!-- ARTICLE HEADING/////////////////////////////////////////////////////// -->
            <div class="input-group p-2 pt-0">
                <input type="text" tabindex="1" maxlength="200" id="heading" class="form-control addheading" name="heading" placeholder="Заголовок">
            </div>
            <!-- ARTICLE HEADING END/////////////////////////////////////////////////////////////// -->
            <hr class="my-0">
            <!-- ARTICLE/////////////////////////////////////////////////////////////// -->
            <div class="toolbar">
                <button class="btn iconstl fa fa-undo" onclick="document.execCommand('undo', false, null)"></button>
                <button class="btn iconstl fa fa-redo" onclick="document.execCommand('redo', false, null)"></button>

                <button class="btn iconstl fa fa-heading" onclick="document.execCommand('formatBlock', false,'h4')"></button>
                <button class="btn iconstl fa fa-paragraph" onclick="document.execCommand('formatBlock', false,'p')"></button>

                <button class="btn iconstl fa fa-strikethrough"  onclick="document.execCommand('strikeThrough', false, null);"></button>
                <button class="btn iconstl fa fa-underline"  onclick="document.execCommand('underline', false, null);"></button>
                <button class="btn iconstl fa fa-italic" onclick="document.execCommand('italic', false, null);"></button>
                <button class="btn iconstl fa fa-bold" onclick="document.execCommand('bold', false, null);"></button>

                <button class="btn iconstl fa fa-link" onclick="link()"></button>
                <button class="btn iconstl fa fa-image" onclick="image()"></button>

            </div>
            <hr class="mb-2 mt-0">
            <div>
                <div class="p-2 postcard" tabindex="2" id="editor" contenteditable></div>
            </div>
            <!-- ARTICLE END////////////////////////////////////////////////////////// -->
            <hr class="mb-2 mt-2">
            <!-- FOOTER////////////////////////////////////////////////////////// -->

            <div class="alert alert-danger m-0 mb-2 p-2 text-center pagealert" id="send_error" role="alert">
                <h5 class="m-0">Не удалось опубликовать<br>Пожалуйста, повторите попытку</h5>
            </div>
            <div class="alert alert-danger m-0 mb-2 p-2 text-center pagealert" id="page_empty" role="alert">
                <h5 class="m-0">Невозможно опубликовать, если поля ввода пустые</h5>
            </div>
            <div class="d-flex">
                <button class="btn btnlight w-100 m-0 p-2" tabindex="3" id="send" onclick="send()"><b>Опубликовать</b></button>
            </div>
            <!-- FOOTER END////////////////////////////////////////////////////// -->
        </div>

    </div>
    <script src="${rc.getContextPath()}/static/js/article-editor.js"></script>
</@p.page>