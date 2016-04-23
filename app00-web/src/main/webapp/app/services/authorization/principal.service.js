
(function() {
    'use strict';

    angular.module('twicepixelsApp').factory('Principal', principalService);
    principalService.$inject = ['$q', 'Account'];

    function principalService($q, _account) {
        var _identity,
            _authenticated = false;

        var service = {
            authenticate: authenticate,
            hasAnyAuthority: hasAnyAuthority,
            hasAuthority: hasAuthority,
            identity: identity,
            isAuthenticated: isAuthenticated,
            isIdentityResolved: isIdentityResolved
        };

        return service;

        function authenticate(identity) {
            _identity = identity;
            _authenticated = identity !== null;
        }

        function hasAnyAuthority(authorities) {
            if (!_authenticated || !_identity || !_identity.authorities) {
                return false;
            }

            for (var i = 0; i < authorities.length; i++) {
                if (_identity.authorities.indexOf(authorities[i]) !== -1) {
                    return true;
                }
            }

            return false;
        }

        function hasAuthority(authority) {
            if (!_authenticated) {
                return $q.when(false);
            }

            return this.identity().then(function(_id) {
                return _id.authorities && _id.authorities.indexOf(authority) !== -1;
            }, function() {
                return false;
            });
        }

        function identity(force) {
            var deferred = $q.defer();

            if (force === true) {
                _identity = undefined;
            }

            // check and see if we have retrieved the identity data from the server.
            // if we have, reuse it by immediately resolving
            if (angular.isDefined(_identity)) {
                deferred.resolve(_identity);

                return deferred.promise;
            }

            // retrieve the identity data from the server,
            // update the identity object, and then resolve.
            _account.get().$promise
                .then(function(account) {
                    _identity = account.data;
                    _authenticated = true;
                    deferred.resolve(_identity);
                })
                .catch(function() {
                    _identity = null;
                    _authenticated = false;
                    deferred.resolve(_identity);
                });
            return deferred.promise;
        }

        function isAuthenticated() {
            return _authenticated;
        }

        function isIdentityResolved() {
            return angular.isDefined(_identity);
        }
    }
})();