import java.util.List;

public class AsynchronyMGMv2 extends Asynchrony{

	public AsynchronyMGMv2(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo="MGM_asynchrony_v2";
	}

	@Override
	public void agentDecide(int i) {
		for (AgentField a : agents) {
			a.mgmDecideV2();
		}
	}
	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {	
		agentZero.sendUnsynchNonMonotonicByValueMsgsMgm(msgToSend);
	}
}
