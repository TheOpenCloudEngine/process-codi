var org_uengine_codi_mw3_subscription_TimeLine = function(objectId, className){
    var dataSet = [
        [
            "Tiger Nixon",
            "System Architect",
            "Edinburgh",
            "5421",
            "2011/04/25",
            "$320,800"
        ],
        [
            "Garrett Winters",
            "Accountant",
            "Tokyo",
            "8422",
            "2011/07/25",
            "$170,750"
        ]
    ];

    var table = $('#timeLine').DataTable({
        dom: 'ftipr',
        data: dataSet,
        bPaginate: false,
        columns: [
            { data: '0' },
            { data: '1' },
            { data: '2' },
            { data: '3' },
            { data: '4' },
            { data: '5' }
        ]
    });

    $('#timeLine').width('650');
}
