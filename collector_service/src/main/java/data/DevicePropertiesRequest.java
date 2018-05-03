package data;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import utils.SNMPPrivacyAlgorithm;
import utils.SNMPVersion;

@Data
@ApiModel(description = "Report information")
public class DevicePropertiesRequest {


    @ApiModelProperty(
            position = 1,
            value = "IP of the device",
            required = true,
            example = "123.45.67.89")
    private String ip;

    @ApiModelProperty(
            position = 2,
            value = "SNMP port",
            required = true,
            example = "161")
    private String port;

    @ApiModelProperty(
            position = 3,
            value = "Transport layer protocol",
            required = false,
            example = "udp")
    private String protocol;

    @ApiModelProperty(
            position = 4,
            value = "Application layer protocol",
            required = true,
            example = "snmp")
    private String appProtocol;

    @ApiModelProperty(
            position = 5,
            value = "Device type",
            required = true,
            example = "NPT-1200")
    private String type;

    @ApiModelProperty(
            position = 6,
            value = "Version of the device type",
            required = true,
            example = "6.1")
    private String version;

    @ApiModelProperty(
            position = 7,
            value = "Snmp version",
            required = false,
            example = "V2")
    private String snmpVersion;

    @ApiModelProperty(
            position = 8,
            value = "Snmp credentials: user",
            required = false,
            example = "swillin")
    private String snmpUser;

    @ApiModelProperty(
            position = 9,
            value = "Snmp credentials: password",
            required = false,
            example = "****")
    private String snmpPassword;

    @ApiModelProperty(
            position = 10,
            value = "Snmp Privacy Algorithm",
            required = false,
            example = "SNMPPrivacyAlgorithm.DES")
    private SNMPPrivacyAlgorithm snmpPrivacyAlgorithm;

    @ApiModelProperty(
            position = 11,
            value = "Snmp Privacy password",
            required = false,
            example = "****")
    private String snmpPrivacyPassword;

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getAppProtocol() {
        return appProtocol;
    }

    public String getType() {
        return type;
    }

    public String getVersion() {
        return version;
    }

    public SNMPVersion getSnmpVersion() {
        return SNMPVersion.valueOf( snmpVersion);
    }

    public String getSnmpUser() {
        return snmpUser;
    }

    public String getSnmpPassword() {
        return snmpPassword;
    }

    public SNMPPrivacyAlgorithm getSnmpPrivacyAlgorithm() {
        return snmpPrivacyAlgorithm;
    }

    public String getSnmpPrivacyPassword() {
        return snmpPrivacyPassword;
    }
}
