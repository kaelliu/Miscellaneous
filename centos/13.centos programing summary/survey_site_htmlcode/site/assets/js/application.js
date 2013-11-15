var currentCatalogy=1;
var currentPage = 1;
var currentMaxId = 0;// record for current page's lagest question id
var collapsePerPage = 6;// for temp,must be configable
var answers = {};
var questions = new Array();

// constans
var typeRadio = 1;
var typeCheckbox = 2;
var typeInput = 3;
var typeDragSort = 5;
var typeDigiSpin = 4;

var cataStudent = 1;
var cataTeacher = 2;
var cataParents = 3;
var assetDir="assets/data/";
var colp = "collapse_body";

!function ($) {
	// function on document ready
	// like ($document).ready(function(){})
	$(function(){
		var $window = $(window)
		var $body   = $(document.body)
		
		$window.on('load', function () {
			// init some thing
			$('.counted').charCounter(320,{container: '#counter'});
			// for test
			setUiDragState('#sortable');
			$( "ul, li" ).disableSelection();
		})
		
		// next question set button click event
		$('#nextBtn').click(function (e) {
			collectThisPageData();
			currentPage++;
			onLoadJson();
			e.preventDefault();
		});

		$('#catalogStu').click(function (e) {
			if(currentCatalogy == cataStudent){
				return;
			}
			$('#catalogStu').removeClass("active").addClass("active");
			$('#catalogTea').removeClass("active");
			$('#catalogPar').removeClass("active");
			currentCatalogy = cataStudent;
			onChangeTab();
		});
		
		$('#catalogTea').click(function (e) {
			if(currentCatalogy == cataTeacher){
				return;
			}
			$('#catalogStu').removeClass("active");
			$('#catalogTea').removeClass("active").addClass("active");
			$('#catalogPar').removeClass("active");
			currentCatalogy = cataTeacher;
			onChangeTab();
		});
		
		$('#catalogPar').click(function (e) {
			if(currentCatalogy == cataParents){
				return;
			}
			$('#catalogStu').removeClass("active");
			$('#catalogTea').removeClass("active");
			$('#catalogPar').removeClass("active").addClass("active");
			currentCatalogy = cataParents;
			onChangeTab();
		});

		onChangeTab();
	});
	
	function onChangeTab(){
		answers = {};
		answers[currentCatalogy]={};
		currentPage = 1;
		onLoadJson();
		$("#nextBtn").attr('disabled',false);
	}
	
	function setUiDragState(id){
		$(id).sortable({
			  revert: true,
			  start:onDragStart,
			  stop:onDragStop
		});
	}

	function onDragStart(event, ui){
		ui.item.removeClass("active").addClass("active");
	}

	function onDragStop(event, ui){
		ui.item.removeClass("active");
	}

	// reload data
	function onLoadJson(){
		var fileName = String(currentCatalogy)+"_"+String(currentPage)+".js";
		var jqxhr = $.getJSON( assetDir + fileName, function(json) {
			$('#accordion').empty();
			var inc = 0;
			$.each(json, function(i,single){
			  if(single.type == typeRadio){
				formateRadioForm(single);
			  }else if(single.type == typeCheckbox){
				formateCheckForm(single);
			  }else if(single.type == typeInput){
				formateInputForm(single);
			  }else if(single.type == typeDigiSpin){
				formateInputDigiForm(single);
			  }else if(single.type == typeDragSort){
				formateDragNDropForm(single);
			  }
			  if(inc == 0){
				  // make first one collapse in
				$('#collapse_body'+String(single.id)).removeClass("collapse").addClass("collapse in");
			  }
			  inc=single.id;
			});
		}).done(function() {
			// alert( "second success" );
		}).fail(function(e) {
			// consider as finished
			// post and save to db
			// and show thanks view
			var panel_data = {
				msg:" 非常感谢您百忙之中抽空参与此次调查! ",
				link:"了解更多请点击"
			};
			$('#accordion').empty();
			var msg = ich.msgBar(panel_data);
			msg.appendTo('#accordion');
			$("#nextBtn").attr('disabled',true);
			// save to database
			// get test
			//$.get("http://www.kuaidi100.com/query" ,{Action:"get",type:"quanyikuaidi",postid:"111400806274"}
			//	,function(result){
			//		console.log(result);
			//	});
			if(objectHaveAttr(answers[currentCatalogy]) == 1){
				var result = JSON.stringify(answers);
				//console.log(result);
				$.post('http://127.0.0.1/index.php',{r:result},function(result){
					console.log(result);
				});
			}
			
		}).always(function() {
			//alert( "complete" );
		});
	}
	
	function objectHaveAttr(obj){
		var have = 0;
		for(x in obj){
			have = 1;
			break;
		}
		return have;
	}
	
	function onViewChangeOver(){
		
	}
	
	function formateRadioForm(json){
		var collapseData = formateCollapse(json);
		var question = {
			"stooges": []
		};
		var answers = json.answer.split(",");
		var firstid = collapseData.b+"_rad_"+String(0);
		for(var key in answers){
			var user_data = { id: collapseData.b+"_rad_"+String(key),checked:'',contents:answers[key]};
            question.stooges.push(user_data);
        }
		var radio = ich.radioType(question);
		$('#accordion').append(collapseData.u);
		radio.appendTo($('#'+collapseData.b));
		// set first one checked
		$('#'+firstid).attr('checked','true');
	}

	function formateCheckForm(json){
		var collapseData = formateCollapse(json);
		var question = {
			"stooges": []
		};
		var answers = json.answer.split(",");
		for(var key in answers){
			var user_data = { id: collapseData.b+"_chk_"+String(key),checked:'checked',contents:answers[key]};
            question.stooges.push(user_data);
        }
		var check = ich.checkType(question);
		$('#accordion').append(collapseData.u);
		check.appendTo($('#'+collapseData.b));
	}
	
	function formateInputForm(json){
		var collapseData = formateCollapse(json);
		var question = {
			charChout:320
		};
		var input = ich.inputMutile(question);
		$('#accordion').append(collapseData.u);
		input.appendTo($('#'+collapseData.b));
		$('.counted').charCounter(320,{container: '#counter'});
	}

	function formateInputDigiForm(json){
		var collapseData = formateCollapse(json);
		var inputid = 'numberInput'+String(json.id);
		var question = {
			iid:inputid
		};
		var input = ich.inputSingleDigi(question);
		$('#accordion').append(collapseData.u);
		input.appendTo($('#'+collapseData.b));
		
		var $container = $('#'+inputid);
		$container.spinner({ min: 1, max: 100 ,step: 1}).focus(function () {
					value = $container.val();
				}).blur(function () {
					var value1 = $container.val();
					if (value1<=0) {
						$container.val(value);
					}
					if (value1>100) {
						$container.val(value);
					}
					if(isNaN(value1))
					{
						$container.val(value);
					}
				});
	}

	function formateDragNDropForm(json){
		var collapseData = formateCollapse(json);
		var sortableid = 'sortable'+String(json.id);
		var question = {
			sid:sortableid,
			"stooges": []
		};
		var answers = json.answer.split(",");
		for(var key in answers){
			var user_data = { dndid: collapseData.b+"_dnd_"+String(key),contents:answers[key]};
            question.stooges.push(user_data);
        }
		var dnd = ich.dragableCell(question);
		$('#accordion').append(collapseData.u);
		dnd.appendTo($('#'+collapseData.b));
		
		setUiDragState('#'+sortableid);
	}

	function formateCollapse(json){
		// div id
		var _passinid = colp+json.id;
		// div sub child body id
		// collapse_body_catalog_id_type
		var _bodyid = colp+"_"+String(currentCatalogy)+"_"+json.id+"_"+json.type;
		var panel_data = {
				passinid:_passinid,
				hrefid:'#'+_passinid,
				QuestionTitle:json.question,
				bodyid:_bodyid
        };
		var user = ich.user(panel_data);
		var data = {
			'u':user,
			'b':_bodyid,
		};
		return data;
	}

	function collectThisPageData(){
		// all body child
		var childs = $('.panel-body');
		for (var i = 0; i < childs.length; i++) {
			// take element
			var scheme = childs.eq(i);
			var id = scheme[0].id;
			var splited = id.split("_");
			var questionid = parseInt(splited[3]);
			var type = parseInt(splited[4]);
			var answerKey = String(currentCatalogy)+'_'+String(questionid);
			if(type == typeRadio){
				var sub = scheme.find("input[type=radio]");
				// find out what be checked
				for(var j = 0;j<sub.length;++j){
					var indx = '#'+colp+'_'+answerKey+'_'+type +'_rad_'+String(j);
					// id selector with pop query
					if($(indx).prop('checked')){
						answers[currentCatalogy][questionid] = j+1;
						break;
					}
				}
			}else if(type == typeCheckbox){
				// class selector
				var sub = scheme.find("input[type=checkbox]");
				answers[currentCatalogy][questionid] = "";
				for(var j = 0;j<sub.length;++j){
					var indx = '#'+colp+'_'+answerKey+'_'+type +'_chk_'+String(j);
					if($(indx).prop('checked')){
						answers[currentCatalogy][questionid] 
						= ((answers[currentCatalogy][questionid] == undefined)?'':(answers[currentCatalogy][questionid]+',')) + String(j+1);
					}
				}
			}else if(type == typeInput){
				// element selector
				var sub = scheme.find("textarea");
				answers[currentCatalogy][questionid] = sub.val();
			}else if(type == typeDigiSpin){
				var $container = $('#numberInput'+questionid);
				answers[currentCatalogy][questionid] = $container.val();
			}else if(type == typeDragSort){
				// record the sequence
				var sub = scheme.find("a");
				// clear and reset
				answers[currentCatalogy][questionid] = undefined;
				for(var j = 0;j<sub.length;++j){
					var info = sub[j].id.split('_');
					var seq = parseInt(info[6]);
					answers[currentCatalogy][questionid] 
						= ((answers[currentCatalogy][questionid] == undefined)?'':(answers[currentCatalogy][questionid]+',')) 
						+ String(seq);
				}
			}
		}
	}
}(window.jQuery)