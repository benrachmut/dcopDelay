
public class MessageRecieve {
private int value;
private int counter;
public MessageRecieve(int value, int counter) {
	super();
	this.value = value;
	this.counter = counter;
}
public int getValue() {
	return value;
}
public int getCounter() {
	return counter;
}

@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "value: "+value+" counter: "+counter;
	}

}
