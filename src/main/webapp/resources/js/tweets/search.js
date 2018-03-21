var searchPage = {cursor: 0, content: ""};

function freshSearchTweets() {
	searchPage.cursor = -1;
	searchPage.content = "";
	setPageContent(searchPage.content);
	searchTweetsRESTCall();
}

function loadMoreSearchTweets() {
	searchTweetsRESTCall();
}

function searchTweetsRESTCall() {
	global.currentPage = "Search";
	searchPage.cursor = searchPage.cursor + 1;
	showProgressbar();
	restCall("GET", "/bizTweets/searchTweets?searchString=" + $("#srch-term").val() + "&cursor="+ searchPage.cursor, "", "json", "application/json", success_searchTweetsRESTCall);
}

function success_searchTweetsRESTCall(response) {
	global.fetchingOnScroll = false;
	hideProgressbar();
	
	var result = "<table class='table table-striped table-hover'><tbody>";
	result = result + renderTweets(response, searchPage.cursor);
	result = result + "</tbody></table>";
	searchPage.content = searchPage.content + result;

	setPageContent(searchPage.content);
}

