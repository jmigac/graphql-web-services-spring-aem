$(document).ready(function(){
	console.log("Ide gas");
	$("korisnikDataTable").DataTable();

	$.ajax({
        url: "http://localhost:8080/api/v1/potresi",
        type: 'GET',
        headers:{
        	Accept:"application/json", "Access-Control-Allow-Origin": "*"
        },
        contentType: 'application/json',
        crossDomain: true,
        success: function (data) {
            console.log(data);
        },
        error: function(xhr, ajaxOptions, throwError){
        	console.log("Throw error: " + throwError);
        	console.log("Status: " + xhr.status + " " + xhr.responseText);
        }
    });
});