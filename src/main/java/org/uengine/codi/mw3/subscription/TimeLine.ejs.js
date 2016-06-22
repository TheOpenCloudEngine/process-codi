var org_uengine_codi_mw3_subscription_TimeLine = function(objectId, className){


    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];
    var json = JSON.parse(this.object.timeLineInfo);
    var jsonArray = new Array();

    for(key in json) {
        if(key == "bundles") {

            var timelineObj = json[key][0].timeline;
            var bundleId = timelineObj.bundleId;

            timelineObj.events.forEach(function(json){
                json["type"] = "timeline";
                json["bundleId"] = bundleId;
                jsonArray.push(json);
            });

        } else if(key == "invoices") {
            json[key].forEach(function(json) {
                json["type"] = "invoice";
                jsonArray.push(json);
            });
        }
    }

    $('#timeLine').dataTable({
        aaData: jsonArray,
        searching:false,
        bPaginate: false,
        columnDefs: [
            {
                "targets": 0,
                "data": null,
                "render": function ( data, type, full, meta ) {
                    var dataStr = "";
                    if(full.type == "timeline") {
                        dataStr = full.effectiveDate;
                    } else {
                        dataStr = full.invoiceDate;
                    }

                    return dataStr;
                }
            },
            {
                "targets": 1,
                "data": null,
                "render": function ( data, type, full, meta ) {
                    var dataStr = "";
                    if(full.type == "timeline") {
                        dataStr = full.bundleId;
                    } else {
                        dataStr = full.bundleKeys;
                    }

                    return dataStr;
                }
            },
            {
                "targets": 2,
                "data": null,
                "render": function ( data, type, full, meta ) {
                    var dataStr = "";
                    if(full.type == "timeline") {
                        dataStr = full.eventType;
                    } else {
                        dataStr = "INVOICE";
                    }

                    return dataStr;
                }
            },
            {
                "targets": 3,
                "data": null,
                "render": function ( data, type, full, meta ) {
                    var dataStr = "";
                    if(full.type == "timeline") {
                        dataStr = full.phase;
                    } else {
                        var dataStr =  '<ul>';
                        dataStr += '<li><span class="hide" title="11"></span>';
                        dataStr += 'Amount: $'+ full.amount + '('+ full.currency +')</li>';
                        dataStr += '<li>Balance: $'+ full.balance + '('+ full.currency +')</li>';
                        dataStr += '</ul>';

                    }
                    return dataStr;
                }
            },
            {
                "targets": 4,
                "data": null,
                "render": function ( data, type, full, meta ) {
                    var dataStr = "Done By " + full.auditLogs[0].changedBy;
                    return dataStr;
                }
            }
         ],
        "aoColumns": [
            { "mDataProp": "0" },
            { "mDataProp": "1" },
            { "mDataProp": "2" },
            { "mDataProp": "3" },
            { "mDataProp": "4" }
        ]
    });

    $('#timeLine').width('650');
    $('#timeLine_info').remove();
}
