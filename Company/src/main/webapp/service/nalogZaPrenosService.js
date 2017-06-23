/**
 * Created by Olivera on 22.6.2017..
 */

app.service('nalogZaPrenosService', function($http){
    return {
        read: function(onSuccess, onError){
            $http.get('/api/naloziZaPrenos').then(onSuccess, onError);
        },
        readPoverilac: function(naziv, onSuccess, onError){
            $http.get('/api/naloziZaPrenos/firmaPoverilac/' + naziv).then(onSuccess, onError);
        },
        readDuznik: function(naziv, onSuccess, onError){
            $http.get('/api/naloziZaPrenos/firmaDuznik/' + naziv).then(onSuccess, onError);
        },
        kreirajNalog: function (podaciZaNalog, onSuccess, onError) {
            alert("Bla");
            $http.post('api/naloziZaPrenos/kreirajNalog', podaciZaNalog).then(onSuccess, onError);
        }
    }
});
