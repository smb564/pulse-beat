(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('CompanyAdminDeleteController',CompanyAdminDeleteController);

    CompanyAdminDeleteController.$inject = ['$uibModalInstance', 'entity', 'CompanyAdmin'];

    function CompanyAdminDeleteController($uibModalInstance, entity, CompanyAdmin) {
        var vm = this;

        vm.companyAdmin = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CompanyAdmin.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
