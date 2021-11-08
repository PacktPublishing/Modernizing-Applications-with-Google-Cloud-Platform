app.controller('signout', ['$scope', 'Auth', '$location', function SignoutController($scope, Auth, $location) {
	Auth.$onAuthStateChanged(function(firebaseUser) {
		$scope.user = firebaseUser;
	});

	$scope.signOut = function() {
		Auth.$signOut().then(function() {
			$location.path('/');
		});
	}
}]);