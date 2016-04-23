(function() {
    'use strict';

    angular.module('twicepixelsApp').factory('AuthProvider', authProviderService);
    authProviderService.$inject = ['$http', '$localStorage', 'Base64'];

    function authProviderService($http, $localStorage, _base64) {
        var service = {
            login: login,
            logout: logout,
            getToken: getToken,
            hasValidToken: hasValidToken
        };

        return service;

        function login(credentials) {
            var password = encodeURIComponent(credentials.password);
            var username = encodeURIComponent(credentials.username);
            var basicAut = _base64.encode("acme" + ':' + "acmesecret");
            var data = "username=" + username + "&password=" + password +
                "&grant_type=password&scope=read%20write" +
                "&client_id=acme&client_secret=acmesecret";
            return $http.post('http://localhost:8090/oauth/token', data, {
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded",
                    "Accept": "application/json",
                    "Authorization": "Basic " + basicAut
                }
            }).success(function(response) {
                var expiredAt = new Date();
                expiredAt.setSeconds(expiredAt.getSeconds() + response.expires_in);
                response.expires_at = expiredAt.getTime();
                $localStorage.token = response;
                return response;
            });
        }
        function logout() {
             // logout from the server
             $http.post('http://localhost:8090/logout').then(function() {
                 $localStorage.$reset();
             });
        }
        function getToken() {
            return $localStorage.token
        }
        function hasValidToken() {
            var token = this.getToken();
            return token && token.expires_at && token.expires_at > new Date().getTime();
        }
    }
})();