package service.collectingData;

import data.CollectableEntityDTO;
import data.CountersResults;
import exceptions.CollectionException;

import java.util.List;
import java.util.Map.Entry;

public interface Collector {

	final static long INVALID_COUNTER_VALUE = -1;
	final static String INVALID_COUNTER_VALUE_STRING = String.valueOf(INVALID_COUNTER_VALUE);

	public Collector init() throws CollectionException;

	public void closeSession(); // clean resources

	public void prepareCollection(CollectableEntityDTO port);

	public long collectAll();

	public void updateDB(Entry<String, List<CountersResults>> entry);

}
