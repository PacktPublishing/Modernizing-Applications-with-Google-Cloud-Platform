angular.module('component.accountDetail').component('accountDetail', {
	templateUrl: 'component/account-detail/template.html',
	controller: ['Auth', '$routeParams', '$rootScope', 'Account', function AccountDetailController(Auth, $routeParams, $rootScope, Account) {
		var self = this;
		self.accountId = $routeParams.accountId;
		Auth.$getAuth().getIdToken().then(function(token) {
			Account.setToken(token);
			self.account = Account.getUnique($routeParams.accountId, function() {
				self.transactions = Account.getAllTransactions(self.accountId);
			});
			$rootScope.$apply();
		})
	}]
});