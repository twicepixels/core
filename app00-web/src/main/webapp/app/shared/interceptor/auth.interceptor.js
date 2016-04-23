(function() {
    'use strict';
    angular.module('twicepixelsApp').factory('authStartInterceptor', authStartInterceptor);
    authStartInterceptor.$inject = ['$rootScope', '$q', '$injector', '$localStorage'];
    angular.module('twicepixelsApp').factory('authExpiredInterceptor', authExpiredInterceptor);
    authExpiredInterceptor.$inject = ['$rootScope', '$q', '$injector', '$localStorage'];

    function authStartInterceptor($rootScope, $q, $location, $localStorage) {
        return {
            // Add authorization token to headers
            request: function (config) {
                config.headers = config.headers || {};
                var token = $localStorage.token;

                if (token && token.expires_at && token.expires_at > new Date().getTime()) {
                    config.headers.Authorization = 'Bearer ' + token.access_token;
                }

                return config;
            }
        };
    }
    function authExpiredInterceptor($rootScope, $q, $injector, $localStorage) {
        return {
            responseError: function (response) {
                // token has expired
                if (response.status === 401 && (response.data.error == 'invalid_token' ||
                        response.data.error == 'Unauthorized')) {
                    delete $localStorage.counter;
                    var Principal = $injector.get('Principal');
                    if (Principal.isAuthenticated()) {
                        var Auth = $injector.get('Auth');
                        Auth.authorize(true);
                    }
                }
                return $q.reject(response);
            }
        };
    }
})();
