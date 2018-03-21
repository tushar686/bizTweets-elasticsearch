var global = {fetchingOnScroll: false, followType: "", currentPage: ""};

$(function(){
	loadFollowType();
	});

function restCall(method, url, data, dataType, contentType, successCallback, completeCallback) {
	 $.ajax({ 
         type: method,       
         url: url,
         data: data,
         dataType: dataType,
         contentType: contentType,
         success: successCallback,
         complete: completeCallback,
         failure: failureCallback
     });
}

function failureCallback(error) {
	alert(error);
	console.log(error);
}

function hideProgressbar() {
	$("#mainProgressbar").hide();
}

function showProgressbar() {
	$("#mainProgressbar").show();
}

function setPageContent(content) {
	$("#content").html(content);
}