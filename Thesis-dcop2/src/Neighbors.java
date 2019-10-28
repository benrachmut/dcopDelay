import java.util.HashMap;
import java.util.Map;

public class Neighbors implements Comparable<Neighbors> {
	private Agent a1;
	private Agent a2;
	// private double p3;
	private int itirations;
	private boolean isDelay12;
	private boolean isDelay21;
	private Map<Integer, Integer> delayMap12; // key = iteration, value = delay
	private Map<Integer, Integer> delayMap21; // key = iteration, value = delay
	// private int delayUpperBound;

	public Neighbors(Agent a1, Agent a2) {
		super();

		this.a1 = a1;
		this.a2 = a2;
		// this.p3 = 0;
		this.itirations = 0;
		this.delayMap12 = null;
		this.delayMap21 = null;

		// this.isDelay12 = false;
		// this.isDelay12 = false;
	}

	public Neighbors(Agent a1, Agent a2, int itirations) {
		super();

		this.a1 = a1;
		this.a2 = a2;
		// this.p3 = p3;
		this.itirations = itirations;
		this.delayMap12 = new HashMap<Integer, Integer>();
		this.delayMap21 = new HashMap<Integer, Integer>();
		// this.isDelay12 = false;
		// this.isDelay12 = false;
		// this.delayUpperBound=delayUpperBound;
		// createFluds(0,0);
	}

	@Override
	public boolean equals(Object obj) {
		/*
		 * if (!(obj instanceof Neighbors)) { return false; }
		 */

		Neighbors n = (Neighbors) obj;
		Agent na1 = n.getA1();
		Agent na2 = n.getA2();

		return (na1.getId() == a1.getId() && na2.getId() == a2.getId()) 
				|| 
				(na1.getId() == a2.getId() && na2.getId() == a1.getId());

		// return false;
	}

	public boolean isDelay12() {
		return isDelay12;
	}

	public void setDelay12(boolean delay12) {
		this.isDelay12 = delay12;
	}

	public boolean isDelay21() {
		return isDelay21;
	}

	public void setDelay21(boolean delay21) {
		this.isDelay21 = delay21;
	}

	public Agent getA1() {
		return a1;
	}

	public Agent getA2() {
		return a2;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub

		return "{" + this.a1 + "," + this.a2 + "}";
		// return "{A"+this.a1.getId()+",A"+this.a2.getId()+"}";
	}

	public void createFluds(double p3, int delayUB, Double p4) {
		// double rnd = Main.rProblem.nextDouble();
		// if (rnd < p3) {
		// this.isDelay12 = true;
		this.delayMap12 = this.setDelayMap(delayUB, p4, p3);
		// } else {
		// this.isDelay12 = false;
		// this.delayMap12 = new HashMap<Integer, Integer>();
		// }
		// rnd = Main.rProblem.nextDouble();
		// if (rnd < p3) {
		// this.isDelay21 = true;
		this.delayMap21 = this.setDelayMap(delayUB, p4, p3);
		// } else {
		// this.isDelay21=false;
		// this.delayMap21 = new HashMap<Integer, Integer>();
		// }

	}

	private Map<Integer, Integer> setDelayMap(int delayUB, Double p4, Double p3) {
		Map<Integer, Integer> ans = new HashMap<Integer, Integer>();
		for (int i = 0; i < itirations; i++) {
			double rnd = Main.rP3.nextDouble();
			int rndDelay = 0;

			if (rnd < p3) {
				rndDelay = Main.getRandomInt(Main.rDelay, 1, delayUB);

				rnd = Main.rP4.nextDouble();

				if (rnd < p4) {
					rndDelay = Integer.MAX_VALUE;
				}

			}
			ans.put(i, rndDelay);

		}
		return ans;

	}

	public int getDelay12(int currentIteration) {
		// if (!isDelay12) {
		// return 0;
		// }else {
		return delayMap12.get(currentIteration);
	}

	public int getDelay21(int currentIteration) {
		// if (!isDelay21) {
		// return 0;
		// }else {
		return delayMap21.get(currentIteration);
		// }
	}

	@Override
	public int compareTo(Neighbors o) {
		if (this.a1.getId()<o.a1.getId()) {
			return 1;
		}
		if (this.a1.getId()>o.a1.getId()) {
			return -1;
		}else {
			if (this.a2.getId()<o.a2.getId()) {
				return 1;
			}
			if (this.a2.getId()>o.a2.getId()) {
				return -1;
			
		}
		return 0;
	}
	}
}

