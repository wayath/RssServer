'use strict';

var config = {
    apiUrl: "http://romain-zanchi.com:4498/api",
    apiPort: "4498"
};

// Declare app level module which depends on views, and components
var feedify = angular.module('feedify', [
    'ngResource',
    'ngCookies',
    'ngAnimate',
    'ngSanitize',
    'ui.router',
    'ui.bootstrap',
    'vcRecaptcha',
    'feedify.menu',
    'feedify.apphome',
    'feedify.appdetail',
    'feedify.appsetup',
    'feedify.signup',
    'feedify.signin',
    'myApp.version'
]).run(function($rootScope, $state, $cookies, $http, $location) {
  //config.apiUrl = $location.protocol() + "://" + $location.host() + ":" + config.apiPort;

    $rootScope.$on('$stateChangeStart', function(event, toState, toParams) {
        var requireLogin = toState.data.requireLogin;
        if (!$rootScope.userToken) {
            var userCookie = $cookies.getObject('userToken');
            if (typeof userCookie != 'undefined') {
                $rootScope.userToken = userCookie;
                $http.defaults.headers.common['User-token'] = userCookie;

                var userUsername = $cookies.getObject('userUsername');
                $rootScope.userUsername = userUsername;

            }
        }
        if (!$rootScope.userInfos) {
            var userInfosCookie = $cookies.getObject('userInfos');
            if (typeof userInfosCookie != 'undefined') {
                $rootScope.userInfos = userInfosCookie;
            }
        }
        if (requireLogin && typeof $rootScope.userToken === 'undefined') {
            event.preventDefault();
            $state.go('signin');
        }
    });
}).config(['$stateProvider', '$urlRouterProvider', function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise("/home");

    $stateProvider
        .state('home', {
            url: "/home",
            templateUrl: 'landingView.html',
            data: {
                requireLogin: false
            }
        })
        .state('signup', {
            url: "/signup",
            templateUrl: 'signup/signupView.html',
            controller: "SignUpCtrl",
            data: {
                requireLogin: false
            }
        })
        .state('signin', {
            url: "/signin",
            templateUrl: 'signin/signinView.html',
            controller: "SignInCtrl",
            data: {
                requireLogin: false
            }
        })
        .state('app', {
            templateUrl: "appView.html",
            controller: "MenuCtrl",
            data: {
                requireLogin: true // this property will apply to all children of 'app' state
            }
        })
        .state('app.home', {
            url: "/app/home",
            templateUrl: "apphome/apphomeView.html",
            controller: "AppHomeCtrl"
        })
        .state('app.setup', {
            url: "/app/setup",
            templateUrl: "appsetup/appsetupView.html",
            controller: "AppSetupCtrl"
        })
        .state('app.detail', {
            url: "/app/detail/:id",
            templateUrl: "appdetail/appdetailView.html",
            controller: "AppDetailCtrl"
        });

}]);
