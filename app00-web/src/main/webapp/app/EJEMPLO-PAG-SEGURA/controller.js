(function() {
    'use strict';
        angular.module('twicepixelsApp').controller('EJEMPLOCONTROLLER', secureController);
        secureController.$inject = ['$scope'];

        function secureController ($scope) {
            var vm = this;
        }
})();