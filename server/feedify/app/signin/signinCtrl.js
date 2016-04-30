'use strict';

Date.prototype.addHours = function(h) {
   this.setTime(this.getTime() + (h*60*60*1000));
   return this;
}


angular.module('feedify.signin', [])
    .controller('SignInCtrl', ['$scope', 'UserModel', '$rootScope', '$cookies', '$http', '$state',
        function($scope, UserModel, $rootScope, $cookies, $http, $state) {

            $scope.signInUser = function() {
                UserModel.signIn($scope.user,
                    function(data) {
                        console.log(data);

                        if (data.token) {
                            // setting default header token for future API calls
                            $http.defaults.headers.common['User-token'] = data.token;
                            // setting a date object for cookie expiration (2 hours)
                            var exp = new Date();
                            exp.addHours(4);
                            // storing token in user cookies
                            $cookies.putObject("userToken", data.token, {
                                expires: exp
                            });
                            $cookies.putObject("userUsername", $scope.user.username, {
                                expires: exp
                            });

                            $rootScope.userToken = data.token;
                            $rootScope.userUsername = $scope.user.username;

                            UserModel.getUser({username: $scope.user.username}, function(data) {
                              console.log(data);
                              $cookies.putObject("userInfos", data, {
                                  expires: exp
                              });
                              $state.go('app.home');
                            }, function(data) {
                              console.log(data);
                            });




                        }

                    },
                    function(data) {
                        $scope.showError = true;
                        console.log(data);
                    });
            };

        }
    ]);
