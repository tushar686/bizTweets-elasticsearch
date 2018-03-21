<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
<head>
  <title>bizTweets</title>
  <meta name="description" content="website description" />
  <meta name="keywords" content="website keywords, website keywords" />
  <meta http-equiv="content-type" content="text/html; charset=windows-1252" />
  <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Tangerine&amp;v1" />
  <link rel="stylesheet" type="text/css" href="http://fonts.googleapis.com/css?family=Yanone+Kaffeesatz" />
  <link rel="stylesheet" type="text/css" href="resources/css/tweets/tweets.css" />
  <link rel="stylesheet" type="text/css" href="resources/css/jquery-ui-1.10.4.custom.min.css" />
  <link rel="stylesheet" type="text/css" href="resources/css/bootstrap.min.css" />
  <link rel="stylesheet" type="text/css" href="resources/css/bootstrap-dialog.min.css" />
   
</head>

<body>
  <jsp:include page="header.jsp" />
  <div class="progress progress-striped active" id="mainProgressbar">
  	<div class="progress-bar" style="width: 100%"></div>
  </div>
  <div id="content"></div>
</body>

<script src="resources/js/jquery-2.1.0.min.js"></script>
<script src="resources/js/jquery-ui-1.10.4.custom.min.js"></script>
<script src="resources/js/bootstrap.min.js"></script>
<script src="resources/js/bootstrap-dialog.min.js"></script>
<script src="resources/js/main.js"></script>
<script src="resources/js/tweets/alltweets.js"></script>
<script src="resources/js/tweets/followingTweets.js"></script>
<script src="resources/js/tweets/search.js"></script>
<script src="resources/js/tweets/renderTweets.js"></script>
<script src="resources/js/tweets/followMe.js"></script>
<script src="resources/js/tweets/renderFollowMe.js"></script>

<script lang="javascript">
$(window).scroll(function () {
	if (global.fetchingOnScroll)
	    return false;

	if ($(window).scrollTop() >= ($(document).height() - $(window).height())*0.9){
		global.fetchingOnScroll = true;
		if (global.currentPage == "All")
			loadMoreAllTweets();
		else if (global.currentPage == "Search")
			loadMoreSearchTweets();
		else if (global.currentPage == "Following")
			loadMoreFollowingTweets();
		else	
			loadMoreAllTweets();
	}
	
});
 
</script>
</html>
