angular.module('component.accountDeposit').component('accountDeposit', {
	templateUrl: 'component/account-deposit/template.html',
	controller: ['Auth', '$location', '$timeout', '$routeParams', '$rootScope', 'Account', function AccountDetailController(Auth, $location, $timeout, $routeParams, $rootScope, Account) {
		var self = this;

		$timeout(function() {
			jQuery('#deposit').validator();
			$rootScope.$apply();
		}, 100);

		self.accountId = $routeParams.accountId;

		Auth.$getAuth().getIdToken().then(function(token) {
			Account.setToken(token);
			self.account = Account.getUnique(self.accountId);
			$rootScope.$apply();
		})
		
		self.deposit = function() {
			Auth.$getAuth().getIdToken().then(function(token) {
				Account.setToken(token);
				self.account = Account.depositFunds(self.accountId, { amount: self.amount, description: self.description });
				$location.path('/accounts/' + self.accountId);
				$rootScope.$apply();
			})
		}
	}]
});