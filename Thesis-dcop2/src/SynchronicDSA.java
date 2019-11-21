import java.util.List;

public class SynchronicDSA extends AsynchronyDSA{
	
	public SynchronicDSA(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double stochastic) {
		super(dcop, agents, aZ, meanRun, stochastic);
		this.algo = "DSA_synchronic";
	}




	public void agentDecide(int i) {
		if (Main.currMeanRun==1) {
			System.out.println();
		}
		for (AgentField a : agents) {
				a.dsaSynchronicDecide(stochastic);
			}
	}
	

	public void agentsSendMsgs(List<Message> msgToSend) {
		agentZero.sendSynchronicDsa(msgToSend);
		
	}

	
	
}
