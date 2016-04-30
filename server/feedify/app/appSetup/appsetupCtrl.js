'use strict';


angular.module('feedify.appsetup', [])
    .controller('AppSetupCtrl', ['$scope', 'UserModel', '$rootScope', '$cookies', '$http', '$state', '$window', '$uibModal',
        function($scope, UserModel, $rootScope, $cookies, $http, $state, $window, $uibModal) {

          $scope.userInfos = $rootScope.userInfos;


            $scope.updatePassword = function(usernameMod) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'shared/updatePasswordModal.html',
                    controller: 'UpdatePasswordCtrl',
                    resolve: {
                        username: function() {
                            return usernameMod;
                        }
                    }
                });
            };

            $scope.makeAdmin = function(theUser) {
                var toggle = "";
                if (theUser.type == 'admin')
                    toggle = 'user';
                else if (theUser.type == 'user') {
                    toggle = 'admin';
                }
                UserModel.updateUser({
                    username: theUser.username
                }, {
                    username: theUser.username,
                    type: toggle
                }, function(data) {
                    console.log(data);
                    refreshData();
                }, function(data) {
                    console.log(data);
                });
            }

            $scope.deleteUser = function(username) {
                UserModel.deleteUser({
                        username: username
                    },
                    function(data) {
                        console.log(data);
                        if (username == $scope.userInfos.username) {
                          $state.go('home');
                        }
                        refreshData();
                    },
                    function(data) {
                        console.log(data);
                    }
                );
            }


            function refreshData()  {
                if ($rootScope.userInfos.type == 'admin') {
                    console.log('admin');
                    $scope.admin = true;

                    UserModel.getUsers(function(data) {
                        console.log(data);
                        $scope.users = data.users;
                    }, function(data) {
                        console.log(data);
                    });
                }
                else {
                  $scope.users = [];
                  $scope.users.push($rootScope.userInfos);
                }
            }
            refreshData();




        }
    ])
    .controller('UpdatePasswordCtrl', ['$scope', 'UserModel', '$uibModalInstance', 'username',
        function($scope, UserModel, $uibModalInstance, username) {

            $scope.username = username;


            $scope.updatePassword = function() {
                UserModel.updateUser({
                    username: username
                }, {
                    username: username,
                    password: $scope.password
                }, function(data) {
                    console.log(data);
                    $uibModalInstance.close('ok');
                }, function(data) {
                    console.log(data);
                })
            };

            $scope.cancel = function() {
                $uibModalInstance.dismiss();
            };


        }
    ]);
