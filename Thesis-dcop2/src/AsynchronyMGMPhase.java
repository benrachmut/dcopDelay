import java.util.List;

public class AsynchronyMGMPhase extends Asynchrony{

	public AsynchronyMGMPhase(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo="MGM_phase";
	}

	@Override
	public void agentDecide(int i) {

		for (AgentField a : agents) {
			a.mgmAsynchDecide();
		}
	}
	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {	
		agentZero.sendAsynchronyMgm(msgToSend);
	}
}
