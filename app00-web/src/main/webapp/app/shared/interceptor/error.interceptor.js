(function() {
    'use strict';

    angular.module('twicepixelsApp').factory('errorInterceptor', errorInterceptor);
    errorInterceptor.$inject = ['$q', '$rootScope'];

    function errorInterceptor ($q, $rootScope) {
        var service = {
            responseError: responseError
        };
        return service;

        function responseError (response) {
            if (!(response.status === 401 && (response.data === '' ||
                response.data.path === undefined ||
                response.data.path.indexOf('/api/account') === 0 ))) {
                $rootScope.$emit('twicepixelsApp.httpError', response);
            }
            return $q.reject(response);
        }
    }
})();
