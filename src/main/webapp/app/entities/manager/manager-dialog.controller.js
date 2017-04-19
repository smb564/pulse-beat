(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('ManagerDialogController', ManagerDialogController);

    ManagerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Manager'];

    function ManagerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Manager) {
        var vm = this;

        vm.manager = entity;
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
            if (vm.manager.id !== null) {
                Manager.update(vm.manager, onSaveSuccess, onSaveError);
            } else {
                Manager.save(vm.manager, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('pulsebeatApp:managerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
