var app = angular.module('app', [
	'ngRoute',
	'ngCookies',
	'ngSanitize',
	'pascalprecht.translate',
	'ui.bootstrap',
	'firebase',

	'component.accountDeposit',
	'component.accountDetail',
	'component.accountOpen',
	'component.accountTable',
	'component.accountTransfer',
	'component.accountWithdraw',

	'component.authenticate',
	'component.changePassword',
	'component.signin',
	'component.signup',
	'component.reset',
	'component.verifyEmail',
	
	'controller.locale'
]);

app.factory("Auth", ["$firebaseAuth", 
	function($firebaseAuth) {
		return $firebaseAuth();
	}
]);