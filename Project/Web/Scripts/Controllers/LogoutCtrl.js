/*global angular*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('LogoutCtrl', ['$scope', 'Auth', '$window', '$translate', 'CONSTANTS', '$cookies', function ($scope, Auth, $window, $translate, CONSTANTS, $cookies) {

		$scope.$root.title = $translate.instant('TITLE_LOGOUT') + " | " + CONSTANTS.WEB_NAME;

		Auth.logout(function () {
			$cookies.remove(CONSTANTS.COOKIES_NAME);
			$window.location.href = 'login';
		}, function () {
			$cookies.remove(CONSTANTS.COOKIES_NAME);
			$window.location.href = 'login';
		});

	}]);

}());