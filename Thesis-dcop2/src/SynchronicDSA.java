import java.util.List;

public class SynchronicDSA extends AsynchronyDSA{
	
	public SynchronicDSA(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double stochastic) {
		super(dcop, agents, aZ, meanRun, stochastic, false);
		this.algo = "DSA_synchronic_"+stochastic;
	}




	public void agentDecide(int i) {
	
		for (AgentField a : agents) {
		
			a.dsaSynchronicDecide(stochastic);
			}
	}
	

	public void agentsSendMsgs(List<Message> msgToSend) {
		agentZero.sendSynchronicDsa(msgToSend);
		
	}

	
	
}
