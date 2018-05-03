package data;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
public class ValueRecord {

	private LocalDateTime date;

	private ArrayList<String> value;


	@Override
	public String toString() {
		return "ValueRecord [date=" + date + ", value=" + value + "]";
	}

	public ValueRecord(LocalDateTime  date, ArrayList<String> value) {
		super();
		this.date=date;
		this.value = value;
	}

	public ArrayList<String> getValue() {
		return value;
	}

	public void setValue(ArrayList<String> value) {
		this.value = value;
	}

	public static ValueRecord createValueRecord(LocalDateTime date, ArrayList<String> value) {
		// SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss"); // I
		// don't think we need seconds
		// String dateToStr = format.format(date);
		ValueRecord valueRecord = new ValueRecord(date, value);
		log.debug("valueRecord=" + valueRecord);
		return valueRecord;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(int secondsOffset) {
		this.date = date;
	}
}
