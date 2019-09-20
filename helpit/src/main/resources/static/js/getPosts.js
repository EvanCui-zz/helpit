var searchParam = {};
var pageIndex = 0;
var sources = [];
var subscribeTopics = [];
var subscribeSources = [];

function init() {

    // searchParam = { page:pageIndex,topics:topics,sources:sources };
    //
    // $.ajax({
    //     method: "POST",
    //     url: "/search/getInSubscriptions",
    //     data: JSON.stringify(searchParam),
    //     async:true,
    //     contentType:"application/json",
    //     dataType:"json"
    // }).done(function (data) {
    //     if(data.length>0)
    //         $('#posts_panel').html(showPosts(data));
    //     else{
    //         if(topics.length==subscribeTopics.length)
    //             $('#posts_panel').html('You haven\'t subscribed any topics. <a href=\'/topic\'>Subscribe here.</a>');
    //     }
    // });

    initTopicsFilter();
    initSourcesFilter();
    // initTopicsRank();
    // initKeywordsRank();
    filterTopics();
    filterSources();
    filterKeywords();
    // topicRankSubscribe();


    $(window).scroll(function(){
        // $('#notification_bar').hide();
        var scrollTop = $(this).scrollTop(),scrollHeight = $(document).height(),windowHeight = $(this).height();
        if(scrollTop + windowHeight - scrollHeight>=-1){
            loadMore();
        }
    });

    $('#top_button').on("click",function () {
        $("body , html").animate({scrollTop:0},300);
    });
}

// function initSubscribeTopics() {
//     $.ajax({
//         method: "POST",
//         url: "/topic/getByUpn",
//         data: { upn: upn },
//         async:false
//     }).done(function (data) {
//         if(data!=null&&data.length>0){
//             subscribeTopics = data.slice(0);
//         }
//     });
// }
//
// function initSubscribeSources() {
//     $.ajax({
//         method: "POST",
//         url: "/source/getSubscribed",
//         data: { upn: upn },
//         async:false
//     }).done(function (data) {
//         if(data!=null&&data.length>0){
//             subscribeSources = data.slice(0);
//         }
//     });
// }

function showPosts(data) {
    var content = "";
    var i = 0;
    while(i<data.length){
        content+='<div class="row" style="margin-top: 20px;">';
        for(var k = 0;k<3;k++){
            if(i<data.length){
                var ct = data[i]['title'];
                var stmp = '';
                if(data[i]['source']=='yammer'){
                    if(ct.length>100)
                        ct = ct.substring(0,100)+'...';
                    stmp = 'from Yammer';
                    // content+='<div class="col-md-4">\n' +
                    //     '                <h6 class="card-subtitle mb-2 text-muted">from Yammer</h6>\n' +
                    //     '                <p style="word-break:break-all;">'+ct+'</p>\n' +
                    //     '                <p>topics:\n' +
                    //     '                    <small class="text-muted">\n';
                }else{
                //     content+='<div class="col-md-4">\n' +
                //         '                <h5 class="card-title" style="word-break:break-all;">'+data[i]['title']+'</h5>\n' +
                //         '                <h6 class="card-subtitle mb-2 text-muted">from Stackoverflow</h6>\n' +
                //         '                <p style="margin-bottom: 5px;">topics:\n' +
                //         '                    <small class="text-muted">\n';
                    stmp = 'from Stackoverflow';
                }
                content+='<div class="col-md-4" data-id="'+data[i]['threadId']+'">\n' +
                    '                <h5 class="card-title" style="word-break:break-all;">'+ct+'</h5>\n' +
                    '                <h6 class="card-subtitle mb-2 text-muted">'+stmp+
                    '<a style="float: right;margin-right: 5%;" href="#" title="invite others to answer" data-toggle="modal" data-target="#inviteModal" onclick="showInviteModal(this)">invite</a></h6>\n' +
                    '                <p style="margin-bottom: 5px;">topics:\n' +
                    '                    <small class="text-muted">\n';

                var tags = data[i]['topics'];
                for(var j = 0;j<tags.length;j++){
                    content+='<span class="badge badge-primary">'+tags[j]+'</span>\n';
                }

                var create_time = data[i]['create_time'];
                var create_date = create_time.substring(0,create_time.length-9).replace('T',' ');
                var active_time = data[i]['active_time'];
                var active_date = active_time.substring(0,create_time.length-9).replace('T',' ');
                content+='</small>\n' +
                    '                </p>\n' +
                    '                <p style="margin-bottom: 0px;">\n' +
                    '                    <small>created by <a href="'+data[i]['creator_url']+'" target="_blank">'+data[i]['creator_name']+'</a> at '+create_date+'</small>\n' +
                    '                </p>\n' +
                    '                <p>\n' +
                    '                    <small>last updated <a href="'+data[i]['updator_url']+'" target="_blank">'+data[i]['updator_name']+'</a> at '+active_date+'</small>\n' +
                    '                </p>\n' +
                    '                <p><a class="btn btn-secondary post_web_url" href="'+data[i]['web_url']+'" role="button" target="_blank">View details &raquo;</a></p>\n' +
                    '            </div>';
                i++;
            }else{
                break;
            }
        }
        content+='</div><hr>';
    }
    return content;
}

function addVisitCount() {
    $("body").on("click",".post_web_url",function(){
        $.ajax({
            method: "POST",
            url: "/report/addVisitCount",
            data: {upn: upn},
            async:false
        }).done(function (data) {
        });
    });
}

function loadMore() {
    pageIndex+=1;
    searchParam['page'] = pageIndex;
    // var tmp = { topics: searchParam['topics'],page:searchParam['page'],sources:searchParam['sources']};
    // if(searchParam['keywords']!=undefined&&searchParam['keywords']!=null&&searchParam['keywords'].length>0)
    //     tmp['keywords'] = searchParam['keywords'];
    // if(searchParam['topics'].length==0)
    //     tmp['topics'] = topics.slice(0);
    // if(searchParam['sources'].length==0)
    //     tmp['sources'] = sources.slice(0);

    $.ajax({
        method: "POST",
        url: "/search/getInSubscriptions",
        data: JSON.stringify(searchParam),
        async:false,
        contentType:"application/json",
        dataType:"json"
    }).done(function (data) {
        $('#posts_panel').append(showPosts(data));
    });
}

function initSourcesFilter() {
    var content = '';
    for(var i = 0;i<sources.length;i++){
        content+='<div style="display: inline-block;" class="filter_checkbox_div"><input name="source_checkbox" type="checkbox" value="'+sources[i]+'" checked="checked"/><span style="margin-left: 5px;">'+sources[i]+"</span></div>";
    }
    $('#sources_filter_div').html(content);
}

function filterTopics() {
   $('input[name="topic_checkbox"]').change(function () {
       if(this.checked){
           searchParam['topics'].push($(this).val());
       }else{
           searchParam['topics'].splice(searchParam['topics'].indexOf($(this).val()),1);
       }
       search();
   });
}

function filterSources() {
    $('input[name="source_checkbox"]').change(function () {
        if(this.checked){
            searchParam['sources'].push($(this).val());
        }else{
            searchParam['sources'].splice(searchParam['sources'].indexOf($(this).val()),1);
        }
        search();
    });
}

function filterKeywords() {
    $('#autocomplete-ajax').blur(function () {
        searchParam['keywords'] = $(this).val();
        search();
    });
    $('#autocomplete-ajax').keydown(function (event) {
        if (event.keyCode == "13"){
            searchParam['keywords'] = $(this).val();
            search();
        }
    });
}

function search(){
    // var tarr = [];
    // $('.tag_filter_div').each(function (i) {
    //     tarr[i] = $(this).attr("data-val");
    // });
    // if(tarr.length==0)
    //     tarr = topics;
    //
    // var sarr = [];
    // $('.source_filter_div').each(function (i) {
    //     sarr[i] = $(this).attr("data-val");
    // });
    // if(sarr.length==0)
    //     sarr = sources;
    //
    // var keywords = $('#keywords_input').val();
    pageIndex=0;
    searchParam['page'] = pageIndex;
    // searchParam = { topics: tarr,page:pageIndex,sources:sarr,keywords:keywords};
    // var tmp = { topics: searchParam['topics'],page:0,sources:searchParam['sources']};
    if(searchParam['keywords']!=undefined&&searchParam['keywords']!=null&&searchParam['keywords'].length>0) {
        // tmp['keywords'] = searchParam['keywords'];
        $.ajax({
            method: "POST",
            url: "/keyword/save",
            data: JSON.stringify({keyword:searchParam['keywords'],upn:upn,time:new Date()}),
            async:true,
            contentType:"application/json"
        }).done(function (data) {

        });
    }
    // if(searchParam['topics'].length==0)
    //     tmp['topics'] = topics.slice(0);
    // if(searchParam['sources'].length==0)
    //     tmp['sources'] = sources.slice(0);

    // $('body').loading({
    //     overlay: $('#custom-overlay')
    // });
    $.ajax({
        method: "POST",
        url: "/search/getInSubscriptions",
        data: JSON.stringify(searchParam),
        async:false,
        contentType:"application/json",
        dataType:"json"
    }).done(function (data) {
        $('#posts_panel').html(showPosts(data));
        // $('body').loading('stop');
    });
}

function initKeywordsRank() {
    $.ajax({
        method: "POST",
        url: "/keyword/getHotByNum",
        data:{num:10},
        async:true,
        dataType:"json"
    }).done(function (data) {
        var content = '';
        for(var i = 0;i<data.length;i++){
            content+='<li class="list-group-item keyword_rank_li" onMouseOver="$(this).tooltip(\'show\')" data-toggle="tooltip" data-placement="left" title="click to search">'+data[i]['keyword']+'</li>'
        }
        $('#keywords_rank').html(content);
        clickKeywordToSearch();
    });
}

function clickKeywordToSearch() {
    $('.keyword_rank_li').click(function () {
        var keywordtmp = $(this).html();
        $("#autocomplete-ajax").val(keywordtmp);
        searchParam['keywords'] = keywordtmp;
        search();
    });
}

// function initTopicsRank() {
//     $.ajax({
//         method: "POST",
//         url: "/topic/getTop10",
//         async:true,
//         dataType:"json"
//     }).done(function (data) {
//         var content = '';
//         for(var i = 0;i<data.length;i++){
//             content+='<li class="list-group-item topic_rank_li" title="click to subscribe">'+data[i]['topic']+'</li>'
//         }
//         $('#topics_rank').html(content);
//         topicRankSubscribe();
//     });
// }

// function topicRankSubscribe() {
//     $('.topic_rank_li').click(function () {
//         var topic = $(this).html();
//         var timestamp = new Date().getTime();
//         var content = "<div class=\"toast\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\" data-delay=\"10000\" id='" + timestamp + "'>\n" +
//             "            <div class=\"toast-header\">\n" +
//             "                <strong class=\"mr-auto\">helpit</strong>\n" +
//             "<!--                <small class=\"text-muted\">just now</small>-->\n" +
//             "                <button type=\"button\" class=\"ml-2 mb-1 close\" data-dismiss=\"toast\" aria-label=\"Close\">\n" +
//             "                    <span aria-hidden=\"true\">&times;</span>\n" +
//             "                </button>\n" +
//             "            </div>\n" +
//             "            <div class=\"toast-body\">\n";
//
//         if(subscribeTopics.indexOf(topic)>=0){
//             $("#notifications").append(content +
//                 "                You have already subscribe this topic.\n" +
//                 "            </div>\n" +
//                 "        </div>");
//             var option = '#' + timestamp;
//             $(option).toast('show');
//         }else {
//             $.ajax({
//                 method: "POST",
//                 url: "/user/subscribe",
//                 data: {upn: upn, topic: topic},
//                 async: false
//             }).done(function (data) {
//                 if (data) {
//                     $("#notifications").append(content +
//                         "                Subscribe successfully.\n" +
//                         "            </div>\n" +
//                         "        </div>");
//                     if(topics.length==subscribeTopics.length){
//                         topics.push(topic);
//                         initTopicsFilter();
//                         search();
//                     }
//                     subscribeTopics.push(topic);
//                 } else {
//                     $("#notifications").append(content +
//                         "                Subscribe failed,please try again\n" +
//                         "            </div>\n" +
//                         "        </div>");
//                 }
//                 var option = '#' + timestamp;
//                 $(option).toast('show');
//             });
//         }
//     });
// }

function showInviteModal(e) {
    var pid = $(e).parent().parent().attr('data-id');
    $('#inviteModal').attr('data-pid',pid);
}

function sendInvitation() {
    var receiver = $('#receiver_input').val();
    var pid = $('#inviteModal').attr('data-pid');
    var pdiv = $('div[data-id="'+pid+'"]');
    var title = $(pdiv).find('.card-title').eq(0).html();
    var web_url = $(pdiv).find('.btn-secondary').eq(0).attr('href');

    if(checkReceiver(receiver)){
        var date = new Date();
        var message = '';
        var otime = date.toLocaleString();
        var bell_title = '';
        // var timestamp = new Date().getTime();
        // var content = "<div class=\"toast\" role=\"alert\" aria-live=\"assertive\" aria-atomic=\"true\" data-delay=\"10000\" id='" + timestamp + "'>\n" +
        //     "            <div class=\"toast-header\">\n" +
        //     "                <strong class=\"mr-auto\">helpit</strong>\n" +
        //     "<!--                <small class=\"text-muted\">just now</small>-->\n" +
        //     "                <button type=\"button\" class=\"ml-2 mb-1 close\" data-dismiss=\"toast\" aria-label=\"Close\">\n" +
        //     "                    <span aria-hidden=\"true\">&times;</span>\n" +
        //     "                </button>\n" +
        //     "            </div>\n" +
        //     "            <div class=\"toast-body\">\n";

        $.ajax({
            method: "POST",
            url: "/invitation/send",
            data: JSON.stringify({sender:upn,receiver:receiver,title:title,web_url:web_url,time:date,status:false}),
            contentType:"application/json",
            async: false
        }).done(function (data) {
            if(data=='success'){
                message = "Send an invitation successfully.";
                bell_title = "Send an invitation successfully.";
                // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                //     "        <p>Send an invitation successfully.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                //     "    </div> <hr>");
                // $('#bell_nav_li').attr("data-original-title","Send an invitation successfully.");
                // $('#bell_nav_li').tooltip('show');
                // $("#notifications").append(content +
                //     "                Send successfully.\n" +
                //     "            </div>\n" +
                //     "        </div>");
            }else if(data=="duplicate"){
                message = "You have already sent this invitation.";
                bell_title = "You have already sent this invitation.";
                // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                //     "        <p>You have already sent this invitation.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                //     "    </div> <hr>");
                // $('#bell_nav_li').attr("data-original-title","You have already sent this invitation.");
                // $('#bell_nav_li').tooltip('show');
                // $("#notifications").append(content +
                //     "                You have already sent this invitation.\n" +
                //     "            </div>\n" +
                //     "        </div>");
            }else{
                message = "Failed to send the invitation, try again later.";
                bell_title = "Failed to send the invitation.";
                // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                //     "        <p>Failed to send the invitation, try again later.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                //     "    </div> <hr>");
                // $('#bell_nav_li').attr("data-original-title","Failed to send the invitation.");
                // $('#bell_nav_li').tooltip('show');
                // $("#notifications").append(content +
                //     "                Failed to send, try again later.\n" +
                //     "            </div>\n" +
                //     "        </div>");
            }
            addNotification(message,otime,bell_title);
            $('#inviteModal').modal('toggle');
            // $('#' + timestamp).toast('show');
        });
    }
}

function checkReceiver(receiver) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    if(re.test(String(receiver).toLowerCase())){
        var res = true;
        $.ajax({
            method: "POST",
            url: "/user/isExisted",
            data: {upn:receiver},
            async: false
        }).done(function (data) {
            if(!data){
                $('#invite_error').text('user does not exist');
                $('#invite_error').show();
                res = data;
            }
        });
        return res;
    }else{
        $('#invite_error').text('incorrect format');
        $('#invite_error').show();
        return false;
    }
}

function inviteInputFocus() {
    $('#invite_error').hide();
}

// function initModalSources() {
//     var content = '';
//     for(var i = 0;i<sources.length;i++){
//         content+='<div class="subscribe_div"><input name="source_checkbox" type="checkbox" value="'+sources[i]+'"/>'+sources[i]+"</div>";
//     }
//     $('#mySources_div').html(content);
// }
//
// function addTagsFilter() {
//     var tarr = [];
//     var tmp = '';
//     $("input[name='topic_checkbox']:checked").each(function(i){
//         var t = $(this).val();
//         tarr[i] = t;
//         tmp+='<div class="tag_filter_div" data-val="'+t+'">'+t
//             +'<button type="button" class="close" style="padding-left: 2px;display: inline-block;" onclick="removeTagsFilter(\''+t+'\')">\n' +
//             '                                <span aria-hidden="true">&times;</span>\n' +
//             '                            </button>' +
//             '</div>';
//     });
//     $('#topics_filter_div').html(tmp);
//     $('#topicsModal').modal('hide');
// }
//
// function addSourcesFilter() {
//     var sarr = [];
//     var tmp = '';
//     $("input[name='source_checkbox']:checked").each(function(i){
//         var t = $(this).val();
//         sarr[i] = t;
//         tmp+='<div class="source_filter_div" data-val="'+t+'">'+t
//             +'<button type="button" class="close" style="padding-left: 2px;display: inline-block;" onclick="removeSourcesFilter(\''+t+'\')">\n' +
//             '                                <span aria-hidden="true">&times;</span>\n' +
//             '                            </button>' +
//             '</div>';
//     });
//     $('#sources_filter_div').html(tmp);
//     $('#sourcesModal').modal('hide');
// }
//
// function removeTagsFilter(t) {
//     $('.tag_filter_div[data-val=\''+t+'\']').remove();
//     $("input[value='"+t+"']").prop("checked",false);
// }
//
// function removeSourcesFilter(t) {
//     $('.source_filter_div[data-val=\''+t+'\']').remove();
//     $("input[value='"+t+"']").prop("checked",false);
// }