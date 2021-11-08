angular.module('component.changePassword').component('changePassword', {
	templateUrl: 'component/change/template.html',
	controller: ['Auth', '$location', '$timeout', '$rootScope', '$translate', '$window', function ChangePasswordController(Auth, $location, $timeout, $rootScope, $translate, $window) {
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
					$translate('change.error.general').then(function (translation) {
						self.currentPasswordError = translation;
						$rootScope.$apply();
					});
				})
			}).catch(function() {
				$translate('change.error.credentials').then(function (translation) {
					self.currentPasswordError = translation;
					$rootScope.$apply();
				});
			});
		};
		
		self.cancel = function() {
			$window.history.back();
		}
	}]
})