(function() {
    'use strict';

    angular
        .module('twicepixelsApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('prueba-pagina', {
            parent: 'app',
            url: '/prueba-pagina-segura',
            data: {
                authorities: ['ROLE_USER']
            },
            views: {
                'content@': {
                    templateUrl: 'app/EJEMPLO-PAG-SEGURA/test.html',
                    controller: 'EJEMPLOCONTROLLER',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader',
                    function ($translate,$translatePartialLoader) {
                        return $translate.refresh();
                    }]
            }
        });
    }
})();
