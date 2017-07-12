$(document).ready(function() {

	var formatter = new Array();
	var formatterId = 0;
	var hiveSubmitColumns = new Array();
	var hiveSubmitColumnsAlias = new Array();
	var hiveSubmitTableName = "";

	$("#select2val").select2({
		placeholder : "Select Hive Table"
	});

	$("#select2val_formatter").select2({
		placeholder : "Select Column Formatter"
	});

	$("#select2val_filter").select2({
		placeholder : "Select Filter"
	});

	$("#select2val_formatter_date").select2({
		data : [ "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" ]
	});

	$("#select2val_filter_condition").select2({
		data : [ "=", ">", ">=", "<", "<=", "in", "like", "between and" ]
	});

	// Set hive task name
	$("#hc_hive_task_name").val("Export Date | " + getNowFormatDate());

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
		$('#reportrange span').html(start.format('YYYY-MM-DD') + ' To ' + end.format('YYYY-MM-DD'));
	}

	var reportrange = $('#reportrange').daterangepicker({
		startDate : start,
		endDate : end,
		ranges : {
			'Today' : [ moment(), moment() ],
			'Yesterday' : [ moment().subtract(1, 'days'), moment().subtract(1, 'days') ],
			'Lastest 7 days' : [ moment().subtract(6, 'days'), moment() ],
			'Lastest 30 days' : [ moment().subtract(29, 'days'), moment() ],
			'This Month' : [ moment().startOf('month'), moment().endOf('month') ],
			'Last Month' : [ moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month') ]
		}
	}, cb);

	cb(start, end);

	$("#select2val").select2({
		placeholder : "Hive Table",
		ajax : {
			url : "/hc/export/common/table/select/ajax",
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
			url : '/hc/export/common/table/columns/' + tableName + '/ajax',
			success : function(datas) {
				if (datas != null && datas.code == "success") {
					$("#hiveColumns").show();
					$("#hiveColumns").html("");
					$("#select2val_filter").html("");
					$("#select2val_formatter").html("");
					hiveSubmitColumns = [];
					hiveSubmitColumnsAlias = [];
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
			hiveSubmitColumnsAlias.push(extraHiveColumnAlias($(this).val()));
		} else {
			removeByObject(formatter, $(this).val());
			removeByValue(hiveSubmitColumns, extraHiveColumn($(this).val()));
			removeByValue(hiveSubmitColumnsAlias, extraHiveColumnAlias($(this).val()));
		}
		formatterId++;

		$("#select2val_formatter").select2({
			data : formatter
		});
	});

	function extraHiveColumn(extraStr) {
		return extraStr.split("[")[1].split("]")[0];
	}

	function extraHiveColumnAlias(extraStr) {
		return extraStr.split("[")[0];
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
			var hive_filter = new Array();
			$("#hive_table_info_tag").find("span").each(function() {
				hive_filter.push($(this).text());
			})
			if (hive_filter.length > 0) {
				sql = sql.substr(0, sql.length - 1) + " from " + hiveSubmitTableName + " where ";
				for (var i = 0; i < unique(hive_filter).length; i++) {
					if (i == 0) {
						sql += unique(hive_filter)[i];
					} else {
						sql += " and " + unique(hive_filter)[i];
					}
				}
				console.log(sql);
				var columnAlias = "";
				for (var i = 0; i < hiveSubmitColumnsAlias.length; i++) {
					columnAlias += hiveSubmitColumnsAlias[i] + ","
				}
				var json = {
					"column" : columnAlias.substr(0, columnAlias.length - 1),
					"context" : [ {
						"operate" : sql,
						"type" : "hive",
						"order" : 1
					} ]
				}
				console.log(json)
				var url = '/hc/export/custom/task/ajax?content=' + JSON.stringify(json) + '&name=' + $("#hc_hive_task_name").val();
				$.ajax({
					type : 'get',
					dataType : 'json',
					url : url,
					success : function(datas) {
						if (datas != null) {
							console.log(datas);
							$('#hc_task_alert_dialog').modal('show');
							if (datas.code > 0) {
								$("#hc_task_result_dialog").html("");
								$("#hc_task_result_dialog").append("<span style='color:green'>The task was added successfully</span>. Please visit <a href='/hc/tasks/private'>Tasks</a> view current task details.");
							} else {
								$("#hc_task_result_dialog").html("");
								$("#hc_task_result_dialog").append("<span style='color:red'>The task was added failed. Check to see if the writing task is up to specification.</span>");
							}
						}
					}
				});
			} else {
				$("#alert_mssage_common").show();
				setTimeout(function() {
					$("#alert_mssage_common").hide()
				}, 3000);
			}
		}
	});

	$(document).on("click", "#btn_filter_condition", function() {
		var key = $('#select2val_filter').select2('data')[0].text;
		var condition = $('#select2val_filter_condition').select2('data')[0].text;
		var value = $('#hive_table_input_filter').val();
		if (value.length != 0 || condition.indexOf("between") > -1) {
			if (condition.indexOf("between") > -1) {
				var stime = reportrange[0].innerText.replace(/-/g, '').split("To")[0].trim();
				var etime = reportrange[0].innerText.replace(/-/g, '').split("To")[1].trim();
				var html = "<p><span class='label label-primary'>" + (parseen(key) + " between " + stime + " and " + etime) + "<span class='glyphicon glyphicon-remove' onclick='labelClose(this)'></span></span></p>"
				$('#hive_table_info_tag').append(html);
			} else {
				if (condition.indexOf("in") > -1) {
					var html = "<p><span class='label label-primary'>" + (parseen(key) + " " + condition + " " + value) + "<span class='glyphicon glyphicon-remove' onclick='labelClose(this)'></span></span></p>"
				} else {
					var html = "<p><span class='label label-primary'>" + (parseen(key) + " " + condition + " '" + value + "'") + "<span class='glyphicon glyphicon-remove' onclick='labelClose(this)'></span></span></p>"
				}
				$('#hive_table_info_tag').append(html);
			}
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