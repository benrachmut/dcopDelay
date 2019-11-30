import java.util.List;


public class SynchronicMGM extends AsynchronyMGMPhase{

	public SynchronicMGM(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo="MGM_synchronic";
	}

	@Override
	public void agentDecide(int i) {
		if (i == 0 && Main.currentUb==2) {
			System.out.println();
		}
		for (AgentField a : agents) {
			a.mgmSynchronicDecide();
		}
	}
	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {	
		agentZero.sendSynchronicMgm(msgToSend);
	}


	
		
	


	




	
	
}
