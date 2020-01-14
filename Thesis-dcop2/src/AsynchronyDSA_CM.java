import java.util.List;

public class AsynchronyDSA_CM extends AsynchronyDSA{

	public AsynchronyDSA_CM(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double stochastic) {
		super(dcop, agents, aZ, meanRun, stochastic, false);
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	public void agentsSendMsgs(List<Message> msgToSend) {
		agentZero.sendAsynchronyDsa_cm(msgToSend);

	}

}
