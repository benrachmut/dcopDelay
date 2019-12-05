import java.util.List;

public class AsynchronyDSA_SDP extends Asynchrony {
	
	private double pA,pB,pC,pD,k;

	public AsynchronyDSA_SDP(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double pA,
			double pB, double pC,double pD, int k) {
		super(dcop, agents, aZ, meanRun);
	
		this.pA = pA;
		this.pB = pB;
		this.pC = pC;
		this.pD = pD;
		this.k = k;
	}
	
	
	public AsynchronyDSA_SDP(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, int k) {
		super(dcop, agents, aZ, meanRun);
	
		this.pA = 0.6;
		this.pB = 0.15;
		this.pC = 0.4;
		this.pD = 0.8;
		this.k = k;
	}
	
	public AsynchronyDSA_SDP(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.pA = 0.6;
		this.pB = 0.15;
		this.pC = 0.4;
		this.pD = 0.8;
		this.k = 40;

	}



	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void agentDecide(int i) {

		
		for (AgentField a : agents) {
				a.dsaSdpAsynchronyDecide( pA, pB, pC, pD);
			}
		
	}

}
