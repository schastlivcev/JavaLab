<#import "*/page.ftl" as p>
<@p.page title="Регистрация - ArticleFeed">
    <div class="d-flex flex-column m-2 justify-content-center widwin mx-auto" style="height: 98vh">
        <form class="card text-center" name="model" method="post" accept-charset="UTF-8">
            <h4 class="cardhead activewhite"><b>ArticleFeed</b><i class="fas fa-rss pl-2 iconstl activewhite"></i></h4>
            <h4 class="mt-1"><@s.message 'sign.in.button'/></h4>

            <#if errors?? && errors?seq_contains("DATA_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.error.data'/></h5>
                </div>
            </#if>

            <#if errors?? && errors?seq_contains("EMAIL_UNAVAILABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.email.error.unavailable'/></h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("EMAIL_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.email.error.unacceptable'/></h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("EMAIL_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.email.error.empty'/></h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="mail"><b>Почта</b></span>
                </div>
                <input type="email" maxlength="50" class="form-control" name="email" placeholder="mail@example.com"
                       aria-describedby="mail" <#if email??>value="${email}"</#if>>
            </div>

            <#if errors?? && errors?seq_contains("PASSWORD_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.password.error.empty'/></h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("PASSWORD_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.password.error.unacceptable'/></h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("PASSWORD_SHORT")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.password.error.short'/></h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="pass"><b><@s.message 'sign.password'/></b></span>
                </div>
                <input type="password" class="form-control" name="password" minlength="8"
                       placeholder="<@s.message 'sign.password.placeholder'/>" aria-describedby="pass">
            </div>

            <#if errors?? && errors?seq_contains("PASSWORD_MISMATCH")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.repeat.password.error.mismatch'/></h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="confpass"><b><@s.message 'sign.repeat.password'/></b></span>
                </div>
                <input type="password" class="form-control" name="passwordRepeat" minlength="8"
                       aria-describedby="conpass">
            </div>

            <#if errors?? && errors?seq_contains("NAME_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.name.error.empty'/></h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("NAME_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.name.error.unacceptable'/></h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="nm"><b><@s.message 'sign.name'/></b></span>
                </div>
                <input type="text" maxlength="30" minlength="1" class="form-control" name="name" placeholder="<@s.message 'sign.name.placeholder'/>"
                       aria-describedby="nm" <#if name??>value="${name}"</#if>>
            </div>

            <#if errors?? && errors?seq_contains("SURNAME_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.surname.error.empty'/></h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("SURNAME_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.surname.error.unacceptable'/></h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="surnm"><b><@s.message 'sign.surname'/></b></span>
                </div>
                <input type="text" maxlength="30" minlength="1" class="form-control" name="surname" placeholder="<@s.message 'sign.name.placeholder'/>"
                       aria-describedby="surnm" <#if surname??>value="${surname}"</#if>>
            </div>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text rounded"><b><@s.message 'sign.sex'/></b></span>
                </div>

                <div class="d-flex flex-fill">
                    <div class="form-check ml-2 mr-auto">
                        <input class="form-check-input gendinp" type="radio" name="sex" id="mal"
                               value="true" <#if !sex?? || sex == "true">checked</#if>>
                        <label class="form-check-label darkblue" for="mal"><b><@s.message 'sign.sex.male'/></b></label>
                    </div>
                    <div class="form-check pl-1">
                        <input class="form-check-input gendinp" type="radio" name="sex" id="femal"
                               value="false" <#if sex?? && sex == "false">checked</#if>>
                        <label class="form-check-label darkblue" for="femal"><b><@s.message 'sign.sex.female'/></b></label>
                    </div>
                </div>
            </div>

            <#if errors?? && errors?seq_contains("BIRTHDAY_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.birthday.error.empty'/></h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("BIRTHDAY_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.birthday.error.unacceptable'/></h5>
                </div>
            </#if>

            <div class="input-group p-2 d-inline-flex">
                <div class="input-group-prepend p-0 col-auto">
                    <span class="input-group-text" id="brth"><b><@s.message 'sign.birthday'/></b></span>
                </div>
                <input type="date" class="form-control" min="1940-01-01" max="${.now?string('yyyy')?number - 1}-12-31" name="birthday"
                       aria-describedby="brth" <#if birth??>value="${birth}"</#if>>
            </div>

            <#if errors?? && errors?seq_contains("COUNTRY_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.country.error.empty'/></h5>
                </div>
            </#if>

            <div class="input-group p-2 d-inline-flex">
                <div class="input-group-prepend p-0 col-auto">
                    <span class="input-group-text"><b><@s.message 'sign.country'/></b></span>
                </div>

                <div class="form-group my-auto col p-0">
                    <select class="form-control" name="country" style="border-radius: 0; border-bottom-right-radius: .25rem; border-top-right-radius: .25rem">
                        <#list countries as country>
                            <option <#if country.getNameRu() == selected_country>selected</#if>>${country.getNameRu()}</option>
                        </#list>
                    </select>
                </div>
            </div>

            <#if errors?? && errors?seq_contains("AGREEMENT_UNCHECKED")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5><@s.message 'sign.accept.error.unchecked'/></h5>
                </div>
            </#if>

            <div class="form-check">
                <input type="checkbox" name="agreement" class="form-check-input agrinp" id="agree" value="true" required>
                <label class="form-check-label darkblue" for="agree"><b><@s.message 'sign.accept'/></b></label>
            </div>

            <button type="submit" class="btn btnlight m-2 p-2 w-auto"><b><@s.message 'sign.up.button'/></b></button>

        </form>
    </div>
</@p.page>