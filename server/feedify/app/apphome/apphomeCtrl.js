'use strict';


angular.module('feedify.apphome', [])
    .controller('AppHomeCtrl', ['$scope', 'FeedModel', '$rootScope', '$cookies', '$http', '$state', '$window', '$sce',
        function($scope, FeedModel, $rootScope, $cookies, $http, $state, $window, $sce) {

          $scope.currentPage = 1;

          $scope.$watch('currentPage', function(newValue, oldValue) {
            refresh();
          })


            function refresh() {
                FeedModel.getArticles({
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
              FeedModel.markArticleAsRead({id: article.id}, {}, function(data) {
                $window.open(article.link, '_blank');
                refresh();
              }, function(data) {
                console.log(data);
              });
            };

            $scope.markAsRead = function(article) {
              FeedModel.markArticleAsRead({id: article.id}, {}, function(data) {
                refresh();
              }, function(data) {
                console.log(data);
              });
             };

            $scope.refreshData = function() {
                $scope.articles = [];
                refresh();
            };

        }
    ]);
