$(function () {
    changePersonalTimeScoreChart("week");

    $('#score_interval_select').change(function () {
        changePersonalTimeScoreChart($(this).val());
    });

    changePersonalTopicChart("week");

    $('#topic_interval_select').change(function () {
        changePersonalTopicChart($(this).val());
    });

    changePersonalSourceChart("week");
    $('#source_interval_select').change(function () {
        changePersonalSourceChart($(this).val());
    });
});

function showPersonalTimeScoreChart(data) {
    Highcharts.chart('score_personal_chart', {
        chart: {
            type: 'spline'
        },
        title: {
            text: 'Score Distribution'
        },
        xAxis: {
            type: 'datetime',
            dateTimeLabelFormats: { // don't display the dummy year
                month: '%e. %b',
                year: '%b'
            },
            title: {
                text: 'Date'
            }
        },
        yAxis: {
            title: {
                text: 'Score'
            },
            min: 0
        },
        tooltip: {
            headerFormat: '<b>{point.x:%b %eth}: {point.y:.2f}</b><br>',
            pointFormat: 'answer count: {point.count}'
        },

        plotOptions: {
            spline: {
                marker: {
                    enabled: true
                }
            }
        },

        // Define the data points. All series have a dummy year
        // of 1970/71 in order to be compared on the same x axis. Note
        // that in JavaScript, months start at 0 for January, 1 for February etc.
        series: [{
            name: "",
            data: data,
            showInLegend: false
        }]
    });
}

function changePersonalTimeScoreChart(interval) {
    $.ajax({
        method: "POST",
        url: "/report/getPersonalScoreByTime",
        data:{upn:upn,"interval":interval},
        async:true,
        dataType:"json"
    }).done(function (data) {
        if(data.length>0){
            $('#score_personal_chart').height(380);
            showPersonalTimeScoreChart(data);
            $('#pscore_chart_nodata').hide();
        }else{
            $('#score_personal_chart').height(50);
            $('#score_personal_chart').html('<div class="nodata_span" id="pscore_chart_nodata">No data</div>');
            $('#pscore_chart_nodata').show();
        }
    });
}

function showPersonalTopicScoreChart(x,y) {
    Highcharts.chart('topic_score_personal_chart', {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Score By Topic'
        },
        xAxis: {
            categories: x,
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Score',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px"></span>',
            pointFormat: '<td style="color:{series.color};padding:0">{point.category}: </td>' +
                '<td style="padding:0"><b>{point.y:.2f}</b></td>'
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true,
                    formatter:function () {
                        return Highcharts.numberFormat(this.point.y,2)
                    }
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 80,
            floating: true,
            borderWidth: 1,
            backgroundColor:
                Highcharts.defaultOptions.legend.backgroundColor || '#FFFFFF',
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: [{
            name: '',
            data: y,
            showInLegend: false
        }]
    });
}

function changePersonalTopicChart(interval) {
    $.ajax({
        method: "POST",
        url: "/report/getPersonalTopicScore",
        data:{upn:upn,"interval":interval},
        async:true,
        dataType:"json"
    }).done(function (data) {
        var x= [];
        var y = [];
        for(var i = 0;i<data.length;i++){
            x[i] = data[i]['topic'];
            y[i] = data[i]['score'];
        }

        if(data.length>0){
            $('#topic_score_personal_chart').height(Math.max(Math.min(data.length*12,1200),380));
            showPersonalTopicScoreChart(x,y);
            $('#ptopic0_chart_nodata').hide();
        }else{
            $('#topic_score_personal_chart').height(50);
            $('#topic_score_personal_chart').html('<div class="nodata_span" id="ptopic0_chart_nodata">No data</div>');
            $('#ptopic0_chart_nodata').show();
        }
    });

    $.ajax({
        method: "POST",
        url: "/report/getPersonalTopicCount",
        data:{upn:upn,"interval":interval},
        async:true,
        dataType:"json"
    }).done(function (data) {
        var x= [];
        var y = [];
        for(var i = 0;i<data.length;i++){
            x[i] = data[i]['topic'];
            y[i] = data[i]['count'];
        }

        if(data.length>0){
            $('#topic_count_personal_chart').height(Math.max(Math.min(data.length*12,1200),380));
            showPersonalTopicCountChart(x,y);
            $('#ptopic1_chart_nodata').hide();
        }else{
            $('#topic_count_personal_chart').height(50);
            $('#topic_count_personal_chart').html('<div class="nodata_span" id="ptopic1_chart_nodata">No data</div>');
            $('#ptopic1_chart_nodata').show();
        }
    });
}

function showPersonalTopicCountChart(x,y) {
    Highcharts.chart('topic_count_personal_chart', {
        chart: {
            type: 'bar'
        },
        title: {
            text: 'Count By Topic'
        },
        xAxis: {
            categories: x,
            title: {
                text: null
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Count',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px"></span>',
            pointFormat: '<td style="color:{series.color};padding:0">{point.category}: </td>' +
                '<td style="padding:0"><b>{point.y}</b></td>'
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -40,
            y: 80,
            floating: true,
            borderWidth: 1,
            backgroundColor:
                Highcharts.defaultOptions.legend.backgroundColor || '#FFFFFF',
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: [{
            name: '',
            data: y,
            showInLegend: false
        }]
    });
}

function showPersonalSourceScoreChart(x,y){
    Highcharts.chart('source_score_personal_chart', {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Score By Source'
        },
        xAxis: {
            categories: x,
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'score'
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px"></span>',
            pointFormat: '<td style="color:{series.color};padding:0">{point.category}: </td>' +
                '<td style="padding:0"><b>{point.y:.2f}</b></td>',
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

function showPersonalSourceCountChart(x,y){
    Highcharts.chart('source_count_personal_chart', {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Count By Source'
        },
        xAxis: {
            categories: x,
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'count'
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

function changePersonalSourceChart(interval) {
    $.ajax({
        method: "POST",
        url: "/report/getPersonalSourceScore",
        data:{upn:upn,"interval":interval},
        async:true,
        dataType:"json"
    }).done(function (data) {
        var x= [];
        var y = [];
        for(var i = 0;i<data.length;i++){
            x[i] = data[i]['source'];
            y[i] = data[i]['score'];
        }

        if(data.length>0){
            $('#source_score_personal_chart').height(380);
            showPersonalSourceScoreChart(x,y);
            $('#psource0_chart_nodata').hide();
        }else{
            $('#source_score_personal_chart').height(50);
            $('#source_score_personal_chart').html('<div class="nodata_span" id="psource0_chart_nodata">No data</div>');
            $('#psource0_chart_nodata').show();
        }
    });

    $.ajax({
        method: "POST",
        url: "/report/getPersonalSourceCount",
        data:{upn:upn,"interval":interval},
        async:true,
        dataType:"json"
    }).done(function (data) {
        var x= [];
        var y = [];
        for(var i = 0;i<data.length;i++){
            x[i] = data[i]['source'];
            y[i] = data[i]['count'];
        }

        if(data.length>0){
            $('#source_count_personal_chart').height(380);
            showPersonalSourceCountChart(x,y);
            $('#psource1_chart_nodata').hide();
        }else{
            $('#source_count_personal_chart').height(50);
            $('#source_count_personal_chart').html('<div class="nodata_span" id="psource1_chart_nodata">No data</div>');
            $('#psource1_chart_nodata').show();
        }
    });
}