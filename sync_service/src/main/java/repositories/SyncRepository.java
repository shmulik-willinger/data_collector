package repositories;

import configuration.MicroServiceMain;
import data.ConfigurationProperties;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Repository
@EnableMongoRepositories
public class SyncRepository implements ISyncRepository
{
    private static final Logger logger = LoggerFactory.getLogger( MicroServiceMain.class);

    @Autowired
    private MongoOperations mongoOps;

    @Autowired
    private ConfigurationRepository configurationRepository;


    List<ConfigurationProperties> configuration;
    private static String local_DB_Host;
    private static String local_DB_port;
    private static String centrelize_DB_Host;
    private static String centrelize_DB_port;

    public SyncRepository()
    {
        try {
  /*          configuration = configurationRepository.findAll();
            local_DB_Host = getValueForKey( "local_DB_Host" );
            local_DB_port = getValueForKey( "local_DB_port" );
            centrelize_DB_Host = getValueForKey( "centrelize_DB_Host" );
            centrelize_DB_port = getValueForKey( "centrelize_DB_port" );
*/
            local_DB_Host = "localhost";
            local_DB_port = "9200";
            centrelize_DB_Host = "localhost";
            centrelize_DB_port = "9200";
        }
        catch (Exception e)
        {
            logger.error("Failed to get DB configurations from Mongo.  Exception = " + e.getMessage());
        }

    }

    private String getValueForKey(String key)
    {
        return((ConfigurationProperties)configuration.get( configuration.indexOf(key) )).getValue();
    }

    @Override
    public SearchHit[] getLocalData(long size)
    {
        RestHighLevelClient client = new RestHighLevelClient( RestClient.builder(new HttpHost(local_DB_Host, Integer.parseInt( local_DB_port ), "http")));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query( QueryBuilders.matchAllQuery()).size( (int) size );

        SearchRequest searchRequest = new SearchRequest("local");
        searchRequest.source(searchSourceBuilder);
        searchRequest.types("doc");

        SearchHit[] searchHits = new SearchHit[0];
        try {
            SearchResponse searchResponse = client.search(searchRequest);
            SearchHits hits = searchResponse.getHits();
            searchHits = hits.getHits();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchHits;
    }

    @Override
    public void saveToCentralize(ArrayList<Map<String, Object>> quotaEntry)
    {
        RestHighLevelClient client = new RestHighLevelClient( RestClient.builder(new HttpHost(centrelize_DB_Host, Integer.parseInt( centrelize_DB_port ), "http")));
        BulkRequest bulkRequest = new BulkRequest();

        for (Map<String, Object> doc : quotaEntry)
            bulkRequest.add(new IndexRequest("centralize", "doc").source(doc));

        try {
            client.bulk(bulkRequest);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearLocalDocs(SearchHit[] searchHits)
    {
        RestHighLevelClient client = new RestHighLevelClient( RestClient.builder(new HttpHost(local_DB_Host, Integer.parseInt( local_DB_port ), "http")));
        BulkRequest bulkRequest = new BulkRequest();

        for (SearchHit hit : searchHits)
            bulkRequest.add( new DeleteRequest( "local", "doc", hit.getId() ));

        try {
            client.bulk(bulkRequest);
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
