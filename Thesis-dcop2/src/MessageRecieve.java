
public class MessageRecieve {
private int value;
private int date;
public MessageRecieve(int value, int date) {
	super();
	this.value = value;
	this.date = date;
}
public int getValue() {
	return value;
}
public int getDate() {
	return date;
}

@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "value: "+value+" date: "+date;
	}

}
