/*global angular, console*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('RegistrationCtrl', function ($scope, RegistrationService) {
        $scope.FirstNamePattern = /^[a-zA-Z0-9]{3,50}$/;
        $scope.LastNamePattern = /^[a-zA-Z0-9]{3,50}$/;

        $scope.user = {};
        $scope.user.IsActive = true;
        
        $scope.register = function () {
            console.log("ctrl register()");
            RegistrationService.register($scope.user)
                .success(function (data) {
                    
                })
                .error(function (data) {
                
                });
        };
        
        $scope.isUserNameFree = function () {
            console.log("ctrl isusernamefree");
            
            RegistrationService.isUserNameFree($scope.user)
                .success(function (data) {
                    console.log(data);
                    $scope.userNameIsFree = data;
                })
                .error(function (data) {
                    console.log(data);
                });
        };
	});

}());