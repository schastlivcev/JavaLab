<#import "*/page.ftl" as p>
<@p.page title="Вход - ArticleFeed">
    <div class="d-flex flex-column m-2 justify-content-center min-vh-100 widwin mx-auto">
        <form class="card text-center" method="post" accept-charset="UTF-8">
            <h4 class="cardhead activewhite"><b>ArticleFeed</b><i class="fas fa-rss pl-2 iconstl activewhite"></i></h4>
            <h4 class="mt-1">Вход</h4>

            <#if error?? && error == "USER_NOT_FOUND">
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Пользователь не найден</h5>
                </div>
            </#if>
            <#if error?? && error == "DATA_UNACCEPTABLE">
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Произошла ошибка при обработке данных</h5>
                </div>
            </#if>

            <#if error?? && error == "EMAIL_UNACCEPTABLE">
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Неверный формат почты</h5>
                </div>
            </#if>
            <#if errors?? && error == "EMAIL_EMPTY">
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Пожалуйста, укажите почту</h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="mail"><b>Почта</b></span>
                </div>
                <input type="email" class="form-control" name="email" placeholder="mail@example.com"
                       aria-describedby="mail" <#if email??>value="${email}"</#if> required>
            </div>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="pass"><b>Пароль</b></span>
                </div>
                <input type="password" class="form-control" name="password" placeholder="не менее 8 символов"
                       aria-describedby="pass" minlength="8" required>
            </div>

<#--            <div class="form-check">-->
<#--                <input type="checkbox" name="remember" class="form-check-input agrinp" id="rem">-->
<#--                <label class="form-check-label darkblue" for="rem"><b>Запомнить меня</b></label>-->
<#--            </div>-->

            <button type="submit" class="btn btnlight m-2 p-2 w-auto"><b>Отправить</b></button>

        </form>
    </div>
</@p.page>
