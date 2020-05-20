angular.module('service.account').
	service('Account', ['$resource', function($resource) {
		var token = null;
		var account = null;
		var withdrawal = null;
		var deposit = null;
		var transaction = null;
		var self = this;
		this.setToken = function(newToken) {
			if(token !== newToken) {
				token = newToken;
				account = $resource('account/:accountId', {accountId: '@accountId'}, {
					getAll: {
						method: 'GET',
						isArray: true,
						headers: {
							'Authorization': 'Bearer ' + token
						}
					},
					getUnique: {
						method: 'GET',
						params: {accountId: '@accountId'},
						isArray: false,
						headers: {
							'Authorization': 'Bearer ' + token
						}
					},
					openAccount: {
						method: 'POST',
						headers: {
							'Authorization': 'Bearer ' + token,
							'Content-Type': 'application/json'
						}
					},
				});
				withdrawal = $resource('account/:accountId/withdrawal', {accountId: '@accountId'}, {
					withdrawFunds: {
						method: 'POST',
						params: {accountId: '@accountId'},
						headers: {
							'Authorization': 'Bearer ' + token,
							'Content-Type': 'application/json'
						}
					}
				});
				deposit = $resource('account/:accountId/deposit', {accountId: '@accountId'}, {
					depositFunds: {
						method: 'POST',
						params: {accountId: '@accountId'},
						headers: {
							'Content-Type': 'application/json',
							'Authorization': 'Bearer ' + token,
						}
					}
				});
				transaction = $resource('account/:accountId/transaction/:transactionId', {accountId: '@accountId', transactionId: '@transactionId'}, {
					getAll: {
						method: 'GET',
						params: {accountId: '@accountId'},
						isArray: true,
						headers: {
							'Authorization': 'Bearer ' + token
						}
					},
					getUnique: {
						method: 'GET',
						params: {accountId: '@accountId', transactionId: '@transactionId'},
						isArray: false,
						headers: {
							'Authorization': 'Bearer ' + token
						}
					}
				});
			};
			return self;
		};

		this.depositFunds = function(accountId, transaction, success, error) {
			return deposit.depositFunds({accountId: accountId}, transaction, success, error);
		}

		this.getAll = function(success, error) {
			return account.getAll(success, error);
		};
		
		this.getUniqueTransaction = function(accountId, transactionId, success, error) {
			return transaction.getUnique({accountId: accountId, transactionId: transactionId}, success, error);
		}
		
		this.getAllTransactions = function(accountId, success, error) {
			return transaction.getAll({accountId: accountId}, success, error);
		}

		this.getUnique = function(accountId, success, error) {
			return account.getUnique({accountId: accountId}, success, error);
		};
	
		this.openAccount = function(accountName, success, error) {
			return account.openAccount(accountName, success, error);
		};

		this.withdrawFunds = function(accountId, transaction, success, error) {
			return withdrawal.withdrawFunds({accountId: accountId}, transaction, success, error);
		}
	}
]);