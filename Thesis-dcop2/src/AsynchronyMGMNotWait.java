
public class AsynchronyMGMNotWait extends AsynchronyMGM{

	public AsynchronyMGMNotWait(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void agentDecide(int i) {

		for (AgentField a : agents) {
			a.mgmAsynchDecideNotWait();
		}
	}

}
