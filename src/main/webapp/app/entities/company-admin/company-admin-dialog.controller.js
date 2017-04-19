(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('CompanyAdminDialogController', CompanyAdminDialogController);

    CompanyAdminDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CompanyAdmin'];

    function CompanyAdminDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CompanyAdmin) {
        var vm = this;

        vm.companyAdmin = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.companyAdmin.id !== null) {
                CompanyAdmin.update(vm.companyAdmin, onSaveSuccess, onSaveError);
            } else {
                CompanyAdmin.save(vm.companyAdmin, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pulsebeatApp:companyAdminUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
