'use strict';

angular.module('feedify.menu', [])
    .controller('MenuCtrl', ['$scope', '$rootScope', 'FeedModel', '$state', '$cookies', '$interval', '$uibModal',
        function($scope, $rootScope, FeedModel, $state, $cookies, $interval, $uibModal) {
            console.log($rootScope.userToken);
            $scope.username = $rootScope.userUsername;

            $scope.logout = function() {
                $cookies.remove('userToken');
                $cookies.remove('userUsername');
                $cookies.remove('userInfos');
                $rootScope.userToken = null;
                $rootScope.userUsername = null;
                $state.go('signin');
            };

            $scope.openAddFeedModal = function() {
                var modalInstance = $uibModal.open({
                    templateUrl: 'shared/addFeedModal.html',
                    controller: 'AddFeedModalCtrl'
                        //size: size
                });
                modalInstance.result.then(function(data) {
                  console.log(data);
                  FeedModel.updateFeedWorker({id: data.id}, function(data) {
                    console.log(data);
                    refresh();
                  }, function(data) {
                    console.log(data);
                  });
                }, function() {
                });
            };

            function refresh() {
                FeedModel.getFeeds(function(data) {
                    $scope.feeds = data.feeds;
                }, function(data) {
                    console.log(data);
                });
            }

            refresh();

            $interval(refresh, 5000);



        }
    ])
    .controller('AddFeedModalCtrl', ['$scope', 'FeedModel', '$uibModalInstance',
        function($scope, FeedModel, $uibModalInstance) {
            $scope.cancel = function() {
                $uibModalInstance.dismiss();
            };

            $scope.addFeed = function() {
                FeedModel.addFeed($scope.feed, function(data) {
                    $uibModalInstance.close(data);
                }, function(data) {
                    console.log(data);
                });
            };
        }
    ]);
