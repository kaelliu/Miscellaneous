/**
 * Created by kael on 14-5-24.
 */
// Managing the poll list
function PollListCtrl($scope) {
    $scope.polls = [{_id:1,question:'is big bro ao awesome?'}];
}
// Voting / viewing poll results
function PollItemCtrl($scope, $routeParams) {
    $scope.poll = {};
    $scope.vote = function() {};
}
// Creating a new poll
function PollNewCtrl($scope) {
    $scope.poll = {
        question: '',
        choices: [{ text: '' }, { text: '' }, { text: '' }]
    };
    $scope.addChoice = function() {
        $scope.poll.choices.push({ text: '' });
    };
    $scope.createPoll = function() {};
}