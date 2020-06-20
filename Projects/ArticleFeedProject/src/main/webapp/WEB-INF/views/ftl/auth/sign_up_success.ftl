<#import "*/templates/form_message.ftl" as m>
<@m.form_message title="Регистрация - ArticleFeed" footer_type="button" footer_text="Войти" button_href="/signIn">
    <@s.message 'sign.up.success'/>
</@m.form_message>