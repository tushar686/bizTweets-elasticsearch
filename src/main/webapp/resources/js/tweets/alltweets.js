var allTweetsPage = {cursor: 0, content: ""};

function FreshAllTweets() {
	allTweetsPage.cursor = -1;
	allTweetsPage.content = "";
	setPageContent(allTweetsPage.content);
	loadAllTweetsRESTCall();
}

function loadMoreAllTweets() {
	loadAllTweetsRESTCall();
}

function loadAllTweetsRESTCall() {
	global.currentPage = "All";
	allTweetsPage.cursor = allTweetsPage.cursor + 1;
	showProgressbar();
	restCall("GET", "/bizTweets/getTweets?user=tushar686@gmail.com&cursor="+ allTweetsPage.cursor, "", "json", "application/json", success_getTweetsRESTCall);
}

function success_getTweetsRESTCall(response) {
	global.fetchingOnScroll = false;
	hideProgressbar();
	
	var result = "<table class='table table-striped table-hover'><tbody>";
	result = result + renderTweets(response, allTweetsPage.cursor);
	result = result + "</tbody></table>";
	
	allTweetsPage.content = allTweetsPage.content + result;

	setPageContent(allTweetsPage.content);
}