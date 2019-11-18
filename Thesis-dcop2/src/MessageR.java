
public class MessageR extends Message<Integer> {
	private int rCounter;

	public MessageR(AgentField sender, AgentField reciever, Integer messageInformation, int delay, int counter) {
		super(sender, reciever, messageInformation, delay);
		this.rCounter = counter;

	}
	
	public int getRCounter() {
		return this.rCounter;
	}
}
