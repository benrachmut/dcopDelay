import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class AsynchronyMonotonic extends Asynchrony {


	public AsynchronyMonotonic(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo = "Monotonic";

	}

	@Override
	public void agentDecide(int i) {
		for (AgentField a : agents) {
			a.monotonicDecide();
		}

	}


	public void agentsSendMsgs(List<Message> msgToSend) {
		agentZero.sendMonotonicMsgs(msgToSend);

	}

	
	



}
