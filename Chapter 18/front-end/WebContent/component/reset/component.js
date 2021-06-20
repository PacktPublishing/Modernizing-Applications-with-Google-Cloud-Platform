angular.module('component.reset').component('reset', {
	templateUrl: 'component/reset/template.html',
	controller: ['Auth', '$location', '$timeout', '$rootScope', '$translate', function ResetController(Auth, $location, $timeout, $rootScope, $translate) {
		var self = this;
		
		$timeout(function() {
			jQuery('#reset').validator();
			$rootScope.$apply();
		}, 100);

		self.reset = function() {
			Auth.$sendPasswordResetEmail(self.email).then(function() {
				$translate('reset.message').then(function (translation) {
					self.message = translation;
				});
			}).catch(function() {
				$translate('reset.error').then(function (translation) {
					self.error = translation;
				});
			});
		}
		
		self.cancel = function() {
			$location.path('/');
		}
	}]
})