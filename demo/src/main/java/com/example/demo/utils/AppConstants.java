package com.example.demo.utils;

public class AppConstants {
	
	public static final long NUMBER_OF_VIDEOS_RETURNED = 25;
	public static final String FILE_PATH = "classpath:YoutubeMetaData.json";
	public static final String KIND = "youtube#video";
	public static final String FIELDS = "items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)";
	public static final String ID_SNIPPET = "id,snippet";
	public static final String YOU_TUBE_PROJECT = "youTubeProject";
	public static final String VIDEO = "video";

}
