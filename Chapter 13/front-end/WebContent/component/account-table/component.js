angular.
	module('component.accountTable').
	component('accountTable', {
	templateUrl: 'component/account-table/template.html',
	controller: ['Auth', 'Account', function AccountTableController(Auth, Account) {
		var self = this;

		Auth.$getAuth().getIdToken().then(function(token) {
			self.accounts = Account.setToken(token).getAll();
		})
	}]
})