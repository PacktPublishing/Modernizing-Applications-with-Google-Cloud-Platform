angular.module('component.authenticate').component('authenticate', {
	templateUrl: 'component/auth/template.html',
	controller: ['Auth', '$location', '$timeout', '$rootScope', function AuthenticateController(Auth, $location, $timeout, $rootScope) {
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
					self.message = "Email Verified, you may now login";
					$rootScope.$apply();
				}).catch(function() {
					self.error = "Invalid or Expired Verification Link";
					$rootScope.$apply();
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
				self.error = "Invalid or Expired Link";
				$rootScope.$apply();
				break;
		}
		
		self.change = function() {
			self.mode = "resetPassword";
			extraAuth.confirmPasswordReset(self.code, self.newPassword).then(function() {
				self.message = "Pasword Reset, you may now login";
				$rootScope.$apply();
			}).catch(function() {
				self.error = "Bad Password or Invalid/Expired Reset Link";
				$rootScope.$apply();
			});
		}
	}]
});