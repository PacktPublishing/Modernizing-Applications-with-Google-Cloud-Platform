angular.module('component.accountOpen').component('accountOpen', {
	templateUrl: 'component/account-open/template.html',
	controller: ['Auth', '$location', '$timeout', '$rootScope', 'Account', function AccountOpenController(Auth, $location, $timeout, $rootScope, Account) {
		var self = this;
		
		$timeout(function() {
			jQuery('#openAccount').validator();
			$rootScope.$apply();
		}, 100);
		
		self.openAccount = function() {
			Auth.$getAuth().getIdToken().then(function(token) {
				Account.setToken(token).openAccount({ name: self.name }, function(result) {
					self.message = result;
					$location.path("/accounts");
				}, function(error) {
					self.message = error;
				});
			})
		}
		
		self.cancel = function() {
			$location.path('/accounts');
		}
	}]
});