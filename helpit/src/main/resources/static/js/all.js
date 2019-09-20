var topics = [];

$(function () {
    // $('body').loading({
    //     overlay: $('#custom-overlay')
    // });
    initKeywordsRank();

    $.when(
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
        }),
        $.ajax({
            method: "POST",
            url: "/topic/getAll",
            async: true
        }),
        $.ajax({
            method: "POST",
            url: "/source/getAll",
            async: true
        })
    ).done(function (a, b, c, d) {
        if (a[0] != null && a[0].length > 0) {
            subscribeTopics = a[0].slice(0);
        }
        if (b[0] != null && b[0].length > 0) {
            subscribeSources = b[0].slice(0);
        }
        for (var i = 0; i < c[0].length; i++) {
            topics[i] = c[0][i]['topic'];
        }
        sources = d[0].slice(0);
        searchParam['page'] = 0;
        searchParam['topics'] = topics.slice(0);
        searchParam['sources'] = sources.slice(0);

        init();
        $('#topic_filter_label').append('<a style="display: block" href="#" onclick="unselectAllTopics()">unselect all</a>');
    });

    if (keyword != null && keyword.toString().trim().length > 0) {
        keyword = keyword.toString().trim();
        $("#autocomplete-ajax").val(keyword);
        searchParam['keywords'] = keyword;
        $.ajax({
            method: "POST",
            url: "/search/getByKeyword",
            data: {keyword: keyword, page: 0},
            async: true,
            dataType: "json"
        }).done(function (data) {
                if (data.length > 0) {
                    $('#posts_panel').html(showPosts(data));

                } else {
                    $('#posts_panel').html('<div style="text-align: center;color:gray;font-size: 24px;line-height:100px;height: 100px;background: rgba(180,180,180,0.4);">NO DATA</div>');
                }
            addVisitCount();
            }
        );
    } else {
        $.ajax({
            method: "POST",
            url: "/search/getAll",
            data: {page: 0},
            async: true,
            dataType: "json"
        }).done(function (data) {
            if (data.length > 0) {
                $('#posts_panel').html(showPosts(data));
            } else {
                $('#posts_panel').html('<div style="text-align: center;color:gray;font-size: 24px;line-height:100px;height: 100px;background: rgba(180,180,180,0.4);">NO DATA</div>');
            }
            addVisitCount();
        });
    }

    $.ajax({
        method: "POST",
        url: "/topic/getTop",
        data: {num: 10},
        async: true,
        dataType: "json"
    }).done(function (data) {
        var content = '';
        for (var i = 0; i < data.length; i++) {
            content += '<li class="list-group-item topic_rank_li" onMouseOver="$(this).tooltip(\'show\')" data-toggle="tooltip" data-placement="left" title="click to search">' + data[i]['topic'] + '</li>'
        }
        $('#topics_rank').html(content);
        // $('[data-toggle="tooltip"]').tooltip();
        searchHotTopic();
    });
// $('body').loading('stop');
})
;

function unselectAllTopics() {
    $("input[name='topic_checkbox']").prop("checked", false);
    $('#posts_panel').html("");
    searchParam['page'] = 0;
    searchParam['topics'] = [];
}

function initTopicsFilter() {
    var content = '';
    for (var i = 0; i < topics.length; i++) {
        content += '<div style="display: inline-block;" class="filter_checkbox_div"><input name="topic_checkbox" type="checkbox" value="' + topics[i] + '"  checked="checked"/><span style="margin-left: 5px;">' + topics[i] + "</span></div>";
    }
    $('#topics_filter_div').html(content);
}

function searchHotTopic() {
    $('.topic_rank_li').click(function () {
        var topic = $(this).html();
        searchParam['topics'] = [topic];
        searchParam['sources'] = sources.slice(0);
        searchParam['keywords'] = null;

        $("input[name='topic_checkbox']").prop("checked", false);
        $("input[value='" + topic + "']").prop("checked", true);
        $("input[name='source_checkbox']").prop("checked", true);
        $("#autocomplete-ajax").val('');

        search();
    });
}

// function initModalTopics() {
//     var content = '';
//     for(var i = 0;i<topics.length;i++){
//         content+='<div class="subscribe_div"><input name="topic_checkbox" type="checkbox" value="'+topics[i]+'"/>'+topics[i]+"</div>";
//     }
//     $('#myTopics_div').html(content);
// }

// function subscribe() {
//     var topics = [];
//     $("input[name='topic_checkbox']:checked").each(function(i){
//         topics[i] =$(this).val();
//     });
//
//     $.ajax({
//         method: "POST",
//         url: "/user/modifySubscription",
//         data: JSON.stringify({ upn: upn, topics:topics }),
//         contentType:"application/json",
//         async:false
//     }).done(function (data) {
//     });
// }