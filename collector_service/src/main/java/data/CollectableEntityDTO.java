package data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class CollectableEntityDTO {

	@Id
	private String entityOID;
	private String entityName;
	private int bulkSize;
	private String type;
	private String version;

	public CollectableEntityDTO() {
	}

	public CollectableEntityDTO(CollectableEntityRequest collectableEntityRequest)
	{
		super();
		this.entityName = collectableEntityRequest.getEntityName();
		this.entityOID = collectableEntityRequest.getEntityOID();
		this.bulkSize = Integer.parseInt( collectableEntityRequest.getBulkSize() );
		this.type = collectableEntityRequest.getType();
		this.version = collectableEntityRequest.getVersion();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEntityOID() {
		return entityOID;
	}

	public void setEntityOID(String entityOID) {
		this.entityOID = entityOID;
	}

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getBulkSize() {
		return bulkSize;
	}

	public void setBulkSize(int bulkSize) {
		this.bulkSize = bulkSize;
	}


	//private String host;
	//private CollectionProtocol protocol;
	//private String dn;


	public CollectableEntityDTO(String type, String entityName, String entityOID, int bulkSize, Date date, String version) {
		super();
		this.entityName = entityName;
		this.entityOID = entityOID;
		this.bulkSize = bulkSize;
		this.type = type;
		this.version = version;
	}
	public static Date convertDate2dateKey(Date date) {

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		String retVal = formatter.format(date);
		Date dateWithoutTime = null;
		try {
			dateWithoutTime = formatter.parse(retVal);
		} catch (ParseException e) {
			log.error("Can't get dateWithoutTime for " + retVal);
		}

		return dateWithoutTime;
	}

	@Override
	public String toString() {
		return "CollectableEntityDTO [key=" + entityOID + ", entityName=" + entityName + "]";
	}

	public static CollectableEntityDTO cloneWithoutValues(CollectableEntityDTO src, int bulkSize, Date newDate, String version) {
		return new CollectableEntityDTO(src.getType(), src.getEntityOID(), src.getEntityName(), bulkSize, newDate, version);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entityName == null) ? 0 : entityName.hashCode());
		result = prime * result + ((entityOID == null) ? 0 : entityOID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CollectableEntityDTO other = (CollectableEntityDTO) obj;
		if (entityName == null) {
			if (other.entityName != null)
				return false;
		} else if (!entityName.equals( other.entityName ))
			return false;

		if (entityOID == null) {
			if (other.entityOID != null)
				return false;
		} else if (!entityOID.equals( other.entityOID ))
			return false;

		return true;
	}
}
