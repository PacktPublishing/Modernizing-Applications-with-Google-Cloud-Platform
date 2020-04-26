angular.module('component.accountDetail').component('accountDetail', {
	templateUrl: 'component/account-detail/template.html',
	controller: ['Auth', '$routeParams', 'Account', function AccountDetailController(Auth, $routeParams, Account) {
		var self = this;
		self.accountId = $routeParams.accountId;

		Auth.$getAuth().getIdToken().then(function(token) {
			self.account = Account.setToken(token).getUnique($routeParams.accountId, function() {
				self.transactions = Account.getAllTransactions(self.accountId);
			});
		});

		self.css_class = function(transaction) {
			if(transaction.correction == true) {
				return "warning";
			}
			if(self.accountId != transaction.referenceAccountId) {
				return "info";
			}
			return "";
		};
	}]
});