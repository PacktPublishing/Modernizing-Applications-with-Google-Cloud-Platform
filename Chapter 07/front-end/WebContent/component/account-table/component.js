angular.
	module('component.accountTable').
	component('accountTable', {
	templateUrl: 'component/account-table/template.html',
	controller: ['Auth', '$rootScope', 'Account', function AccountTableController(Auth, $rootScope, Account) {
		var self = this;

		Auth.$getAuth().getIdToken().then(function(token) {
			Account.setToken(token);
			self.accounts = Account.getAll();
			$rootScope.$apply();
		})
		
		self.open = function() {
			
		}
	}]
})