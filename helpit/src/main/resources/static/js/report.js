$(function () {
    // $.ajax({
    //     method: "POST",
    //     url: "/user/getNumber",
    //     async:true
    // }).done(function (data) {
    //     $('#userNum').text(data);
    // });
    //
    // $('#visits_total').text(visit_total);
    // $('#visits_today').text(visit_today);

    initUserChart();
    userChartChange();

    // changeReplyCountChart("yesterday");
    //
    // $('#rcount_interval_select').change(function () {
    //     changeReplyCountChart($(this).val());
    // });

    changeGeneralTopicChart(10);

    $('#topic_num_input').blur(function () {
        changeGeneralTopicChart($(this).val());
    });
    $('#topic_num_input').keydown(function (event) {
        if (event.keyCode == "13")
            changeGeneralTopicChart($(this).val());
    });

    changeGeneralKeywordChart(12);

    $('#keyword_num_input').blur(function () {
        changeGeneralKeywordChart($(this).val());
    });
    $('#keyword_num_input').keydown(function (event) {
        if (event.keyCode == "13")
            changeGeneralKeywordChart($(this).val());
    });
});

// Returns a flattened hierarchy containing all leaf nodes under the root.
function classes(root) {
    var classes = [];

    function recurse(name, node) {
        if (node.children) node.children.forEach(function(child) { recurse(node.name, child); });
        else classes.push({packageName: name, className: node.name, value: node.size});
    }

    recurse(null, root);
    return {children: classes};
}

function initUserChart() {
    $.ajax({
        method: "POST",
        url: "/report/getUserScoresByTime",
        data: {"interval":"week"},
        async:true,
        dataType:"json"
    }).done(function (data) {
        var chartdata = {"name": "flare","children":[]};
        for(var i = 0;i<data.length;i++){
            chartdata['children'][i] = { "name": data[i]['upn'].split('@')[0],
                "children": [
                    {"name":  data[i]['upn'].split('@')[0], "size":  data[i]['score']}
                ]};
        }
        if(data.length>0) {
            $('#user_chart_nodata').hide();

            var diameter = Math.min(1500,data.length*50),
                format = d3.format(",.2f"),
                color = d3.scaleOrdinal(d3.schemeCategory20c);

            var bubble = d3.pack()
                .size([diameter, diameter])
                .padding(1.5);

            var svg = d3.select("#user_chart").append("svg")
                .attr("width", diameter)
                .attr("height", diameter)
                .attr("class", "bubble");

            var root = d3.hierarchy(classes(chartdata))
                .sum(function(d) { return d.value; })
                .sort(function(a, b) { return b.value - a.value; });

            bubble(root);
            var node = svg.selectAll(".node")
                .data(root.children)
                .enter().append("g")
                .attr("class", "node")
                .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

            node.append("title")
                .text(function(d) { return d.data.className + ": " + format(d.value); });

            node.append("circle")
                .attr("r", function(d) { return d.r; })
                .style("fill", function(d) {
                    return color(d.data.packageName);
                });

            node.append("text")
            // .attr("dy", ".1em")
                .style("word-break", "break-all")
                .style("font-size", "10px")
                .style("text-anchor", "middle")
                .text(function(d) { return d.data.className; });

            d3.select(self.frameElement).style("height", diameter + "px");
        }else{
            $('#user_chart').height(50);
            $('#user_chart_nodata').show();
        }
    });
}

function userChartChange() {
    $('#user_interval_select').change(function () {

        $.ajax({
            method: "POST",
            url: "/report/getUserScoresByTime",
            data: {"interval":$(this).val()},
            async:true,
            dataType:"json"
        }).done(function (data) {

            var chartdata = {"name": "flare","children":[]};
            for(var i = 0;i<data.length;i++){
                chartdata['children'][i] = { "name": data[i]['upn'].split('@')[0],
                    "children": [
                        {"name":  data[i]['upn'].split('@')[0], "size":  data[i]['score']}
                    ]};
            }

            if(data.length>0){
                $('#user_chart_nodata').hide();
                var diameter = Math.min(1500,data.length*50),
                    format = d3.format(",.2f"),
                    color = d3.scaleOrdinal(d3.schemeCategory20c);

                var bubble = d3.pack()
                    .size([diameter, diameter])
                    .padding(1.5);

                var svg = d3.select("#user_chart").select("svg");
                if(svg['_groups'][0][0]==null){
                    svg = d3.select("#user_chart").append("svg").attr("class", "bubble");
                }
                svg.attr("width", diameter)
                    .attr("height", diameter);

                var root = d3.hierarchy(classes(chartdata))
                    .sum(function(d) { return d.value; })
                    .sort(function(a, b) { return b.value - a.value; });

                bubble(root);
                var node = svg.selectAll(".node").data(root.children)
                    .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

                svg.selectAll("title").data(root.children)
                    .text(function(d) { return d.data.className + ": " + format(d.value); });

                svg.selectAll("circle").data(root.children)
                    .attr("r", function(d) { return d.r; })
                    .style("fill", function(d) {
                        return color(d.data.packageName);
                    });

                svg.selectAll("text").data(root.children)
                    .text(function(d) {return d.data.className; });

                var nodeEnter = node.enter()
                    .append("g")
                    .attr("class", "node")
                    .attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });

                nodeEnter.append("title")
                    .text(function(d) { return d.data.className + ": " + format(d.value); });

                nodeEnter.append("circle")
                    .attr("r", function(d) { return d.r; })
                    .style("fill", function(d) {
                        return color(d.data.packageName);
                    });

                nodeEnter.append("text")
                // .attr("dy", ".1em")
                    .style("word-break", "break-all")
                    .style("font-size", "10px")
                    .style("text-anchor", "middle")
                    .text(function(d) { return d.data.className; });

                // node.transition()
                //     .duration(500);
                node.exit().transition()
                    .duration(500).remove();

                d3.select(self.frameElement).style("height", diameter + "px");
            }else{
                d3.select("#user_chart").select("svg").selectAll(".node").data([]).exit().remove();
                $('#user_chart').height(50);
                $('#user_chart_nodata').show();
            }
        });
    });
}

// function showReplyCountChart(x,y) {
//     Highcharts.chart('reply_count_chart', {
//         chart: {
//             type: 'column'
//         },
//         title: {
//             text: 'Number of Replies'
//         },
//         xAxis: {
//             categories: x,
//             crosshair: true
//         },
//         yAxis: {
//             min: 0,
//             title: {
//                 text: 'number of replies'
//             }
//         },
//         tooltip: {
//             headerFormat: '<span style="font-size:10px"></span>',
//             pointFormat: '<td style="color:{series.color};padding:0">{point.category}: </td>' +
//                 '<td style="padding:0"><b>{point.y}</b></td>',
//             /* footerFormat: '</table>', */
//             shared: true,
//             useHTML: true
//         },
//         plotOptions: {
//             column: {
//                 pointPadding: 0.2,
//                 borderWidth: 0
//             }
//         },
//         series: [{
//             name: '',
//             data: y,
//             showInLegend: false
//         }]
//     });
// }
//
// function changeReplyCountChart(interval) {
//     var x = [];
//     var y = [];
//     $.ajax({
//         method: "POST",
//         url: "/report/getReplyCount",
//         data:{"interval":interval},
//         async:true,
//         dataType:"json"
//     }).done(function (data) {
//         for(var i = 0;i<data.length;i++){
//             x[i] = data[i]['upn'];
//             y[i] = data[i]['tcount'];
//         }
//
//         if(data.length>0){
//             $('#reply_count_chart').height(380);
//             showReplyCountChart(x,y);
//             $('#rcount_chart_nodata').hide();
//         }else{
//             $('#reply_count_chart').height(50);
//             $('#reply_count_chart').html('<div class="nodata_span" id="rcount_chart_nodata">No data</div>');
//             $('#rcount_chart_nodata').show();
//         }
//     });
// }

function showGeneralTopicChart(x,y) {
    Highcharts.chart('topic_general_chart', {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Hot Topics'
        },
        xAxis: {
            categories: x,
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'number of posts'
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

function changeGeneralTopicChart(num) {
    var x = [];
    var y = [];
    $.ajax({
        method: "POST",
        url: "/topic/getTop",
        data:{num:num},
        async:true,
        dataType:"json"
    }).done(function (data) {
        for(var i = 0;i<data.length;i++){
            x[i] = data[i]['topic'];
            y[i] = data[i]['count'];
        }
        showGeneralTopicChart(x,y);

    });
}

function showGeneralKeywordChart(x,y) {
    Highcharts.chart('keyword_general_chart', {
        chart: {
            type: 'column'
        },
        title: {
            text: 'Hot Keywords'
        },
        xAxis: {
            categories: x,
            crosshair: true
        },
        yAxis: {
            min: 0,
            title: {
                text: 'search times'
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

function changeGeneralKeywordChart(num) {
    var x = [];
    var y = [];
    $.ajax({
        method: "POST",
        url: "/keyword/getHotByNum",
        data:{num:num},
        async:true,
        dataType:"json"
    }).done(function (data) {
        for(var i = 0;i<data.length;i++){
            x[i] = data[i]['keyword'];
            y[i] = data[i]['num'];
        }
        showGeneralKeywordChart(x,y);
    });
}
