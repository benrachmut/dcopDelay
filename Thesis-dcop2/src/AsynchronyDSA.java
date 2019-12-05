import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class AsynchronyDSA extends Asynchrony {
	protected double stochastic;

	public AsynchronyDSA(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double stochastic) {
		super(dcop, agents, aZ, meanRun);
		this.stochastic = stochastic;
		this.algo = "DSA_asynchrony_"+stochastic;
	}
	
	public AsynchronyDSA(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double knownCounterRatio, double stochastic) {
		super(dcop, agents, aZ, meanRun, knownCounterRatio);
		this.stochastic = stochastic;
		this.algo = "DSA_asynchrony_"+stochastic+"_"+knownCounterRatio;
	}
	public AsynchronyDSA(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double knownCounterRatio, double ratioOfNeighborsToChangeKnownDate, double stochastic) {
		super(dcop, agents, aZ, meanRun, 1, ratioOfNeighborsToChangeKnownDate);
		this.stochastic = stochastic;
		this.algo = "DSA_"+stochastic;
	}





	public void agentDecide(int i) {
		
		
	
		for (AgentField a : agents) {
				
			
			a.dsaAsynchronyDecide(stochastic);
			}
	}
	
	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {
		agentZero.sendAsynchronyDsa(msgToSend);
	}






	

	

	

	

}
