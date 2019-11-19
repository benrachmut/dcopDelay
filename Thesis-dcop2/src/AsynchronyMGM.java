import java.util.List;

public class AsynchronyMGM extends Asynchrony{

	public AsynchronyMGM(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo="MGM_asynchrony";
	}

	@Override
	public void agentDecide(int i) {
		for (AgentField a : agents) {
			a.mgmAsynchDecide();
		}
	}
	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {	
		agentZero.sendUnsynchNonMonotonicByValueMsgsMgm(msgToSend);
	}
}
