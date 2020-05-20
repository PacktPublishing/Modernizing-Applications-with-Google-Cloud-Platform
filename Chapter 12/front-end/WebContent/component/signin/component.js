angular.module('component.signin').component('signin', {
	templateUrl: 'component/signin/template.html',
	controller: ['Auth', '$location', '$cookies', '$timeout', '$rootScope', '$translate', function SigninController(Auth, $location, $cookies, $timeout, $rootScope, $translate) {
		var self = this;
		
		$timeout(function() {
			jQuery('#signin').validator();
			$rootScope.$apply();
		}, 100);
		
		if($cookies.get('authenticating')) {
			self.authenticating = true;
			$timeout(function() { self.authenticating = false; $cookies.remove('authenticating'); }, 5000);
		} else {
			self.authenticating = false;
		}
		
		Auth.$onAuthStateChanged(function(firebaseUser) {
			if(firebaseUser) {
				$cookies.remove('authenticating');
			}
		});

		self.signIn = function() {
			$cookies.put('authenticating', true);
			Auth.$signInWithEmailAndPassword(self.email, self.password).catch(function() {
				$translate('signin.error').then(function (translation) {
					self.error = translation;
				});
			});
		}
	}]
})