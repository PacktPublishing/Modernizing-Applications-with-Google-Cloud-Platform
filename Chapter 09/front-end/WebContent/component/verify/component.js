angular.module('component.verifyEmail').component('verifyEmail', {
	templateUrl: 'component/verify/template.html',
	controller: ['Auth', '$location', '$rootScope', function VerifyController(Auth, $location, $rootScope) {
		var self = this;
		
		Auth.$onAuthStateChanged(function(firebaseUser) {
			self.firebaseUser = firebaseUser;
		});

		self.verify = function() {
			if(self.firebaseUser) {
				self.firebaseUser.sendEmailVerification().then(function() {
					Auth.$signOut();
				})
			}
		};
	}]
})