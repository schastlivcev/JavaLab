<#import "*/page.ftl" as p>
<@p.page title="Регистрация - ArticleFeed">
    <div class="d-flex flex-column m-2 justify-content-center min-vh-100 widwin mx-auto">
        <form class="card text-center" name="model" method="post" accept-charset="UTF-8">
            <h4 class="cardhead activewhite"><b>ArticleFeed</b><i class="fas fa-rss pl-2 iconstl activewhite"></i></h4>
            <h4 class="mt-1">Регистрация</h4>

            <#if errors?? && errors?seq_contains("DATA_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Произошла ошибка при обработке данных</h5>
                </div>
            </#if>

            <#if errors?? && errors?seq_contains("EMAIL_UNAVAILABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Данная почта уже используется</h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("EMAIL_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Неверный формат почты</h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("EMAIL_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Пожалуйста, укажите почту</h5>
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
                    <h5>Пожалуйста, укажите пароль</h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("PASSWORD_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Недопустимый пароль</h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("PASSWORD_SHORT")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Пароль не может быть короче 8 символов</h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="pass"><b>Пароль</b></span>
                </div>
                <input type="password" class="form-control" name="password" minlength="8"
                       placeholder="не менее 8 символов без пробелов" aria-describedby="pass">
            </div>

            <#if errors?? && errors?seq_contains("PASSWORD_MISMATCH")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Пароли не совпадают</h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="confpass"><b>Повторите пароль</b></span>
                </div>
                <input type="password" class="form-control" name="passwordRepeat" minlength="8"
                       aria-describedby="conpass">
            </div>

            <#if errors?? && errors?seq_contains("NAME_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Пожалуйста, укажите имя</h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("NAME_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Недопустимое имя</h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="nm"><b>Имя</b></span>
                </div>
                <input type="text" maxlength="30" minlength="1" class="form-control" name="name" placeholder="без пробелов"
                       aria-describedby="nm" <#if name??>value="${name}"</#if>>
            </div>

            <#if errors?? && errors?seq_contains("SURNAME_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Пожалуйста, укажите фамилию</h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("SURNAME_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Недопустимая фамилия</h5>
                </div>
            </#if>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text" id="surnm"><b>Фамилия</b></span>
                </div>
                <input type="text" maxlength="30" minlength="1" class="form-control" name="surname" placeholder="без пробелов"
                       aria-describedby="surnm" <#if surname??>value="${surname}"</#if>>
            </div>

            <div class="input-group p-2 pt-0">
                <div class="input-group-prepend">
                    <span class="input-group-text rounded"><b>Пол</b></span>
                </div>

                <div class="d-flex flex-fill">
                    <div class="form-check ml-2 mr-auto">
                        <input class="form-check-input gendinp" type="radio" name="sex" id="mal"
                               value="true" <#if !sex?? || sex == "true">checked</#if>>
                        <label class="form-check-label darkblue" for="mal"><b>мужчина</b></label>
                    </div>
                    <div class="form-check pl-1">
                        <input class="form-check-input gendinp" type="radio" name="sex" id="femal"
                               value="false" <#if sex?? && sex == "false">checked</#if>>
                        <label class="form-check-label darkblue" for="femal"><b>женщина</b></label>
                    </div>
                </div>
            </div>

            <#if errors?? && errors?seq_contains("BIRTHDAY_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Пожалуйста, укажите дату рождения</h5>
                </div>
            </#if>
            <#if errors?? && errors?seq_contains("BIRTHDAY_UNACCEPTABLE")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Недопустимая дата рождения</h5>
                </div>
            </#if>

            <div class="input-group p-2 d-inline-flex">
                <div class="input-group-prepend p-0 col-auto">
                    <span class="input-group-text" id="brth"><b>Дата рождения</b></span>
                </div>
                <input type="date" class="form-control" min="1940-01-01" max="${.now?string('yyyy')?number - 1}-12-31" name="birthday"
                       aria-describedby="brth" <#if birth??>value="${birth}"</#if>>
            </div>

            <#if errors?? && errors?seq_contains("COUNTRY_EMPTY")>
                <div class="alert alert-danger m-2 p-1" role="alert">
                    <h5>Пожалуйста, укажите страну</h5>
                </div>
            </#if>

            <div class="input-group p-2 d-inline-flex">
                <div class="input-group-prepend p-0 col-auto">
                    <span class="input-group-text"><b>Страна</b></span>
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
                    <h5>Это поле необходимо отметить</h5>
                </div>
            </#if>

            <div class="form-check">
                <input type="checkbox" name="agreement" class="form-check-input agrinp" id="agree" value="true" required>
                <label class="form-check-label darkblue" for="agree"><b>Согласен на обработку персональных данных</b></label>
            </div>

            <button type="submit" class="btn btnlight m-2 p-2 w-auto"><b>Отправить</b></button>

        </form>
    </div>
</@p.page>