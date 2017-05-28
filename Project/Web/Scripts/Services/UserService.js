/*global angular, console*/
(function () {
	'use strict';

	var app = angular.module('services');

	app.service('UserService', function ($http, CONSTANTS) {

		this.getUsers = function (pagination) {
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/getUsers",
				headers: { 'Content-Type': 'application/json' },
                data: pagination
			}).error(function (data) {
				console.log(data);
				console.log("UserService error");
			});
		};

		this.getUser = function (id) {
			var userProp = {};
		    
		    userProp.Id = id;

			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/getUser",
				headers: { 'Content-Type': 'application/json' },
                data: userProp
			}).error(function (data) {
				console.log(data);
				console.log("UserService error");
			});
		};

		this.saveUser = function (user) {
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/saveUser",
				headers: { 'Content-Type': 'application/json' },
                data: user
			}).error(function (data) {
				console.log(data);
				console.log("UserService error");
			});
		};

		this.setTelescopesForUser = function (idUser, tels) {
			var prop = {};

			prop.Id = idUser;
			prop.Telescopes = tels;

			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/setTelescopesForUser",
				headers: { 'Content-Type': 'application/json' },
                data: prop
			}).error(function (data) {
				console.log(data);
				console.log("UserService error");
			});
		};

		this.getTelescopesForUser = function (idUser) {
			var prop = {};

			prop.Id = idUser;

			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/getTelescopesForUser",
				headers: { 'Content-Type': 'application/json' },
                data: prop
			}).error(function (data) {
				console.log(data);
				console.log("UserService error");
			});
		};
	});

}());