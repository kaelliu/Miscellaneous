/**
 * Created by kael on 14-5-24.
 * route for login page
 */
angular.module('polls',[]).config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/list', { templateUrl: 'partials/list.html', controller:
            PollListCtrl }).
        when('/poll/:pollId', { templateUrl: 'partials/item.html', controller:
            PollItemCtrl }).
        when('/new', { templateUrl: 'partials/new.html', controller:
            PollNewCtrl }).
        otherwise({ redirectTo: '/list' });// default module.view,controller when you route from login page,see detail in login.jade
}]);