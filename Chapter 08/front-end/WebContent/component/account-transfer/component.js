angular.module('component.accountTransfer').component('accountTransfer', {
	templateUrl: 'component/account-transfer/template.html',
	controller: ['Auth', '$location', '$timeout', '$routeParams', '$rootScope', 'Account', '$translate', '$log', function AccountTransferController(Auth, $location, $timeout, $routeParams, $rootScope, Account, $translate, $log) {
		var self = this;

		$timeout(function() {
			jQuery('#transfer').validator();
			$rootScope.$apply();
		}, 100);

		self.accountId = $routeParams.accountId;

		Auth.$getAuth().getIdToken().then(function(token) {
			self.account = Account.setToken(token).getUnique(self.accountId);
			self.accounts = Account.setToken(token).getAll();
		})
		
		self.withdraw = function() {
			$log.log("Other account ID: " + self.otherAccountId)
			if(self.otherAccountId == null) {
				$translate('transfer.account.errors.select').then(function(translation) {
					self.error = translation;
				})
				return;
			}

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