angular.module('service.user').
	service('User', ['$resource', function($resource) {
		var token = null;
		var user = null;
		var self = this; 
		this.setToken = function(newToken) {
			if(token !== newToken) {
				token = newToken;
				user = $resource('bank/user/preference', {}, {
					getPreference: {
						method: 'GET',
						isArray: false,
						headers: {
							'Authorization': 'Bearer ' + token
						}
					}
				});
			};
			return self;
		};

		this.getPreference = function(success, error) {
			return user.getPreference({}, success, error);
		};
	}
]);