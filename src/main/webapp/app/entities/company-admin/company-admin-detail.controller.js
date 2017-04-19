(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('CompanyAdminDetailController', CompanyAdminDetailController);

    CompanyAdminDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CompanyAdmin'];

    function CompanyAdminDetailController($scope, $rootScope, $stateParams, previousState, entity, CompanyAdmin) {
        var vm = this;

        vm.companyAdmin = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pulsebeatApp:companyAdminUpdate', function(event, result) {
            vm.companyAdmin = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
