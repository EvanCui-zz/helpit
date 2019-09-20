var topics = [];

$(function () {
    // $('body').loading({
    //     overlay: $('#custom-overlay')
    // });
    initKeywordsRank();

    $.when(
        // initSubscribeTopics(),
        //     initSubscribeSources()
        $.ajax({
            method: "POST",
            url: "/topic/getByUpn",
            data: {upn: upn},
            async: true
        }),
        $.ajax({
            method: "POST",
            url: "/source/getSubscribed",
            data: {upn: upn},
            async: true
        })
    ).done(function (a, b) {
        if (a[0] != null && a[0].length > 0) {
            subscribeTopics = a[0].slice(0);
        }
        if (b[0] != null && b[0].length > 0) {
            subscribeSources = b[0].slice(0);
        }
        topics = subscribeTopics.slice(0);
        sources = subscribeSources.slice(0);

        searchParam = {page: pageIndex, topics: topics.slice(0), sources: sources.slice(0)};

        $.ajax({
            method: "POST",
            url: "/search/getInSubscriptions",
            data: JSON.stringify(searchParam),
            async: true,
            contentType: "application/json",
            dataType: "json"
        }).done(function (data) {
            if (data.length > 0) {
                $('#posts_panel').html(showPosts(data));
            } else {
                $('#posts_panel').html('You haven\'t subscribed any topics. <a href=\'/topic\'>Subscribe here.</a>');
            }
        });
        addVisitCount();
        init();
    });

    $.ajax({
        method: "POST",
        url: "/topic/getTop",
        data: {num: 10},
        async: true,
        dataType: "json"
    }).done(function (data) {
        var content = '';
        for (var i = 0; i < data.length; i++) {
            content += '<li class="list-group-item topic_rank_li" onMouseOver="$(this).tooltip(\'show\')" data-toggle="tooltip" data-placement="left" title="click to subscribe">' + data[i]['topic'] + '</li>'
        }
        $('#topics_rank').html(content);
        // $('[data-toggle="tooltip"]').tooltip();
        topicRankSubscribe();
    });
    // initSubscribeTopics();
    // initSubscribeSources();
    // topics = subscribeTopics.slice(0);
    // sources = subscribeSources.slice(0);

    // init();

    // $('body').loading('stop');
});

function initTopicsFilter() {
    var content = '';
    // topics = ['test1','test2'];
    for (var i = 0; i < topics.length; i++) {
        content += '<div style="display: inline-block;" class="filter_checkbox_div"><input name="topic_checkbox" type="checkbox" value="' + topics[i] + '"  checked="checked"/><span style="margin-left: 5px;">' + topics[i] + "</span></div>";
    }

    if (topics.length == 0) {
        content = "You haven't subscribed any topics. <a href='/topic'>Subscribe here.</a>"
    }
    $('#topics_filter_div').html(content);
}

function topicRankSubscribe() {
    $('.topic_rank_li').click(function () {
        var topic = $(this).html();
        var message = '';
        var otime = new Date().toLocaleString();
        var bell_title = '';

        if (subscribeTopics.indexOf(topic) >= 0) {
            message = "You have already subscribed topic " + topic + ".";
            bell_title = "You have already subscribed topic " + topic + ".";

        } else {
            $.ajax({
                method: "POST",
                url: "/user/subscribe",
                data: {upn: upn, topic: topic},
                async: false
            }).done(function (data) {
                if (data) {
                    message = "Subscribe topic " + topic + " successfully.";
                    bell_title = "Subscribe topic " + topic + " successfully.";

                    if (topics.length == subscribeTopics.length) {
                        topics.push(topic);
                        initTopicsFilter();
                        search();
                    }
                    subscribeTopics.push(topic);
                } else {
                    message = "Subscribe topic " + topic + " failed,please try again.";
                    bell_title = "Subscribe topic " + topic + " failed.";
                }
            });
        }
        addNotification(message, otime, bell_title);
    });
}

