package utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.mp.SnmpConstants;

public class SNMPVersionConvertors {

	private static final Logger logger = LoggerFactory.getLogger(SNMPVersionConvertors.class);

	private static Map<SNMPVersion,Integer>  collector2SNMP4JMap= new HashMap<SNMPVersion, Integer>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -960143970154266163L;

		{
			put(SNMPVersion.V1, SnmpConstants.version1);
			put(SNMPVersion.V2, SnmpConstants.version2c);
			put(SNMPVersion.V3, SnmpConstants.version3);
		}
	};

	public static int collector2SNMP4J(SNMPVersion collectorSNMPVersion){
		Integer retVal=collector2SNMP4JMap.get(collectorSNMPVersion);
		if (retVal==null) {
			throw new NullPointerException("Invalid convertor="+collectorSNMPVersion);
		}
		return retVal;
	}
}
