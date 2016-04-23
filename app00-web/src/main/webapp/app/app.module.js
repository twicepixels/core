(function() {
    'use strict';
        angular.module('twicepixelsApp', [
            'ui.router',
            'ui.bootstrap',
            'ngStorage',
            'ngCookies',
            'ngResource',
            'ngCacheBuster',
            'tmh.dynamicLocale',
            'pascalprecht.translate'
        ])
        .run(run);
    run.$inject = [ 'stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();