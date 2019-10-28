import java.util.TreeSet;

public class UnsynchDSA extends UnsynchAnytime{
	
	private double stochastic;

	public UnsynchDSA(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double stochastic) {
		super(dcop, agents, aZ, meanRun);
		this.stochastic = stochastic;
		this.algo = "DSA" + stochastic + "asynch";

	}
	
	public void algorithmDecide(int i) {

		this.didDecide = new TreeSet<AgentField>();
		for (AgentField a : whoCanDecide) {
			if (i != 0) {
				boolean didChange = a.dsaDecide(stochastic);
				if (didChange) {
					this.didDecide.add(a);
				}
			} else {
				int value = a.createRandFirstValue();
				a.setValue(value);
				didDecide.add(a);
			}
		}
		
	}


}
