(function() {
    'use strict';

    angular.module('twicepixelsApp').config(stateConfig);
    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('app', {
            abstract: true,
            views: {
                'navbar@': {
                    templateUrl: 'app/shared/templates/navbar/navbar.html',
                    controller: 'navbarController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                authorize: ['Auth', function(_auth) {
                    return _auth.authorize();
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader',
                    function($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                    }
                ]
            }
        });
    }
})();