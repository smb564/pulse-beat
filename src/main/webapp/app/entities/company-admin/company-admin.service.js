(function() {
    'use strict';
    angular
        .module('pulsebeatApp')
        .factory('CompanyAdmin', CompanyAdmin);

    CompanyAdmin.$inject = ['$resource'];

    function CompanyAdmin ($resource) {
        var resourceUrl =  'api/company-admins/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
