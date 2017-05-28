/*global angular*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('LoginCtrl', function ($scope, $window, Auth, $translate, CONSTANTS) {

		$scope.IsSubmitEnabled = function () {
			return (user.UserName && user.Password);
		};
        $scope.user = {};
        
		$scope.$root.title = $translate.instant('TITLE_LOGIN') + " | " + CONSTANTS.WEB_NAME;

		$scope.rememberme = true;

		$scope.login = function () {
			Auth.login($scope.user,
				function (res) {
					$window.location.href = CONSTANTS.WEB_PREFIX + '/';

					$scope.loginError = undefined;
				},
				function (err) {
					//$scope.loginError = err.Message;
					//$scope.username = "";
					$scope.password = "";
				});
		};
	});

}());