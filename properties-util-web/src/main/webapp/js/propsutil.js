var myApp = angular.module('PropsUtilApp', []);

myApp.controller('PropsUtilController',
	function($scope, $http) {
		var noProps = [{name:'none'}];
		$('#file1').change(function(){
		    $('#subfile1').val($(this).val());
		    $('#loadFile1Btn').prop('disabled', false);
		});
	
		$('#file2').change(function(){
		    $('#subfile2').val($(this).val());
		    $('#loadFile2Btn').prop('disabled', false);
		});
		
		function checkInput() {
			if ($scope.data1 && $scope.data2) {
				$('#compareBtn').prop('disabled', false);
			} else {
				$('#compareBtn').prop('disabled', true);
			}
		}

		$scope.add1 = function() {
			var f = document.getElementById('file1').files[0], r = new FileReader();
			r.onloadend = function(e) {
				$scope.data1 = e.target.result;
				$('#loadFile1OkInd').show();
				checkInput();
			}
			r.readAsBinaryString(f);
		}

		$scope.add2 = function() {
			var f = document.getElementById('file2').files[0], r = new FileReader();
			r.onloadend = function(e) {
				$scope.data2 = e.target.result;
				$('#loadFile2OkInd').show();
				checkInput();
			}
			r.readAsBinaryString(f);
		}
		
		$scope.compare = function() {
			$http.post('/propsutil/process', { file1: $scope.data1, file2: $scope.data2} ).
			success(function(data) {
				if (typeof data.fileOneDeficiencies[0] != 'undefined') {
					$scope.file1Missing = JSON.parse(JSON.stringify(data.fileOneDeficiencies));
				} else {
					$scope.file1Missing = noProps;
				}
				if (typeof data.fileTwoDeficiencies[0] != 'undefined') {
					$scope.file2Missing = JSON.parse(JSON.stringify(data.fileTwoDeficiencies));
				} else {
					$scope.file2Missing = noProps;
				}
				if (typeof data.fileOneDuplicates[0] != 'undefined') {
					$scope.file1Duplicates = JSON.parse(JSON.stringify(data.fileOneDuplicates));
				} else {
					$scope.file1Duplicates = noProps;
				}
				if (typeof data.fileTwoDuplicates[0] != 'undefined') {
					$scope.file2Duplicates = JSON.parse(JSON.stringify(data.fileTwoDuplicates));
				} else {
					$scope.file2Duplicates = noProps;
				}
				//$scope.file1Duplicates = JSON.parse(JSON.stringify(data.fileOneDuplicates));
				//$scope.file2Duplicates = JSON.parse(JSON.stringify(data.fileTwoDuplicates));
				//$scope.file1Missing = JSON.parse(JSON.stringify(data.fileOneDeficiencies));
				//$scope.file2Missing = JSON.parse(JSON.stringify(data.fileTwoDeficiencies));
			}).
			error(function() {
				alert('ERROR Processing Data');
			});
		}
		
		$scope.reset = function() {
			$scope.file1Duplicates = null;
			$scope.file2Duplicates = null;
			$scope.file1Missing = null;
			$scope.file2Missing = null;
			
			$scope.data1 = null;
			$scope.data2 = null;
			
			$('#subfile1').val('');
			$('#subfile2').val('');
			
			$('#loadFile1OkInd').hide();
			$('#loadFile2OkInd').hide();
			$('#loadFile1Btn').prop('disabled', true);
			$('#loadFile2Btn').prop('disabled', true);
			
			$('#compareBtn').prop('disabled', true);
		}
	});