angular.module('component.accountDeposit').component('accountDeposit', {
	templateUrl: 'component/account-deposit/template.html',
	controller: ['Auth', '$location', '$timeout', '$routeParams', '$rootScope', 'Account', function AccountDepositController(Auth, $location, $timeout, $routeParams, $rootScope, Account) {
		var self = this;

		$timeout(function() {
			jQuery('#deposit').validator();
			$rootScope.$apply();
		}, 100);

		self.accountId = $routeParams.accountId;

		Auth.$getAuth().getIdToken().then(function(token) {
			self.account = Account.setToken(token).getUnique(self.accountId);
		})
		
		self.deposit = function() {
			Auth.$getAuth().getIdToken().then(function(token) {
				self.account = Account.setToken(token).depositFunds(self.accountId, { amount: self.amount, description: self.description }, function() {
					$location.path('/accounts/' + self.accountId);
				});
			})
		}
		
		self.cancel = function() {
			$location.path('/accounts/' + self.accountId);
		}
	}]
});