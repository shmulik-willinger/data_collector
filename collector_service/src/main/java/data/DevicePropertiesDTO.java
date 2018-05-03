package data;

import lombok.Data;
import org.springframework.data.annotation.Id;
import utils.SNMPPrivacyAlgorithm;
import utils.SNMPVersion;

import java.io.Serializable;

@Data
public class DevicePropertiesDTO implements Serializable {

	private static final long serialVersionUID = -1323244033233664170L;
	@Id
	private String ip;
	private String port="";
	private String protocol="";
	private String appProtocol="";
	private String type="";
	private String version="";
	private String snmpUser="";
	private String snmpPassword="";
	private SNMPPrivacyAlgorithm snmpPrivacyAlgorithm=SNMPPrivacyAlgorithm.DES;
	private String snmpPrivacyPassword="";
	private SNMPVersion snmpVersion;


	public void setPort(String port) {
		this.port = port;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAppProtocol() {
		return appProtocol;
	}

	public void setAppProtocol(String appProtocol) {
		this.appProtocol = appProtocol;
	}



	public DevicePropertiesDTO(){
		
	}
	
	public String getIp() {
		return ip;
	}
	public String getPort() {
		return port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public SNMPVersion getSnmpVersion() {
		return snmpVersion;
	}
	public void setSnmpVersion(SNMPVersion snmpVersion) {
		this.snmpVersion = snmpVersion;
	}

	//Ctor for SNMPV3
	public DevicePropertiesDTO(String ip, String snmpUser, String snmpPassword,
                               SNMPPrivacyAlgorithm snmpPrivacyAlgorithm, String snmpPrivacyPassword,
                               String port, String protocol, String appProtocol,
                               String type, String version)
	{
		super();
		this.ip = ip;
		this.snmpVersion = SNMPVersion.V3;
		this.snmpUser = snmpUser;
		this.snmpPassword = snmpPassword;
		this.snmpPrivacyAlgorithm = snmpPrivacyAlgorithm;
		this.snmpPrivacyPassword = snmpPrivacyPassword;
		this.port = port;
		this.protocol = protocol;
		this.appProtocol = appProtocol;
		this.type = type;
		this.version = version;
	}

	// Ctor for SNMP V1
	public DevicePropertiesDTO(String ip, SNMPVersion snmpVersion) {
		super();
		this.ip=ip;
		this.snmpVersion=snmpVersion;
	}


	public DevicePropertiesDTO(DevicePropertiesRequest request) {
		this.ip = request.getIp();
		this.snmpVersion = request.getSnmpVersion();
		this.snmpUser = request.getSnmpUser();
		this.snmpPassword = request.getSnmpPassword();
		this.snmpPrivacyAlgorithm = request.getSnmpPrivacyAlgorithm();
		this.snmpPrivacyPassword = request.getSnmpPrivacyPassword();
		this.port = request.getPort();
		this.protocol = request.getProtocol();
		this.appProtocol = request.getAppProtocol();
		this.type = request.getType();
		this.version = request.getVersion();
	}
	public String getSnmpUser() {
		return snmpUser;
	}
	public void setSnmpUser(String snmpUser) {
		this.snmpUser = snmpUser;
	}
	public String getSnmpPassword() {
		return snmpPassword;
	}
	public void setSnmpPassword(String snmpPassword) {
		this.snmpPassword = snmpPassword;
	}
	public SNMPPrivacyAlgorithm getSnmpPrivacyAlgorithm() {
		return snmpPrivacyAlgorithm;
	}
	public void setSnmpPrivacyAlgorithm(SNMPPrivacyAlgorithm snmpPrivacyAlgorithm) {
		this.snmpPrivacyAlgorithm = snmpPrivacyAlgorithm;
	}
	public String getSnmpPrivacyPassword() {
		return snmpPrivacyPassword;
	}
	public void setSnmpPrivacyPassword(String snmpPrivacyPassword) {
		this.snmpPrivacyPassword = snmpPrivacyPassword;
	}
	@Override
	public String toString() {
		return "DevicePropertiesDTO [ip=" + ip + ", snmpVersion=" + snmpVersion
				+ ", snmpUser=" + snmpUser + ", snmpPassword=" + snmpPassword + ", snmpPrivacyAlgorithm="
				+ snmpPrivacyAlgorithm + ", snmpPrivacyPassword=" + snmpPrivacyPassword +
				", port=" + port + ", protocol=" + protocol +"]";
	}

	public static DevicePropertiesDTO getDefaultObject(String host) {
		return new DevicePropertiesDTO(host,SNMPVersion.V2);// Used for dev. env.
	} 

}
