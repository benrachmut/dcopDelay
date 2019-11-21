
public class MessageValue extends Message <Integer>  {
	
	//private int senderSelfCounter;
	private int decisionCounter;

	public MessageValue(AgentField sender, AgentField reciever, int senderValue, int delay, int counter) {
		super(sender, reciever,senderValue, delay);
		this.decisionCounter = counter;
	}
/*
	public MessageNormal(AgentField sender, AgentField reciever, int senderValue, int delay, int currentIteration, int senderSelfCounter) {
		this( sender,  reciever,  senderValue,  delay,  currentIteration);
	
	}
*/

	@Override
	public String toString() {
		return "sender:"+sender+", reciver:"+reciever+", sender value:"+messageInformation+", delay:"+this.delay;
	}
	
	public MessageValue(MessageValue m) {
		this( m.getSender(),  m.getReciever(),  m.getMessageInformation(),  m.getDelay(), m.getDecisionCounter());
	}
	
	public int getDecisionCounter() {
		return this.decisionCounter;
	}

	
/*
	public int getSenderValue() {
		return senderValue;
	}
*/
	
	

}
