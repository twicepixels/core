(function() {
    'use strict';

    angular.module('twicepixelsApp').factory('loginService', loginService);
    loginService.$inject = ['$uibModal'];

    function loginService($uibModal) {
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function() {
            modalInstance = null;
        };
        return service;

        function open() {
            if (modalInstance !== null) return;
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/shared/templates/login/login.html',
                controller: 'loginController',
                controllerAs: 'vm',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader',
                        function($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('login');
                            return $translate.refresh();
                        }
                    ]
                }
            });
            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();