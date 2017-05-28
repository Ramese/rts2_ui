/*global angular, console*/

/*
README:

For dependencies declaration use file controllers.js, where is inicialized module name "controllers".

If you insert dependencies here, you override all modeles with name "controllers" (YOU DONT WANT!).
*/

(function () {
	'use strict';

	var app = angular.module('controllers');

	app.controller('UserCtrl', function ($scope, UserService, $routeParams, RegistrationService, TelescopeService) {
        
        $scope.FirstNamePattern = /^[a-zA-Z0-9]{3,50}$/;
        $scope.LastNamePattern = /^[a-zA-Z0-9]{3,50}$/;
        
        function fillTels(tels, userHas) {
            for(var i = 0; i < tels.length; i++) {
                if(userHas.includes(tels[i].Id)) {
                    tels[i].Has = true;
                } else {
                    tels[i].Has = false;
                }
            }

            console.log("tels", $scope.telescopes);
        }

        function getArray(tels) {
            var result = [];

            for(var i = 0; i < tels.length; i++) {
                if(tels[i].Has){
                    result.push(tels[i].Id);
                }
            }

            return result;
        }

        $scope.getTelescopesForUser = function () {

            TelescopeService.getTelescopes()
                .then(function(data) {
                    console.log("ok", data);
                    $scope.telescopes = data.data;
                    loadTelescopesForUser();
                },
                function (data) {
                    console.log("not ok, getTelescopes()", data);
                });

            function loadTelescopesForUser () {
                UserService.getTelescopesForUser($scope.user.Id)
                    .then(function(data) {
                        console.log("ok", data);
                        fillTels($scope.telescopes, data.data);
                    },
                    function (data) {
                        console.log("not ok, getTelescopesForUser", data);
                    });
            }
            
        };

        $scope.saveTelescopes = function () {
            var array = getArray($scope.telescopes);

            UserService.setTelescopesForUser($scope.user.Id, array)
                .then(function(data) {
                    console.log("ok", data);
                },
                function (data) {
                    console.log("not ok", data);
                });
        };

        $scope.register = function () {
            if($routeParams.id != "new") {
                $scope.save();
            }else {
                console.log("ctrl register()");
                RegistrationService.register($scope.user)
                    .success(function (data) {
                        BootstrapDialog.show({
                                title: 'Created',
                                closable: false,
                                type: BootstrapDialog.TYPE_SUCCESS,
                                message: "User created.",
                                buttons: [{
                                    label: 'Close',
                                    cssClass: 'btn-success',
                                    action: function (dialogRef) {
                                        $scope.user = {};
                                        dialogRef.close();
                                    }
                                }]
                            });
                    })
                    .error(function (data) {
                    
                    });
            }
            
        };
        
        $scope.isUserNameFree = function () {
            console.log("ctrl isusernamefree");
            if($routeParams.id === "new"){
                RegistrationService.isUserNameFree($scope.user)
                    .success(function (data) {
                        console.log(data);
                        $scope.userNameIsFree = data;
                    })
                    .error(function (data) {
                        console.log(data);
                    });
            }
        };

        $scope.save = function () {
            UserService.saveUser($scope.user)
                .success(function (data) {
                    //$scope.user = data;

                     BootstrapDialog.show({
                                title: 'Saved',
                                closable: false,
                                type: BootstrapDialog.TYPE_SUCCESS,
                                message: "User updated.",
                                buttons: [{
                                    label: 'Close',
                                    cssClass: 'btn-success',
                                    action: function (dialogRef) {
                                        $scope.user = {};
                                        dialogRef.close();
                                    }
                                }]
                            });
                })
                .error(function (data) {
                    console.log("telescope save error", data);
                });
        };
        
        function loadData() {
            UserService.getUser($routeParams.id)
                .success(function (data) {
                    $scope.user = data;
                })
                .error(function (data) {
                
                });
        }
        
        function init() {
            $scope.user = {};
            $scope.user.IsActive = true;
            $scope.userNameIsFree = true;
            
            $scope.IdUser = $routeParams.id;

            if ($routeParams.id === "new") {
                console.log("new");
            } else {
                loadData();

                $scope.getTelescopesForUser();
            }
        }
        
        init();
        
	});

}());
