/**
 * Created by JELENA on 20.6.2017.
 */
app.controller('FakturaController', function ($scope, $state, $rootScope, $mdDialog, fakturaService, nalogZaPrenosService, authenticationService) {

    $scope.page.current = 3.2;

    var loadData = function () {
        zaposleni = authenticationService.getUser();
        if($state.current.name === "home.fakturaDobavljac") {
            fakturaService.readDobavljac(zaposleni.tpodaciSubjektDTO.pib,  function (response) {
                $scope.uloga = "dobavljac";
                $scope.fakture = response.data;
            });
        } else if ($state.current.name === "home.fakturaKupac") {
            fakturaService.readKupac(zaposleni.tpodaciSubjektDTO.pib, function (response) {
                $scope.uloga = "kupac";
                $scope.fakture = response.data;
            });
        }
        };

    loadData();

    var openForm = function (faktura) {
        $mdDialog.show({
            parent: angular.element(document.body),
            templateUrl: 'dialog/fakturaForm.html',
            controller: 'FakturaFormController',
            locals: { faktura: faktura}
        }).finally(function () {
           // loadData();
        });
    };

    $scope.addFaktura = function() {
        openForm(null);
    };

    $scope.showStavke = function(faktura) {
        $mdDialog.show({
            parent: angular.element(document.body),
            templateUrl: 'dialog/stavkeFakture.html',
            controller: 'StavkeFaktureController',
            locals: {faktura: faktura}
        });
    }

    $scope.kreirajNalog = function (faktura) {
        alert("OVCEE")
        nalogZaPrenosService.kreirajNalog(faktura.toString(),  function (response) {
           alert("Uspeh");
        }).finally(function () {
            alert("Kraj")
        });

    }

    $scope.query = {
        order: 'name',
        limit: 5,
        page: 1
    };
});
