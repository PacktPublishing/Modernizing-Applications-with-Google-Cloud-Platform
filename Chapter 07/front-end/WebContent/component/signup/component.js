angular.module('component.signup').component('signup', {
	templateUrl: 'component/signup/template.html',
	controller: ['Auth', '$location', '$timeout', "$rootScope", function SignupController(Auth, $location, $timeout, $rootScope) {
		var self = this;
		self.authenticating = false;

		self.strongPattern = "^((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!'\"@#\\$%\\^&\\*\\(\\)\\-=_\\+\\[\\]\\{\\},.\\/\\|<>\\?`¬¦]).{8,})";
		
		$timeout(function() {
			jQuery('#signup').validator();
			$rootScope.$apply();
		}, 100);

		self.signUp = function() {
			self.authenticating = true;

			Auth.$createUserWithEmailAndPassword(self.email, self.password)
			.then(function() {
				self.authenticating = false;
				$location.path('/verify');
				$rootScope.$apply();
			})
			.catch(function() {
				self.authenticating = false;
				self.userError = "Unable to Register this User";
				$rootScope.$apply();
			});
		}
	}]
})