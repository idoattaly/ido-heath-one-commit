/**
 * Created by Administrator on 2017/7/5.
 */

var htmlCtrl = documentApp.controller("htmlCtrl",['$scope','$compile','$http',function($scope,$compile,$http){

    $scope.setValue=function(){
        console.log(htmlObject.htmlResult);
        var compileFn = $compile(htmlObject.htmlResult);
        var $dom = compileFn($scope);
        console.log($dom);
        htmlObject.htmlResult = $dom.html();
    }


}])