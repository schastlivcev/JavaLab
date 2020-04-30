<#macro form_message title footer_type footer_text button_href="">
<#import "*/page.ftl" as p>
<@p.page title=title>
    <div class="d-flex flex-column m-2 justify-content-center widwin mx-auto" style="height: 98vh">
        <div class="card text-center">
            <h4 class="cardhead activewhite"><b>ArticleFeed</b><i class="fas fa-rss pl-2 iconstl activewhite"></i></h4>
            <h4 class="cardcent"><#nested/></h4>
            <#if footer_type == "error">
                <h3 class="cardfoot">${footer_text}</h3>
            <#elseif footer_type == "button">
                <a class="btn p-2 mx-2 mb-2 w-auto btnlight" href="${button_href}"><b>${footer_text}</b></a>
            <#elseif footer_type == "empty"></#if>
        </div>
    </div>
</@p.page>
</#macro>