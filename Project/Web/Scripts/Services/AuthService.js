/*global angular */

/*
README:

For dependencies declaration use file services.js, where is inicialized module name "services".

If you insert dependencies here, you override all modeles with name "services".
*/

(function () {
	'use strict';

	var app = angular.module('services');

	app.factory('Auth', function ($http, $cookies, $rootScope, CONSTANTS) {

		var cookiesName = CONSTANTS.COOKIES_NAME;

		$rootScope.user = $cookies.getObject(cookiesName);

		function changeUser(user) {
			$cookies.putObject(cookiesName, user);
			$rootScope.user = user;
		}

		function removeUser() {
			$rootScope.user = undefined;
			$cookies.remove(cookiesName);
		}

		function isUserLoggedIn() {
			return $rootScope.user !== undefined;
		}

		return {
			isLoggedIn: function () {
				return isUserLoggedIn();
			},
			login: function (user, success, error) {
				var request = $http({
					method: "POST",
					url: CONSTANTS.API_PREFIX + "/login",
					headers: { 'Content-Type': 'application/json' },
					data: user
				}).success(function (user) {
					user.lang = "cs";
					changeUser(user);
					success(user);
				}).error(function (data) {
					removeUser();
					error(data);
				});
			},
			logout: function (success) {
				removeUser();
				success();
			},
			user: function () {
				return $rootScope.user;
			},
			getLanguage: function () {
				if (isUserLoggedIn()) {
					return $rootScope.user.lang;
				}
				return "cs";
			},
			setLanguage: function (lang) {
				if (isUserLoggedIn()) {
					$rootScope.user.lang = lang;
					changeUser($rootScope.user);
				}
			}
		};
	});

}());