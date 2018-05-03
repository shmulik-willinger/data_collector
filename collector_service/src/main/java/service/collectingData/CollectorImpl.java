package service.collectingData;

import com.google.common.collect.Lists;
import data.CollectableEntityDTO;
import data.CountersResults;
import data.DevicePropertiesDTO;
import exceptions.CollectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import repositories.Dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class CollectorImpl implements Collector {

	private static final Logger logger = LoggerFactory.getLogger( CollectorImpl.class);
	@Autowired
	private Dao dao;

	private String portName;
	private String counterName;
	private List<CountersResults> countersResults = new ArrayList<CountersResults>();

	private long updatedNum;
	
	public String getPortName() {
		return portName;
	}

	public void setPortName(String portName) {
		this.portName = portName;
	}

	public String getCounterName() {
		return counterName;
	}

	public void setCounterName(String counterName) {
		this.counterName = counterName;
	}

	public List<CountersResults> getCountersResults() {
		return countersResults;
	}

	public void setCountersResults(List<CountersResults> countersResults) {
		this.countersResults = countersResults;
	}

	@Override
	public String toString() {
		return "CollectorImpl [portName=" + portName + ", counterName=" + counterName + "]";
	}

	public CollectorImpl() {
		super();
	}

	protected int quotaPerHost() {
		return 21; //TODO resource
	}

	public Collector init() throws CollectionException {
		try {
			countersResults.clear();
			updatedNum=0;
		} catch (Exception e) {
			logger.error("Exception in init: " + e.getMessage());
			closeSession();
		}
		return this;
	}

	public String getAddress() {
		return portName;
	}

	public void setAddress(String address) {
		this.portName = address;
	}

	public String getId() {
		return counterName;
	}

	public void setId(String id) {
		this.counterName = id;
	}

	@Override
	public void closeSession() {
		int size=countersResults.size();
	
		countersResults=new ArrayList<CountersResults>(size); 
	}
	
	
	@Override
	public long collectAll() {
		updatedNum=0;
		try {
			updatedNum= collectAllData();
		} catch (CollectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.debug("Finished collecting all data");

		closeSession();
		long retVal=updatedNum;
		try {
			init();
		} catch (CollectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retVal;
	}


	@Override
	public void updateDB(Entry<String, List<CountersResults>> entry) {
		String host = entry.getKey();
		entry.getValue().parallelStream().forEach(
				result -> dao.updateEntityPM(host, LocalDateTime.now(), result.getValue()));
		logger.debug("Finished updateDB for " + host + " with " + entry.getValue().size() + " items");
	}

	abstract protected long collectAllData() throws CollectionException; // SNMPCollector.collectAllData()


	protected Map<String, List<List<CountersResults>>> getEntitiesPerType(DevicePropertiesDTO device, List<CollectableEntityDTO> entities) {
		Map<String, List<CountersResults>> map1 = new HashMap<String, List<CountersResults>>();
		entities.stream().forEach(entity -> {
			String key = device.getIp() + "_" + entity.getEntityName();
			if (!map1.containsKey(key) && device.getType().equals( entity.getType()) && device.getVersion().equals( entity.getVersion()))
			{
				map1.put(key, new ArrayList<CountersResults>());
				map1.get(key).add(new CountersResults(entity));
			}

		});

		// split by QUOTA
		Map<String, List<List<CountersResults>>> map2 = new HashMap<String, List<List<CountersResults>>>(map1.size());
		map1.entrySet().forEach(entry -> map2.put(entry.getKey(),
				Lists.partition(entry.getValue(), quotaPerHost())));

		return map2;
	}
	
	DevicePropertiesDTO getDeviceCollectionProperties(String deviceId){
	 return dao.getDeviceCollectionPropertiesEntity(deviceId);
	}

	public List<DevicePropertiesDTO> getAllDevices(){
		return dao.getAllDevices();
	}

}