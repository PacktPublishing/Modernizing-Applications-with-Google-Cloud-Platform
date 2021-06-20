angular.module('component.signup').component('signup', {
	templateUrl: 'component/signup/template.html',
	controller: ['Auth', '$location', '$timeout', "$rootScope", '$translate', function SignupController(Auth, $location, $timeout, $rootScope, $translate) {
		var self = this;
		self.authenticating = false;

		self.strongPattern = "^((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!'\"@#\\$%\\^&\\*\\(\\)\\-=_\\+\\[\\]\\{\\},.\\/\\|<>\\?`¬¦]).{8,})";
		
		$timeout(function() {
			jQuery('#signup').validator();
			$rootScope.$apply();
		}, 100);

		self.signUp = function() {
			self.authenticating = true;

			Auth.$createUserWithEmailAndPassword(self.email, self.password).then(function(user) {
				self.authenticating = false;
			})
			.catch(function() {
				self.authenticating = false;
				$translate('signup.error').then(function (translation) {
					self.userError = translation;
				});
			});
		}

		self.cancel = function() {
			$location.path('/');
		}
	}]
})