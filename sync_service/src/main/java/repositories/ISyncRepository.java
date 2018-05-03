package repositories;

import org.elasticsearch.search.SearchHit;

import java.util.ArrayList;
import java.util.Map;

public interface ISyncRepository
{
    SearchHit[] getLocalData(long size);

    void clearLocalDocs(SearchHit[] searchHits);

    void saveToCentralize(ArrayList<Map<String, Object>> quotaEntry);
}
