/*global angular, console*/
(function () {
	'use strict';

	var app = angular.module('services');

	app.service('ImageService', function ($http, CONSTANTS) {

		this.getCurrent = function (id) {
            var telProp = {};
            telProp.Id = id;
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/current",
				headers: { 'Content-Type': 'application/json' },
				data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("ImageService error");
			});
		};

		this.getImage = function (id, path) {
            var telProp = {};
            telProp.Id = id;
			telProp.Value = path;
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/getImage",
				headers: { 'Content-Type': 'application/json' },
				data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("ImageService error");
			});
		};

		this.getFitsImage = function (id, path) {
            var telProp = {};
            telProp.Id = id;
			telProp.Value = path;
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/getFitsImage",
				headers: { 'Content-Type': 'application/json' },
				data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("ImageService error");
			});
		};

		this.getListOfImagesForDate = function (id, date) {
            var telProp = {};
            telProp.Id = id;
			telProp.Value = date;
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/getListOfImagesForDate",
				headers: { 'Content-Type': 'application/json' },
				data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("ImageService error");
			});
		};

		this.getListOfImagesForTarget = function (idTelescope, idTarget) {
            var telProp = {};
            telProp.Id = idTelescope;
			telProp.IdTarget = idTarget;
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/getListOfImagesForTarget",
				headers: { 'Content-Type': 'application/json' },
				data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("ImageService error");
			});
		};

	});

}());
