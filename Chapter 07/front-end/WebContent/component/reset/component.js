angular.module('component.reset').component('reset', {
	templateUrl: 'component/reset/template.html',
	controller: ['Auth', '$location', '$timeout', '$rootScope', function ResetController(Auth, $location, $timeout, $rootScope) {
		var self = this;
		
		$timeout(function() {
			jQuery('#reset').validator();
			$rootScope.$apply();
		}, 100);

		self.reset = function() {
			Auth.$sendPasswordResetEmail(self.email).then(function() {
				self.message = "Password Reset Email Sent"
				$rootScope.$apply();
			}).catch(function() {
				self.error = "Error Sending Password Reset Email"
				$rootScope.$apply();
			});
		}
	}]
})