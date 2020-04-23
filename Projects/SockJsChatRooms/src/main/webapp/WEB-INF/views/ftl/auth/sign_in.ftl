<#import "*/page.ftl" as p>
<@p.page title="Sign In - ChatRooms">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center">
        <form method="post">
            <h3 style="margin: 0">Sign In</h3>
            <#if errors??>
                <#list errors as error>
                    <h4 style="color: red; margin: 0">${error}</h4>
                </#list>
            </#if>
            <label>Email</label>
            <input type="email" name="email" <#if email??>value="${email}"</#if> /><br>

            <label>Password</label>
            <input type="password" name="password" /><br>

            <input type="submit" value="Sign In" />
        </form>
    </div>
</@p.page>