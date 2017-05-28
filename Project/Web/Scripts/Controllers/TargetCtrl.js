/*global angular, console*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('TargetCtrl', function ($scope, TelescopeService, $routeParams, $location) {

        $scope.UpdateTarget = function () {
            var data = $scope.targetData.d[0];
            TelescopeService.updateTarget($scope.telescopeId, data[0], data[1], data[2], data[3])
                .success(function (data) {
                    console.log(data);
                    if(data.Code == 200) {
                        //ok
                    } else {
                        BootstrapDialog.show({
							title: 'Error',
							closable: false,
							type: BootstrapDialog.TYPE_DANGER,
							message: data.Body,
							buttons: [{
								label: 'Close',
								cssClass: 'btn-danger',
								action: function (dialogRef) {
									dialogRef.close();
								}
							}]
						});
                    }
                })
                .error(function (data) {
                    console.log("err", data);
                });
        };

        $scope.CreateTarget = function () {
            
            TelescopeService.createTarget($scope.telescopeId, $scope.target)
                .success(function (data) {
                    console.log(data);
                    if(data.Code == 200) {
                        var body = JSON.parse(data.Body);
                        $location.path("/target/" + $scope.telescopeId + "/" + body.id);
                    } else {
                        BootstrapDialog.show({
							title: 'Error',
							closable: false,
							type: BootstrapDialog.TYPE_DANGER,
							message: data.Body,
							buttons: [{
								label: 'Close',
								cssClass: 'btn-danger',
								action: function (dialogRef) {
									dialogRef.close();
								}
							}]
						});
                    }
                })
                .error(function (data) {
                    console.log("err", data);
                });
        };

        $scope.IsAttributeReadOnly = function (header) {
            switch (header) {
                case "RA":
                    return false;
                case "DEC":
                    return false;
                case "Target Name":
                    return false;
                default:
                    return true;
            }
        };

        function loadData() {
            TelescopeService.getTarget($routeParams.telescopeId, $routeParams.targetId)
                .success(function (data) {
                    console.log("tels ctrl: ", data);
                    $scope.targetData = JSON.parse(data.Body);
                    console.log($scope.targetData);
                })
                .error(function (data) {
                
                });
        }
        
        function init() {
            $scope.telescopeId = $routeParams.telescopeId;
            $scope.targetId = $routeParams.targetId;

            if($scope.targetId !== "new") {
                loadData();
            }
        }
        
        init();
	});

}());