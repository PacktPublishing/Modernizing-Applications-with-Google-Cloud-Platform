app.controller('signout', ['$scope', 'Auth', '$location', function SignoutController($scope, Auth, $location) {
	$scope.auth = Auth;

	Auth.$onAuthStateChanged(function(firebaseUser) {
		$scope.user = firebaseUser;
	});

	$scope.signOut = function() {
		$scope.auth.$signOut().then(function() {
			$location.path('/');
		});
	}
}]);