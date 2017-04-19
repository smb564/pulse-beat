(function() {
    'use strict';

    angular
        .module('pulsebeatApp')
        .controller('ManagerDetailController', ManagerDetailController);

    ManagerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Manager'];

    function ManagerDetailController($scope, $rootScope, $stateParams, previousState, entity, Manager) {
        var vm = this;

        vm.manager = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('pulsebeatApp:managerUpdate', function(event, result) {
            vm.manager = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
