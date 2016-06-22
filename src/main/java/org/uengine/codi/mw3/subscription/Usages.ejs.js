var org_uengine_codi_mw3_subscription_Usages = function(objectId, className){
    this.objectId = objectId;
    this.className = className;
    this.object = mw3.objects[this.objectId];

    this.drawUsages();
    this.drawSettings();

}

org_uengine_codi_mw3_subscription_Usages.prototype = {
    drawUsages: function() {
        var jsonArray = new Array()

        if (this.object.usagesInfo == null) {
            jsonArray = [];
        } else {
            var json = JSON.parse(this.object.usagesInfo);
            jsonArray.push(json);
        }

        $('#usages').DataTable({
            aaData: jsonArray,
            bPaginate: false,
            columnDefs: [{
                targets: 3,
                data: null,
                render: function ( data, type, full, meta ) {
                    var htmlStr =  "TOTAL USAGES : " + full.rolledUpUnits[0].amount + " " + full.rolledUpUnits[0].unitType
                    return htmlStr;
                }
            }],
            aoColumns: [
                { "mDataProp": "subscriptionId" },
                { "mDataProp": "startDate" },
                { "mDataProp": "endDate" },
                { "mDataProp": "3" }
            ]
        });
    },

    drawSettings: function() {
        $('#usages').width('650');
        $('#usages_info').remove();
    }
}