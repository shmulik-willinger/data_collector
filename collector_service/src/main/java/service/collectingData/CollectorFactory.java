package service.collectingData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.CollectionProtocol;

@Service
public class CollectorFactory {

	@Autowired
	Collector collector;

	public Collector getCollector(CollectionProtocol param) throws UnsupportedOperationException {
		if (param == CollectionProtocol.SNMP) {
			return collector;
		}
		throw new UnsupportedOperationException("Unsupported protocol: " + param);
	}
}
