(function() {
    'use strict';

    angular.module('twicepixelsApp').factory('Account', accountService);
    accountService.$inject = ['$q', '$resource'];

    function accountService ($q, $resource) {
        var service = $resource('http://localhost:8090/user', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });
        return service;
    }
})();
