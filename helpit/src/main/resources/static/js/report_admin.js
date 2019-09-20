var visit_counts = {};

$(function () {
    $.ajax({
        method: "POST",
        url: "/user/getNumber",
        async:true
    }).done(function (data) {
        $('#userNum').text(data);
    });

    $('#visits_total').text(visit_total);
    $('#visits_today').text(visit_today);

    // changeReplyCountChart("yesterday");
    // changeVisitCountChart("yesterday");

    $.when(
        $.ajax({
            method: "POST",
            url: "/report/getReplyCount",
            data:{"interval":"week"},
            async:true,
            dataType:"json"
        }).done(function (data) {
            var x = [];
            var y = [];

            for(var i = 0;i<data.length;i++){
                x[i] = data[i]['upn'];
                y[i] = data[i]['tcount'];
            }

            if(data.length>0){
                $('#reply_count_chart').height(380);
                showReplyCountChart(x,y);
                $('#rcount_chart_nodata').hide();
            }else{
                $('#reply_count_chart').height(50);
                $('#reply_count_chart').html('<div class="nodata_span" id="rcount_chart_nodata">No data</div>');
                $('#rcount_chart_nodata').show();
            }
        }),
        $.ajax({
            method: "POST",
            url: "/report/getVisitCount",
            data:{"interval":"week"},
            async:true,
            dataType:"json"
        }).done(function (data) {
            var x = [];
            var y = [];

            for(var i = 0;i<data.length;i++){
                x[i] = data[i]['upn'];
                y[i] = data[i]['tcount'];
                visit_counts[data[i]['upn']]=data[i]['tcount'];
            }

            if(data.length>0){
                $('#visit_count_chart').height(380);
                showVisitCountChart(x,y);
                $('#vcount_chart_nodata').hide();
            }else{
                $('#visit_count_chart').height(50);
                $('#visit_count_chart').html('<div class="nodata_span" id="vcount_chart_nodata">No data</div>');
                $('#vcount_chart_nodata').show();
            }
        })
        // changeReplyCountChart("yesterday"),
        // changeVisitCountChart("yesterday")
    ).then(function (a,b) {

        var active_users = {};
        for(var i = 0;i<a[0].length;i++){
            if(i>20)
                break;
            var a_upn_tmp = a[0][i]['upn'];
            active_users[a_upn_tmp]=a[0][i]['tcount']*0.8+visit_counts[a_upn_tmp]*0.2;
        }

        var sdic=Object.keys(active_users).sort(function(a,b){return active_users[a]-active_users[b]});
        var num = 0;
        for(var u in sdic){
            $('#active_users_ul').append('<li class="list-group-item">'+sdic[u].split('@')[0]+'</li>');
            num+=1;
            if(num>=5)
                break;
        }
        if(sdic.length>0)
            $('#active_users_ul').parent().show();
    });

    $('#rcount_interval_select').val("week");
    $('#vcount_interval_select').val("week");

    $('#rcount_interval_select').change(function () {
        changeReplyCountChart($(this).val());
    });

    $('#vcount_interval_select').change(function () {
        changeVisitCountChart($(this).val());
    });

});

function showReplyCountChart(x,y) {
    Highcharts.chart('reply_count_chart', {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Number of Replies'
        },
        xAxis: {
            categories: x,
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'number of replies'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px"></span>',
            pointFormat: '<td style="color:{series.color};padding:0">{point.category}: </td>' +
                '<td style="padding:0"><b>{point.y}</b></td>',
            /* footerFormat: '</table>', */
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [{
            name: '',
            data: y,
            showInLegend: false
        }]
    });
}

function changeReplyCountChart(interval) {
    $.ajax({
        method: "POST",
        url: "/report/getReplyCount",
        data:{"interval":interval},
        async:true,
        dataType:"json"
    }).done(function (data) {
        var x = [];
        var y = [];

        for(var i = 0;i<data.length;i++){
            x[i] = data[i]['upn'];
            y[i] = data[i]['tcount'];
        }

        if(data.length>0){
            $('#reply_count_chart').height(380);
            showReplyCountChart(x,y);
            $('#rcount_chart_nodata').hide();
        }else{
            $('#reply_count_chart').height(50);
            $('#reply_count_chart').html('<div class="nodata_span" id="rcount_chart_nodata">No data</div>');
            $('#rcount_chart_nodata').show();
        }
    });
}

function showVisitCountChart(x,y) {
    Highcharts.chart('visit_count_chart', {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Visit times of posts'
        },
        xAxis: {
            categories: x,
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'visit times of posts'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px"></span>',
            pointFormat: '<td style="color:{series.color};padding:0">{point.category}: </td>' +
                '<td style="padding:0"><b>{point.y}</b></td>',
            /* footerFormat: '</table>', */
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.2,
                borderWidth: 0
            }
        },
        series: [{
            name: '',
            data: y,
            showInLegend: false
        }]
    });
}

function changeVisitCountChart(interval) {

    $.ajax({
        method: "POST",
        url: "/report/getVisitCount",
        data:{"interval":interval},
        async:true,
        dataType:"json"
    }).done(function (data) {
        var x = [];
        var y = [];

        for(var i = 0;i<data.length;i++){
            x[i] = data[i]['upn'];
            y[i] = data[i]['tcount'];
            visit_counts[data[i]['upn']]=data[i]['tcount'];
        }

        if(data.length>0){
            $('#visit_count_chart').height(380);
            showVisitCountChart(x,y);
            $('#vcount_chart_nodata').hide();
        }else{
            $('#visit_count_chart').height(50);
            $('#visit_count_chart').html('<div class="nodata_span" id="vcount_chart_nodata">No data</div>');
            $('#vcount_chart_nodata').show();
        }
    });
}