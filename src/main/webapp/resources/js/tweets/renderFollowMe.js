
function renderOneTweetDialog(tweetId, tweetModel) {
	var row = "<tr>";
	var col = "<td id=" + tweetId + ">" +
					"<div id=" + tweetId + "entity>From <span class='entity'>" + tweetModel.tweet.appName + "</span> update on <span class='entity'>" + tweetModel.tweet.entityName + "</span> ";
	
					"</span></div>";
					
					col = col + renderMetadataInformationForOneTweetDialog(tweetId, tweetModel.tweet.metadata);
					
				col = col + "</td>";
	
	return row + col + "</tr>";
}

function renderMetadataInformationForOneTweetDialog(tweetId, metadata) {
	var result = "<div>";
	$.each(metadata, function (index_meta, metadata) {
		var fieldId = tweetId + "field" + index_meta;
		result = result + "<div class='oneTweetBorder'>" + 
								"<span class='oneTweetFieldValue'> " + metadata.value + "</span>" + 
								"<span id=" + fieldId + " class='oneTweetField'> @ " + metadata.key + "</span>";
					var follow = getFollowImage(metadata);
					var ignore = "<span class='floatright'><a href='#'><i class='glyphicon glyphicon-remove'></i></a>&nbsp;&nbsp;&nbsp;</span>";
					var relabel = "<span class='floatright'><a href='#'><i class='glyphicon glyphicon-pencil'></i></a>&nbsp;&nbsp;&nbsp;</span>";
							
		result = result + relabel + ignore + follow + "</div>";
	});
	return result + "</div>";
}

function getFollowImage(metadata) {
	var follow = "<span class='floatright'><a href='#'><i id=" + metadata.key + followMePage.keyvalueDelimiter + metadata.value + " ";
	
	var following = false;
	$.each(global.followType, function (index, followMetadata) {
		if ((metadata.key + followMePage.keyvalueDelimiter + metadata.value) == followMetadata.followQuery)
			following = true;
	});

	if(following)
		follow = follow + "class='glyphicon glyphicon-chevron-left' onClick='followMe(this.id)';>";
	else
		follow = follow + "class='glyphicon glyphicon-hand-right' onClick='followMe(this.id)';>";
		
	return follow + "</i></a>&nbsp;&nbsp;&nbsp;</span>";
}