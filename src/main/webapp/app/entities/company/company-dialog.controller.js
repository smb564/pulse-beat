(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('CompanyDialogController', CompanyDialogController);

    CompanyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Company'];

    function CompanyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Company) {
        var vm = this;

        vm.company = entity;
        vm.clear = clear;
        vm.save = save;

        if (typeof vm.company.departments==="undefined"){
            vm.company.departments = [];
        }

        vm.addDepartment = function(){
            vm.company.departments.push(vm.department);
            vm.department = "";
        };

        vm.removeDepartment = function(index){
            vm.company.departments = vm.company.departments.splice(index,1);
        };

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.company.id !== null) {
                Company.update(vm.company, onSaveSuccess, onSaveError);
            } else {
                Company.save(vm.company, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pulsebeatApp:companyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
