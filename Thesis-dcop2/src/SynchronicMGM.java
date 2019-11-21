import java.util.List;

public class SynchronicMGM extends AsynchronyMGM {

	public SynchronicMGM(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void agentDecide(int i) {
		for (AgentField a : agents) {
			a.mgmAsynchDecide();
			a.mgmSynchDecide();
		}
	}
	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {	
		//agentZero.sendUnsynchNonMonotonicByValueMsgsMgm(msgToSend);
	}
}
