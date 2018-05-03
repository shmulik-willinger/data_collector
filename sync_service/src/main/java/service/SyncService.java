package service;

import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.ISyncRepository;

import java.util.ArrayList;
import java.util.Map;


@Service
public class SyncService implements ISyncService {


	@Autowired
	private ISyncRepository syncRepository;

	private static final Logger logger = LoggerFactory.getLogger( SyncService.class);

	@Override
	public void sync(long size)
	{
		logger.info(">> Starting Scheduler sync for {} docs", size);

		SearchHit[] searchHits = syncRepository.getLocalData(size);
		if (searchHits.length > 0) {
            ArrayList<Map<String, Object>> docs = prepareData( searchHits );
            syncRepository.saveToCentralize( docs );
            syncRepository.clearLocalDocs( searchHits );
        }
		logger.info("<< Sync data finished successfully. " + searchHits.length +" docs were moved to Centralize DB");
	}

	private ArrayList<Map<String, Object>> prepareData(SearchHit[] searchHits)
	{
		ArrayList<Map<String, Object>> docs = new ArrayList<>();
		for (SearchHit hit : searchHits)
			docs.add( hit.getSourceAsMap() );

		return docs;
	}



}
