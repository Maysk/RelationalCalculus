var app = angular.module('myApp',[])
.controller('TrcToSql', ['$scope','$http', function($scope, $http){
	

	
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
	

	$scope.cleanScope = function(){
		$scope.projection = "";
		$scope.trcformula = "";
		$scope.outputformula = "";
		$scope.sqlnfformula = "";
		$scope.sqlexpression = "";
		$("#submitFormula").removeAttr("disabled");
		$("#projection").removeAttr("readonly");
		$("#trcformula").removeAttr("readonly");
		$("#errorsDiv").hide();
		$("#listOfErrors").empty();
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
			$("#listOfErrors").empty();
			$("#listOfErrors").append(data.responseBody);
			console.log(data);
		}
		else{
			console.log("DeuCerto!");
			}
		
		});
		
		httpResponse.error(function(data, status, headers, config) {
			alert( "failure message: " + JSON.stringify({data: data}));
		});
	}
	
	
	$scope.addTextToFormulaBox = function(newText){
		
		var el = $("#trcformula");
		var start = el.prop("selectionStart")
		var end = el.prop("selectionEnd")
		var text = el.val()
		var before = text.substring(0, start)
		var after  = text.substring(end, text.length)
		el.val(before + newText + after)
		el[0].selectionStart = el[0].selectionEnd = start + newText.length
		el.focus()
	}
	
	$scope.init();
	
}]);