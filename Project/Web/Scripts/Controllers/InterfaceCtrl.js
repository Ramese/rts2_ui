/*global angular,console, $*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/
/*jslint eqeq: true*/
/*jslint bitwise: true*/
(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('InterfaceCtrl', function ($scope, $routeParams, TelescopeService) {
        
		$scope.telescope = {};
		$scope.telescope.Id = $routeParams.id;

		TelescopeService.getDevices($routeParams.id)
			.success(function (data) {
				if (data.Code == 200) {
					$scope.devices = JSON.parse(data.Body);

					$scope.devices.push("centrald");
				} else {
					console.log("Chyba!", data);
				}
			})
			.error(function () {
				
			});
			
		$scope.GetDeviceProperties = function (device) {
			$scope.choosenDevice = device;
			
			TelescopeService.getDeviceProperties($routeParams.id, device)
				.success(function (data) {
					//console.log(data.Body);
					if (data.Code == 200) {
						$scope.device = JSON.parse(data.Body);
						//console.log("device", $scope.device);
						
						$scope.properties = [];
						var key;
						for (key in $scope.device.d) {
							var dev = $scope.device.d[key];
                            if ($scope.device.d.hasOwnProperty(key)) {
                                $scope.properties.push(key);

								
								if($scope.IsBoolean(dev[0])) {
									if(dev[1]) {
										dev[1] = true;
									} else {
										dev[1] = false;
									}
								}

								if($scope.IsTime(dev[0])) {
									dev[1] = new Date(dev[1] * 1000);
								}

								if($scope.IsAltAzValue(dev[0])){
									console.log(dev);
								}
								
                            }
							
						}
						
					} else {
						console.log("Chyba!", data);
					}
				})
				.error(function () {
				
				});
		};
        
        $scope.SetValue = function (name, param) {
			console.log("p",$routeParams.id, $scope.choosenDevice, name, param[1]);
			var value = param[1];
			if($scope.IsBoolean(param[0])) {
				if(param[1]) {
					value = 1;
				} else {
					value = 0;
				}
			}

			if($scope.IsAltAzValue(param[0])){
				value = param[1].alt + " " + param[1].az;
			}

			if($scope.IsRaDecValue(param[0])){
				value = param[1].ra + " " + param[1].dec;
			}

			if($scope.IsArray(param[0]) || $scope.IsRectangle(param[0])){
				if(param[1].length > 0) {
					value = param[1][0];
					for(var i = 1; i < param[1].length; i++) {
						value += " " + param[1][i];
					}
				} else {
					value = "";
				}
			}

            TelescopeService.setValue($routeParams.id, $scope.choosenDevice, name, value)
				.success(function (data) {
					if (data.Code == 200) {
						console.log("imagedata", data);
					} else {
						console.log("Chyba!", data);
					}
				})
				.error(function () {
				
				});
        };

		$scope.RemoveFromArray = function (index, array) {
			if (index > -1) {
				array.splice(index, 1);
			}
		}

		function Logic(a, flag) {
			return a == flag;

			// var tmp = a & flag;
			//if(flag == 7) {
			//	console.log(a, flag, tmp,  a === tmp);
			//}
			//return a == tmp;
		}
		
		$scope.IsReadOnly = function (flag) {
			return !$scope.IsWriteable(flag);
		};
		

		$scope.HasValueError = function (flag) {
			return Logic(0x20000000, flag);
		};

		$scope.HasValueWarning = function (flag) {
			return Logic(0x10000000, flag);
		};

		$scope.IsString = function (flag) {
			return Logic(0x00000001, flag & 0xF);
		};

		$scope.IsInteger = function (flag) {
			return Logic(0x00000002, flag & 0xF);
		};

		$scope.IsTime = function (flag) {
			return Logic(0x00000003, flag & 0xF);
		};

		$scope.IsDouble = function (flag) {
			return Logic(0x00000004, flag & 0xF);
		};

		$scope.IsFloat = function (flag) {
			return Logic(0x00000005, flag & 0xF);
		};

		$scope.IsBoolean = function (flag) {
			return Logic(0x00000006, flag & 0xF);
		};

		$scope.IsSelection = function (flag) {
			return Logic(0x00000007, flag & 0xF);
		};

		$scope.IsLong = function (flag) {
			return Logic(0x00000008, flag & 0xF);
		};
		
		$scope.IsRaDecValue = function (flag) {
			return Logic(0x00000009, flag & 0xF);
		};

		$scope.IsAltAzValue = function (flag) {
			return Logic(0x0000000A, flag & 0xF);
		};

		$scope.IsPID = function (flag) {
			return Logic(0x0000000B, flag & 0xF);
		};

		$scope.IsStat = function (flag) {
			return Logic(0x00000010, flag & 0xF0);
		};

		$scope.IsMinMax = function (flag) {
			return Logic(0x00000020, flag & 0xF0);
		};

		$scope.IsRectangle = function (flag) {
			return Logic(0x00000030, flag & 0xF0);
		};

		$scope.IsArray = function (flag) {
			return Logic(0x00000040, flag & 0xF0);
		};

		$scope.IsTimeSerie = function (flag) {
			return Logic(0x00000070, flag & 0xF0);
		};

		$scope.IsWriteable = function (flag) {
			return 0x02000000 & flag;

			//return Logic(0x02000000, flag & 0xF000000);
		};

		$scope.IsNotNull = function (flag) {
			return 0x08000000 & flag;
		};

	});

	
}());
