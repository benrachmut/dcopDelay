import java.util.List;


public class AsynchronyMGMv1 extends Asynchrony{

	public AsynchronyMGMv1(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo="MGM_asynchrony_v1";
	}

	@Override
	public void agentDecide(int i) {
		for (AgentField a : agents) {
			a.mgmDecideV1();
		}
	}
	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {	
		agentZero.sendUnsynchNonMonotonicByValueMsgsMgm(msgToSend);
	}


	
		
	


	




	
	
}
