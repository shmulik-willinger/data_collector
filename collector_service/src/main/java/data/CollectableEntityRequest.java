package data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Entities information")
public class CollectableEntityRequest {

	@ApiModelProperty(
			position = 1,
			value = "Entity Name",
			required = true,
			example = "ipOutRequest")
	private String entityName;

	@ApiModelProperty(
			position = 2,
			value = "Entity unique OID",
			required = true,
			example = ".1.3.6.1.2.1.1.3.0")
	private String entityOID;

	@ApiModelProperty(
			position = 3,
			value = "How many places to collect (1=get, 1<getBulk)",
			required = true,
			example = "10")
	private String bulkSize;

	@ApiModelProperty(
			position = 4,
			value = "Device type",
			required = true,
			example = "NPT-1200")
	private String type;

	@ApiModelProperty(
			position = 4,
			value = "Device sub version",
			required = true,
			example = "6.1")
	private String version;

	CollectableEntityRequest() {
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getEntityOID() {
		return entityOID;
	}

	public void setEntityOID(String entityOID) {
		this.entityOID = entityOID;
	}

	public String getBulkSize() {
		return bulkSize;
	}

	public void setBulkSize(String bulkSize) {
		this.bulkSize = bulkSize;
	}


	@JsonInclude(Include.NON_NULL)
	public String getVersion() {
		return version;
	}
	@JsonInclude(Include.NON_NULL)
	public void setVersion(String version) {
		this.version = version;
	}
	public String getProtocol() {
		return entityOID;
	}
	public void setProtocol(String protocol) {
		this.entityOID = protocol;
	}
	public CollectableEntityRequest(String type, String entityOID, String entityName, String bulkSize, String version) {
		super();
		this.type = type;
		this.entityName = entityName;
		this.entityOID = entityOID;
		this.bulkSize = bulkSize;
		this.version = version;
	}

	@Override
	public String toString() {
		return "CollectableEntityRequest [entityName=" + entityName + ", entityOID=" + entityOID
				+ ", type=" + type + "]";
	}


}
