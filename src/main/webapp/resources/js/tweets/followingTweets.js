var followingTweetsPage = {cursor: 0, content: ""};

function FreshFollowingTweets() {
	followingTweetsPage.cursor = -1;
	followingTweetsPage.content = "";
	setPageContent(followingTweetsPage.content);
	loadFollowingTweetsRESTCall();
}

function loadMoreFollowingTweets() {
	loadFollowingTweetsRESTCall();
}

function loadFollowingTweetsRESTCall() {
	global.currentPage = "Following";
	followingTweetsPage.cursor = followingTweetsPage.cursor + 1;
	showProgressbar();
	restCall("GET", "/bizTweets/getFollowingTweets?user=tushar686@gmail.com&cursor="+ followingTweetsPage.cursor, "", "json", "application/json", success_getFollowingTweetsRESTCall);
}

function success_getFollowingTweetsRESTCall(response) {
	global.fetchingOnScroll = false;
	hideProgressbar();
	
	var result = "<table class='table table-striped table-hover'><tbody>";
	result = result + renderTweets(response, followingTweetsPage.cursor);
	result = result + "</tbody></table>";
	
	followingTweetsPage.content = followingTweetsPage.content + result;

	setPageContent(followingTweetsPage.content);
}