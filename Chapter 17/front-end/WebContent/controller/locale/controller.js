angular.module('controller.locale').controller('locale', ['$scope', 'Auth', '$location', '$translate', '$window', 'User', function LocaleController($scope, Auth, $location, $translate, $window, User) {
	var self = this;
	Auth.$onAuthStateChanged(function(firebaseUser) {
		// All navigation depends on the locale setting so login/out navigation happens here
		if(firebaseUser) {
			Auth.$getAuth().getIdToken().then(function(token) {
				User.setToken(token).getPreference(function(preference) {
					$translate.use(preference.locale);
					if(firebaseUser.emailVerified == true) {
						$location.path('/accounts');
					}
					else {
						$location.path('/verify');
					}
				});
			});
		}
		else {
			$location.path('/');
		}
	});

	$scope.changeLocale = function(locale) {
		$translate.use(locale);
	};

	var locale = $window.navigator.languages 
		? $window.navigator.languages[0] 
		: ($window.navigator.language || $window.navigator.userLanguage);
	locale = locale + "";
	locale = locale.replace("-", "_");

	// correct mismatch between browser locale and java locale
	if(locale == "en_GB") {
		locale = "en_UK";
	}

	$translate.use(locale);
}]);