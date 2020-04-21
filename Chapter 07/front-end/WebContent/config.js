angular.module('app').
	run(["$rootScope", "$location", function($rootScope, $location) {
		$rootScope.$on("$routeChangeError", function(event, next, previous, error) {
			if (error === "AUTH_REQUIRED") {
				$location.path("/");
			}
		});
	}
]);

angular.module('app').
	config(['$routeProvider', function config($routeProvider) {
		$routeProvider.
		when('/', {
			template: '<signin></signin>'
		}).
		when('/signup', {
			template: '<signup></signup>'
		}).
		when('/reset', {
			template: '<reset></reset>'
		}).
		when('/verify', {
			template: '<verify-email></verify-email>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$requireSignIn();
				}]
			}
		}).
		when('/change', {
			template: '<change-password></change-password>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$requireSignIn(true);
				}]
			}
		}).
		when('/accounts', {
			template: '<account-table></account-table>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$requireSignIn(true);
				}]
			}
		}).
		when('/accounts/open', {
			template: '<account-open></account-open>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$requireSignIn(true);
				}]
			}
		}).
		when('/accounts/:accountId', {
			template: '<account-detail></account-detail>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$requireSignIn(true);
				}]
			}
		}).
		when('/accounts/:accountId/deposit', {
			template: '<account-deposit></account-deposit>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$requireSignIn(true);
				}]
			}
		}).
		when('/accounts/:accountId/withdraw', {
			template: '<account-withdraw></account-withdraw>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$requireSignIn(true);
				}]
			}
		}).
		when('/__/auth/action', { //?mode=:action&oobCode=:code', {
			template: '<authenticate></authenticate>'
		}).
		otherwise('/');
	}
]);