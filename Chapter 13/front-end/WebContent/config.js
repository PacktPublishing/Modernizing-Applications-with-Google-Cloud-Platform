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
	config(['$routeProvider', '$translateProvider', function config($routeProvider, $translateProvider) {
		$routeProvider.
		when('/reset', {
			template: '<reset></reset>'
		}).
		when('/', {
			template: '<signin></signin>'
		}).
		when('/signup', {
			template: '<signup></signup>'
		}).
		when('/__/auth/action', {
			template: '<authenticate></authenticate>'
		}).
		when('/verify', {
			template: '<verify-email></verify-email>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$requireSignIn();
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
		when('/accounts/:accountId/transfer', {
			template: '<account-transfer></account-transfer>',
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
		when('/change', {
			template: '<change-password></change-password>',
			resolve: {
				"currentAuth": ["Auth", function(Auth) {
					return Auth.$requireSignIn(true);
				}]
			}
		}).
		otherwise('/');

		$translateProvider.useSanitizeValueStrategy('sanitize');
		$translateProvider.useStaticFilesLoader({
			prefix: 'i18n/messages_',
			suffix: '.json'
		});
		$translateProvider.registerAvailableLanguageKeys(['en', 'en_UK', 'en_CA', 'en_US'], {
	        'en_*': 'en',
	        '*': 'en'
	     });
	    $translateProvider.preferredLanguage("en_UK");
	    $translateProvider.fallbackLanguage("en");
	    $translateProvider.useLocalStorage();
	}
]);