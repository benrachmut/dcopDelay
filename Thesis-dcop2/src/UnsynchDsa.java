import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class UnsynchDsa extends Unsynch {
	private double stochastic;


	//public static int counterPermutationAtTop;

	public UnsynchDsa(Dcop dcop, AgentField[] agents, AgentZero aZ, int meanRun, double stochastic) {
		super(dcop, agents, aZ, meanRun);

		this.stochastic = stochastic;
		Main.rDsa.setSeed(meanRun);
		this.algo = "DSA asynchrony";

	
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
		dsaDecide(i);
		setFlagToFalse();
	}

	private void setFlagToFalse() {
		for (AgentField a : whoCanDecide) {
			a.setUnsynchFlag(false);
		}

	}

	private void dsaDecide(int i) {

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

	// ---- 3

	@Override
	protected void afterDecideTakeAction(int i) {
		//if (Main.trySendValueAsPermutation) {
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

	

	

	

	

}
