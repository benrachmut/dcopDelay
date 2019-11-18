
public abstract class Message<T> implements Comparable<Message> {
	protected AgentField sender;
	protected AgentField reciever;
	protected int delay;
	protected T messageInformation;

	public Message(AgentField sender, AgentField reciever, T messageInformation, int delay) {
		super();
		this.sender = sender;
		this.reciever = reciever;
		this.delay = delay;
		this.messageInformation = messageInformation;
	}

	@Override
	public int compareTo(Message o) {
		return this.delay - o.delay;
	}

	public int getDelay() {
		return this.delay;
	}

	public void setDelay(int input) {
		this.delay = input;

	}

	public AgentField getSender() {
		return sender;
	}

	public AgentField getReciever() {
		return reciever;
	}

	public T getMessageInformation() {
		return messageInformation;
	}
}
	

