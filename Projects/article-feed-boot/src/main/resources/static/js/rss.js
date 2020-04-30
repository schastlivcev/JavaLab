window.onload = function () {
    loadNews();
};

function loadNews() {
    $.ajax({
        url: 'https://api.rss2json.com/v1/api.json',
        method: 'GET',
        dataType: 'json',
        data: {
            rss_url: 'http://lenta.ru/rss/last24',
            api_key: 'v6zojhkstcogzz9jaq6o1s4uc1usxlanejzun1mb',
            count: 5
        },
        // beforeSend: function () {
        //     $("#news_load").show();
        // }
    })
        .done(function (response) {
        if(response.status != 'ok'){
            $("#news_load").hide();
            $("#news_server_error").show();
        } else {
            $("#news_load").hide();
            for(let i in response.items){
                let item = response.items[i];
                $("#google_news_feed").append($.parseHTML('<hr class="my-1"><div class="d-inline-flex justify-content-between"><a class="m-0" href="' + item.link + '" style="word-break: break-word; max-width: 75%; color: black; line-height: 1.5rem">'+ item.title + '</a><h5 class="activegrey m-0">' + item.pubDate + '</h5></div>'));
            }
        }
    })
        .fail(function (jqxhr) {
            $("#news_load").hide();
            if(jqxhr.status === 500 || jqxhr.status === 505) {
                $("#news_load_error").show();
            } else {
                $("#news_server_error").show();
            }
        });
}