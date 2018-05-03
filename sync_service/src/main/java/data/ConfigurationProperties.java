package data;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

public class ConfigurationProperties implements Serializable {

	private static final long serialVersionUID = -1323244033233664170L;
	@Id
	private String key;
	private String value="";

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ConfigurationProperties(String key, String value){
		this.key = key;
		this.value = value;
	}

	@Override
	public String toString() {
		return "ConfigurationProperties [key=" + key + ", value=" + value +"]";
	}


}
