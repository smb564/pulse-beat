(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('CompanyAdminDetailController', CompanyAdminDetailController);

    CompanyAdminDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CompanyAdmin', 'User', 'Company'];

    function CompanyAdminDetailController($scope, $rootScope, $stateParams, previousState, entity, CompanyAdmin, User, Company) {
        var vm = this;

        vm.companyAdmin = entity;
        vm.previousState = previousState.name;
        getCompany(vm.companyAdmin.companyId);
        getUserInfo(vm.companyAdmin.userId);

        var unsubscribe = $rootScope.$on('pulsebeatApp:companyAdminUpdate', function(event, result) {
            vm.companyAdmin = result;
        });

        // functions to get company and user details
        function getCompany(companyId){
            vm.company = Company.get({'id' : companyId});
        }

        function getUserInfo(userLogin){
            vm.user = User.get({'login' : userLogin});
        }

        $scope.$on('$destroy', unsubscribe);
    }
})();
