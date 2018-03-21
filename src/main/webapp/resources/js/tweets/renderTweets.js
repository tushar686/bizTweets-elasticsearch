
function renderTweets(tweets, cursor) {
	var result = "";
	$.each(tweets, function (index_tweet, tweetModel) {
		var tweetId = cursor + "_" + index_tweet;
		result = result + renderTweetRow(tweetId, tweetModel, false);
	});
	return result;
}

function renderTweetRow(tweetId, tweetModel) {
	var row = "<tr>";
	var col = "<td id=" + tweetId + ">" +
					"<div id=" + tweetId + "entity>From <span class='entity'>" + tweetModel.tweet.appName + "</span> update on <span class='entity'>" + tweetModel.tweet.entityName + "</span> " +
					" <span class='floatright'>";
	
					var explodeIcon = "<a href='#'><i id='"+ tweetModel.id +"' class='glyphicon glyphicon-resize-full' onClick='showTweetDetails(this.id)';>" + 
					"</i></a>&nbsp;&nbsp;&nbsp;&nbsp;";
					
					var ago = "<button type='button' class='btn btn-default' data-toggle='tooltip' data-placement='bottom' title='" + tweetModel.tooltipForAgo + "'>" +	tweetModel.howMuchAgo +
					"</span></div>";
				
					col = col + explodeIcon + ago + renderMetadataInformation(tweetId, tweetModel.tweet.metadata);
					
				col = col + "</td>";
	
	return row + col + "</tr>";
}

function renderMetadataInformation(tweetId, metadata) {
	var result = "<div>";
	$.each(metadata, function (index_meta, metadata) {
		var fieldId = tweetId + "field" + index_meta;
		
		result = result + "<span id=" + fieldId;		
		if (isFollowTypeContains(metadata.key + followMePage.keyvalueDelimiter + metadata.value))
			result = result + " class='followingFieldStyle'"; 
			
		result = result + ">" + 
								"<span class='fieldValue'> " + metadata.value + "</span>" + 
								"<span id=" + fieldId + " class='field'>@" + metadata.key + "&nbsp;&nbsp;</span>" +
							"</span>";
	});
	return result + "</div>";
}

function isFollowTypeContains(currentMetadata) {
	var contains = false;
	$.each(global.followType, function (index, followMetadata) {
		if (currentMetadata == followMetadata.followQuery)
			contains = true;
	})
	return contains;
}