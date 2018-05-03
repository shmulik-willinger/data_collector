package data;

import java.util.ArrayList;

public class CountersResults {

	private static final float NO_VALUE = -1;

	private CollectableEntityDTO port;
	private ArrayList<String> value = new ArrayList<>();

	public ArrayList<String> getValue() {
		return value;
	}

	public void setValue(ArrayList<String> value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CountersResults [port=" + port + ", value=" + value + "]";
	}

	public CountersResults(CollectableEntityDTO port, ArrayList<String> value) {
		super();
		this.port = port;
		this.value = value;
	}

	public CountersResults(CollectableEntityDTO port) {
		this(port, new ArrayList<>());
	}

	public CollectableEntityDTO getPort() {
		return port;
	}

	public void setPort(CollectableEntityDTO port) {
		this.port = port;
	}

}
