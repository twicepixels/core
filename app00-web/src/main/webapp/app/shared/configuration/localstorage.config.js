(function() {
    'use strict';

    angular.module('twicepixelsApp').config(localStorageConfig);
    localStorageConfig.$inject = ['$localStorageProvider'];

    function localStorageConfig($localStorageProvider) {
        $localStorageProvider.setKeyPrefix('twp_');
    }
})();
