<#import "*/page.ftl" as p>
<@p.page title="ChatRooms">
    <div style="display: flex; flex-direction: column; align-items: center; justify-content: center">
        <h3>Create Room</h3>
        <form method="post">
            <label>Name</label>
            <input type="text" name="name" placeholder="optional">

            <input type="submit" value="Create">
        </form>
    </div>
</@p.page>