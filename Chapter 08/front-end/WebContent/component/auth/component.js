angular.module('component.authenticate').component('authenticate', {
	templateUrl: 'component/auth/template.html',
	controller: ['Auth', '$location', '$timeout', '$rootScope', '$transalate', function AuthenticateController(Auth, $location, $timeout, $rootScope, $translate) {
		var self = this;
		self.mode = $location.search().mode;
		self.code = $location.search().oobCode;
		self.strongPattern = "^((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!'\"@#\\$%\\^&\\*\\(\\)\\-=_\\+\\[\\]\\{\\},.\\/\\|<>\\?`¬¦]).{8,})";

		// exposing raw Firebase Auth object as the AngularFire API is not complete
		// Using Angular rather than AngularJS will remove this issue
		var extraAuth = Auth._._auth; 

		switch (self.mode) {
			case "verifyEmail":
				extraAuth.applyActionCode(self.code).then(function() {
					$translate('auth.messages.verificaion').then(function (translation) {
						self.message = translation;
						$rootScope.$apply();
					});
				}).catch(function() {
					$translate('auth.errors.verificaion').then(function (translation) {
						self.error = translation;
						$rootScope.$apply();
					});
				});
				break;

			case "resetPassword":
				self.mode = "enterNewPassword";
				$timeout(function() {
					jQuery('#change').validator();
					$rootScope.$apply();
				}, 100);
				break;

			default:
				$translate('auth.errors.default').then(function (translation) {
					self.error = translation;
					$rootScope.$apply();
				});
				break;
		}
		
		self.change = function() {
			self.mode = "resetPassword";
			extraAuth.confirmPasswordReset(self.code, self.newPassword).then(function() {
				$translate('auth.messages.reset').then(function (translation) {
					self.message = translation;
					$rootScope.$apply();
				});
			}).catch(function() {
				$translate('auth.errors.reset').then(function (translation) {
					self.error = translation;
					$rootScope.$apply();
				});
			});
		}
	}]
});