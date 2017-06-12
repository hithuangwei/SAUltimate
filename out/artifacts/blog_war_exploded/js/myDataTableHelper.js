//document.write('<script src="/sinopharm-tr/assets/js/plugins/dataTables/datatables.min.js" type="text/javascript"></script>')
//document.write('<script src="/sinopharm-tr/assets/common/supplement/datatable-select/dataTables.select.min.js" type="text/javascript"></script>')
//<asset:javascript src="js/plugins/dataTables/datatables.min.js"></asset:javascript>
//<asset:javascript src="common/supplement/datatable-select/dataTables.select.min.js"></asset:javascript>
/*有调用者决定js引用*/
$.MyDataTableHelper = {
    /**
     * 初始化一个一般的Datatable
     * @param cssid
     * @param _options
     * @returns {jQuery}
     */
    initGeneralDatatable: function (cssid, _options) {
        var options = $.extend(true, {}, $.MyDataTableHelper.defaultDatatableOptions, _options);
        console.log("datatable options", options);
        var table = $('#' + cssid).DataTable(options);
        $('#' + cssid + '_filter').addClass('hidden');//隐藏右上角全局搜索框
        return table;
    },
    /**
     * Updates "Select all" control in a data table
     * @param table
     */
    updateDataTableSelectAllCtrl: function (table) {
        var $table = table.table().container();
        var $chkbox_all = $('tbody input[type="checkbox"]', $table);
        var $chkbox_checked = $('tbody input[type="checkbox"]:checked', $table);
        var chkbox_select_all = $('thead input[type="checkbox"]', $table).get(0);

        // If none of the checkboxes are checked
        if ($chkbox_checked.length === 0) {
            chkbox_select_all.checked = false;
            if ('indeterminate' in chkbox_select_all) {
                chkbox_select_all.indeterminate = false;
            }

            // If all of the checkboxes are checked
        } else if ($chkbox_checked.length === $chkbox_all.length) {
            chkbox_select_all.checked = true;
            if ('indeterminate' in chkbox_select_all) {
                chkbox_select_all.indeterminate = false;
            }

            // If some of the checkboxes are checked
        } else {
            chkbox_select_all.checked = true;
            if ('indeterminate' in chkbox_select_all) {
                chkbox_select_all.indeterminate = true;
            }
        }
    },
    /**
     * 初始化一个带checkbox的Datatable ，需要datatable的select扩展
     * @param cssid
     * @param _options
     * @returns {jQuery}
     */
    initCheckboxDatatable: function (cssid, _options) {
        var options = $.extend(true, {}, $.MyDataTableHelper.defaultDatatableOptions,
            {'select': {'style': 'multi'}}, _options);
        console.log('datatableHelper');
        console.log(options);
        var table = $('#' + cssid).DataTable(options);
        $('#' + cssid + '_filter').addClass('hidden');//隐藏右上角全局搜索框


        // Handle row selection event
        $('#' + cssid).on('select.dt deselect.dt', function (e, api, type, items) {
            console.log('select.dt deselect.dt');
            if (e.type === 'select') {
                $('tr.selected input[type="checkbox"]', api.table().container()).prop('checked', true);
            } else {
                $('tr:not(.selected) input[type="checkbox"]', api.table().container()).prop('checked', false);
            }
            // Update state of "Select all" control
            $.MyDataTableHelper.updateDataTableSelectAllCtrl(table);
        });


        // Handle click on "Select all" control
        $('#' + cssid + ' thead').on('click', 'input[type="checkbox"]', function (e) {
            console.log('click checkbox');
            if (this.checked) {
                table.rows({page: 'current'}).select();
            } else {
                table.rows({page: 'current'}).deselect();
            }
            e.stopPropagation();
        });

        // Handle click on heading containing "Select all" control
        $('thead', table.table().container()).on('click', 'th:first-child', function (e) {
            console.log('click select All checkbox');
            $('input[type="checkbox"]', this).trigger('click');
        });

        // Handle table draw event
        $('#' + cssid).on('draw.dt', function () {
            // Update state of "Select all" control
            console.log('draw.dt');
            $.MyDataTableHelper.updateDataTableSelectAllCtrl(table);
        });

        return table;
    },
    defaultDatatableOptions: {
        "info": false,//去掉左下角信息
        "processing": true,//显示正在处理的文本
        "searching": true,//全局搜索功能
        "lengthMenu": [10, 25, 50, 75, 100],//左上角数量
        "autoWidth": true,
        "serverSide": true,
        "search": null,//全局收缩设置
        "ajax": {
            "type": "POST",
            "url": null,
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                /*var res = JSON.parse(XMLHttpRequest.responseText);
                 alert(textStatus+'error:'+res.statusText);*/
                if (XMLHttpRequest.status != 0) {//忽略0 abort 中断连接错误
                    alert(XMLHttpRequest.status + ' error ' + XMLHttpRequest.statusText);
                }
            }
        },
        "columns": null,
        'order': [[0, 'asc']],//设定初始默认排序
        "language": { //部分语言中文化
            "lengthMenu": "每页显示 _MENU_ 条记录",
            "info": "从 _START_ 到 _END_ /共 _TOTAL_ 条数据",
            "infoEmpty": "没有数据",
            "infoFiltered": "(从 _MAX_ 条数据中检索)",
            "zeroRecords": "没有检索到数据",
            "search": "名称:",
            "paginate": {
                "first": "首页",
                "previous": "前一页",
                "next": "后一页",
                "last": "尾页"
            },
            "processing": "正在获取数据"
        }
    }
};