Ext.namespace('Ext.ux.gantt');

/**
 * 간트챠트 : Ext.grid.GridPanel 을 Extension 하여 간트차트의
 * 오른쪽 영역인 스케줄바를 드로잉하는 차트영역을 구현한다.
 *
 * <pre>
 * config =
 *  {
 *      id: '',         // {String} ChartGrid ID
 *      chart_id: '',   // {String} GanttGhart ID
 *      viewMode: '',   // {String} 뷰모드(D:Day, W:Week, M:Month)
 *      startDate: '',  // {Date} 프로젝트 시작일
 *      finishDate: '', // {Date} 프로젝트 종료일
 *      store: ''       // {Ext.data.Store} Task Record Store
 *  }
 * </pre>
 *
 * @class Ext.ux.gantt.ChartGrid
 * @extends Ext.grid.GridPanel
 * @dependency ExtJS
 * @param config {JSON Object}
 * @author 이승백
 */
Ext.ux.gantt.ChartGrid = function(config) {
    Ext.apply(this, config);

    this.startDate = config.startDate ? config.startDate : new Date();
    this.finishDate = config.finishDate ? config.finishDate : new Date().add(Date.MONTH, 3);
    this.viewMode = config.viewMode ? config.viewMode : 'D';

    // ViewMode 에 따라 헤더그룹을 생성한다.
    var headerGroup;
    if (this.viewMode == 'D') {
        headerGroup = this.createHeaderGroupByDay(this.startDate, this.finishDate);
    } else if (this.viewMode == 'W') {
        headerGroup = this.createHeaderGroupByWeek(this.startDate, this.finishDate);
    } else if (this.viewMode == 'M') {
        headerGroup = this.createHeaderGroupByMonth(this.startDate, this.finishDate);
    }

    // call constructor
    Ext.ux.gantt.ChartGrid.superclass.constructor.call(this, {
        store: config.store,
        enableColumnMove: false,
        enableColumnResize: false,
        enableHdMenu: false,
        border: false,
        viewConfig: {
            forceFit: false,
            viewTree: false
        },
        sm: new Ext.grid.RowSelectionModel({
            singleSelect: true
        }),
        plugins: new Ext.ux.grid.ColumnHeaderGroup({
            rows: [headerGroup.hdGroupRows]
        }),
        colModel : new Ext.grid.ColumnModel({
            columns :  headerGroup.hdColumns,
            defaults: {
                sortable: false,
                width: Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH
            }
        })
    });
};
Ext.extend(Ext.ux.gantt.ChartGrid, Ext.ux.maximgb.tg.EditorGridPanel, {
    /**
     * 뷰모드(D:Day) 로 Header Group을 생성하여 반환한다.
     *
     * @param startDate {Date}
     * @param finishDate {Date}
     * @return {hdGroupRows, hdColumns, storeFields}
     * @private
     */
    createHeaderGroupByDay: function(startDate, finishDate) {
        var groupRows = [];
        var hdDate, idx, idxDate;
        var columns = [];
        var fields = [];
        var DAY_OF_WEEK = 7;

        var cStartDate = startDate.add(Date.DAY, -1 * startDate.format('w') - DAY_OF_WEEK * 2);
        var cEndDate = finishDate.add(Date.DAY, 6 - finishDate.format('w') + DAY_OF_WEEK * 2);
        var differentWeek = (Ext.ux.gantt.Common.differentDay(cStartDate, cEndDate) + 1) / DAY_OF_WEEK;

        for (var i = 0; i < differentWeek; i++) {
            hdDate = cStartDate.add(Date.DAY, i * DAY_OF_WEEK);
            groupRows.push({
                header: hdDate.format(Gantt.Date.dayFormat),
                colspan: DAY_OF_WEEK,
                align: 'center'
            });

            for (var j = 0; j < DAY_OF_WEEK; j++) {
                idxDate = hdDate.add(Date.DAY, j);
                idx = idxDate.format('Y-m-d');

                columns.push({
                    header: Gantt.Date.dayNames[idxDate.format('w')],
                    dataIndex: idx,
                    css : (new Date()).format('Y-m-d') == idx ? 'background-color: #faebd7;' : (j == 0 || j == 6 ? 'background-color: #eee;' : '')
                });

                fields.push({
                    name: idx
                })
            }
        }

        return {
            hdGroupRows : groupRows,
            hdColumns : columns,
            storeFields : fields
        }
    },

    /**
     * 뷰모드(W:Week) 로 Header Group을 생성하여 반환한다.
     *
     * @param startDate {Date}
     * @param finishDate {Date}
     * @return {hdGroupRows, hdColumns, storeFields}
     * @private
     */
    createHeaderGroupByWeek: function(startDate, finishDate) {
        var groupRows = [];
        var hdDate, idx;
        var columns = [];
        var fields = [];
        var DAY_OF_WEEK = 7;
        var countOfWeek = 0;

        var firstDateOfMonth = startDate.add(Date.MONTH, -2).getFirstDateOfMonth();
        var lastDateOfMonth = finishDate.add(Date.MONTH, 2).getLastDateOfMonth();

        var cStartDate = firstDateOfMonth.add(Date.DAY, -1 * firstDateOfMonth.format('w'));
        if (cStartDate.getMonth() < firstDateOfMonth.getMonth()) {
            cStartDate = cStartDate.add(Date.DAY, DAY_OF_WEEK);
        }
        var cEndDate = lastDateOfMonth.add(Date.DAY, 6 - lastDateOfMonth.format('w'));
        var differentWeek = (Ext.ux.gantt.Common.differentDay(cStartDate, cEndDate) + 1) / DAY_OF_WEEK;

        for (var i = 0; i < differentWeek; i++) {
            if (hdDate != null && hdDate.format('Y-m') != cStartDate.add(Date.DAY, i * DAY_OF_WEEK).format('Y-m')) {
                groupRows.push({
                    header: hdDate.format(Gantt.Date.monthFormat),
                    colspan: countOfWeek,
                    align: 'center'
                });
                countOfWeek = 1;
            } else if (i == differentWeek - 1) {
                groupRows.push({
                    header: hdDate.format(Gantt.Date.monthFormat),
                    colspan: countOfWeek + 1,
                    align: 'center'
                });
                countOfWeek = 1;
            } else {
                countOfWeek++;
            }

            hdDate = cStartDate.add(Date.DAY, i * DAY_OF_WEEK);
            idx = hdDate.format('Y-m-d');
            columns.push({
                header: hdDate.format('d'),
                dataIndex: idx,
                css : (new Date()).add(Date.DAY, -1 * (new Date()).format('w')).format('Y-m-d') == idx ? 'background-color: #faebd7;' : ''
            });

            fields.push({
                name: idx
            })
        }

        return {
            hdGroupRows : groupRows,
            hdColumns : columns,
            storeFields : fields
        }
    },

    /**
     * 뷰모드(M:Month) 로 Header Group을 생성하여 반환한다.
     *
     * @param startDate {Date}
     * @param finishDate {Date}
     * @return {hdGroupRows, hdColumns, storeFields}
     * @private
     */
    createHeaderGroupByMonth: function(startDate, finishDate) {
        var groupRows = [];
        var hdDate, idx, idxDate;
        var columns = [];
        var fields = [];
        var MONTH_OF_YEAR = 12;

        var cStartYear = startDate.add(Date.MONTH, -6).getFullYear();
        var cEndYear = finishDate.add(Date.MONTH, 6).getFullYear();

        for (var i = cStartYear; i <= cEndYear; i++) {
            hdDate = new Date(i, 0, 1);
            groupRows.push({
                header: hdDate.format('Y'),
                colspan: MONTH_OF_YEAR,
                align: 'center'
            });

            for (var j = 0; j < MONTH_OF_YEAR; j++) {
                idxDate = hdDate.add(Date.MONTH, j);
                idx = idxDate.format('Y-m');
                columns.push({
                    header: idxDate.format('m'),
                    dataIndex: idx,
                    css : (new Date()).format('Y-m') == idx ? 'background-color: #faebd7;' : ''
                });

                fields.push({
                    name: idx
                })
            }
        }

        return {
            hdGroupRows : groupRows,
            hdColumns : columns,
            storeFields : fields
        }
    },

    /**
     * 뷰모드를 일,주,월 단위로 변경한다.
     *
     * @param mode 뷰모드(D:일, W:주, M:월)
     * @public
     */
    changeViewMode: function(mode) {
        var headerGroup;

        switch (mode) {
            case 'D':
                headerGroup = this.createHeaderGroupByDay(this.startDate, this.finishDate);
                break;
            case 'W':
                headerGroup = this.createHeaderGroupByWeek(this.startDate, this.finishDate);
                break;
            case 'M':
                headerGroup = this.createHeaderGroupByMonth(this.startDate, this.finishDate);
                break;
        }

        this.viewMode = mode;

        this.getColumnModel().rows = [headerGroup.hdGroupRows];
        this.getColumnModel().setConfig(headerGroup.hdColumns);

        this.drawChart();
    },

    /**
     * 날짜 헤더그룹을 동적으로 추가한다.
     *
     * @param direction {String} 추가 방향(L:Left, R:Right)
     * @param offset {Number} 추가될 헤더컬럼 그룹의 Offset(뷰모드 D인경우 일주일추가, W인경우 한달, M인경우 일년 단위), 디폴트 1
     * @public
     */
    addHeaderBlock: function(direction, offset) {
        if (offset == null) offset = 1;
        switch (this.viewMode) {
            case 'D':
                if (direction == 'L') {
                    this.startDate = this.startDate.add(Date.DAY, -7 * offset);
                } else {
                    this.finishDate = this.finishDate.add(Date.DAY, 7 * offset);
                }
                break;
            case 'W':
                if (direction == 'L') {
                    this.startDate = this.startDate.add(Date.MONTH, -1 * offset);
                } else {
                    this.finishDate = this.finishDate.add(Date.MONTH, 1 * offset);
                }
                break;
            case 'M':
                if (direction == 'L') {
                    this.startDate = this.startDate.add(Date.YEAR, -1 * offset);
                } else {
                    this.finishDate = this.finishDate.add(Date.YEAR, 1 * offset);
                }
                break;
        }

        this.changeViewMode(this.viewMode);

        if (direction == 'L') {
            this.getGridEl().child('.x-grid3-scroller').dom.scrollLeft = 0;
        } else {
            this.getGridEl().child('.x-grid3-scroller').dom.scrollLeft = 99999;
        }
    },

    /**
     * 설정된 Store값에 따로 스케줄바를 드로잉한다.
     *
     * @public
     */
    drawChart: function() {
        this.getStore().each(function(record, index, allRecords) {
            this.drawScheduleBar(record);
        }, this);
    },

    /**
     * Task Record 값으로 스케줄바를 드로잉한다.
     *
     * @param record {Ext.data.Record}
     * @private
     */
    drawScheduleBar: function(record) {
        var store = this.getStore();
        var columnModel = this.getColumnModel();
        var gridView = this.getView();

        // Task 의 시작일 위치 Cell HTML Element 얻는다.
        var cellElement = null;
        switch (this.viewMode) {
            case 'D':
                cellElement = gridView.getCell(store.indexOf(record), columnModel.findColumnIndex(record.get('startDate').format('Y-m-d')));
                break;
            case 'W':
                cellElement = gridView.getCell(store.indexOf(record),
                        columnModel.findColumnIndex(record.get('startDate').add(Date.DAY, -1 * record.get('startDate').format('w')).format('Y-m-d')));
                break;
            case 'M':
                cellElement = gridView.getCell(store.indexOf(record), columnModel.findColumnIndex(record.get('startDate').format('Y-m')));
                break;
        }

        var from = record.get('@from');
        var fromArray = new Array(0);
        if (from != null && from != '') {
            fromArray = from.split(',');
        }

        // 선행 Task 정보를 수집한다.
        var previousTasksInfo = new Array();
        var fromRecord = null;
        for (var i = 0; i < fromArray.length; i++) {
            fromRecord = store.getById(fromArray[i]);
            if (fromRecord != null && store.isVisibleNode(fromRecord)) {
                // 감춰진 Task 에 대한 index difference 보정
                var indexDif = 0;
                if (store.indexOf(fromRecord) < store.indexOf(record)) {
                    for (var j = store.indexOf(fromRecord); j < store.indexOf(record); j++) {
                        if (store.isVisibleNode(store.getAt(j))) {
                            indexDif = indexDif + 1;
                        }
                    }
                } else {
                    for (var j = store.indexOf(fromRecord); j > store.indexOf(record); j--) {
                        if (store.isVisibleNode(store.getAt(j))) {
                            indexDif = indexDif - 1;

                        }
                    }
                }

                previousTasksInfo.push({
                    uid: fromRecord.get('@uid'),
                    indexDif: indexDif,
                    dateDif: Ext.ux.gantt.Common.differentDay(fromRecord.get('endDate'), record.get('startDate'))
                });
            }

        }

        // 스케줄바를 드로잉한다.
        var scheduleBar = new Ext.ux.gantt.ScheduleBar({
            chartId : this.getId(),
            viewMode : this.viewMode,
            targetEl : cellElement,
            taskRecord : record,
            prevTasksInfo : previousTasksInfo
        });

        scheduleBar.draw();

    }
});