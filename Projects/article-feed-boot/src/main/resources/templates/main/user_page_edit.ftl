<#import "*/page.ftl" as p>
<@p.page title="Редактирование страницы - ArticleFeed">
    <@p.navbar active="user"/>
    <script src="${rc.getContextPath()}/static/js/page-editor.js"></script>

    <div class="d-flex flex-column m-2 wid mx-auto">

        <input id="csrf" type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

        <form method="get" class="card m-1 p-2" id="page_edit">
            <div class="d-flex">
                <div class="d-flex flex-column">
                    <div id="profile_image" class="profilepic my-2 mr-2" style="width: 9rem">
                        <#if !user.image??>
                            <img src="${rc.getContextPath()}/static/defaults/user_image.png">
                        <#else>
                        <#-- CHANGE -->
                            <img src="${user.image}">
                        </#if>
                    </div>
                    <input id="image_url" type="hidden" name="imageUrl">
                    <button type="button" class="m-2 p-0 btn btnwhite" onclick="changeImg()"><b class="lightblue" style="font-size: 1.25rem">Изменить</b></button>
                </div>
                <div class="d-flex flex-column w-100">
                    <div class="d-flex justify-content-between">
                        <div class="w-100">
                            <h4><b>${user.name} ${user.surname}</b></h4>
                            <input type="text" maxlength="60" class="form-control addheading" name="userStatus" id="user_status" <#if user.status??>value="${user.status}"</#if> placeholder="Статус">
                        </div>

                    </div>
                    <hr>
                    <small><p class="lightblue"><b>Страна: </b><b class="darkblue">${user.country.nameRu}</b></p></small>
                    <small><p class="lightblue"><b>Пол: </b><b class="darkblue"><#if user.isMan == true>Мужчина<#else>Женщина</#if></b></p></small>
                    <small><p class="lightblue"><b>Дата рождения: </b><b class="darkblue">${user.birthday}</b></p></small>
                    <small><p class="lightblue"><b>Дата регистрации: </b><b class="darkblue">${user.createdAt?date}</b></p></small>

                    <hr class="mt-1">
                    <p><textarea name="userAbout" class="form-control w-100 addheading" id="user_about" rows="3" placeholder="О себе" style="resize: none"><#if user.bio??>${user.bio?replace("<br>", "\n")}</#if></textarea></p>
                </div>
            </div>

            <hr class="mb-2 mt-0">
            <div class="alert alert-danger m-0 mb-2 p-2 text-center pagealert" id="send_error" role="alert">
                <h5 class="m-0">Не удалось сохранить изменения. Пожалуйста, повторите попытку</h5>
            </div>
            <button type="button" class="w-100 p-2 btn btnlight" onclick="sendPageEdit()"><b>Сохранить</b></button>

        </form>

    </div>
</@p.page>