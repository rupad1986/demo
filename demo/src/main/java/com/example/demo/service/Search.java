package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;

import com.example.demo.utils.AppConstants;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.gson.JsonIOException;

@Component
@Qualifier("search")
public class Search {
	private static final Logger LOG = LoggerFactory.getLogger(Search.class);
    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;
    
    @Autowired
    private Environment env;
   
    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     * @param args command line args.
     */
    public void retrieveDetails() {
        try {
            // This object is used to make YouTube Data API requests. The last
            // argument is required, but since we don't need anything
            // initialized when the HttpRequest is initialized, we override
            // the interface and provide a no-op function.
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName(AppConstants.YOU_TUBE_PROJECT).build();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list(AppConstants.ID_SNIPPET);

            // Set your developer key from the {{ Google Cloud Console }} for
            // non-authenticated requests. See:
            // {{ https://cloud.google.com/console }}
            search.setKey(env.getProperty("key"));
            search.setQ(env.getProperty("queryTerm"));

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType(AppConstants.VIDEO);

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            search.setFields(AppConstants.FIELDS);
            search.setMaxResults(AppConstants.NUMBER_OF_VIDEOS_RETURNED);

            // Call the API and print results.
            SearchListResponse searchResponse = search.execute();
            LOG.info("Input search string : {} " ,search);
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
            	prettyPrint(searchResultList);
            }
        } catch (GoogleJsonResponseException e) {
            LOG.error("Failed to retrieve results: {} " , e);
        } catch (IOException e) {
        	LOG.error("IO Failure : {} " , e);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

   
    /*
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, and thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    private static void prettyPrint(List<SearchResult> searchResultList) throws JsonGenerationException, JsonMappingException, IOException {
      if(searchResultList != null && !CollectionUtils.isEmpty(searchResultList)){
        	List<SearchResult> newList = searchResultList.stream().filter(result -> result.getId()!= null && result.getId().getKind()!= null
        	&& result.getId().getKind().equals(AppConstants.KIND)).collect(Collectors.toList());
            writeToFile(newList);
        }
    }
    

	/**
	 * @Function : writeToFile : Writes the you tube files meta data information in json file
	 * @param resourceIdList
	 * @return void
	 */
	private static void writeToFile(List<SearchResult> searchResult) {
		try {
			File file = ResourceUtils.getFile(AppConstants.FILE_PATH);
			LOG.info("searchResult IN writeToFile {} " , searchResult.size());
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(file, searchResult);

		} catch (JsonIOException | IOException e) {
			LOG.error("Failed to write  Youtube metadata {} : {}  ", e);
		}
	}
}
