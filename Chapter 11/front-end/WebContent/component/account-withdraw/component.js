angular.module('component.accountWithdraw').component('accountWithdraw', {
	templateUrl: 'component/account-withdraw/template.html',
	controller: ['Auth', '$location', '$timeout', '$routeParams', '$rootScope', 'Account', function AccountWithdrawController(Auth, $location, $timeout, $routeParams, $rootScope, Account) {
		var self = this;

		$timeout(function() {
			jQuery('#withdraw').validator();
			$rootScope.$apply();
		}, 100);

		self.accountId = $routeParams.accountId;

		Auth.$getAuth().getIdToken().then(function(token) {
			self.account = Account.setToken(token).getUnique(self.accountId);
		})
		
		self.withdraw = function() {
			Auth.$getAuth().getIdToken().then(function(token) {
				self.account = Account.setToken(token).withdrawFunds(self.accountId, { amount: self.amount, description: self.description }, function() {
					$location.path('/accounts/' + self.accountId);
				});
			})
		}
		
		self.cancel = function() {
			$location.path('/accounts/' + self.accountId);
		}
	}]
});