$(document).ready(function() {

	var formatter = new Array();
	var formatterId = 0;
	var hiveSubmitColumns = new Array();
	var hiveSubmitTableName = "";

	$("[name='userid-upload-switch']").bootstrapSwitch('state', false);
	$("[name='userid-log-switch']").bootstrapSwitch('state', true);
	$("[name='userid-user-switch']").bootstrapSwitch('state', false);

	$("[name='userid-upload-switch']").on('switchChange.bootstrapSwitch', function(event, state) {
		if (state) {
			$("#mf-btn-upload").show()
		} else {
			$("#mf-btn-upload").hide()
		}
	});

	// hive table
	$("[name='userid-log-switch']").on('switchChange.bootstrapSwitch', function(event, state) {
		if (state) {
			$("#mf_user_log_info").show()
		} else {
			$("#mf_user_log_info").hide()
		}
	});

	// hbase user info
	$("[name='userid-user-switch']").on('switchChange.bootstrapSwitch', function(event, state) {
		if (state) {
			$("#mf_user_user_info").show()
			$("#hbase_user_info").show();
		} else {
			$("#mf_user_user_info").hide()
			$("#hbase_user_info").hide();
		}
	});

	$("#mf_upload_file").click(function() {
		$('#batchImportModal').modal('show');
	});

	$("#select2val").select2({
		placeholder : "选择数据表"
	});

	$("#select2val_formatter").select2({
		placeholder : "选择格式化字段"
	});

	$("#select2val_filter").select2({
		placeholder : "选择过滤条件"
	});

	$("#select2val_formatter_date").select2({
		data : [ "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" ]
	});

	$("#select2val_filter_condition").select2({
		data : [ "等于(=)", "大于(>)", "大于等于(>=)", "小于(<)", "小于等于(<=)", "包含(in)", "匹配(like)" ]
	});

	var filter_hbases = new Array();
	$(ucuser).each(function() {
		filter_hbases.push(this.valueName + "(" + this.value + ")");
	});

	$("#select2val_filter_hbase").select2({
		data : filter_hbases
	});

	$("#select2val_filter_condition_hbase").select2({
		data : [ "等于(=)", "大于(>)", "大于等于(>=)", "小于(<)", "小于等于(<=)", "包含(in)", "匹配(like)" ]
	});

	// Set hive task name
	$("#mf_hive_task_name").val("数据导出 | " + getNowFormatDate());

	function getNowFormatDate() {
		var date = new Date();
		var seperator1 = "-";
		var seperator2 = ":";
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		var hour = date.getHours();
		var min = date.getMinutes();
		var sec = date.getSeconds()
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		if (hour >= 0 && hour <= 9) {
			hour = "0" + hour;
		}
		if (min >= 0 && min <= 9) {
			min = "0" + min;
		}
		if (sec >= 0 && sec <= 9) {
			sec = "0" + sec;
		}
		var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate + " " + hour + seperator2 + min + seperator2 + sec;
		return currentdate;
	}

	var start = moment();
	var end = moment();

	function cb(start, end) {
		$('#reportrange span').html(start.format('YYYY-MM-DD') + ' 至 ' + end.format('YYYY-MM-DD'));
	}

	var reportrange = $('#reportrange').daterangepicker({
		startDate : start,
		endDate : end,
		locale : {
			applyLabel : '确定',
			cancelLabel : '取消',
			fromLabel : '起始时间',
			toLabel : '结束时间',
			customRangeLabel : '自定义',
			daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
			monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月' ],
			firstDay : 1
		},
		ranges : {
			'今天' : [ moment(), moment() ],
			'昨天' : [ moment().subtract(1, 'days'), moment().subtract(1, 'days') ],
			'最近7天' : [ moment().subtract(6, 'days'), moment() ],
			'最近30' : [ moment().subtract(29, 'days'), moment() ],
			'本月' : [ moment().startOf('month'), moment().endOf('month') ],
			'上月' : [ moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month') ]
		}
	}, cb);

	cb(start, end);

	$("#select2val").select2({
		placeholder : "数据表",
		ajax : {
			url : "/mf/export/common/table/select/ajax",
			dataType : 'json',
			delay : 250,
			data : function(params) {
				params.offset = 10;
				params.page = params.page || 1;
				return {
					name : params.term,
					page : params.page,
					offset : params.offset
				};
			},
			cache : true,
			processResults : function(data, params) {
				return {
					results : data.items,
					pagination : {
						more : (params.page * params.offset) < data.total
					}
				};
			},
			escapeMarkup : function(markup) {
				return markup;
			},
			minimumInputLength : 1
		}
	});

	// Hive table change
	$('#select2val').on('select2:select', function(evt) {
		var text = evt.params.data.text;
		$("#select2val").val(text);
		$("#hive_table_info_tag").html("");
		var tableName = text.split("[")[1].split("]")[0];
		hiveSubmitTableName = tableName;
		showTableColumns(tableName);
	});

	function showTableColumns(tableName) {
		$.ajax({
			type : 'get',
			dataType : 'json',
			url : '/mf/export/common/table/columns/' + tableName + '/ajax',
			success : function(datas) {
				if (datas != null && datas.code == "success") {
					$("#hiveColumns").show();
					$("#hiveColumns").html("");
					$("#select2val_filter").html("");
					$("#select2val_formatter").html("");
					hiveSubmitColumns = [];
					formatter = [];
					var data = JSON.parse(datas.data);
					var cbox = "";
					var filter = new Array();
					for (var i = 0; i < data.length; i++) {
						var selectValue = new Object();
						var column = data[i].col_name;
						var comment = data[i].comment;
						if (comment.length == 0) {
							comment = column;
						}
						cbox += "<label class='checkbox-inline'><input type='checkbox' class='hive_tab_column' value='" + comment + "[" + column + "]'>" + comment + "[" + column + "]</label>";
						if ((i + 1) % 5 == 0) {
							var div = "<div class='form-group'>" + cbox + "</div>"
							$("#hiveColumns").append(div);
							cbox = "";
						}
						selectValue.id = i;
						selectValue.text = comment + "[" + column + "]";
						filter.push(selectValue);
					}
					var div = "<div class='form-group'>" + cbox + "</div>"
					$("#hiveColumns").append(div);
					$("#select2val_filter").select2({
						data : filter
					});
				}
			}
		});
	}

	$(document).on("click", ".hive_tab_column", function() {
		$("#select2val_formatter").html("");
		var formatterValue = new Object();
		formatterValue.id = formatterId;
		formatterValue.text = $(this).val();
		if ($(this).is(":checked")) {
			formatter.push(formatterValue);
			hiveSubmitColumns.push(extraHiveColumn($(this).val()));
		} else {
			removeByObject(formatter, $(this).val());
			removeByValue(hiveSubmitColumns, extraHiveColumn($(this).val()));
		}
		formatterId++;

		$("#select2val_formatter").select2({
			data : formatter
		});
	});

	function extraHiveColumn(extraStr) {
		return extraStr.split("[")[1].split("]")[0];
	}

	function removeByObject(arr, val) {
		for (var i = 0; i < arr.length; i++) {
			if (arr[i].text == val) {
				arr.splice(i, 1);
				break;
			}
		}
	}

	function removeByValue(arr, val) {
		for (var i = 0; i < arr.length; i++) {
			if (arr[i] == val) {
				arr.splice(i, 1);
				break;
			}
		}
	}

	// load user info
	loadUcuser();

	$(document).on("click", "#hive_btn_submit_task", function() {
		if (hiveSubmitColumns.length == 0 || hiveSubmitTableName == "") {
			$("#alert_mssage_common").show();
			setTimeout(function() {
				$("#alert_mssage_common").hide()
			}, 3000);
		} else {
			var sql = "select "
			for (var i = 0; i < hiveSubmitColumns.length; i++) {
				sql += "`" + hiveSubmitColumns[i] + "`,"
			}
			var stime = reportrange[0].innerText.replace(/-/g, '').split("至")[0].trim();
			var etime = reportrange[0].innerText.replace(/-/g, '').split("至")[1].trim();
			var hive_filter = new Array();
			$("#hive_table_info_tag").find("span").each(function() {
				hive_filter.push($(this).text());
			})
			sql = sql.substr(0, sql.length - 1) + " from " + hiveSubmitTableName + " where tm between " + stime + " and " + etime;
			for (var i = 0; i < unique(hive_filter).length; i++) {
				sql += " and " + unique(hive_filter)[i];
			}
			console.log(sql);
		}
	});

	$(document).on("click", "#btn_filter_condition", function() {
		var key = $('#select2val_filter').select2('data')[0].text;
		var condition = $('#select2val_filter_condition').select2('data')[0].text;
		var value = $('#hive_table_input_filter').val();
		if (value.length != 0) {
			var html = "<p><span class='label label-primary'>" + (parseen(key) + " " + parseen(condition) + " " + value) + "<span class='glyphicon glyphicon-remove' onclick='labelClose(this)'></span></span></p>"
			$('#hive_table_info_tag').append(html);
		}
	});

	function parseen(key) {
		if (key.indexOf("(") > -1) {
			return key.split("(")[1].split(")")[0];
		} else {
			return "`" + key.split("[")[1].split("]")[0] + "`"
		}
	}

	function unique(target) {
		var result = [];
		loop: for (var i = 0, n = target.length; i < n; i++) {
			for (var x = i + 1; x < n; x++) {
				if (target[x] === target[i])
					continue loop;
			}
			if (target[i].length > 0) {
				result.push(target[i]);
			}
		}
		return result;
	}

});