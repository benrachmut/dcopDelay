import java.util.List;
import java.util.TreeSet;

public class UnsynchMgm extends Unsynch{

	public UnsynchMgm(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.algo="UnsynchMgm";
	}

	@Override
	protected void addTopCountersChanges(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void updateWhoCanDecide(int i) {
		
		
		
		for (AgentField af : agents) {
			
			if (i != 0) {
				if (af.isWaitingForValueStatuesFlag()) {
					if (af.isValueRecieveFlag()) {
						af.setValueRecieveFlag(false);
						this.whoCanDecide.add(af);
					}	
				}else {
					if (af.isRRecieveFlag()) {
						af.setRRecieveFlag(false);
						this.whoCanDecide.add(af);
					}
				}
				
			} 
			else {
				this.whoCanDecide.add(af);
			}		
		}	
	}

	@Override
	protected void afterDecideTakeAction(int i) {
		this.agentZero.afterDecideTakeActionUnsynchNonMonotonicByValueMgm(this.didDecide, i);
		this.whoCanDecide = new TreeSet<AgentField>();
		
		agentsChangeStatues();
		this.didDecide = new TreeSet<AgentField>();
		
	}

	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createAnytimeUp(int i) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getCounterRatio(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getCounterTop(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getTopCostNotBest(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void agentDecide(int i) {
		this.didDecide = new TreeSet<AgentField>();
		for (AgentField a : whoCanDecide) {
			if (i != 0) {
				boolean didChange = a.MgmUnsynchDecide();
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
