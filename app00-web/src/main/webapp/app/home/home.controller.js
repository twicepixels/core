(function() {
    'use strict';
    angular.module('twicepixelsApp').controller('homeController', homeController);
    homeController.$inject = ['$scope', 'Principal', 'loginService'];

    function homeController ($scope, _principal, loginService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = loginService.open;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            _principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = _principal.isAuthenticated;
            });
        }
    }
})();