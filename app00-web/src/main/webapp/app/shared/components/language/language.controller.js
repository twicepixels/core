(function() {
    'use strict';

    angular.module('twicepixelsApp').controller('twpLanguageController', twpLanguageController);
    twpLanguageController.$inject = ['$translate', 'twpLanguageService', 'tmhDynamicLocale' ];

    function twpLanguageController($translate, twpLanguageService , tmhDynamicLocale ) {
        var vm = this;
        vm.languages = null;
        vm.languageKey = null;
        vm.changeLanguage = changeLanguage;

        twpLanguageService.getCurrent().then(function(key) {
            vm.languageKey = key;
        });

        twpLanguageService.getAll().then(function(languages) {
            vm.languages = languages;
        });
        function changeLanguage(languageKey) {
            vm.languageKey = languageKey;
            $translate.use(languageKey);
            tmhDynamicLocale.set(languageKey);
        }
    }
})();