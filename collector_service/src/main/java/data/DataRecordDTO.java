package data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;


public class DataRecordDTO {
	private static final Logger logger = LoggerFactory.getLogger( DataRecordDTO.class);

	public DataRecordDTO() {
	}

	public DataRecordDTO(String key) {
		super();
		this.key = key;
	}

	@Id
	String key;

	private List<ValueRecord> values = new ArrayList<ValueRecord>();

	public void addValue(ValueRecord valueRecord) {
		values.add(valueRecord);
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<ValueRecord> getValues() {
		return values;
	}

	public void setValues(List<ValueRecord> values) {
		this.values = values;
	}
}
