<#import "*/page.ftl" as p>
<#import "spring.ftl" as s>
<@p.page title="Вход - ArticleFeed">
    <div class="d-flex flex-column m-2 justify-content-center widwin mx-auto" style="height: 98vh">
        <form class="card text-center" method="post" accept-charset="UTF-8">
            <h4 class="cardhead activewhite"><b>ArticleFeed</b><i class="fas fa-rss pl-2 iconstl activewhite"></i></h4>
            <h4 class="mt-1"><@s.message 'sign.in.header'/></h4>

            <#if error?? && error == "USER_NOT_FOUND">
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.error.not.found'/></h5>
                </div>
            </#if>
            <#if error?? && error == "DATA_UNACCEPTABLE">
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.error.data'/></h5>
                </div>
            </#if>

            <#if error?? && error == "EMAIL_UNACCEPTABLE">
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.email.error.unacceptable'/></h5>
                </div>
            </#if>
            <#if errors?? && error == "EMAIL_EMPTY">
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.email.error.empty'/></h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="mail"><b><@s.message 'sign.email'/></b></span>
                </div>
                <input type="email" class="form-control" name="email" placeholder="mail@example.com"
                       aria-describedby="mail" <#if email??>value="${email}"</#if> required>
            </div>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="pass"><b><@s.message 'sign.password'/></b></span>
                </div>
                <input type="password" class="form-control" name="password" placeholder="<@s.message 'sign.password.placeholder'/>"
                       aria-describedby="pass" minlength="8" required>
            </div>

            <div class="form-check">
                <input type="checkbox" name="remember-me" class="form-check-input agrinp" id="rem">
                <label class="form-check-label darkblue" for="rem"><b><@s.message 'sign.in.remember'/></b></label>
            </div>

            <button type="submit" class="btn btnlight m-2 p-2 w-auto"><b><@s.message 'sign.in.button'/></b></button>

        </form>
    </div>
</@p.page>
