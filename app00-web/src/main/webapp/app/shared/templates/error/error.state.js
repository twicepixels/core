(function() {
    'use strict';

    angular.module('twicepixelsApp').config(stateConfig);
    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('error', {
                parent: 'app',
                url: '/error',
                data: {
                    authorities: [],
                    pageTitle: 'error.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/shared/templates/error/error.html'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader',
                        function($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('error');
                            return $translate.refresh();
                        }
                    ]
                }
            })
            .state('accessdenied', {
                parent: 'app',
                url: '/accessdenied',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'app/shared/templates/error/401.html'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader',
                        function($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('error');
                            return $translate.refresh();
                        }
                    ]
                }
            })
            .state('not-found', {
                parent: 'app',
                url: '/not-found',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'app/shared/templates/error/404.html'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader',
                        function($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('error');
                            return $translate.refresh();
                        }
                    ]
                }
            });
    }
})();