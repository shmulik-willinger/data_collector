package utils;

import javax.activation.UnsupportedDataTypeException;

public enum CollectionProtocol {
	SNMP;
	// FILE,

	public static CollectionProtocol fromString(String param) throws UnsupportedDataTypeException {

		switch (param) {
		case "SNMP":
			return SNMP;

		case "FILE":
		case "NA":
		default:
			throw new UnsupportedDataTypeException("Unsupported collectionProtocol " + param);
		}

	}
}
