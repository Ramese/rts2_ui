/*global angular, console*/
(function () {
	'use strict';

	var app = angular.module('services');

	app.service('TelescopeService', function ($http, CONSTANTS) {

		this.getTelescopes = function (pagination) {
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/telescopes",
				headers: { 'Content-Type': 'application/json' },
                data: pagination
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.getPublicTelescopes = function () {
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/publicTelescopes",
				headers: { 'Content-Type': 'application/json' }
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};
        
		this.getTelescope = function (id) {
		    var telProp = {};
		    
		    telProp.Id = id;
		    
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/telescope",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};
		
		this.saveTelescope = function (telescope) {
			return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/savetelescope",
				headers: { 'Content-Type': 'application/json' },
                data: telescope
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};
        
        this.getDevices = function (id) {
            var telProp = {};
        		
            telProp.Id = id;

            return $http({
                method: "POST",
                url: CONSTANTS.API_PREFIX + "/devices",
                headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};
		
		this.getDeviceProperties = function (id, device) {
            var telProp = {};
        		
            telProp.Id = id;
            telProp.Device = device;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/deviceproperties",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};
		
		this.getCurrentImage = function (id, device) {
            var telProp = {};
        		
            telProp.Id = id;
            telProp.Device = device;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/currentimage",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};
		
		this.getExposeData = function (id, device) {
            var telProp = {};
        		
            telProp.Id = id;
            telProp.Device = device;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/exposedata",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};
        
        this.setValue = function (id, device, valueName, value) {
            var telProp = {};
        		
            telProp.Id = id;
            telProp.Device = device;
            telProp.ValueName = valueName;
            telProp.Value = value;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/set",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.addToQueue = function (id, device, queue, idTarget, from, to, rep, sep) {
            var telProp = {};
        		
            telProp.Id = id;
            telProp.Device = device;
            telProp.Queue = queue;
			telProp.IdTarget = idTarget;
            telProp.From = from;
			telProp.To = to;
			telProp.Rep = rep;
			telProp.Sep = sep;

			console.log(telProp);

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/addToQueue",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.getTargets = function (id) {
            var telProp = {};
        		
            telProp.Id = id;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/targets",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.searchTargets = function (id, searchTarget) {
            var telProp = {};
        	
			telProp.Id = id;
            telProp.Value = searchTarget;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/searchTargets",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.getTargetById = function (idTelescope, idTarget) {
            var telProp = {};
        	
			telProp.Id = idTelescope;
            telProp.IdTarget = idTarget;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/getTargetById",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.getTarget = function (telescopeId, targetId) {
            var telProp = {};
        		
            telProp.Id = telescopeId;
			telProp.Target = {};
			telProp.Target.Id = targetId;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/target",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.createTarget = function (telescopeId, target) {
            var telProp = {};
        		
            telProp.Id = telescopeId;
			telProp.Target = target;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/createTarget",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.updateTarget = function (telescopeId, targetId, targetName, targetRA, targetDEC) {
            var telProp = {};
        		
            telProp.Id = telescopeId;
			telProp.Target = {};
			telProp.Target.Id = targetId;
			telProp.Target.RA = targetRA;
			telProp.Target.DEC = targetDEC;
			telProp.Target.Name = targetName;

			console.log("tar for up", telProp);

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/updateTarget",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.setAutonomousTarget = function (telescopeId, targetId, targetEnabled) {
            var telProp = {};
        		
            telProp.Id = telescopeId;
			telProp.Target = {};
			telProp.Target.Id = targetId;
			telProp.Target.Enabled = targetEnabled;

            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/autonomousTarget",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};

		this.getPlan = function (telescopeId, from, to) {
            var telProp = {};
        		
            telProp.Id = telescopeId;
			telProp.From  = from;
			telProp.To = to;
			
            return $http({
				method: "POST",
				url: CONSTANTS.API_PREFIX + "/getPlan",
				headers: { 'Content-Type': 'application/json' },
                data: telProp
			}).error(function (data) {
				console.log(data);
				console.log("TelescopeService error");
			});
		};
	});

}());
