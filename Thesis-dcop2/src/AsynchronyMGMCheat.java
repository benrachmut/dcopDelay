import java.util.List;


public class AsynchronyMGMCheat extends Asynchrony{

	public AsynchronyMGMCheat(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo="MGM_asynchrony_cheat";
	}

	@Override
	public void agentDecide(int i) {
		for (AgentField a : agents) {
			a.mgmAsynchCheatDecide();
		}
	}
	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {	
		agentZero.sendUnsynchNonMonotonicByValueMsgsMgm(msgToSend);
	}


	
		
	


	




	
	
}
