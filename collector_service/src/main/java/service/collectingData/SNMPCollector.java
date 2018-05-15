package service.collectingData;

import data.CollectableEntityDTO;
import data.CountersResults;
import data.DevicePropertiesDTO;
import exceptions.CollectionException;
import exceptions.CollectionTimeoutException;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.PushGateway;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.DefaultPDUFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import repositories.Dao;
import utils.SNMPVersionConvertors;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;


@SuppressWarnings("unused")
@Service
@Slf4j
public class SNMPCollector extends CollectorImpl {

	private static final Logger logger = LoggerFactory.getLogger( SNMPCollector.class);

	@Autowired
	private Dao dao;

	@Value("${Collector.defaultTimeout}")
	private int DEFAULT_TIMEOUT = 2000;
	private static final String DEFAULT_COMMUNITY = "public";
	//	private static final int COUNTERS_QUOTA_PER_COLLECTION_PER_HOST = 20;
	private long updatedNum;

	//	private ResponseEvent testEvent=null;
	Snmp snmp = null;
	TransportMapping<?> transport;

	public SNMPCollector() {
	}


	//	@Override
	//	protected int quotaPerHost() {
	//		// TODO Auto-generated method stub
	//		return COUNTERS_QUOTA_PER_COLLECTION_PER_HOST;
	//	}
	/**
	 * Start the Snmp session. If you forget the listen() method you will not
	 * get any answers because the communication is asynchronous and the
	 * listen() method listens for answers.
	 * 
	 * @return
	 * @throws IOException
	 */
	@Override
	public Collector init() throws CollectionException {
		super.init();
		if (snmp != null)
			return this;
		try {
			logger.debug("snmp init");
			transport = new DefaultUdpTransportMapping();
			logger.debug("transport created");
			MessageDispatcher disp = new MessageDispatcherImpl();
			disp.addMessageProcessingModel(new MPv2c());
			disp.addMessageProcessingModel(new MPv3());

			snmp = new Snmp(disp, transport);
			logger.debug("snmp created");
			transport.listen();
			updatedNum=0;
		} catch (IOException e) {
			closeSession();
			throw new CollectionException("Can't init SNMP session: " + e.getMessage());
		}
		logger.debug("finished snmp init");
		return this;
	}

	@Override
	public void closeSession() {
		try {
			snmp.close();
		} catch (IOException e) {
		}
		snmp = null;
		try {
			transport.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		transport = null;

		//		logger.debug("SNMP session closed");
	}

	/**
	 * This method is capable of handling multiple OIDs
	 * 
	 * @param oids
	 * @param deviceCollectionProperties 
	 * @return
	 * @throws IOException
	 */
	private ResponseEvent get(OID oids[], DevicePropertiesDTO deviceCollectionProperties, int bulkSize) throws CollectionException {
		Target target = createTarget(deviceCollectionProperties);
		int snmpVersionConvertors = SNMPVersionConvertors.collector2SNMP4J(deviceCollectionProperties.getSnmpVersion());
		snmpVersionConvertors = -91; // = GETBULK code
		PDU pdu=DefaultPDUFactory.createPDU(target,snmpVersionConvertors, bulkSize, 0);
		for (OID oid : oids) {
			VariableBinding vb = new VariableBinding(oid);
			pdu.add(vb);
		}

		if (bulkSize > 1)
			pdu.setType(PDU.GETBULK);
		else
			pdu.setType(PDU.GET);

		try
		{
			logger.debug("target=" + target);
			ResponseEvent event = null;
			event=snmp.send(pdu, target, null);

			if (event != null)
			{
				logger.debug("Response=" + event.getResponse().toString());
				return event;
			}
		} catch (NullPointerException | IOException e) {
			// possibly invalid/unreachable host/id
			//throw new UnreachableDestinationException("Can't send to " + deviceCollectionProperties.getIp()+": "+e.getMessage());
			log.debug("Can't send to " + deviceCollectionProperties.getIp()+": "+e.getMessage());
			return null;
		}
		throw new CollectionTimeoutException("GET timed out for " + deviceCollectionProperties.getIp());
	}

	/**
	 * This method returns a Target, which contains information about where the
	 * data should be fetched and how.
	 * @param deviceCollectionProperties 
	 * 
	 * @return
	 */
	private Target createTarget(DevicePropertiesDTO deviceCollectionProperties) {
		String host = deviceCollectionProperties.getProtocol() + ":" + deviceCollectionProperties.getIp() + "/" + deviceCollectionProperties.getPort();
		Address targetAddress = GenericAddress.parse(host);
		Target  target = targetFactoryMethod(deviceCollectionProperties);
		target.setAddress(targetAddress);
		target.setRetries(2);
		target.setTimeout(DEFAULT_TIMEOUT);
		target.setVersion(utils.SNMPVersionConvertors.collector2SNMP4J(deviceCollectionProperties.getSnmpVersion()));
		return target;
	}

	// Creates and initialize concrete target
	private Target targetFactoryMethod(DevicePropertiesDTO deviceCollectionProperties) {
		Target target=null;
		String deviceId = deviceCollectionProperties.getIp();

		String snmpUser = deviceCollectionProperties.getSnmpUser();
		switch(deviceCollectionProperties.getSnmpVersion()) {
		case V1:
		case V2:
			target=new CommunityTarget();
			((CommunityTarget) target).setCommunity(new OctetString(DEFAULT_COMMUNITY));
			break;
		case V3:
			target = new UserTarget(GenericAddress.parse(deviceId),
					new OctetString(snmpUser),// I am not sure about this one
					"".getBytes());//deviceCollectionProperties.getSnmpPrivacyPassword().getBytes());// I am not sure about this one neither

			USM usm = new USM(SecurityProtocols.getInstance(),
					new OctetString(MPv3.createLocalEngineID()), 0);
			SecurityModels.getInstance().addSecurityModel(usm);

			// add user to the USM
			SecurityModel securityModel=   new MPv3().getSecurityModel(0);
			
			snmp.getUSM().addUser(new OctetString(snmpUser),
					new UsmUser(new OctetString(snmpUser),
							AuthMD5.ID,
							new OctetString(deviceCollectionProperties.getSnmpPassword()),
							PrivDES.ID,
							new OctetString(deviceCollectionProperties.getSnmpPrivacyPassword())));
			target.setVersion(SnmpConstants.version3);
			target.setSecurityLevel(SecurityLevel.AUTH_PRIV);
			target.setSecurityName(new OctetString(snmpUser));
			break;
		default:
			throw new RuntimeException(deviceCollectionProperties.getSnmpVersion().toString());
		}

		return target;
	}


	@Override
	public void prepareCollection(CollectableEntityDTO port) {
		String counterName = port.getEntityName();
		if (!counterName.equals("")) {
			getCountersResults().add(new CountersResults(port));
		}
	}

	void executeBatchJob()
	{
		CollectorRegistry registry = new CollectorRegistry();
		Gauge duration = Gauge.build()
				.name("my_batch_job_duration_seconds")
				.help("Duration of my batch job in seconds.")
				.register(registry);
		Gauge.Timer durationTimer = duration.startTimer();
		try
		{
			// This is only added to the registry after success,
			// so that a previous success in the Pushgateway is not overwritten on failure.
			Gauge lastSuccess = Gauge.build()
					.name("my_batch_job_last_success_unixtime")
					.help("Last time my batch job succeeded, in unixtime.")
					.register(registry);
			lastSuccess.setToCurrentTime();
		}
		finally
		{
			durationTimer.setDuration();
			PushGateway pg = new PushGateway("127.0.0.1:9090");
			try {
				pg.pushAdd(registry, "my_batch_job");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	void saveToElastic(Entry<String, List<CountersResults>> quotaEntry)
	{
		RestHighLevelClient client = new RestHighLevelClient( RestClient.builder(new HttpHost("localhost", 9200, "http")));
		Map<String, Object> jsonMap = new HashMap<>();
		String[] key_parts = quotaEntry.getKey().split("_");
		jsonMap.put("ip", key_parts[0]);
		jsonMap.put(key_parts[1], quotaEntry.getValue().get(0).getValue());
		jsonMap.put("collectTime", new DateTime());
		IndexRequest indexRequest = new IndexRequest("local", "doc").source(jsonMap);
		try {
			client.index(indexRequest);
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected long collectAllData() throws CollectionException {
		String retVal = null;
		updatedNum=0;

		// Get PMs list
		List<CollectableEntityDTO> collectableEntities = dao.getAllCollectableEntities();
		if (collectableEntities.isEmpty())
			return 0;

		// Get Devices
		List<DevicePropertiesDTO> devices =dao.getAllDevices();
		if (devices == null) {
			logger.error( "No deviceCollectionProperties were found" );
			return 0;
		}

		devices.stream().forEach(device ->
		{
            Map<String, List<List<CountersResults>>> host2resultsMap = getEntitiesPerType(device, collectableEntities);
            host2resultsMap.entrySet().parallelStream().forEach(entry ->
			{
				entry.getValue().parallelStream().forEach(quotaList ->
				{
					Entry<String, List<CountersResults>> quotaEntry = new AbstractMap.SimpleEntry<>(device.getIp() + "_" + quotaList.get(0).getPort().getEntityName(), quotaList );
					try
					{
						logger.debug("Going to collect " + collectableEntities.size() + " items from host " + device.getIp());
						quotaEntry = collectPerHostPerQuota(device,quotaEntry);
						updateDB(quotaEntry);
						saveToElastic(quotaEntry);
						long currentSize=quotaEntry.getValue().size();
						updatedNum+=currentSize;
					}
					catch (CollectionException e)
					{
						logger.error("Exception when collecting PMs from host "+ device.getIp() + ". Error: " +e.getMessage());
					}
				});
			});
		});
		return updatedNum;
	}

	private Entry<String, List<CountersResults>> collectPerHostPerQuota(DevicePropertiesDTO deviceCollectionProperties, Entry<String, List<CountersResults>> quotaEntry)
			throws CollectionException {

		String host = quotaEntry.getKey();
		List<CountersResults> countersResults = quotaEntry.getValue();

		OID[] oids = new OID[countersResults.size()];
		for (int index = 0; index < countersResults.size(); index++) {
			CountersResults result = countersResults.get(index);
			OID oid = new OID(result.getPort().getEntityOID());
			oids[index] = oid;
		}

		logger.debug(oids.length + " oids to get for host " + host);
		ResponseEvent event = null;
		int bulkSize = countersResults.get(0).getPort().getBulkSize();
		event = get(oids, deviceCollectionProperties, bulkSize);

		if (event == null)
		    return quotaEntry;

		PDU response = event.getResponse();

		if (response != null) {
			if (response.getErrorStatus() != 0) {
				int errorIndex = response.getErrorIndex()-1;// SNMP is 1-based
				String errorMsg="Problems in getting SNMP response: index="+errorIndex+ " status=" + response.getErrorStatus() + ", text="
						+ response.getErrorStatusText();
				try{
					errorMsg +=  " uid= "+ oids[errorIndex];
				} catch (IndexOutOfBoundsException e) {
					errorMsg+=" but sizes are only "+oids.length+", "+quotaEntry.getValue().size();
				}
				logger.error(errorMsg);
				if (response.getErrorStatus()==PDU.tooBig) {
					logger.error("Encoding too big="+event.getRequest().getBERLength()+", "+response.getBERLength());
				}
				throw new CollectionException("can't get response in collectAllData: " + errorMsg);
			}
			logger.debug("msg ok with size="+event.getRequest().getBERLength()+", "+response.getBERLength());
			for (int index = 0; index < countersResults.size(); index++) {
				CountersResults counterResults = countersResults.get(index);

				ArrayList<String> resultList = new ArrayList<>();
				for (int i=0; i < bulkSize; i++)
				{
					resultList.add( response.get(i).getVariable().toString());
				}

				VariableBinding variableBinding = response.get(index);
				counterResults.setValue(resultList);
				logger.debug("counterResults is now " + counterResults);

			}
		} else {
			logger.error("can't get response in  collectAllData. Is it time-out ?");
			throw new CollectionException("can't get response in collectAllData");
		}
		logger.debug("collectPerHost for " + host + " finished ");

		return quotaEntry;
	}



}
