'use strict';


angular.module('feedify.appdetail', [])
    .controller('AppDetailCtrl', ['$scope', 'FeedModel', '$rootScope', '$cookies', '$http', '$state', '$window', '$sce', '$stateParams',
        function($scope, FeedModel, $rootScope, $cookies, $http, $state, $window, $sce, $stateParams) {

            $scope.currentPage = 1;

            $scope.$watch('currentPage', function(newValue, oldValue) {
                refresh();
            })

            function refreshFeedInfos() {
                FeedModel.getFeedInfos({
                    id: $stateParams.id
                }, function(data) {
                    console.log(data);
                    $scope.feed = data;
                    $scope.newFeedName = data.name;
                }, function(data) {
                    console.log(data);
                });
            }
            refreshFeedInfos();



            function refresh() {
                FeedModel.getFeedArticles({
                        id: $stateParams.id,
                        page: $scope.currentPage
                    },
                    function(data) {
                        $scope.articles = data.articles;
                        for (var i = 0; i < $scope.articles.length; i++) {
                            $scope.articles[i].fullHTML = $sce.trustAsHtml($scope.articles[i].full);
                        }
                        console.log($scope.articles);
                    },
                    function(data) {
                        console.log(data);
                    });
            }
            refresh();


            $scope.openArticle = function(article) {
                console.log(article);
                FeedModel.markArticleAsRead({
                    id: article.id
                }, {}, function(data) {
                    $window.open(article.link, '_blank');
                    refresh();
                }, function(data) {
                    console.log(data);
                });
            };

            $scope.markAllAsRead = function() {
                FeedModel.markFeedAsRead({
                    id: $stateParams.id
                }, {}, function(data) {
                    console.log(data);
                    refresh();
                }, function(data) {
                    console.log(data);
                });
            };

            $scope.markAsRead = function(article) {
                FeedModel.markArticleAsRead({
                    id: article.id
                }, {}, function(data) {
                    refresh();
                }, function(data) {
                    console.log(data);
                });
            };

            $scope.updateFeedName = function() {
                FeedModel.updateFeedInfos({
                        id: $stateParams.id
                    }, {
                        name: $scope.newFeedName
                    },
                    function(data) {
                        console.log(data);
                        refreshFeedInfos();
                        $scope.showNewName = false;
                    },
                    function(data) {
                        console.log(data);
                    }
                );
            };

            $scope.refreshData = function() {
                $scope.articles = [];
                refresh();
            };

            $scope.deleteFeed = function() {
              FeedModel.deleteFeed({id: $stateParams.id}, function(data) {
                console.log(data);
                $state.go('app.home');
              }, function(data) {
                console.log(data);
              });
             };

        }
    ]);
