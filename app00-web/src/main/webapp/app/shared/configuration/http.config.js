(function() {
    'use strict';

    angular.module('twicepixelsApp').config(httpConfig);

    httpConfig.$inject = ['$urlRouterProvider', '$httpProvider', '$locationProvider',
        'httpRequestInterceptorCacheBusterProvider', '$urlMatcherFactoryProvider'];

    function httpConfig($urlRouterProvider, $httpProvider, $locationProvider,
        httpRequestInterceptorCacheBusterProvider, $urlMatcherFactoryProvider) {

        $locationProvider.html5Mode(true);
        
        //enable CSRF
//        $httpProvider.defaults.xsrfCookieName = 'XSRF-TOKEN';
//        $httpProvider.defaults.xsrfHeaderName = 'X-XSRF-TOKEN';
        
        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider
            .setMatchlist([/.*api.*/, /.*protected.*/], true);

        // the known route
        $urlRouterProvider.when('', '/');
        // For any unmatched url, send to 404
        $urlRouterProvider.otherwise('/not-found');

        $httpProvider.interceptors.push('errorInterceptor');
        $httpProvider.interceptors.push('authStartInterceptor');
        $httpProvider.interceptors.push('authExpiredInterceptor');
//        $httpProvider.interceptors.push('notificationInterceptor');

        $urlMatcherFactoryProvider.type('boolean', {
            name : 'boolean',
            decode: function(val) { return val === true || val === 'true'; },
            encode: function(val) { return val ? 1 : 0; },
            equals: function(a, b) { return this.is(a) && a === b; },
            is: function(val) { return [true,false,0,1].indexOf(val) >= 0; },
            pattern: /bool|true|0|1/
        });
    }
})();
