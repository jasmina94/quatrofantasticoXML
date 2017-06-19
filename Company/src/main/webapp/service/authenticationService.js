app.service('authenticationService', function ($http, $window) {

    var LOCAL_STORAGE_KEY = 'restaurantUser';
    var LOCAL_STORAGE_INSTANCE = $window.localStorage;

    return {
        login: function (zaposleni, successCallback, errorCallback) {
            alert("Usao sam ovde");
            $http.post('/api/login', zaposleni, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                transformRequest: function (obj) {
                    var str = [];
                    for (var p in obj)
                        str.push(encodeURIComponent(p) + "=" + encodeURIComponent(obj[p]));
                    return str.join("&");
                }
            }).success(successCallback).error(errorCallback);
        },
        logout: function (successCallback, errorCallback) {
            var loggedInUser = this.getUser();
            if (loggedInUser) {
                $http.post("/api/logout", loggedInUser.id).success(successCallback).error(errorCallback);
            }
        },
        getUser: function () {
            if (LOCAL_STORAGE_INSTANCE) {
                var loggedInUser = LOCAL_STORAGE_INSTANCE.getItem(LOCAL_STORAGE_KEY);
                if (loggedInUser) {
                    return JSON.parse(loggedInUser);
                }
            }
        },
        setUser: function (zaposleni) {
            if (LOCAL_STORAGE_INSTANCE && zaposleni) {
                LOCAL_STORAGE_INSTANCE.setItem(LOCAL_STORAGE_KEY, JSON.stringify(zaposleni));
            }
        }
    }
});