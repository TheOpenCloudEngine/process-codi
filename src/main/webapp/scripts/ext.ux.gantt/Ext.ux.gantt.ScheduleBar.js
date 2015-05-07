Ext.namespace('Ext.ux.gantt');

/**
 * 간트차트 : 일정바(진행바, 연결선) 클래스
 *
 * <pre>
 * config =
 *  {
 *      viewMode : '',      // {String} 뷰모드(D:일, W:주, M:월)
 *      targetEl : '',      // {HTMLElement} 일정바를 드로잉할 위치의 HTML 엘리먼트
 *      taskRecord : '',    // {Ext.data.Record} Task Data Record
 *      prevTasksInfo :     // {Array} 선행 Task 정보 Array
 *      [{
 *          uid : '',       // {String} 선행 Task ID
 *          indexDif : '',  // {Number} 선행 Task 와의 Row 차이
 *          dateDif : ''    // {Number} 시작일과 선행 Task 의 종료일의 날짜 차이
 *      }]
 *  }
 * </pre>
 *
 * @param config {JSON Object}
 * @author 이승백
 */
Ext.ux.gantt.ScheduleBar = function(config) {
    /**
     * Task Template
     */
    this.template_Task = new Ext.XTemplate(
        '<div id="{chartId}_ganttchart_{uid}" style="position:relative; left: {barOffset}px;">',
        '<div id="{chartId}_schedulebar_{uid}" class="ux-schedulebar" style="width:{duration}px;z-index:1">',
        '<div id="{chartId}_progress_{uid}" class="progressbar" style="width:{completeRate}%; "></div>',
        '</div>',
        '<tpl for="lineOffset">',
        '<div id="{parent.chartId}_line_{uid_fromto}" style="position:absolute; top:{top}px; left: {left}px; width: {width}px; height:{height}px;z-index:0;"></div>',
        '</tpl>',
        '</div>'
    );

    /**
     * Summary of Task Template
     */
    this.template_Summary = new Ext.XTemplate(
        '<div id="{chartId}_ganttchart_{uid}" style="position:relative; left: {barOffset}px;">',
        '<div id="{chartId}_schedulebar_{uid}" class="ux-summary" style="width:{duration}px;z-index:1">',
        '<div id="{chartId}_progress_{uid}" class="progressbar" style="width:{completeRate}%; "></div>',
        '<div class="leftarrow"></div>',
        '<div class="rightarrow"></div>',
        '</div>',
        '<tpl for="lineOffset">',
        '<div id="{parent.chartId}_line_{uid_fromto}" style="position:absolute; top:{top}px; left: {left}px; width: {width}px; height:{height}px;z-index:0;"></div>',
        '</tpl>',
        '</div>'
    );

    /**
     * Milestone Template
     */
    this.template_Milestone = new Ext.XTemplate(
        '<div id="{chartId}_ganttchart_{uid}" style="position:relative;">',
        '<div id="{chartId}_milestone_{uid}" class="ux-milestone" style="z-index:1">',
        '</div>',
        '<tpl for="lineOffset">',
        '<div id="{parent.chartId}_line_{uid_fromto}" style="position:absolute; top:{top}px; left: {left}px; width: {width}px; height:{height}px;z-index:0"></div>',
        '</tpl>',
        '</div>'
    );

    /**
     * {String} chartId
     */
    this.chartId = config.chartId;

    /**
     * {String} 뷰모드(D:일, W:주, M:월)
     */
    this.viewMode = config.viewMode;

    /**
     * {HTMLElement} 일정바를 드로잉할 위치의 HTML 엘리먼트
     */
    this.targetElement = config.targetEl;

    /**
     * {Ext.data.Record} Task Data Record
     */
    this.taskRecord = config.taskRecord;

    /**
     * 선행 Task 정보 Array
     *
     * <pre>
     * JSON Object
     * {
     *      uid : '',       // 선행 Task ID
     *      indexDif : '',  // 선행 Task 와의 Row 차이
     *      dateDif : ''    // 시작일과 선행 Task 의 종료일의 날짜 차이
     * } 의 Array
     * </pre>
     */
    this.prevTasksInfo = config.prevTasksInfo;
};
Ext.ux.gantt.ScheduleBar.prototype = {
    /**
     * 스케줄바를 드로잉한다.
     *
     * @public
     */
    draw: function() {
        switch (this.viewMode) {
            case 'D':
                this.drawByDay();
                break;
            case 'W':
                this.drawByWeek();
                break;
            case 'M':
                this.drawByMonth();
                break;
        }
    },

    /**
     * 스케줄바를 드로잉한다.(뷰모드:일)
     *
     * @private
     */
    drawByDay: function() {
        var BASE_WIDTH = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH;
        var BASE_HEIGHT = Ext.ux.gantt.Properties.BASE_ROW_HEIGHT;

        // 연결선 드로잉 위한 영역 Offset 계산
        var offsetArray = new Array();
        var top, left, width;

        for (var i = 0; i < this.prevTasksInfo.length; i++) {
            if (this.prevTasksInfo[i].dateDif > 0) {
                left = -1 * (BASE_WIDTH * this.prevTasksInfo[i].dateDif + 1);
                width = BASE_WIDTH * (Math.abs(this.prevTasksInfo[i].dateDif) + 1) - 1;
            } else {
                left = -1 * BASE_WIDTH;
                width = BASE_WIDTH * (Math.abs(this.prevTasksInfo[i].dateDif) + 3) - 1;
            }

            if (this.prevTasksInfo[i].indexDif > 0) {
                top = -1 * ((BASE_HEIGHT + 2) * this.prevTasksInfo[i].indexDif);
            } else {
                top = 0;
            }

            offsetArray.push({
                uid_fromto: this.prevTasksInfo[i].uid + '~' + this.taskRecord.get('@uid'),
                top: top,
                left: left,
                width: width,
                height: (BASE_HEIGHT + 2) * (Math.abs(this.prevTasksInfo[i].indexDif) + 1) + i
            });
        }

        // 템플릿에 사용될 값 설정
        var task = {
            chartId: this.chartId,
            uid: this.taskRecord.get('@uid'),
            barOffset: 0,
            duration: parseInt(this.taskRecord.get('duration')) * BASE_WIDTH - 1,
            completeRate: this.taskRecord.get('completeRate'),
            lineOffset: offsetArray
        }

        // 오늘 날짜 까지의 X축 차이(이동 및 리사이즈 제한 위해)
        var xConstraint_s = Ext.ux.gantt.Common.differentDay(new Date(), this.taskRecord.get('startDate').add(Date.HOUR, 23)) *
            Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH;
        var xConstraint_e = Ext.ux.gantt.Common.differentDay(new Date(), this.taskRecord.get('endDate').add(Date.HOUR, 23)) *
            Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH;

        // 타입에 따라 템플릿 이용하여 스케줄바 드로잉
        if (this.taskRecord.get('type') == 'M') {
            this.template_Milestone.overwrite(this.targetElement, task);

            // 지난 일정의 경우 수정 불가
            if (this.taskRecord.get('endDate').format('Ymd') >= (new Date()).format('Ymd')) {
                // 마일스톤 이동가능 가능처리 및 이벤트 핸들
                var taskResizer = new Ext.Resizable(this.chartId + '_milestone_' + task.uid, {
                    handles: 'none',
                    draggable: true,
                    dynamic: true,
                    constrainTo: this.targetElement.parentNode
                });

                // 마우스 move 포인터로
                Ext.get(this.chartId + '_milestone_' + task.uid).addClass('move_cursor');

                // 마일스톤 이동 이벤트 처리
                var tRecord = this.taskRecord;
                taskResizer.dd.setXConstraint(xConstraint_s); // 왼쪽 이동 제한(오늘 날짜까지)
                taskResizer.dd.setYConstraint(0, 0);
                taskResizer.dd.startDrag = function(x, y) {
                    taskResizer.dragStartX = x;
                };
                taskResizer.dd.endDrag = function(e) {
                    var movingDay = Math.round((e.xy[0] - taskResizer.dragStartX ) / Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH);
                    tRecord.set('startDate', tRecord.get('startDate').add(Date.DAY, movingDay));
                    tRecord.set('endDate', tRecord.get('endDate').add(Date.DAY, movingDay));

                     this.adjustParentTask(tRecord);
                }.createDelegate(this);
            }
        } else {
            if (this.taskRecord.get('@isLeaf')) {
                this.template_Task.overwrite(this.targetElement, task);
            } else {
                this.template_Summary.overwrite(this.targetElement, task);
            }

            // 지난 일정의 경우 수정 불가
            if (this.taskRecord.get('endDate').format('Ymd') >= (new Date()).format('Ymd')) {
                // 스케쥴바 리사이징 가능처리 및 이벤트 핸들
                var taskResizer = new Ext.Resizable(this.chartId + '_schedulebar_' + task.uid, {
                    handles: this.taskRecord.get('@isLeaf') ? (this.taskRecord.get('startDate').format('Ymd') >= (new Date()).format('Ymd') ? 'e w' : 'e') : 'none',
                    draggable: this.taskRecord.get('startDate').format('Ymd') >= (new Date()).format('Ymd') ? true : false,
                    dynamic: true,
                    constrainTo: this.targetElement.parentNode,
                    minWidth: Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH,
                    listeners: {
                        beforeresize: function(thisObj, event) {
                            thisObj.activatedPosition = thisObj.activeHandle.position;
                            // 왼쪽 리사이즈 제한(오늘 날짜까지)
                            if (thisObj.activatedPosition == 'west') {
                                thisObj.minX = event.getPageX() - xConstraint_s;
                            } else if (thisObj.activatedPosition == 'east') {
                                thisObj.minX = event.getPageX() - xConstraint_e;
                            }
                        }
                    }
                });

                // 스케줄바 리사이징 이벤트 처리
                taskResizer.on('resize', function(thisObj, width, height, event) {
                    var duration = Math.round(width / Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH);
                    if (thisObj.activatedPosition == 'east') {
                        this.taskRecord.set('endDate', this.taskRecord.get('endDate').add(Date.DAY, duration - this.taskRecord.get('duration')));

                        this.adjustParentTask(this.taskRecord);
                    } else if (thisObj.activatedPosition == 'west') {
                        this.taskRecord.set('startDate', this.taskRecord.get('startDate').add(Date.DAY, this.taskRecord.get('duration') - duration));

                        this.adjustParentTask(this.taskRecord);
                    }
                }, this);

                if (this.taskRecord.get('startDate').format('Ymd') >= (new Date()).format('Ymd')) {
                    // 마우스 move 포인터로
                    Ext.get(this.chartId + '_schedulebar_' + task.uid).addClass('move_cursor');

                    // 스케줄바 이동 이벤트 처리
                    var tRecord = this.taskRecord;

                    taskResizer.dd.setXConstraint(xConstraint_s); // 왼쪽 이동 제한(오늘 날짜까지)
                    taskResizer.dd.setYConstraint(0, 0);
                    taskResizer.dd.startDrag = function(x, y) {
                        taskResizer.dragStartX = x;
                    };
                    taskResizer.dd.endDrag = function(e) {
                        // 왼쪽 이동 제한(오늘 날짜까지)
                        var movingDay = Math.round((e.xy[0] - taskResizer.dragStartX ) / Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH);

                        if (movingDay > 0) {
                            tRecord.set('endDate', tRecord.get('endDate').add(Date.DAY, movingDay));
                            tRecord.set('startDate', tRecord.get('startDate').add(Date.DAY, movingDay));

                            this.moveChildTask(tRecord, movingDay);
                            this.adjustParentTask(tRecord);
                        } else {
                            if (tRecord.get('startDate').add(Date.DAY, movingDay).format('Ymd') >= (new Date()).format('Ymd')) {
                                tRecord.set('startDate', tRecord.get('startDate').add(Date.DAY, movingDay));
                                tRecord.set('endDate', tRecord.get('endDate').add(Date.DAY, movingDay));

                                this.moveChildTask(tRecord, movingDay);
                                this.adjustParentTask(tRecord);
                            } else {
                                // redraw
                                this.drawByDay();
                            }
                        }
                    }.createDelegate(this);
                }
            }
        }

        // 시작, 끝 좌표 계산하여 연결선 드로잉
        var x1,y2,x2,y2,line_uid;
        for (var i = 0; i < this.prevTasksInfo.length; i++) {
            y1 = BASE_HEIGHT / 2;
            y2 = BASE_HEIGHT * Math.abs(this.prevTasksInfo[i].indexDif) + BASE_HEIGHT / 2 +
                Math.abs(this.prevTasksInfo[i].indexDif) * 2;
            if (this.prevTasksInfo[i].dateDif > 0) {
                x1 = BASE_WIDTH;
                x2 = BASE_WIDTH * this.prevTasksInfo[i].dateDif;
            } else {
                x1 = BASE_WIDTH * (Math.abs(this.prevTasksInfo[i].dateDif) + 2) - 1;
                x2 = BASE_WIDTH;
            }

            line_uid = this.chartId + '_line_' + this.prevTasksInfo[i].uid + '~' + this.taskRecord.get('@uid');
            if (this.prevTasksInfo[i].indexDif > 0) {
                this.drawWire(document.getElementById(line_uid), x1, y1, x2, y2);
            } else {
                this.drawWire(document.getElementById(line_uid), x1, y2, x2, y1);
            }
        }
    },

    /**
     * 스케줄바를 드로잉한다.(뷰모드:주)
     *
     * @private
     */
    drawByWeek: function() {
        var BASE_WIDTH = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH / 7;
        var BASE_HEIGHT = Ext.ux.gantt.Properties.BASE_ROW_HEIGHT;

        // 연결선 드로잉 위한 영역 Offset 계산
        var offsetArray = new Array();
        var top, left, width;

        for (var i = 0; i < this.prevTasksInfo.length; i++) {
            if (this.prevTasksInfo[i].dateDif > 0) {
                left = -1 * (BASE_WIDTH * this.prevTasksInfo[i].dateDif) -
                    Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH + 1;
                width = BASE_WIDTH * (Math.abs(this.prevTasksInfo[i].dateDif) - 1) +
                    Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH * 2 + 1;
            } else {
                left = -1 * Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH;
                width = BASE_WIDTH * Math.abs(this.prevTasksInfo[i].dateDif) +
                    Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH * 2;
            }

            if (this.prevTasksInfo[i].indexDif > 0) {
                top = -1 * ((BASE_HEIGHT + 2) * this.prevTasksInfo[i].indexDif);
            } else {
                top = 0;
            }

            offsetArray.push({
                uid_fromto: this.prevTasksInfo[i].uid + '~' + this.taskRecord.get('@uid'),
                top: top,
                left: left,
                width: width,
                height: (BASE_HEIGHT + 2) * (Math.abs(this.prevTasksInfo[i].indexDif) + 1) + i
            });
        }

        // 시작날짜 차이에 따른 스케줄바 Offset 계산
        var barOffset = Ext.ux.gantt.Common.differentDay(
            this.taskRecord.get('startDate').add(Date.DAY, -1 * this.taskRecord.get('startDate').format('w')),
            this.taskRecord.get('startDate')) * BASE_WIDTH;

        // 템플릿에 사용될 값 설정
        var task = {
            chartId: this.chartId,
            uid: this.taskRecord.get('@uid'),
            barOffset: barOffset,
            duration: parseInt(this.taskRecord.get('duration')) * BASE_WIDTH - 1,
            completeRate: this.taskRecord.get('completeRate'),
            lineOffset: offsetArray
        }

        // 타입에 따라 템플릿 이용하여 스케줄바 드로잉
        if (this.taskRecord.get('type') == 'M') {
            this.template_Milestone.overwrite(this.targetElement, task);
        } else {
            if (this.taskRecord.get('@isLeaf')) {
                this.template_Task.overwrite(this.targetElement, task);
            } else {
                this.template_Summary.overwrite(this.targetElement, task);
            }
        }

        // 시작, 끝 좌표 계산하여 연결선 드로잉
        var x1,y2,x2,y2,line_uid;
        for (var i = 0; i < this.prevTasksInfo.length; i++) {
            y1 = BASE_HEIGHT / 2;
            y2 = BASE_HEIGHT * Math.abs(this.prevTasksInfo[i].indexDif) + BASE_HEIGHT / 2 +
                Math.abs(this.prevTasksInfo[i].indexDif) * 2;
            if (this.prevTasksInfo[i].dateDif > 0) {
                x1 = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH;
                x2 = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH - 2 + BASE_WIDTH * this.prevTasksInfo[i].dateDif;
            } else {
                x1 = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH + BASE_WIDTH * Math.abs(this.prevTasksInfo[i].dateDif);
                x2 = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH - 1;
            }

            line_uid = this.chartId + '_line_' + this.prevTasksInfo[i].uid + '~' + this.taskRecord.get('@uid');
            if (this.prevTasksInfo[i].indexDif > 0) {
                this.drawWire(document.getElementById(line_uid), x1, y1, x2, y2);
            } else {
                this.drawWire(document.getElementById(line_uid), x1, y2, x2, y1);
            }
        }
    },

    /**
     * 스케줄바를 드로잉한다.(뷰모드:월)
     *
     * @private
     */
    drawByMonth: function() {
        var BASE_WIDTH = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH / 30;
        var BASE_HEIGHT = Ext.ux.gantt.Properties.BASE_ROW_HEIGHT;

        // 연결선 드로잉 위한 영역 Offset 계산
        var offsetArray = new Array();
        var top, left, width;

        for (var i = 0; i < this.prevTasksInfo.length; i++) {
            if (this.prevTasksInfo[i].dateDif > 0) {
                left = -1 * (BASE_WIDTH * this.prevTasksInfo[i].dateDif) -
                    Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH + 1;
                width = BASE_WIDTH * (Math.abs(this.prevTasksInfo[i].dateDif) - 1) +
                    Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH * 2 + 1;
            } else {
                left = -1 * Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH;
                width = BASE_WIDTH * Math.abs(this.prevTasksInfo[i].dateDif) +
                    Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH * 2;
            }

            if (this.prevTasksInfo[i].indexDif > 0) {
                top = -1 * ((BASE_HEIGHT + 2) * this.prevTasksInfo[i].indexDif);
            } else {
                top = 0;
            }

            offsetArray.push({
                uid_fromto: this.prevTasksInfo[i].uid + '~' + this.taskRecord.get('@uid'),
                top: top,
                left: left,
                width: width,
                height: (BASE_HEIGHT + 2) * (Math.abs(this.prevTasksInfo[i].indexDif) + 1) + i
            });
        }

        // 시작날짜 차이에 따른 스케줄바 Offset 계산
        var barOffset = Ext.ux.gantt.Common.differentDay(
            this.taskRecord.get('startDate').getFirstDateOfMonth(),
            this.taskRecord.get('startDate')) * BASE_WIDTH;

        // 템플릿에 사용될 값 설정
        var task = {
            chartId: this.chartId,
            uid: this.taskRecord.get('@uid'),
            barOffset: barOffset,
            duration: parseInt(this.taskRecord.get('duration')) * BASE_WIDTH - 1,
            completeRate: this.taskRecord.get('completeRate'),
            lineOffset: offsetArray
        }

        // 타입에 따라 템플릿 이용하여 스케줄바 드로잉
        if (this.taskRecord.get('type') == 'M') {
            this.template_Milestone.overwrite(this.targetElement, task);
        } else {
            if (this.taskRecord.get('@isLeaf')) {
                this.template_Task.overwrite(this.targetElement, task);
            } else {
                this.template_Summary.overwrite(this.targetElement, task);
            }
        }

        // 시작, 끝 좌표 계산하여 연결선 드로잉
        var x1,y2,x2,y2,line_uid;
        for (var i = 0; i < this.prevTasksInfo.length; i++) {
            y1 = BASE_HEIGHT / 2;
            y2 = BASE_HEIGHT * Math.abs(this.prevTasksInfo[i].indexDif) + BASE_HEIGHT / 2 +
                Math.abs(this.prevTasksInfo[i].indexDif) * 2;
            if (this.prevTasksInfo[i].dateDif > 0) {
                x1 = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH;
                x2 = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH - 2 + BASE_WIDTH * this.prevTasksInfo[i].dateDif;
            } else {
                x1 = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH + BASE_WIDTH * Math.abs(this.prevTasksInfo[i].dateDif);
                x2 = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH - 1;
            }

            line_uid = this.chartId + '_line_' + this.prevTasksInfo[i].uid + '~' + this.taskRecord.get('@uid');
            if (this.prevTasksInfo[i].indexDif > 0) {
                this.drawWire(document.getElementById(line_uid), x1, y1, x2, y2);
            } else {
                this.drawWire(document.getElementById(line_uid), x1, y2, x2, y1);
            }
        }
    },

    /**
     * (x1,y1)좌표에서 (x2,y2)좌표로의 꺽인 연결선을 드로잉한다.
     *
     * @param targetEl {HTMLElement} 드로잉할 타켓 엘리먼트
     * @param x1 {Number} 시작 X 좌표
     * @param y1 {Number} 시작 Y 좌표
     * @param x2 {Number} 끝 X 좌표
     * @param y2 {Number} 끝 Y 좌표
     * @private
     */
    drawWire: function(targetEl, x1, y1, x2, y2) {
        var BASE_WIDTH = Ext.ux.gantt.Properties.BASE_COLUMN_WIDTH;
        var BASE_HEIGHT = Ext.ux.gantt.Properties.BASE_ROW_HEIGHT;

        var paper = Raphael(targetEl);
        var path;
        if (x2 - x1 >= BASE_WIDTH) {
            path = paper.path("M" + x1 + " " + y1 + "H" + (x1 + BASE_WIDTH / 2) + "V" + y2 + "H" + x2 + "L" + (x2 - 6) + " " +
                (y2 - 6) + "M" + x2 + " " + y2 + "L" + (x2 - 6) + " " + (y2 + 6));
        } else {
            if (y2 > y1) {
                path = paper.path("M" + x1 + " " + y1 + "H" + (x1 + BASE_WIDTH / 2) + "V" + (y1 + BASE_HEIGHT / 2 + 1) + "H" + (x2 - BASE_WIDTH / 2 - 1) + "V" + y2 +
                    "H" + x2 + "L" + (x2 - 6) + " " + (y2 - 6) + "M" + x2 + " " + y2 + "L" + (x2 - 6) + " " + (y2 + 6));
            } else {
                path = paper.path("M" + x1 + " " + y1 + "H" + (x1 + BASE_WIDTH / 2) + "V" + (y1 + (y2 - y1) / 2) + "H" + (x2 - BASE_WIDTH / 2 - 1) + "V" + y2 +
                    "H" + x2 + "L" + (x2 - 6) + " " + (y2 - 6) + "M" + x2 + " " + y2 + "L" + (x2 - 6) + " " + (y2 + 6));
            }
        }
        path.attr({stroke: 'blue',"stroke-width": "1px"});

        path.mouseover(function (event) {
            this.attr({stroke: "red", "stroke-width": "2px"});
        });

        path.mouseout(function (event) {
            this.attr({stroke: 'blue',"stroke-width": "1px"});
        });

    },

    /**
     * 하위 태스크를 이동한다.
     *
     * @param record 부모레코드
     * @param movingDay 이동한 날짜 차이
     */
    moveChildTask: function(record, movingDay) {
        var store = Ext.getCmp(this.chartId).getStore();
        if (store.hasChildNodes(record)) {
            var childRecords = store.getNodeChildren(record);

            for (var i = 0; i < childRecords.length; i++) {
                if (movingDay > 0) {
                    childRecords[i].set('endDate', childRecords[i].get('endDate').add(Date.DAY, movingDay));
                    childRecords[i].set('startDate', childRecords[i].get('startDate').add(Date.DAY, movingDay));

                    if (store.hasChildNodes(childRecords[i])) {
                        this.moveChildTask(childRecords[i], movingDay);
                    }
                } else {
                    if (childRecords[i].get('startDate').add(Date.DAY, movingDay).format('Ymd') >= (new Date()).format('Ymd')) {
                        childRecords[i].set('startDate', childRecords[i].get('startDate').add(Date.DAY, movingDay));
                        childRecords[i].set('endDate', childRecords[i].get('endDate').add(Date.DAY, movingDay));

                        if (store.hasChildNodes(childRecords[i])) {
                            this.moveChildTask(childRecords[i], movingDay);
                        }
                    }
                }
            }
        }
    },

    /**
     * 부모 태스크의 일정의 시작일 또는 종료일을 하위 태스크에 맞게 조정한다.
     *
     * @param record
     */
    adjustParentTask: function(record) {
        var store = Ext.getCmp(this.chartId).getStore();
        var parentTask = store.getNodeParent(record);
        if (parentTask) {
            var childRecords = store.getNodeChildren(parentTask);
            var startDate = record.get('startDate');
            var endDate = record.get('endDate');
            for (var i = 0; i < childRecords.length; i++) {
                if(childRecords[i].get('startDate').format('Ymd') < startDate.format('Ymd')) {
                    startDate = childRecords[i].get('startDate');
                }

                if(childRecords[i].get('endDate').format('Ymd') > endDate.format('Ymd')) {
                    endDate = childRecords[i].get('endDate');
                }
            }

            parentTask.set('endDate', endDate);
            parentTask.set('startDate', startDate);

            this.adjustParentTask(parentTask);
        }
    }
}