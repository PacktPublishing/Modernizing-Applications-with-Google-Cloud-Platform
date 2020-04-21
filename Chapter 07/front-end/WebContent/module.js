var app = angular.module('app', [
	'ngRoute',
	'ui.bootstrap',
	'firebase',

	'component.accountDeposit',
	'component.accountDetail',
	'component.accountOpen',
	'component.accountTable',
	'component.accountWithdraw',
	'component.authenticate',
	'component.changePassword',
	'component.signin',
	'component.signup',
	'component.reset',
	'component.verifyEmail'
]);

app.factory("Auth", ["$firebaseAuth", 
	function($firebaseAuth) {
		return $firebaseAuth();
	}
]);