/*global angular, console*/
(function () {
	'use strict';

	var app = angular.module('services');

	app.service('RegistrationService', function ($http, CONSTANTS) {

		this.register = function (user) {
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/register",
				headers: { 'Content-Type': 'application/json' },
				data: user
			}).error(function (data) {
				console.log(data);
				console.log("RegistrationService error");
			});
		};
        
		this.isUserNameFree = function (user) {
            return $http({
                method: "POST",
                url: CONSTANTS.API_PREFIX + "/register/isUsernameFree",
                headers: { 'Content-Type': 'application/json' },
                data: user
            }).error(function (data) {
                console.log(data);
                console.log("RegistrationService error");
            });
        };
	});

}());
