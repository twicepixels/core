(function() {
    'use strict';

    angular.module('twicepixelsApp').controller('navbarController', navbarController);
    navbarController.$inject = ['$location', '$state', 'ENV', 'loginService','Auth', 'Principal'];

    function navbarController ($location, $state,
        ENV, loginService, _auth, _principal) {
        var vm = this;

        vm.login = login;
        vm.logout = logout;
        vm.$state = $state;
        vm.navCollapsed = true;
        vm.inProduction = ENV === 'prod';
        vm.isAuthenticated = _principal.isAuthenticated;

        function login () {
            loginService.open();
        }

        function logout () {
            _auth.logout();
            $state.go('home');
        }
    }
})();
