angular.module('component.changePassword').component('changePassword', {
	templateUrl: 'component/change/template.html',
	controller: ['Auth', '$location', '$timeout', '$rootScope', function ChangePasswordController(Auth, $location, $timeout, $rootScope) {
		var self = this;
		self.strongPattern = "^((?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!'\"@#\\$%\\^&\\*\\(\\)\\-=_\\+\\[\\]\\{\\},.\\/\\|<>\\?`¬¦]).{8,})";
		
		$timeout(function() {
			jQuery('#change').validator();
			$rootScope.$apply();
		}, 100);
		
		self.change = function() {
			var firebaseUser = Auth.$getAuth();
			var credential = firebase.auth.EmailAuthProvider.credential(
					firebaseUser.email,
					self.currentPassword
			);

			firebaseUser.reauthenticateWithCredential(credential).then(function() {
				firebaseUser.updatePassword(self.newPassword).then(function() {
					$location.path('/accounts');
					$rootScope.$apply();
				}).catch(function() {
					self.currentPasswordError = "Something went wrong";
					$rootScope.$apply();
				})
			}).catch(function() {
				self.currentPasswordError = "Invalid credentials";
				$rootScope.$apply();
			});
		};
	}]
})