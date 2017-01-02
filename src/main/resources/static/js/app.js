var app = angular.module('myApp',[])
.controller('TrcToSql', ['$scope','$http', function($scope, $http){
	//$scope.databasesAvailable = ["a", "b"];
	$scope.names = ["Emil", "Tobias", "Linus"];
	
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
	
	$scope.loadDbSchema = function(){
		var httpResponse = $http.post('db/listTables/'+ $scope.dbAvalilable, {});
		
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
		var temp = $scope.trcformula.replace(/∀/g,"FORALL")
									.replace(/∃/g, "EXISTS")
									.replace(/∧/g, "AND")
									.replace(/∨/g, "OR")
									.replace(/→/g, "->")
									.replace(/¬/g, "NOT");
		
		$scope.outputformula = "{" + $scope.projection + " | " + temp + "}";
		
		
		var dataObj = {
				requestBody : $scope.outputformula
		}
		
		var httpResponse = $http.post('trc/converttosqlnf', dataObj);
		httpResponse.success(function(data, status, headers, config) {
			console.log(data);
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