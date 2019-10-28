import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public abstract class UnsynchAnytime extends Unsynch {
	protected SortedSet<AgentField> didDecide;

	protected List<Double> ratioCounterTopCounterChanges;
	protected List<Integer> counterTopChanges;
	public  List<Integer> costOfAllTops;
	public static int counterPermutationAtTop;

	public UnsynchAnytime(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun) {
		super(dcop, agents, aZ, meanRun);
		this.didDecide = new TreeSet<AgentField>();
		Main.rDsa.setSeed(meanRun);
		counterPermutationAtTop = 0;
		ratioCounterTopCounterChanges = new ArrayList<Double>();
		counterTopChanges = new ArrayList<Integer>();
		costOfAllTops = new ArrayList<Integer>();
	}

	// ---- 1
	@Override
	protected void updateWhoCanDecide(int i) {
		for (AgentField a : this.agents) {
			if (i == 0) {
				this.whoCanDecide.add(a);
			} else if (a.getUnsynchFlag()) {
				this.whoCanDecide.add(a);
			}
		}
	}

	// ---- 2

	@Override
	public void agentDecide(int i) {
		algorithmDecide(i);
		setFlagToFalse();
	}

	private void setFlagToFalse() {
		for (AgentField a : whoCanDecide) {
			a.setUnsynchFlag(false);
		}

	}

	public abstract void algorithmDecide(int i); 

	// ---- 3

	@Override
	protected void afterDecideTakeAction(int i) {
		this.agentZero.afterDecideTakeActionUnsynchNonMonotonicByValue(this.didDecide, i);
		this.whoCanDecide = new TreeSet<AgentField>();
		this.didDecide = new TreeSet<AgentField>();
	}

	// ---- 4

	@Override
	public void agentsSendMsgs(List<Message> msgToSend) {
		agentZero.sendUnsynchNonMonotonicByValueMsgs(msgToSend);
		changeFlagForAgentsRecieveMsg(msgToSend);
	}

	private void changeFlagForAgentsRecieveMsg(List<Message> messageSent) {
		SortedSet<AgentField> changeFlag = new TreeSet<AgentField>();
		for (Message m : messageSent) {
			if (!(m instanceof MessageAnyTimeDown) && !(m instanceof MessageAnyTimeUp)) {
				changeFlag.add(m.getReciever());
			}
		}
		for (AgentField a : changeFlag) {
			a.setUnsynchFlag(true);
		}
	}

	@Override
	public void createAnytimeUp(int i) {
		agentZero.createAnyTimeUpUnsynchNonMonotonic(i);
	}

	@Override
	protected void addTopCountersChanges(int i) {
		this.costOfAllTops.add(topCost); 
		if (i>1) {
			int before =this.costOfAllTops.get(i-1);
			int current = this.costOfAllTops.get(i);
			if ( before!= current) {
				counterPermutationAtTop = counterPermutationAtTop+1;
			}
		}
		this.counterTopChanges.add(counterPermutationAtTop);
		int currentCentralistic = Solution.counterCentralisticChanges;
		if (currentCentralistic == 0) {
			ratioCounterTopCounterChanges.add(0.0);
		} else {
			double ratio = (double)counterPermutationAtTop / currentCentralistic;
			ratioCounterTopCounterChanges.add(ratio);
		}

	}

	@Override
	public double getCounterRatio(int i) {
		return this.ratioCounterTopCounterChanges.get(i);
	}

	@Override
	protected int getCounterTop(int i) {
		return counterTopChanges.get(i);
	}

	@Override
	protected int getTopCostNotBest(int i) {
		return costOfAllTops.get(i);
	}

}
