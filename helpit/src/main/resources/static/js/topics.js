// var subscribeTopics = [];
var votedTopics = [];

$(function () {
    // $('body').loading({
    //     overlay: $('#custom-overlay')
    // });

    var hotTopics = [];
    var sutopics = [];
    $.when(
        $.ajax({
            method: "POST",
            url: "/topic/getTop",
            data:{num:10},
            async:true,
            dataType:"json"
        }).done(function (data) {
            hotTopics = data.slice(0);
            $('#hot_topics_group').html(generateTopicsGroup(data));
        }),
        $.ajax({
            method: "POST",
            url: "/topic/getNewest10",
            async:true,
            dataType:"json"
        }).done(function (data) {
            $('#new_topics_group').html(generateTopicsGroup(data));
        }),
        $.ajax({
            method: "POST",
            url: "/topic/getSuggestion",
            data: {upn: upn},
            async:true,
            dataType:"json"
        }).done(function (data) {
            if(data==null)
                data=[];
            var index = 0;
            while(data.length<20){
                if(index>=hotTopics.length){
                    break;
                }
                data.push(hotTopics[index]);
                index+=1;
            }
            $('#suggested_topics_group').html(generateTopicsGroup(data));
        }),
        $.ajax({
            method: "POST",
            url: "/topic/getAll",
            async: true
        }).done(function (data) {
            $('#all_topics_group').html(generateTopicsGroup(data));
        }),

        $.ajax({
            method: "POST",
            url: "/topic/getByUpn",
            data: {upn: upn},
            async: true
        }).done(function (data) {
            sutopics = data.slice(0);
        })
    ).then(function (a,b,c,d,e,f) {
        $.ajax({
            method: "POST",
            url: "/topic/getVoted",
            data: {upn: upn},
            async: true
        }).done(function (data) {
            for(var i = 0;i<data.length;i++){
                votedTopics[i] = data[i]['topic'];
                $('.voteDown_i[data-val="'+data[i]['topic']+'"]').hide();
                $('.undo_vote_i[data-val="'+data[i]['topic']+'"]').show();
            }
        });
        // $('#hot_topics_group').html(generateTopicsGroup(a[0]));
        // $('#new_topics_group').html(generateTopicsGroup(b[0]));
        // $('#suggested_topics_group').html(generateTopicsGroup(c[0]));
        // $('#all_topics_group').html(generateTopicsGroup(d[0]));

        // var votearr = e[0];
        // for(var i = 0;i<votearr.length;i++){
        //     votedTopics[i] = votearr[i]['topic'];
        //     $('.voteDown_i[data-val="'+votearr[i]['topic']+'"]').hide();
        //     $('.undo_vote_i[data-val="'+votearr[i]['topic']+'"]').show();
        // }
        // $('[data-toggle="tooltip"]').tooltip();
        votedTopic();

        $("input[name='topic_checkbox']").prop("checked", false);
        for (var i = 0; i < sutopics.length; i++) {
            $("input[value='" + sutopics[i] + "']").prop("checked", true);
            $('input[value="' + sutopics[i] + '"]').attr("title","click to unsubscribe");
        }

        $('.voteDown_i').hover(function () {
            $(this).removeClass('far');
            $(this).addClass('fas');
        },function () {
            $(this).removeClass('fas');
            $(this).addClass('far');
        });
        subscribeChange();
    });

    // $.ajax({
    //     method: "POST",
    //     url: "/topic/getTop10",
    //     async:false,
    //     dataType:"json"
    // }).done(function (data) {
    //     $('#hot_topics_group').html(generateTopicsGroup(data));
    // });

    // $.ajax({
    //     method: "POST",
    //     url: "/topic/getNewest10",
    //     async:false,
    //     dataType:"json"
    // }).done(function (data) {
    //     $('#new_topics_group').html(generateTopicsGroup(data));
    // });

    // $.ajax({
    //     method: "POST",
    //     url: "/topic/getSuggestion",
    //     data: {upn: upn},
    //     async:false,
    //     dataType:"json"
    // }).done(function (data) {
    //     $('#suggested_topics_group').html(generateTopicsGroup(data));
    // });

    // getAllTopics();
    // getVotedTopics();

    // $.ajax({
    //     method: "POST",
    //     url: "/topic/getByUpn",
    //     data: {upn: upn},
    //     async: false
    // }).done(function (data) {
    //     for (var i = 0; i < data.length; i++) {
    //         $("input[value='" + data[i] + "']").attr("checked", true);
    //     }
    //     // $('body').loading('stop');
    // });


});

function generateTopicsGroup(data) {
    var content = '';
    var len = Math.floor(data.length / 4);
    // var index;
    var remain = data.length-4 * len;
    for (var i = 0; i < 4; i++) {
        content += '<div style="display: inline-block;width: 24%;vertical-align: top;">';
        var tmp_len = len;
        if(i==0){
            tmp_len = len+remain;
        }

        for (var j = 0; j < tmp_len; j++) {
            var index = i * len + j;
            if(i>0)
                index+=remain;
            content += '<div style="margin-top: 10px;"><input onMouseOver="$(this).tooltip(\'show\')" data-toggle="tooltip" data-placement="left" title="click to subscribe" name="topic_checkbox" type="checkbox" value="' + data[index]['topic']
                + '"/><span style="margin-left: 5px;word-break:break-all;">' + data[index]['topic'] + '</span><i class="far fa-thumbs-down voteDown_i" data-val="'
                +data[index]['topic']+'" onMouseOver="$(this).tooltip(\'show\')" data-toggle="tooltip" data-placement="right" title="vote to delete"></i><i class="fas fa-undo-alt undo_vote_i" data-val="'
                +data[index]['topic']+'" onMouseOver="$(this).tooltip(\'show\')" data-toggle="tooltip" data-placement="right" title="undo vote" style="display: none;"></i></div>';
        }
        content+='</div>';
    }
    return content;
}

// function getAllTopics() {
//     $.ajax({
//         method: "POST",
//         url: "/topic/getAll",
//         async: false
//     }).done(function (data) {
//         $('#all_topics_group').html(generateTopicsGroup(data));
//     });
// }

function showAllTopics() {
    
    if($('#all_topics_group').css('display') == 'none'){
        $('#all_topics_group').show();
        // $('#browser_h').html('Hide All Topics<span style="margin-left: 10px;">\n' +
        //     '        <i class="fas fa-angle-double-up" style="font-size: 16px;"></i></span>');
        $('#browser_i').removeClass('fa-angle-double-down');
        $('#browser_i').addClass('fa-angle-double-up');
        $("html, body").animate({scrollTop:$('#all_topics_group').offset().top}, 300);
    } else {
        $('#all_topics_group').hide();
        $('#browser_i').removeClass('fa-angle-double-up');
        $('#browser_i').addClass('fa-angle-double-down');
        // $('#browser_h').html('Browser All Topics<span style="margin-left: 10px;">\n' +
        //     '        <i class="fas fa-angle-double-down" style="font-size: 16px;"></i></span>');
    }
}

function votedTopic() {
    // $.ajax({
    //     method: "POST",
    //     url: "/topic/getVoted",
    //     data: {upn: upn},
    //     async: false
    // }).done(function (data) {
    //     for(var i = 0;i<data.length;i++){
    //         votedTopics[i] = data[i]['topic'];
    //         $('.voteDown_i[data-val="'+data[i]['topic']+'"]').hide();
    //         $('.undo_vote_i[data-val="'+data[i]['topic']+'"]').show();
    //     }
    // });
    $("body").on("click",".voteDown_i",function(){
        var topic = $(this).attr('data-val');
        var message = '';
        var otime = new Date().toLocaleString();
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

        if(votedTopics.indexOf(topic)>=0){
            message = "You have already voted topic "+topic+" before.";
            bell_title = "You have already voted topic "+topic+" before.";
            // $('#notification_bar').append("<div class=\"notification_div\">\n" +
            //     "        <p>You have already voted topic "+topic+" before.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
            //     "    </div> <hr>");
            // $('#bell_nav_li').attr("data-original-title","You have already voted topic "+topic+" before.");
            // $('#bell_nav_li').tooltip('show');
            // $("#notifications").append(content +
            //     "                You have already voted this topic.\n" +
            //     "            </div>\n" +
            //     "        </div>");
        }else{
            $.ajax({
                method: "POST",
                url: "/topic/vote",
                data: {topic: topic,upn: upn},
                async: false
            }).done(function (data) {
                if (data) {
                    message = "Vote topic "+topic+" successfully.";
                    bell_title = "Vote topic "+topic+" successfully.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Vote topic "+topic+" successfully.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Vote topic "+topic+" successfully.");
                    // $('#bell_nav_li').tooltip('show');

                    // $("#notifications").append(content +
                    //     "                Vote successfully.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                    $('.voteDown_i[data-val="'+topic+'"]').hide();
                    $('.undo_vote_i[data-val="'+topic+'"]').show();
                    votedTopics.push(topic);
                } else {
                    message = "Vote topic "+topic+" failed,please try again.";
                    bell_title = "Vote topic "+topic+" failed.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Vote topic "+topic+" failed,please try again.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Vote topic "+topic+" failed.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Vote failed,please try again.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                }
            });
        }
        addNotification(message,otime,bell_title);
        // var option = '#' + timestamp;
        // $(option).toast('show');
    });

    $("body").on("click",".undo_vote_i",function(){
        var topic = $(this).attr('data-val');
        var message = '';
        var otime = new Date().toLocaleString();
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

        if(votedTopics.indexOf(topic)<0){
            message = "You haven't voted topic "+topic+" before.";
            bell_title = "You haven't voted topic "+topic+" before.";
            // $('#notification_bar').append("<div class=\"notification_div\">\n" +
            //     "        <p>You haven't voted topic "+topic+" before.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
            //     "    </div> <hr>");
            // $('#bell_nav_li').attr("data-original-title","You haven't voted topic "+topic+" before.");
            // $('#bell_nav_li').tooltip('show');
            // $("#notifications").append(content +
            //     "                You haven't voted this topic.\n" +
            //     "            </div>\n" +
            //     "        </div>");
        }else{
            $.ajax({
                method: "POST",
                url: "/topic/undoVote",
                data: {topic: topic,upn: upn},
                async: false
            }).done(function (data) {
                if (data) {
                    message = "Undo vote topic "+topic+" successfully.";
                    bell_title = "Undo vote topic "+topic+" successfully.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Undo vote topic "+topic+" successfully.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Undo vote topic "+topic+" successfully.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Undo vote successfully.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                    $('.voteDown_i[data-val="'+topic+'"]').show();
                    $('.undo_vote_i[data-val="'+topic+'"]').hide();
                    votedTopics.splice(votedTopics.indexOf(topic),1);
                } else {
                    message = "Undo vote topic "+topic+" failed,please try again.";
                    bell_title = "Undo vote topic "+topic+" failed.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Undo vote topic "+topic+" failed,please try again.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Undo vote topic "+topic+" failed.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Undo vote failed,please try again.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                }
            });
        }
        addNotification(message,otime,bell_title);
        // var option = '#' + timestamp;
        // $(option).toast('show');
    });
}

function subscribeChange() {
    $('input[name="topic_checkbox"]').change(function () {
        var topic = $(this).val();
        var message = '';
        var otime = new Date().toLocaleString();
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
        if(this.checked){
            $.ajax({
                method: "POST",
                url: "/user/subscribe",
                data: {upn: upn, topic: topic},
                async: false
            }).done(function (data) {
                if (data=='success') {
                    message = "Subscribe topic "+topic+" successfully.";
                    bell_title = "Subscribe topic "+topic+" successfully.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Subscribe topic "+topic+" successfully.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Subscribe topic "+topic+" successfully.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Subscribe successfully.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                    $("input[value='" + topic + "']").prop("checked", true);
                    $('input[value="' + topic + '"]').attr("data-original-title","click to unsubscribe");
                    $('input[value="' + topic + '"]').tooltip('hide');
                } else {
                    message = "Subscribe topic "+topic+" failed,please try again.";
                    bell_title = "Subscribe topic "+topic+" failed.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Subscribe topic "+topic+" failed,please try again.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Subscribe topic "+topic+" failed.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Subscribe failed,please try again.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                }
                // var option = '#' + timestamp;
                // $(option).toast('show');
            });
        }else{
            $.ajax({
                method: "POST",
                url: "/user/unsubscribe",
                data: {upn: upn, topic: topic},
                async: false
            }).done(function (data) {
                if (data=='success') {
                    message = "Unsubscribe topic "+topic+" successfully.";
                    bell_title = "Unsubscribe topic "+topic+" successfully.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Unsubscribe topic "+topic+" successfully.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Unsubscribe topic "+topic+" successfully.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Unsubscribe successfully.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                    $("input[value='" + topic + "']").prop("checked",false);
                    $('input[value="' + topic + '"]').attr("data-original-title","click to subscribe");
                    $('input[value="' + topic + '"]').tooltip('hide');
                } else {
                    message = "Unsubscribe topic "+topic+" failed,please try again.";
                    bell_title = "Unsubscribe topic "+topic+" failed,please try again.";
                    // $('#notification_bar').append("<div class=\"notification_div\">\n" +
                    //     "        <p>Unsubscribe topic "+topic+" failed,please try again.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
                    //     "    </div> <hr>");
                    // $('#bell_nav_li').attr("data-original-title","Unsubscribe topic "+topic+" failed.");
                    // $('#bell_nav_li').tooltip('show');
                    // $("#notifications").append(content +
                    //     "                Unsubscribe failed,please try again.\n" +
                    //     "            </div>\n" +
                    //     "        </div>");
                }
                // var option = '#' + timestamp;
                // $(option).toast('show');
            });
        }
        addNotification(message,otime,bell_title);
    });
}

function addTopicInput() {
    $('#addTopics_div').append('<input type="text" class="form-control" style="margin-top: 20px;">');
}

function addTopics() {
    // $('body').loading({
    //     overlay: $('#custom-overlay')
    // });

    var newTopics = [];
    $('#addTopics_div').find('input').each(function (i) {
        var tmp = $(this).val().trim();
        if(tmp!=null&&tmp.length>0)
            newTopics[i] = $(this).val();
    });

    $.ajax({
        method: "POST",
        url: "/topic/addNew",
        async:false,
        data: JSON.stringify(newTopics),
        contentType:"application/json",
        dataType:"json"
    }).done(function (data) {
        $('#addTopicsModal').modal('hide');
        // $('body').loading('stop');
        var message = "Send requests of adding topics successfully.";
        var otime = new Date().toLocaleString();
        var bell_title = "Send requests of adding topics successfully.";
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

        // $('#notification_bar').append("<div class=\"notification_div\">\n" +
        //     "        <p>We have received you requests of adding topics and will process later.<small class=\"text-muted\" style=\"float: right;\">"+otime.toLocaleString()+"</small></p>\n" +
        //     "    </div> <hr>");
        // $('#bell_nav_li').attr("data-original-title","Receive your requests of adding topics.");
        // $('#bell_nav_li').tooltip('show');

        // $("#notifications").append(content +
        //     "                We have received your requests and will process later.\n" +
        //     "            </div>\n" +
        //     "        </div>");

        // $('#' + timestamp).toast('show');
        addNotification(message,otime,bell_title);
    });
}

// function updateGroup() {
//     $.ajax({
//         method: "POST",
//         url: "/topic/getTop10",
//         async:true,
//         dataType:"json"
//     }).done(function (data) {
//         $('#hot_topics_group').html(generateTopicsGroup(data));
//     });
// }
//
//
// function modifySubscription() {
//     var topics = [];
//     $("input[name='subscribe_checkbox']:checked").each(function (i) {
//         topics[i] = $(this).val();
//     });
//
//     // $('body').loading({
//     //     overlay: $('#custom-overlay')
//     // });
//
//     $.ajax({
//         method: "POST",
//         url: "/user/modifySubscription",
//         data: JSON.stringify({upn: upn, topics: topics}),
//         contentType: "application/json",
//         async: false
//     }).done(function (data) {
//         // $('body').loading('stop');
//
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
//         if (data == "success") {
//             $("#notifications").append(content +
//                 "                Modify successfully.\n" +
//                 "            </div>\n" +
//                 "        </div>");
//         } else {
//             $("#notifications").append(content +
//                 "                Modify failed,please try again.\n" +
//                 "            </div>\n" +
//                 "        </div>");
//         }
//         var option = '#' + timestamp;
//         $(option).toast('show');
//     });
// }