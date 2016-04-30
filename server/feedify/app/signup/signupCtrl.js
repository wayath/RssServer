'use strict';

angular.module('feedify.signup', [])
    .controller('SignUpCtrl', ['$scope', 'UserModel', '$state',
        function($scope, UserModel, $state) {

            $scope.signUpUser = function() {
                UserModel.signUp($scope.user,
                    function(data) {
                        console.log(data);
                        $state.go('signin');
                    },
                    function(data) {
                        console.log(data);
                    });
            };

        }
    ]);
