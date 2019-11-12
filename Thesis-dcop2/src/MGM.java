
public class MGM extends Solution {

	public MGM(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo = "mgm";
		// solve();
	}

	@Override
	public void solve() {
		boolean first = true;
		for (int i = 0; i < this.iteration; i++) {
			if (i%50 == 0) {
				System.out.println(i);
			}
			if (!first) {
				agentsSetR();
				System.out.println("agentsSetR();");

				sendAndRecieveRi(i);
				System.out.println("sendAndRecieveRi(i);");

				agentDecide(i);
				System.out.println("agentDecide(i)");

				first = true;
			} else {
				sendAndRecieve(i);
				System.out.println("sendAndRecieve(i)");

				first = false;
			}
			addCostToList(i);
		}

	}

	protected void sendAndRecieveRi(int i) {
		this.agentZero.createRiMsgs(i);
		this.agentZero.sendMsgs(true);

	}

	protected void agentsSetR() {
		for (AgentField a : agents) {
			a.setR();
		}

	}

	@Override
	public void agentDecide(int i) {
		for (AgentField a : agents) {
			a.mgmDecide();
		}
	}

}
