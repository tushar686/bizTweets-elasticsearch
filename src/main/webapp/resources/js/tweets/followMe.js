var followMePage = {id: "", keyvalueDelimiter: "---"};

function showTweetDetails(id) {
	showProgressbar();
	restCall("GET", "/bizTweets/getTweet/" + id, "", "json", "application/json", success_getTweet);
}

function success_getTweet(response) {
	hideProgressbar();
	
	var result = "<table class='table table-striped table-hover'><tbody>";
	result = result + renderOneTweetDialog(response.id, response);
	result = result + "</tbody></table>";

	
	showDialog(result);
}

function showDialog(tweet) {
	new BootstrapDialog({
		buttons: [ {
            label: 'Close',
            action: function(dialogRef){
                dialogRef.close();
            }
        }],
        closable: true,
		title: "Tweet",
        message: tweet
    }).open();

}

function loadFollowType() {
	restCall("GET", "/bizTweets/getFollowType/tushar686@gmail.com/", "", "json", "application/json", success_getfollowtype);
}

function success_getfollowtype(response) {
	global.followType = response;
	if ((global.followType).length == 0)
		FreshAllTweets();
	else
		FreshFollowingTweets();
}


function followMe(id) {
	followMePage.id = id;
	var followReq = 	"{\"user\": \"tushar686@gmail.com\",\"followQuery\": \""+ id +"\"}";
	restCall("PUT", "/bizTweets/follow", followReq, "json", "application/json", "", complete_followMe);
}

function complete_followMe(response) {
	if (response.status == 201) {
		$("#" + followMePage.id).removeClass("glyphicon-hand-right"); 
		$("#" + followMePage.id).addClass("glyphicon-chevron-left");
	}
}

