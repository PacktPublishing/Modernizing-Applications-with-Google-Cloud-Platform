angular.module('component.accountTransfer').component('accountTransfer', {
	templateUrl: 'component/account-transfer/template.html',
	controller: ['Auth', '$location', '$timeout', '$routeParams', '$rootScope', 'Account', function AccountTransferController(Auth, $location, $timeout, $routeParams, $rootScope, Account) {
		var self = this;

		$timeout(function() {
			var opts = {
				'custom': {
					'select': function ($el) {
						if($el.val() == "? undefined:undefined ?" ) {
							return "failed my select validation";     
						}
					}
				}
			};
			jQuery('#transfer').validator(opts);
			$rootScope.$apply();
		}, 100);

		self.accountId = $routeParams.accountId;

		Auth.$getAuth().getIdToken().then(function(token) {
			self.account = Account.setToken(token).getUnique(self.accountId);
			self.accounts = Account.setToken(token).getAll();
		})
		
		self.withdraw = function() {
			Auth.$getAuth().getIdToken().then(function(token) {
				self.account = Account.setToken(token).withdrawFunds(self.accountId, {
							amount: self.amount,
							description: self.description,
							otherAccount: self.otherAccountId
						}, function() {
					$location.path('/accounts/' + self.accountId);
				});
			})
		}
		
		self.cancel = function() {
			$location.path('/accounts/' + self.accountId);
		}
	}]
});