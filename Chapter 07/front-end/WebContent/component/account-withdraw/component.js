angular.module('component.accountWithdraw').component('accountWithdraw', {
	templateUrl: 'component/account-withdraw/template.html',
	controller: ['Auth', '$location', '$timeout', '$routeParams', '$rootScope', 'Account', function AccountDetailController(Auth, $location, $timeout, $routeParams, $rootScope, Account) {
		var self = this;

		$timeout(function() {
			jQuery('#withdraw').validator();
			$rootScope.$apply();
		}, 100);

		self.accountId = $routeParams.accountId;

		Auth.$getAuth().getIdToken().then(function(token) {
			Account.setToken(token);
			self.account = Account.getUnique(self.accountId);
			$rootScope.$apply();
		})
		
		self.withdraw = function() {
			Auth.$getAuth().getIdToken().then(function(token) {
				Account.setToken(token);
				self.account = Account.withdrawFunds(self.accountId, { amount: self.amount, description: self.description });
				$location.path('/accounts/' + self.accountId);
				$rootScope.$apply();
			})
		}
	}]
});