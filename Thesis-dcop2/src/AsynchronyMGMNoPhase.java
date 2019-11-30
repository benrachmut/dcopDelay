
public class AsynchronyMGMNoPhase extends AsynchronyMGMPhase{

	public AsynchronyMGMNoPhase(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo="MGM_no_phase";
	}
	
	@Override
	public void agentDecide(int i) {

		for (AgentField a : agents) {
			a.mgmAsynchDecideNotWait();
		}
	}

}
