<#import "*/templates/form_message.ftl" as m>
<@m.form_message title="Ошибка - ArticleFeed" footer_type="error" footer_text="Статус: ${status}">
${text}
</@m.form_message>