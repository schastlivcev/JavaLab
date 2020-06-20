<#import "*/page.ftl" as p>
<#import "spring.ftl" as s>
<@p.page title="ArticleFeed">
<div class="d-flex flex-column m-2 justify-content-center widwin mx-auto" style="height: 98vh">
    <div class="card text-center">
        <h4 class="cardhead activewhite"><b>ArticleFeed</b><i class="fas fa-rss pl-2 iconstl activewhite"></i></h4>
        <p><@s.message 'welcome.header'/></p>
        <a class="btn p-2 mx-2 w-auto btnlight" href="/signIn"><b><@s.message 'sign.in.button'/></b></a>
        <p><@s.message 'welcome.or'/></p>
        <a class="btn p-2 mx-2 mb-2 w-auto btnlight" href="/signUp"><b><@s.message 'sign.up.button'/></b></a>
    </div>
</div>
</@p.page>