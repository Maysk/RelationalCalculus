var app = angular.module('myApp',[])
.controller('TrcToSql', ['$scope','$http', function($scope, $http){
	window.$scope = $scope;
	
	$scope.init = function() {
		var httpResponse = $http.post('db/availables', {});
		
		httpResponse.success(function(data, status, headers, config) {
			$scope.databasesAvailable = data.responseBody;
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
			$scope.databasesAvailable = null;
		});
				
	}
	
	$scope.selectedSgbd = "SQLite";
	$scope.hostname = "aaa";
	$scope.port = "";
	$scope.username = ""
	$scope.password = "";
	$scope.testeHostname = "";
	
	$scope.teste = function(){
		console.log($scope.testeHostname)
	}
	
	$scope.cleanScope = function(){
		$scope.projection = "";
		$scope.trcformula = "";
		$scope.outputformula = "";
		$scope.sqlnfformula = "";
		$scope.sqlexpression = "";
		$scope.previousFocusedTextAreaId = "trcformula";
		$("#submitFormula").removeAttr("disabled");
		$("#cleanScope").removeAttr("disabled");
		$("#projection").removeAttr("readonly");
		$("#trcformula").removeAttr("readonly");
		$("#errorsDiv").hide();
		$("#listOfErrors").empty();
	}
	//modalSelectDatabase
	$scope.openModalSelectDatabase = function(){
		var modalSelectSGDB = $('#modalSelectDatabase');
		modalSelectSGDB.modal();
	}
	
	$scope.loadDbSchema = function(){
		var httpResponse = $http.post('db/listTables/'+ $scope.dbAvalilable, {});
		$scope.cleanScope();
		httpResponse.success(function(data, status, headers, config) {
			console.log(data);
			$scope.dbSchema = data.responseBody;
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
			$scope.dbSchema = null;
		});
	}
	
		
	$scope.testDatabaseConnection = function(){
		var httpResponse = $http.post('db/'+"teste", {});
		httpResponse.success(function(data, status, headers, config) {
			console.log(data);
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});
	}
	
	
	$scope.submitFormula = function(){	
		$("#errorsDiv").hide();
		$("#listOfErrors").empty();
		$("#executeSQLQuery").attr("disabled", "disabled");
		$scope.sqlnfformula = "";
		$scope.sqlexpression = "";
		
		var temp = $scope.trcformula.replace(/∀/g,"FORALL")
									.replace(/∃/g, "EXISTS")
									.replace(/∧/g, "AND")
									.replace(/∨/g, "OR")
									.replace(/→/g, "->")
									.replace(/¬/g, "NOT")
									.replace(/≠/g, "!=")
									.replace(/≥/g, ">=")
									.replace(/≤/g, "<=");
										
		
		$scope.outputformula = "{" + $scope.projection + " | " + temp + "}";
		
		
		var dataObj = {
				requestBody : $scope.outputformula
		}
		
		var httpResponse = $http.post('trc/converttosqlnf/'+$scope.dbAvalilable, dataObj);
		httpResponse.success(function(data, status, headers, config) {
			
			
		if(data.status == "ERROR"){
			console.log("ERROR");
			$("#errorsDiv").show();	
			$("#listOfErrors").append("<br/>");
			$("#listOfErrors").append(data.responseBody);
			console.log(data);
		}
		else{
			$scope.sqlnfformula = data.responseBody.SQLNFFormula;
			if(data.responseBody.SQLQuery == ""){
				$("#listOfErrors").append("<br/>");
				$("#errorsDiv").show();
				if(data.responseBody.FormulaError.length != 0) {
					$("#listOfErrors").append("<b>FormulaErros: </b><br/>");
					for(var i=0; i< data.responseBody.FormulaError.length; i++){
						$("#listOfErrors").append(data.responseBody.FormulaError[i] + "<br/>");
						console.log(data.responseBody.FormulaError[i]);
					}
					$("#listOfErrors").append("<br/>");
				}
				if(data.responseBody.ScopeError.length != 0) {
					
					$("#listOfErrors").append("<b>ScopeErrors: </b><br/>");
					for(var i=0; i< data.responseBody.ScopeError.length; i++){
						$("#listOfErrors").append( data.responseBody.ScopeError[i] + "<br/>");
					}
					$("#listOfErrors").append("<br/>");
				}
				
			}
			else{
				$scope.sqlexpression = data.responseBody.SQLQuery;
				$("#executeSQLQuery").removeAttr("disabled");
				console.log(data.responseBody.SQLQuery);
			}
			console.log(data);
			}
		
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});
	}
	
	
	$scope.lastTextAreaFocused = function(id){
		$scope.previousFocusedTextAreaId = id; 
	}
	
	$scope.addTextToFormulaBoxTrc = function(newText){
		if($scope.previousFocusedTextAreaId != null){
			$scope.previousFocusedTextAreaId = "trcformula";
			$scope.addTextToFormulaBox(newText);
		}
	}
	
	$scope.addTextToFormulaBox = function(newText){
		var el = $("#" + $scope.previousFocusedTextAreaId);
		var start = el.prop("selectionStart")
		var end = el.prop("selectionEnd")
		var text = el.val()
		var before = text.substring(0, start)
		var after  = text.substring(end, text.length)
		el.val(before + newText + after)
		el[0].selectionStart = el[0].selectionEnd = start + newText.length
		el.focus()
		
		if($scope.previousFocusedTextAreaId == "projection"){
			$scope.projection = el.val();
		}
		
		if($scope.previousFocusedTextAreaId == "trcformula"){
			$scope.trcformula = el.val();
		}
		
	}
	
	
	$scope.executeSQLQuery = function(){
		var editModal = $('#sqlModalResults');
		$scope.modalTitle = "Results"; 
		var dataObj = {
				requestBody : $scope.sqlexpression
		}
		
		var httpResponse = $http.post('executeSQLQuery/' + $scope.dbAvalilable , dataObj);
		httpResponse.success(function(data, status, headers, config) {
			console.log(data);
			$scope.colunmsQuery = data.responseBody.colunms;
			$scope.sqlQueryResult = data.responseBody.retrivedTuples;
			editModal.modal();
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});
		
	}
	
	
	$scope.getTableData = function(tableName){
		
		
		var editModal = $('#sqlModalResults');
		$scope.modalTitle = tableName; 
		var sqlquery = "SELECT * FROM " + tableName;
		
		var dataObj = {
				requestBody : sqlquery
		}
		
		var httpResponse = $http.post('executeSQLQuery/' + $scope.dbAvalilable, dataObj);
		httpResponse.success(function(data, status, headers, config) {
			console.log(data);
			$scope.colunmsQuery = data.responseBody.colunms;
			$scope.sqlQueryResult = data.responseBody.retrivedTuples;
			editModal.modal();
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});
	} 
	
	$scope.init();
	
}]);