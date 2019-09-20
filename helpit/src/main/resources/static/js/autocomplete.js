/*jslint  browser: true, white: true, plusplus: true */
/*global $, countries */
var keywords_hints = [];

$(function () {
    'use strict';

    $.ajax({
        method: "POST",
        url: "/keyword/getAll",
        async:true,
        dataType:"json"
    }).done(function (data) {
        keywords_hints =  data.map(function(a) {return a['keyword'];});

        // Initialize ajax autocomplete:
        $('#autocomplete-ajax').autocomplete({
            // serviceUrl: '/autosuggest/service/url',
            lookup: keywords_hints,
            lookupFilter: function(suggestion, originalQuery, queryLowerCase) {
                var re = new RegExp('\\b' + $.Autocomplete.utils.escapeRegExChars(queryLowerCase), 'gi');
                return re.test(suggestion.value);
            },
            onSelect: function(suggestion) {
                var keyword = suggestion.value;
                searchParam['keywords'] = keyword;
                search();
            },
            onHint: function (hint) {
                $('#autocomplete-ajax-x').val(hint);
            },
            onInvalidateSelection: function() {
                // $('#selction-ajax').html('You selected: none');
            }
        });
    });

    // Setup jQuery ajax mock:
    // $.mockjax({
    //     url: '*',
    //     responseTime: 2000,
    //     response: function (settings) {
    //         var query = settings.data.query,
    //             queryLowerCase = query.toLowerCase(),
    //             re = new RegExp('\\b' + $.Autocomplete.utils.escapeRegExChars(queryLowerCase), 'gi'),
    //             suggestions = $.grep(keywords_hints, function (keyword) {
    //                 // return country.value.toLowerCase().indexOf(queryLowerCase) === 0;
    //                 return re.test(keyword.value);
    //             }),
    //             response = {
    //                 query: query,
    //                 suggestions: suggestions
    //             };
    //
    //         this.responseText = JSON.stringify(response);
    //     }
    // });


});