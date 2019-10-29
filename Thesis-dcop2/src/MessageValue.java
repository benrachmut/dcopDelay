
public class MessageValue extends Message <Integer>  {
	
	private int senderValue;
	private int decisonCounter;

	public MessageValue(AgentField sender, AgentField reciever, int senderValue, int delay, int currentIteration) {
		super(sender, reciever,senderValue, delay, currentIteration);
		decisonCounter = sender.getDecisonCounter();
		this.senderValue = senderValue;
	}
/*
	public MessageNormal(AgentField sender, AgentField reciever, int senderValue, int delay, int currentIteration, int senderSelfCounter) {
		this( sender,  reciever,  senderValue,  delay,  currentIteration);
	
	}
*/

	@Override
	public String toString() {
		return super.toString()+", value:"+this.senderValue+", dc:"+this.decisonCounter;
	}
	
	public MessageValue(MessageValue m) {
		this( m.getSender(),  m.getReciever(),  m.getMessageInformation(),  m.getDelay(),  m.getDate());
	}

	public int getDecisonCounter() {
		return this.decisonCounter;
	}
	public int getSenderValue() {
		return this.senderValue;
	}
	

	
	

}
