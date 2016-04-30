feedify.factory('UserModel', ['$resource',
    function($resource) {

        return $resource(config.apiUrl, {}, {
            signUp: {
                method: "POST",
                url: config.apiUrl+"/register"
            },
            signIn: {
                method: "POST",
                url: config.apiUrl+"/login"
            },
            getUser: {
              method: "GET",
              url: config.apiUrl+'/user/:username'
            },
            getUsers: {
              method: "GET",
              url: config.apiUrl+'/users'
            },
            updateUser: {
              method: "PUT",
              url : config.apiUrl+'/user/:username'
            },
            deleteUser: {
              method: "DELETE",
              url: config.apiUrl+'/user/:username'
            }
        });
    }
]);
