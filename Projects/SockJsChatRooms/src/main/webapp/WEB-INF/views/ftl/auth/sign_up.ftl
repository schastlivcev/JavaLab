<#import "*/page.ftl" as p>
<@p.page title="Sign Up - ChatRooms">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center">
        <#if success?? && success == true>
            <h3 style="color: green">You have successfully Signed Up. Now you may Sign In.</h3>
            <a href="/signIn">Sign In</a>
        <#else>
            <form method="post">
                <h3 style="margin: 0">Sign Up</h3>

                <#if errors??>
                    <#list errors as error>
                        <h4 style="color: red; margin: 0">${error}</h4>
                    </#list>
                </#if>
                <label>Email</label>
                <input type="email" name="email" <#if email??>value="${email}"</#if> /><br>

                <label>Password</label>
                <input type="password" name="password" placeholder="not less 8 characters" /><br>

                <label>Repeat password</label>
                <input type="password" name="password_repeat" /><br>

                <label>Name</label>
                <input type="text" name="name" <#if name??>value="${name}"</#if> /><br>

                <label>Male</label>
                <input type="radio" name="sex" value="true" <#if !sex?? || sex == "true">checked</#if> />
                <label>Female</label>
                <input type="radio" name="sex" value="false" <#if sex?? && sex == "false">checked</#if> /><br>

                <input type="submit" value="Sign Up" />
            </form>
        </#if>
    </div>
</@p.page>