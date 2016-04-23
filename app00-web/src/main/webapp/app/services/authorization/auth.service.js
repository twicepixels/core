
(function() {
    'use strict';

    angular.module('twicepixelsApp').factory('Auth', Auth);
    Auth.$inject = ['$rootScope', '$state', '$q', '$translate',
        'loginService', 'Principal', 'AuthProvider', 'Account'
    ];
    //        'Register', 'Activate', 'Password', 'PasswordResetInit', 'PasswordResetFinish'

    function Auth($rootScope, $state, $q, $translate,
        loginService, _principal, _authProvider, _account) {

        var service = {
            login: login,
            logout: logout,
            authorize: authorize,
//            createAccount: createAccount,
//            updateAccount: updateAccount
//            activateAccount: activateAccount,
//            changePassword: changePassword,
//            resetPasswordInit: resetPasswordInit,
//            resetPasswordFinish: resetPasswordFinish,
        };

        return service;

        function login(credentials, callback) {
            var cb = callback || angular.noop;
            var deferred = $q.defer();

            _authProvider.login(credentials)
                .then(loginThen)
                .catch(function(err) {
                    this.logout();
                    deferred.reject(err);
                    return cb(err);
                }.bind(this));

            function loginThen(data) {
                _principal.identity(true).then(function(account) {
                    // After the login the language will be changed to
                    // the language selected by the user during his registration
                    if (account !== null) {
                        $translate.use(account.langKey).then(function() {
                            $translate.refresh();
                        });
                    }
                    deferred.resolve(data);
                });
                return cb();
            }

            return deferred.promise;
        }

        function logout() {
            _authProvider.logout();
            _principal.authenticate(null);

            // Reset state memory if not redirected
            if (!$rootScope.redirected) {
                $rootScope.previousStateName = undefined;
                $rootScope.previousStateNameParams = undefined;
            }
        }

        function authorize(force) {
            var authReturn = _principal.identity(force).then(authThen);

            return authReturn;

            function authThen() {
                var isAuthenticated = _principal.isAuthenticated();

                // an authenticated user can't access to login and register pages
                if (isAuthenticated && $rootScope.toState.parent === 'account' &&
                    ($rootScope.toState.name === 'login' || $rootScope.toState.name === 'register')) {
                    $state.go('home');
                }

                if ($rootScope.toState.data.authorities && $rootScope.toState.data.authorities.length > 0 &&
                    !_principal.hasAnyAuthority($rootScope.toState.data.authorities)) {
                    if (isAuthenticated) {
                        // user is signed in but not authorized for desired state
                        $state.go('accessdenied');
                    } else {
                        // user is not authenticated. stow the state they wanted before you
                        // send them to the login service, so you can return them when you're done
                        $rootScope.redirected = true;
                        $rootScope.previousStateName = $rootScope.toState;
                        $rootScope.previousStateNameParams = $rootScope.toStateParams;

                        // now, send them to the signin state so they can log in
                        $state.go('accessdenied');
                        loginService.open();
                    }
                }
            }
        }

//        function updateAccount(account, callback) {
//            var cb = callback || angular.noop;
//            return _account.save(account,
//                function() {
//                    return cb(account);
//                },
//                function(err) {
//                    return cb(err);
//                }.bind(this)).$promise;
//        }
//
//        function activateAccount(key, callback) {
//            var cb = callback || angular.noop;
//
//            return Activate.get(key,
//                function(response) {
//                    return cb(response);
//                },
//                function(err) {
//                    return cb(err);
//                }.bind(this)).$promise;
//        }
//
//        function changePassword(newPassword, callback) {
//            var cb = callback || angular.noop;
//
//            return Password.save(newPassword, function() {
//                return cb();
//            }, function(err) {
//                return cb(err);
//            }).$promise;
//        }
//
//        function createAccount(account, callback) {
//            var cb = callback || angular.noop;
//
//            return Register.save(account,
//                function() {
//                    return cb(account);
//                },
//                function(err) {
//                    this.logout();
//                    return cb(err);
//                }.bind(this)).$promise;
//        }
//        function resetPasswordFinish(keyAndPassword, callback) {
//            var cb = callback || angular.noop;
//
//            return PasswordResetFinish.save(keyAndPassword, function() {
//                return cb();
//            }, function(err) {
//                return cb(err);
//            }).$promise;
//        }
//
//        function resetPasswordInit(mail, callback) {
//            var cb = callback || angular.noop;
//
//            return PasswordResetInit.save(mail, function() {
//                return cb();
//            }, function(err) {
//                return cb(err);
//            }).$promise;
//        }
    }
})();