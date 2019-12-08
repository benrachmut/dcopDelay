import java.util.List;

public class AsynchronyDSA_SDP extends Asynchrony {
	
	private double pA,pB,pC,pD;
	private int k;
	public AsynchronyDSA_SDP(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double pA,
			double pB, double pC,double pD, int k) {
		super(dcop, agents, aZ, meanRun);
		this.pA = pA;
		this.pB = pB;
		this.pC = pC;
		this.pD = pD;
		this.k = k;
		this.algo = "AsynchronyDSA_SDP_k="+this.k;
	}
	
	
	public AsynchronyDSA_SDP(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, int k) {
		this(dcop, agents, aZ, meanRun,0.6,0.1,0.4,0.8,k);
	}
	
	public AsynchronyDSA_SDP(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		this(dcop, agents, aZ, meanRun,40);
		

	}

	public AsynchronyDSA_SDP(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double dateKnownRation,double pA,
			double pB, double pC,double pD, int k) {
		super(dcop, agents, aZ, meanRun,dateKnownRation);
		this.pA = pA;
		this.pB = pB;
		this.pC = pC;
		this.pD = pD;
		this.k = k;
		this.algo = "AsynchronyDSA_SDP_k="+this.k;
	}

	public AsynchronyDSA_SDP(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double dateKnownRatio,int k) {
		this(dcop, agents, aZ, meanRun,dateKnownRatio,0.6,0.1,0.4,0.8,k);
	}
	
	

	


	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {
		agentZero.sendAsynchronyDsa(msgToSend);
		
	}


	@Override
	public void agentDecide(int i) {

	
		
		for (AgentField a : agents) {
				a.dsaSdpAsynchronyDecide( pA, pB, pC, pD,k);
			}
		
	}

}
