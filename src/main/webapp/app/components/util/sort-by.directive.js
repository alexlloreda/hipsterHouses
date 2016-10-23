(function() {
    'use strict';

    angular
        .module('hipsterHousesApp')
        .directive('jhSortBy', jhSortBy);

    function jhSortBy() {
        var directive = {
            restrict: 'A',
            scope: false,
            require: '^jhSort',
            link: linkFunc
        };

        return directive;

        function linkFunc(scope, element, attrs, parentCtrl) {
            element.bind('click', function () {
                parentCtrl.sort(attrs.jhSortBy);
            });
        }
    }
})();
