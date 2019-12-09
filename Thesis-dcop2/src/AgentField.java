import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class AgentField extends Agent implements Comparable<AgentField> {

	private int[] domain;
	private int firstValue;
	private int rCounter;
	private Map<Integer, Set<ConstraintNeighbor>> constraint;
	private Map<Integer, MessageRecieve> neighbor; // id and value
	private Map<Integer, MessageRecieve> neighborR;
	private PotentialCost minPC;
	private int r;
	// ---tree stuff
	private AgentField dfsFather;
	private List<AgentField> dfsSons;
	private AgentField anytimeFather;
	private List<AgentField> anytimeSons;
	private Map<Integer, Integer> neigborCounter;
	private Map<Integer, Integer> aboveMap;
	private Map<Integer, Integer> belowMap;
	private int anytimeFirstValue;
	private boolean iHaveAnytimeNews;
	private int decisonCounter;
	private MessageAnyTimeDown msgDown;
	private MessageAnyTimeUp msgUp;
	private HashSet<Permutation> permutationsPast;
	private HashSet<Permutation> permutationsToSend;
	private HashSet<Permutation> permutationComplete;
	private Set<Permutation> sonsAnytimePermutations;
	private Map<Integer, Integer> counterAndValue;
	private Permutation bestPermuation;
	private List<Permutation> anytimeUpRecieved;
	private boolean valueRecieveFlag;
	private boolean rRcieveFlag;
	private boolean waitingForValueStatuesFlag;
	private Map<AgentField, Permutation> lastPCreatedBy;
	private Map<AgentField, List<Permutation>> pCreatedByLists;

	private Random rDsaPersonal;
	private Random rqDsaSDPPersonal;

	private AgentZero az;
	private boolean checkGoFirst;
	private boolean worldChangeSynchFlag;

	// --- synchronic stuff
	private Map<Integer, Boolean> neighborRecieveBoolean;
	private Map<Integer, List<MessageRecieve>> neighborLaterMsgs;
	private Map<Integer, Boolean> neighborRecieveRMsgBoolean;
	private Map<Integer, List<MessageRecieve>> neighborLaterRMsgs;

	private boolean personalKnownCounter;
	private int counterToChangeKnownDate;
	private int counterToChangeKnownDateDecreases;
	private int waitingForCounterSynch;
	private int waitingForCounterRSynch;
	private int kCounterSdp;
	public AgentField(int domainSize, int id) {
		super(id);
		this.az = Main.agentZero;
		this.domain = createDomain(domainSize);
		setValues();
		decisonCounter = 0;
		this.setFirstValueToValue();
		this.constraint = new TreeMap<Integer, Set<ConstraintNeighbor>>();
		this.neighbor = new TreeMap<Integer, MessageRecieve>();
		this.neighborR = new TreeMap<Integer, MessageRecieve>();
		// --- tree stuff
		this.dfsFather = null;
		this.dfsSons = new ArrayList<AgentField>();
		this.anytimeFather = null;
		this.anytimeSons = new ArrayList<AgentField>();
		this.aboveMap = new HashMap<Integer, Integer>();
		this.belowMap = new HashMap<Integer, Integer>();
		this.msgDown = null;
		this.msgUp = null;
		this.bestPermuation = null;
		// this.currentAnyTimeDate = 0;
		setR();
		initSonsAnytimeMessages();
		this.permutationsPast = new HashSet<Permutation>();
		this.permutationsToSend = new HashSet<Permutation>();
		this.permutationComplete= new HashSet<Permutation>();
		
		
		this.counterAndValue = new HashMap<Integer, Integer>();
		this.counterAndValue.put(decisonCounter, value);
		this.iHaveAnytimeNews = false;
		this.valueRecieveFlag = false;

		resetFlagForMgm();
		restartAnytimeUpRecieved();

		rDsaPersonal = new Random();
		rqDsaSDPPersonal = new Random();
		rCounter = 0;
		checkGoFirst = true;
		worldChangeSynchFlag = false;
		personalKnownCounter = false;
		counterToChangeKnownDate = Integer.MAX_VALUE;
		counterToChangeKnownDateDecreases = Integer.MAX_VALUE;
		resetWaitingForCounterSynch();
		restartKsdpCounter();
	}

	public void restartKsdpCounter() {
		kCounterSdp = 0;
	}

	public void resetWaitingForCounterSynch() {
		this.waitingForCounterSynch = 0;
		this.waitingForCounterRSynch=0;
	}

	public void setWorldChangeSynchFlag(boolean b) {
		worldChangeSynchFlag = b;
	}

	public void setCheckCanGoFirst(boolean b) {
		checkGoFirst = b;
	}

	public void restartForSynchronicAlgos() {

		neighborLaterMsgs = new HashMap<Integer, List<MessageRecieve>>();
		neighborRecieveBoolean = new HashMap<Integer, Boolean>();

		for (Integer i : neighbor.keySet()) {
			neighborRecieveBoolean.put(i, false);

			List<MessageRecieve> t = new ArrayList<MessageRecieve>();
			neighborLaterMsgs.put(i, t);
		}

		neighborLaterRMsgs = new HashMap<Integer, List<MessageRecieve>>();
		neighborRecieveRMsgBoolean = new HashMap<Integer, Boolean>();

		for (Integer i : neighborR.keySet()) {
			neighborRecieveRMsgBoolean.put(i, false);

			List<MessageRecieve> t = new ArrayList<MessageRecieve>();
			neighborLaterRMsgs.put(i, t);
		}

	}

	public void monotonicDecide() {
		if (Asynchrony.iter == 0) {
			if (getDfsFather() == null) {
				firstValDsa();
				setCounterAndValueHistory();// try without this line
			}
		} else {
			if (monotonicCanChange()) {
				setCounterAndValueHistory(); // try without this line
				monotonicAbleToDecide();
			}
		}

		setCounterAndValueHistory();

	}

	public void monotonicAbleToDecide() {
		checkToChangeDSA(1);
		this.decisonCounter++;
		az.createUnsynchMsgs(this, false);
	}

	private boolean monotonicCanChange() {
		boolean aboveOneMoreThenMe = checkAllOneAboveMe();
		boolean belowLikeMe = checkbelowLikeMe();
		if (belowLikeMe && aboveOneMoreThenMe) {
			return true;
		}
		return false;
	}

	public void reciveMsgMonotonic(int senderId, int senderValue, int counterOfOther) {
		if (this.personalKnownCounter) {
			int currentDate = this.neighbor.get(senderId).getCounter();
			if (counterOfOther > currentDate) {
				this.neighbor.put(senderId, new MessageRecieve(senderValue, counterOfOther));
				updateCounterAboveOrBelowMono(senderId);
			}
		} else {
			this.neighbor.put(senderId, new MessageRecieve(senderValue, counterOfOther));
			updateCounterAboveOrBelowMono(senderId);
		}
	}

	public void restartLastPCreatedBy() {

		this.lastPCreatedBy = new TreeMap<AgentField, Permutation>();
		for (AgentField agentField : anytimeSons) {
			this.lastPCreatedBy.put(agentField, null);
		}
		this.lastPCreatedBy.put(this, null);

	}

	public void restartPCreatedByLists() {

		this.pCreatedByLists = new TreeMap<AgentField, List<Permutation>>();
		for (AgentField agentField : anytimeSons) {
			this.pCreatedByLists.put(agentField, new ArrayList<Permutation>());
		}
		this.pCreatedByLists.put(this, new ArrayList<Permutation>());

	}

	public boolean isValueRecieveFlag() {
		return valueRecieveFlag;
	}

	public boolean isRRecieveFlag() {
		return rRcieveFlag;
	}

	public boolean isWaitingForValueStatuesFlag() {
		return waitingForValueStatuesFlag;
	}

	public void resetFlagForMgm() {
		this.valueRecieveFlag = false;
		this.rRcieveFlag = true;
		this.waitingForValueStatuesFlag = true;
	}

	private void setValues() {
		// if (Main.synch) {
		// this.firstValue = createRandFirstValue();
		// this.anytimeFirstValue = firstValue;
		// } else {
		this.firstValue = -1;
		this.anytimeFirstValue = -1;
		// }

	}

	public void restartAnytimeUpRecieved() {
		anytimeUpRecieved = new ArrayList<Permutation>();

	}

	public void restartNeighborCounter() {

		neigborCounter = new HashMap<Integer, Integer>();
		for (Integer i : neighbor.keySet()) {
			this.neigborCounter.put(i, 0);
		}

	}

	public void initSonsAnytimeMessages() {
		this.sonsAnytimePermutations = new HashSet<Permutation>();

	}

	/*
	 * public void setUnsynchFlag(boolean input) { this.dsaDecideFlag = input; }
	 */
	public void setDfsFather(AgentField father) {
		this.dfsFather = father;
	}

	public Permutation getBestPermutation() {
		return this.bestPermuation;
	}

	public void resetBestPermutation() {
		this.bestPermuation = null;
	}

	public void resettopHasAnytimeNews() {
		this.iHaveAnytimeNews = false;
	}

	public boolean isTopHasAnytimeNews() {
		return this.iHaveAnytimeNews;
	}

	public void setFirstValueToValue() {
		this.value = firstValue;
		this.anytimeValue = this.anytimeFirstValue;
	}

	public int getDomainSize() {
		return this.domain.length;
	}

	private int[] createDomain(int domainSize) {
		int[] ans = new int[domainSize];

		for (int i = 0; i < ans.length; i++) {
			ans[i] = i;
		}

		return ans;
	}

	public Integer getFirstValue() {
		return this.firstValue;
	}

	public int[] getDomain() {
		return this.domain;
	}

	public int getCurrentThinkCost() {
		int ans = 0;
		if (this.constraint.get(this.value) == null) {
			return 0;
		}
		Set<ConstraintNeighbor> cNatCurrnetValue = this.constraint.get(this.value);

		for (Entry<Integer, MessageRecieve> n : neighbor.entrySet()) {
			int nId = n.getKey();
			int nValue = n.getValue().getValue();
			Agent aTemp = new Agent(nId, nValue);
			for (ConstraintNeighbor cN : cNatCurrnetValue) {
				if (cN.getAgent().equals(aTemp)) {
					ans += cN.getCost();
				}
			}
		}
		return ans;
	}

	public void addConstraintNeighbor(int d1, ConstraintNeighbor constraintNeighbor) {
		if (!constraint.containsKey(d1)) {
			this.constraint.put(d1, new HashSet<ConstraintNeighbor>());
		}
		Set<ConstraintNeighbor> cN = this.constraint.get(d1);
		cN.add(constraintNeighbor);

	}

	public void changeValOfAllNeighbor() {
		for (Entry<Integer, MessageRecieve> n : neighbor.entrySet()) {
			n.setValue(new MessageRecieve(-1, -1));
		}
	}

	public void reciveRMsgFlag(int senderId, int senderR, int dateOfOther) {
		if (this.personalKnownCounter) {
			int currentDate = this.neighborR.get(senderId).getCounter();
			if (dateOfOther > currentDate) {
				this.neighborR.put(senderId, new MessageRecieve(senderR, dateOfOther));
				rRcieveFlag = true;
			}
		} else {
			this.neighborR.put(senderId, new MessageRecieve(senderR, dateOfOther));
			rRcieveFlag = true;
		}

	}

	public void addNeighborR(int idOther) {
		this.neighborR.put(idOther, new MessageRecieve(-1, -1));

	}

	public void addNeighbor(int agentId) {
		this.neighbor.put(agentId, new MessageRecieve(-1, -1));

	}

	@Override
	public int compareTo(AgentField other) {

		return this.id - other.getId();
	}

	public int getNieghborSize() {
		// TODO Auto-generated method stub
		return this.neighbor.keySet().size();
	}

	public Set<Integer> getNSetId() {
		return this.neighbor.keySet();
	}

	public List<AgentField> getDfsSons() {
		return dfsSons;
	}

	public AgentField getDfsFather() {
		return this.dfsFather;
	}

	public Set<Integer> getNeighborIds() {
		return this.neighbor.keySet();

	}

	public void addBelow() {
		List<Integer> temp = new ArrayList<Integer>();
		for (int n : this.neighbor.keySet()) {
			Set<Integer> isAbove = this.aboveMap.keySet();

			boolean isAlreadyInMap = isAbove.contains(n);
			if (!isAlreadyInMap) {
				temp.add(n);
			}

		}
		for (Integer idTemp : temp) {
			this.putInBelowMap(idTemp, 0);
		}

	}

	public int getDecisonCounter() {
		return this.decisonCounter;
	}

	/*
	 * public void setDecisionCounterMonotonic(int i) { this.decisonCounter = i;
	 * Permutation myPermutation = this.createCurrentPermutationMonotonic();
	 * this.permutationsPast.add(myPermutation);
	 * 
	 * }
	 */

	public void setDecisionCounter(int i) {
		this.decisonCounter = i;

	}

	public Permutation createCurrentPermutationNonMonotonic() {
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		for (Entry<Integer, Integer> e : this.neigborCounter.entrySet()) {
			m.put(e.getKey(), e.getValue());
		}
		m.put(this.id, this.decisonCounter);
		int selfCost = this.calSelfCost(m);

		return new Permutation(m, selfCost, this);
	}

	public void putInAboveMap(Integer agentId, Integer counter) {
		this.aboveMap.put(agentId, counter);
	}

	public void putInBelowMap(Integer agentId, Integer counter) {
		this.belowMap.put(agentId, counter);
	}

	public void setAllAboveMap(int input) {
		for (Entry<Integer, Integer> e : aboveMap.entrySet()) {
			e.setValue(input);
		}
	}

	public void setAllBelowMap(int input) {
		for (Entry<Integer, Integer> e : belowMap.entrySet()) {
			e.setValue(input);
		}
	}
	/*
	 * public void reciveUnsynchMonoMsg(int senderId, int senderValue, int date) {
	 * this.reciveMsg(senderId, senderValue, date); //
	 * this.updateCounterAboveOrBelow( senderId, senderValue, date); }
	 */

	public void updateCounterNonMono(int senderId) {

		int currentCounter = neigborCounter.get(senderId);
		neigborCounter.put(senderId, currentCounter + 1);

	}

	private boolean checkbelowLikeMe() {

		if (belowMap.keySet().size() == 0) {
			return true;
		}

		for (int counterBelow : belowMap.values()) {
			if (counterBelow != this.decisonCounter) {
				return false;
			}
		}
		return true;
	}

	private boolean checkAllOneAboveMe() {

		if (aboveMap.keySet().size() == 0) {
			return true;
		}

		for (int counterAbove : aboveMap.values()) {
			if (counterAbove != this.decisonCounter + 1) {
				return false;
			}
		}
		return true;
	}

	/*
	 * public void setValue(int input) { this.value = input;
	 * 
	 * }
	 */
	public void resetMsgUpAndDown() {
		this.msgDown = null;
		this.msgUp = null;
		// currentAnyTimeDate = 0;

	}

	public boolean hasUpMessage() {
		return this.msgUp != null;
	}

	public boolean hasDownMessage() {
		return this.msgDown != null;
	}
	/*
	 * public int calSelfCost() { if (this.value == -1 || neighborIsMinusOne()) {
	 * return Integer.MAX_VALUE; } List<Neighbors> myNeighbors =
	 * Main.dcop.getHisNeighbors(this); // here is the bug!!!! need to take my
	 * neighbors from somewhere else!!!! since dcop has a pointer to the real
	 * agent!!!! int ans = 0; for (Neighbors n : myNeighbors) { int costOfN =
	 * Main.dcop.calCostPerNeighbor(n, true); ans = ans + costOfN; if
	 * (Main.printSelfN) { System.out.println(n+"| "+costOfN); } }
	 * 
	 * return ans; }
	 */

	public int calSelfCost(Map<Integer, Integer> m) {

		if (this.value == -1 || neighborIsMinusOne(m)) {
			return Integer.MAX_VALUE;
		}
		List<Neighbors> myNeighbors = createNeighborsFromM(m);

		int ans = 0;
		for (Neighbors n : myNeighbors) {
			int costOfN = Main.dcop.calCostPerNeighbor(n, true);
			ans = ans + costOfN;
		}

		return ans;
	}

	private List<Neighbors> createNeighborsFromM(Map<Integer, Integer> m) {
		List<Neighbors> ans = new ArrayList<Neighbors>();

		for (Entry<Integer, Integer> e : m.entrySet()) {
			int nId = e.getKey();
			int nValue = e.getValue();
			Agent a1;
			Agent a2;
			if (this.id < nId) {
				a1 = new Agent(this.id, m.get(this.id));
				a2 = new Agent(nId, nValue);
			} else {
				a1 = new Agent(nId, nValue);
				a2 = new Agent(this.id, m.get(this.id));
			}

			if (this.id != nId) {
				Neighbors n = new Neighbors(a1, a2);
				ans.add(n);
			}

		}

		return ans;
	}

	private boolean neighborIsMinusOne(Map<Integer, Integer> m) {
		for (Integer i : m.values()) {
			if (i == -1) {
				return true;
			}
		}
		return false;
	}

	public void reciveMsgValueFlag(int senderId, int senderValue, int counterOfOther) {
		if (this.personalKnownCounter) {
			int currentDate = this.neighbor.get(senderId).getCounter();
			if (counterOfOther > currentDate) {
				this.neighbor.put(senderId, new MessageRecieve(senderValue, counterOfOther));
				valueRecieveFlag = true;

			}
		} else {
			this.neighbor.put(senderId, new MessageRecieve(senderValue, counterOfOther));
			valueRecieveFlag = true;
		}

	}

	public void reciveMsgValueMap(int senderId, int senderValue, int counterOfOther) {

		if (this.personalKnownCounter) {

			boolean firstCond = waitingForCounterSynch == 0 && this.neighbor.get(senderId).getCounter() == -1
					&& counterOfOther == 1;
			boolean secondCond = this.waitingForCounterSynch == this.neighbor.get(senderId).getCounter()
					&& counterOfOther == this.neighbor.get(senderId).getCounter() + 1;
			if (firstCond || secondCond) {
				this.neighbor.put(senderId, new MessageRecieve(senderValue, counterOfOther));
				this.neighborRecieveBoolean.put(senderId, true);
			} else {
				this.neighborLaterMsgs.get(senderId).add(new MessageRecieve(senderValue, counterOfOther));
			}
		}

		// --- date is not known
		else {

			int currentValue = this.neighbor.get(senderId).getValue();
			if (senderValue != currentValue) {
				worldChangeSynchFlag = true;
			}

			this.neighbor.put(senderId, new MessageRecieve(senderValue, counterOfOther));
			this.neighborRecieveBoolean.put(senderId, true);
		}

	}

	public void reciveRMsgMap(int senderId, int senderValue, int counterOfOther) {

		if (this.personalKnownCounter) {

			boolean firstCond = waitingForCounterRSynch == 0 && this.neighborR.get(senderId).getCounter() == -1
					&& counterOfOther == 1;
			boolean secondCond = this.waitingForCounterRSynch == this.neighborR.get(senderId).getCounter()
					&& counterOfOther == this.neighborR.get(senderId).getCounter() + 1;
			if (firstCond || secondCond) {
				this.neighborR.put(senderId, new MessageRecieve(senderValue, counterOfOther));
				this.neighborRecieveRMsgBoolean.put(senderId, true);
			}

			else {
				this.neighborLaterRMsgs.get(senderId).add(new MessageRecieve(senderValue, counterOfOther));
			}
		}

		// --- date is not known
		else {

			// int currentValue = this.neighborR.get(senderId).getValue();

			this.neighborR.put(senderId, new MessageRecieve(senderValue, counterOfOther));
			this.neighborRecieveRMsgBoolean.put(senderId, true);
		}

	}

	public boolean neighborIsMinusOne() {
		for (MessageRecieve i : this.neighbor.values()) {
			if (i.getValue() == -1) {
				return true;
			}
		}
		return false;
	}

	public Permutation createCurrentPermutationMonotonic() {
		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		for (Entry<Integer, Integer> b : this.belowMap.entrySet()) {
			m.put(b.getKey(), b.getValue());
		}

		m.put(this.id, this.decisonCounter);

		for (Entry<Integer, Integer> a : this.aboveMap.entrySet()) {
			m.put(a.getKey(), a.getValue());

		}

		int selfCost = this.calSelfCost(m);

		return new Permutation(m, selfCost);
	}

	/*
	 * public void leafAddAnytimeUp() { Permutation p = createCurrentPermutation();
	 * // this.permutationsPast.add(p); this.permutationsToSend.add(p); }
	 */

	private void handlePToSend(Permutation pToSend) {
		if (this.isAnytimeTop()) {
			this.iHaveAnytimeNews = fatherCheckForPermutationDown(pToSend);
		} else {
			this.permutationsToSend.add(pToSend);
		}

	}

	private boolean fatherCheckForPermutationDown(Permutation pToSend) {
		boolean noBestPermutation = bestPermuation == null;
		boolean costIsNotInf = pToSend.getCost() < Integer.MAX_VALUE - 10000;
		if (noBestPermutation || costIsNotInf) {
			doPermutationToSend(pToSend);
			return true;
		} else {
			int bestPermutationCost = this.bestPermuation.getCost();
			int pToSendCost = pToSend.getCost();
			if (pToSendCost < bestPermutationCost) {
				doPermutationToSend(pToSend);
				return true;
			}
		}
		return false;
	}

	private void doPermutationToSend(Permutation pToSend) {
		bestPermuation = pToSend;

		int bestCounter = bestPermuation.getM().get(id);

		this.anytimeValue = counterAndValue.get(bestCounter);
		// this.counterAndValue = new HashMap<Integer,Integer>();
		// it is questionalbe!!!!
	}

	public boolean hasAnytimeUpToSend() {
		return !this.permutationsToSend.isEmpty();
	}

	public Set<Permutation> getPermutationsToSend() {
		return this.permutationsToSend;
	}

	public void removeAllPermutationToSend() {

		this.permutationsToSend = new HashSet<Permutation>();

	}

	public void resetCounterAndValue() {
		this.counterAndValue = new HashMap<Integer, Integer>();
		this.counterAndValue.put(decisonCounter, value);
	}

	public void setCounterAndValueHistory() {
		this.counterAndValue.put(decisonCounter, value);
	}

	public void addFirstCoupleToCounterAndVal() {
		this.counterAndValue.put(0, value);
	}

	public MessageAnyTimeDown moveDownToSend() {

		// need to take care when recieving msgs
		MessageAnyTimeDown ans = this.msgDown;
		this.msgDown = null;
		return ans;
	}

	private boolean permutationContainAllSon(Permutation itPermutation) {
		for (AgentField son : anytimeSons) {
			int sonId = son.getId();
			if (!itPermutation.containsId(sonId)) {
				return false;
			}

		}
		return true;
	}

	public void addDfsSon(AgentField son) {
		dfsSons.add(son);
	}

	public int sonsDfsSize() {
		return this.dfsSons.size();
	}

	public boolean isAnytimeLeaf() {
		return this.anytimeSons.size() == 0;
	}

	public AgentField getAnytimeFather() {
		// TODO Auto-generated method stub
		return this.anytimeFather;
	}

	public List<AgentField> getAnytimeSons() {
		// TODO Auto-generated method stub
		return this.anytimeSons;
	}

	public boolean isAnytimeTop() {

		return this.anytimeFather == null;
	}

	public void setAnytimeFather(AgentField input) {
		this.anytimeFather = input;

	}

	public void setAnytimeSons(List<AgentField> input) {
		this.anytimeSons = input;

	}

	public void addAnytimeSon(AgentField input) {
		this.anytimeSons.add(input);

	}
	/*
	 * public boolean getDFlag() { return this.unsynchFlag; }
	 */

	// ----- unsynch monotoic-----

	public void updateCounterAboveOrBelowMono(int senderId) {
		boolean isAbove = this.aboveMap.containsKey(senderId);
		int currentCounter;
		if (isAbove) {
			currentCounter = aboveMap.get(senderId);
			aboveMap.put(senderId, currentCounter + 1);
		} else {

			currentCounter = belowMap.get(senderId);
			belowMap.put(senderId, currentCounter + 1);
		}

	}

	/*
	 * public List<Permutation> permuatationFromAnytimeMsg(Permutation msgP) { //
	 * return tryToCombinePermutation(msgP);
	 * 
	 * }
	 */

	public void updateCounterNonMonoWithSelfCounterSent(int senderId, int date) {
		neigborCounter.put(senderId, date);
	}

	/*
	 * public boolean MgmUnsynchDecide() { boolean ans; if
	 * (this.waitingForValueStatuesFlag) { ans = setR(); if (ans) { rCounter++;
	 * this.rRcieveFlag = true; } return ans; } else {
	 * 
	 * ans = this.mgmDecide(); if (ans) { this.decisonCounter++;
	 * this.valueRecieveFlag = true; } } return ans;
	 * 
	 * }
	 */
	/*
	 * public void changeWaitForValueStatues() { if (waitingForValueStatuesFlag) {
	 * waitingForValueStatuesFlag = false; } else { waitingForValueStatuesFlag =
	 * true; }
	 * 
	 * }
	 */
	/*
	 * public void printN() { for (Entry<Integer, MessageRecieve> e :
	 * neighbor.entrySet()) { System.out.print("[" + e.getKey() + "," +
	 * e.getValue().getValue() + "]"); } System.out.println();
	 * 
	 * }
	 */
	/*
	 * public void printNR() { for (Entry<Integer, MessageRecieve> e :
	 * neighborR.entrySet()) { System.out.print("[" + e.getKey() + "," +
	 * e.getValue().getValue() + "]"); } System.out.println();
	 * 
	 * }
	 */
	/*
	 * public void setValueRecieveFlag(boolean b) { this.valueRecieveFlag = b;
	 * 
	 * }
	 */

	public void updateRecieverUponPermutationCreated(Permutation currPermutation, AgentField reciever) {
		
		
		if (isAnytimeLeaf()) {
			addToPermutationToSendUnsynchNonMonoByValue(currPermutation);
		
		} else {
	
			tryToCombinePermutation(currPermutation);
		}
		addToPermutationPast(currPermutation);
	}

	// --------General use-----

	/**
	 * used by all algorithms to get first val rFirstValue seed is reset each time
	 * the algo is called
	 * 
	 * @return random int from the domain
	 */
	public int createRandFirstValue() {
		return Main.getRandomInt(Main.rFirstValue, 0, this.domain.length - 1);
	}

	// --------DSA-----

	public void dsaSynchronicDecide(double stochastic) {
		if (Asynchrony.iter == 0) {
			firstValDsa();
		} else {

			if (checkNeighborRecieveBoolean()) {
				checkToChangeDSA(stochastic);
				updateMapsAfterReset(neighborRecieveBoolean, neighborLaterMsgs, neighbor);
				this.decisonCounter++;
				az.createUnsynchMsgs(this, false);
				doAnytime();

			}
		}
	}

	private boolean checkNeighborRecieveBoolean() {
		boolean valueOfNeighborRecieveBoolean = checkBooleaValueInMap(neighborRecieveBoolean);
		if (!valueOfNeighborRecieveBoolean) {
			return false;
		}
		this.waitingForCounterSynch += 1;
		return true;
	}

	private boolean checkNeighborRecieveRMsgsBoolean() {

		boolean valueOfNeighborRecieveBoolean = checkBooleaValueInMap(this.neighborRecieveRMsgBoolean);

		if (!valueOfNeighborRecieveBoolean) {
			return false;
		}
		
		waitingForCounterRSynch += 1;
		return true;

	}

	private static void updateMapsAfterReset(Map<Integer, Boolean> booleanMap,
			Map<Integer, List<MessageRecieve>> laterMsgMap, Map<Integer, MessageRecieve> msgsMap) {
		for (Integer i : booleanMap.keySet()) {
			booleanMap.put(i, false);
		}

		for (Entry<Integer, List<MessageRecieve>> e : laterMsgMap.entrySet()) {
			List<MessageRecieve> recievedAlready = e.getValue();
			if (!recievedAlready.isEmpty()) {
				MessageRecieve minRecieve = Collections.min(recievedAlready, new ComparatorMsgCounter());
				int counterOfCurrentMsg = msgsMap.get(e.getKey()).getCounter();
				if (minRecieve.getCounter() == counterOfCurrentMsg + 1) {
					recievedAlready.remove(minRecieve);
					msgsMap.put(e.getKey(), minRecieve);
					booleanMap.put(e.getKey(), true);
				}
			}
		}

	}

	private static boolean checkBooleaValueInMap(Map<Integer, Boolean> input) {
		for (Boolean b : input.values()) {
			if (!b) {
				return false;
			}
		}
		return true;
	}

	public void dsaSdpAsynchronyDecide(double pA, double pB, double pC, double pD, int k) {
		if (Asynchrony.iter == 0) {
			firstValDsa();
		} else {
			boolean didChange = false;
			if (valueRecieveFlag) {
				valueRecieveFlag = false;
				
				kCounterSdp++;
				boolean secondBestFlag = false;
				
				
				double ratio = calcRatio();
				if (kCounterSdp == k) {
					kCounterSdp=0;
					double q = calcQSdp(pC,pD,ratio);					
					double rnd = rDsaPersonal.nextDouble();
					if (rnd<q) {
						this.value = secondBest();
						secondBestFlag = true;
					}
				}	
				if (!secondBestFlag) {
					double stochastic = pA+Math.min(pB, ratio);
					checkToChangeDSA(stochastic);			
				}
				doAnytime();
				this.decisonCounter++;		
				az.createUnsynchMsgs(this, false);
			}	
		}
	}

	

	private double calcRatio() {
		List<PotentialCost> pCostsList = findPotentialCost();
		double currentCost = findCurrentCost(pCostsList);

		PotentialCost minPotentialCost = Collections.min(pCostsList);
		double newCost = minPotentialCost.getCost();

		return Math.abs(currentCost-newCost)/currentCost;
		
	}

	private int secondBest() {
		List<PotentialCost> pCostsList = findPotentialCost();
		PotentialCost minPotentialCost = Collections.min(pCostsList);
		pCostsList.remove(minPotentialCost);		
		PotentialCost minSecondPotentialCost = Collections.min(pCostsList);

		return minSecondPotentialCost.getValue();
	}

	private double calcQSdp(double pC, double pD, double cond) {
		
		
		if (cond>1) {
			return 0;
		}else {
			return Math.max(pC, pD-cond);
		}
	}
/*
	private boolean checkToChangeDSAsdp(double p) {

		List<PotentialCost> pCosts = findPotentialCost();
		int currentPersonalCost = findCurrentCost(pCosts);

		PotentialCost minPotentialCost = Collections.min(pCosts);
		int minCost = minPotentialCost.getCost();

		boolean shouldChange = false;
		if (minCost <= currentPersonalCost) {
			shouldChange = true;
		}
		if (this.value == -1) {
			shouldChange = true;
		}
		return maybeChange(shouldChange, minPotentialCost, p);
	}
	*/

	public void dsaAsynchronyDecide(double stochastic) {
		if (Asynchrony.iter == 0) {
			firstValDsa();
		} else {
			boolean didChange = false;
			if (valueRecieveFlag) {
				valueRecieveFlag = false;
				didChange = checkToChangeDSA(stochastic);
				this.decisonCounter++;
				doAnytime();
				az.createUnsynchMsgs(this, false);
			}
			// if (didChange) {

			// }

			// else {
			// explorationIncrease();
			// }

		}
		// this.az.afterDecideTakeActionUnsynchNonMonotonicByValue(this.didDecide, i);
	}

	private void checkToChangeDSASecond() {

		List<PotentialCost> pCosts = findPotentialCost();
		int currentPersonalCost = findCurrentCost(pCosts);

		PotentialCost minPotentialCost = Collections.min(pCosts);
		// int minCost = minPotentialCost.getCost();
		pCosts.remove(minPotentialCost);
		PotentialCost secondMin = Collections.min(pCosts);
		this.value = secondMin.getValue();
		// return maybeChange(shouldChange, minPotentialCost, stochastic);
	}

	private boolean checkToChangeDSA(double stochastic) {

		List<PotentialCost> pCosts = findPotentialCost();
		int currentPersonalCost = findCurrentCost(pCosts);

		PotentialCost minPotentialCost = Collections.min(pCosts);
		int minCost = minPotentialCost.getCost();

		boolean shouldChange = false;
		if (minCost <= currentPersonalCost) {
			shouldChange = true;
		}
		if (this.value == -1) {
			shouldChange = true;
		}
		return maybeChange(shouldChange, minPotentialCost, stochastic);
	}

	private void explorationIncrease() {
		if (Main.useCounterToChangeTrans) {
			this.counterToChangeKnownDateDecreases--;
			if (counterToChangeKnownDateDecreases == 0) {
				counterToChangeKnownDateDecreases = counterToChangeKnownDate;
				if (Main.secondBest) {
					checkToChangeDSASecond();
				} else {
					if (personalKnownCounter) {
						personalKnownCounter = false;
					} else {
						personalKnownCounter = true;
					}
				}
			}
		}

	}

	private void firstValDsa() {
		valueRecieveFlag = false;
		this.value = createRandFirstValue();
		// setValue(value);
		this.decisonCounter++;
		az.createUnsynchMsgs(this, false);

	}

	private List<PotentialCost> findPotentialCost() {
		List<PotentialCost> pCosts = new ArrayList<PotentialCost>();
		for (int i = 0; i < domain.length; i++) {
			Set<ConstraintNeighbor> neighborsAtDomain = this.constraint.get(i);
			int costPerValue = calCostPerValue(neighborsAtDomain);
			PotentialCost pC = new PotentialCost(domain[i], costPerValue);
			pCosts.add(pC);
		}
		return pCosts;
	}

	private int findCurrentCost(List<PotentialCost> pCosts) {
		for (PotentialCost pC : pCosts) {
			if (pC.getValue() == this.value) {
				return pC.getCost();
			}
		}
		return -1;
	}

	private boolean maybeChange(boolean shouldChange, PotentialCost minPotentialCost, double stochastic) {
		if (shouldChange) {


			double rnd = rDsaPersonal.nextDouble();
			if (rnd < stochastic) {

				int lastValue = this.value;
				this.value = minPotentialCost.getValue();

				if (lastValue == this.value) {
					return false;
				}

				return true;
			}

		}

		return false;
	}

	private int calCostPerValue(Set<ConstraintNeighbor> neighborsAtDomain) {
		int ans = 0;

		if (neighborsAtDomain == null) {
			return 0;
		}
		for (ConstraintNeighbor cN : neighborsAtDomain) {
			Agent a = cN.getAgent();
			int aId = a.getId();

			int aCheckedValue = a.getValue();
			int aNeighborKnownValue = this.neighbor.get(aId).getValue();

			if (aCheckedValue == aNeighborKnownValue) {
				int costFromNeighbor = cN.getCost();
				ans += costFromNeighbor;
			}
		}
		return ans;
	}

	public void setDsaSeed(int meanRun) {
		int newSeed = meanRun + this.id;
		rDsaPersonal.setSeed(meanRun * 88 + this.id * 555);
		rqDsaSDPPersonal.setSeed(meanRun * 77 + this.id * 333);
	}

	public void resetDsaSeed(int meanRun) {
		this.rDsaPersonal = new Random(meanRun * 88 + this.id * 555);
		this.rqDsaSDPPersonal.setSeed(meanRun * 77 + this.id * 333);
	}

	// --------ANY TIME-----

	private void doAnytime() {

		if (Main.anytime) {
			Permutation myPermutation = createCurrentPermutationByValue();
			if (myPermutation!=null) {
				if (this.isAnytimeLeaf()) {
					this.addToPermutationToSendUnsynchNonMonoByValue(myPermutation);
				} else {
					this.tryToCombinePermutation(myPermutation);
				}
			}	
		}

	}

	public List<Permutation> tryToCombinePermutation(Permutation currentP) {
		List<Permutation> listToAddToPast = new ArrayList<Permutation>();
		List<Permutation> completePermutation = new ArrayList<Permutation>();
		List<Permutation> ans = new ArrayList<Permutation>();

		iterateOverPastPermuataion(currentP, listToAddToPast, completePermutation);

		
		
		for (Permutation p : listToAddToPast) {
			addToPermutationPast(p);
			ans.add(p);
		}

		for (Permutation p : completePermutation) {
			addToPermutationToSendUnsynchNonMonoByValue(p);
			ans.add(p);
		}
		addToPermutationPast(currentP);
		return ans;

	}

	public void recieveAnytimeDown(Message msg) {

		MessageAnyTimeDown mad = (MessageAnyTimeDown) msg;
		this.msgDown = mad;
		Permutation pFromMad = mad.getMessageInformation();
		recieveBetterPermutation(pFromMad);
		

	}

	public Permutation createCurrentPermutationByValue() {

		Map<Integer, Integer> m = new HashMap<Integer, Integer>();
		for (Entry<Integer, MessageRecieve> e : this.neighbor.entrySet()) {
			int nId = e.getKey();
			int nValue = e.getValue().getValue();
			if (nValue==-1) {
				return null;
			}
			m.put(nId, nValue);
		}
		m.put(this.id, this.value);
		int cost = calSelfCost(m);
		
		if (this.bestPermuation != null) {
			int costBestPermutation = this.bestPermuation.getCost();
			if (cost>costBestPermutation) {
				return null;
			}
		}
		
		
		return new Permutation(m, cost, this);
	}

	private void iterateOverPastPermuataion(Permutation msgP, List<Permutation> listToAddToPast,
			List<Permutation> completePermutation) {

		for (Permutation pastP : this.permutationsPast) {

			
			Permutation toAdd = msgP.canAdd(this, pastP);

			if (toAdd != null) {

				if (toAdd.getFlagReady()) {
					completePermutation.add(toAdd);
				} else {
					listToAddToPast.add(toAdd);
				}
			}
		}

	}

	public void addToPermutationPast(Permutation input) {
		if (Main.memoryVersion == 1 || Main.memoryVersion == 3) {

			//int time = Asynchrony.iter;
			//input.setTimeEnter(time);
			addToSet(input, permutationsPast);
		}
		if (Main.memoryVersion == 2) {
			// addToSet(input, permutationsPast);

			memoryVersionConstant(input);

		}

		// this.permutationsPast.add(input);

	}

	private void memoryVersionConstant(Permutation input) {
		boolean inputAlreadyInSet = checkIfInputAlreadyInSet(input);
		if (!inputAlreadyInSet) {
			if (this.permutationsPast.size() > Main.memoryMaxConstant) {
				Permutation pToDelete = selectPToDelete(input);

				// Permutation currentP = getPAccordingToCompIndex(input);
				// boolean maxSimilarityFlag = getMaxSimilarityFlagAccordingToCompIndex();

				// 1 = maxSimilarityToAgentView, 2 = fifo
				// 3 = maxSimilarityToLastPFromSender, 4 = maxSimilarityToLastPFromSender

				// Comparator<Permutation> c = new ComparatorPermutationSimilarty(currentP,
				// maxSimilarityFlag);
				// Permutation minP = Collections.min(this.permutationsPast, c);
				this.permutationsPast.remove(pToDelete);
			}

			this.lastPCreatedBy.put(input.getCreator(), input);
			this.pCreatedByLists.get(input.getCreator()).add(input);

			int time = Asynchrony.iter;
			input.setTimeEnter(time);
			// addToSet(input, permutationsPast);
			permutationsPast.add(input);
		}

	}

	// 1 = maxSimilarityToAgentView, 2 = minSimilarityToAgentView,
	// 3 = minSimilarityToLastPFromSender
	private Permutation selectPToDelete(Permutation input) {
		if (Main.currentComparatorForMemory == 1) {
			boolean maxSimilarityFlag;
			Permutation p = createCurrentPermutationByValue();
			maxSimilarityFlag = true;

			Comparator<Permutation> c = new ComparatorPermutationSimilarty(p, maxSimilarityFlag);
			return Collections.min(this.permutationsPast, c);
		}

		if (Main.currentComparatorForMemory == 2) {
			Comparator<Permutation> c = new ComparatorPermuatationByTimeEnter();
			return Collections.min(this.permutationsPast, c);
		}

		else {
			return comp3();
		}
	}

	private Permutation comp3() {
		Map<AgentField, Integer> keepScoreOfEachNeighbor = new TreeMap<AgentField, Integer>();
		Map<AgentField, Permutation> keepPermutationOfEachNeighbor = new TreeMap<AgentField, Permutation>();

		for (Entry<AgentField, List<Permutation>> e : pCreatedByLists.entrySet()) {
			Permutation lastP = this.lastPCreatedBy.get(e.getKey());
			if (lastP != null) {
				boolean maxSimilartiyFlag = true;
				Comparator<Permutation> c = new ComparatorPermutationSimilarty(lastP, maxSimilartiyFlag);
				if (!e.getValue().isEmpty()) {
					Permutation minSimilarityP = Collections.min(e.getValue(), c);
					keepPermutationOfEachNeighbor.put(e.getKey(), minSimilarityP);
					int similarityInt = minSimilarityP.getSimilartyCounterTo(lastP);
					keepScoreOfEachNeighbor.put(e.getKey(), similarityInt);
				}
			}

		}
		Permutation toDelete = null;
		AgentField createdBy = null;
		int minCounterOfAll = Collections.min(keepScoreOfEachNeighbor.values());
		for (Entry<AgentField, Integer> e : keepScoreOfEachNeighbor.entrySet()) {
			if (e.getValue() == minCounterOfAll) {
				toDelete = keepPermutationOfEachNeighbor.get(e.getKey());
				createdBy = e.getKey();
				break;
			}
		}

		this.pCreatedByLists.get(createdBy).remove(toDelete);
		return toDelete;
	}

	private boolean checkIfInputAlreadyInSet(Permutation input) {
		for (Permutation pPast : this.permutationsPast) {
			if (input.equals(pPast)) {
				return true;
			}
		}
		return false;
	}

	public void addToPermutationToSendUnsynchNonMonoByValue(Permutation input) {

		if (this.isAnytimeTop()) {

			Asynchrony.topCost = input.getCost();
			Asynchrony.counterPermutationAtTop = Asynchrony.counterPermutationAtTop + 1;
			if (this.bestPermuation == null || this.bestPermuation.getCost() > input.getCost()) {
				recieveBetterPermutation(input);
				iHaveAnytimeNews = true;
				System.out.println("BEST PERMUTATIONS =cost: " + input.getCost() + " permutation past size: "
						+ this.permutationsPast.size());
			}

		} else {
		
	
			boolean wasSent = addToSet(input, permutationComplete);		
			if (!wasSent) {
				addToSet(input, permutationsToSend);
			}
			
		}
	}
/*
	public boolean addToPermutationToSend(Permutation input) {
		return addToSet(input, permutationsToSend);

	}
	*/

	private void recieveBetterPermutation(Permutation input) {
		bestPermuation = input;
		
		
		
		printTopCompletePermutation(input);
		this.anytimeValue = input.getM().get(this.id);

	}

	private void printTopCompletePermutation(Permutation input) {
		int realCost = Solution.dcopS.calRealSolForDebug(input.getM());

		if (this.isAnytimeTop()) {

			if (input.getCost() == realCost) {
				// System.out.println(input);
			} else {
				System.err.println("cost should be: " + realCost + " |" + input);

			}
		}

	}

	private boolean addToSet(Permutation input, HashSet<Permutation> setToAddTo) {
		boolean flag = false;
		
		
		for (Permutation pFromList : setToAddTo) {
		
			if (pFromList.equals(input)) {
				flag = true;
			}
		}
		if (!flag) {
			setToAddTo.add(input);
		}
		return flag;
	}

	public void iterateOverSonsAndCombineWithInputPermutation(Permutation input) {
		// Permutation myPermutation = this.createCurrentPermutation();
		for (Permutation sonPermutation : this.sonsAnytimePermutations) {
			if (sonPermutation.isCoherent(input)) {
				Permutation pToSend = Permutation.combinePermutations(sonPermutation, input, this);
				handlePToSend(pToSend);
			}
		}
	}

	/**
	 * case 2- called when messaged recieved is anyTimeUp from agent zero
	 * 
	 * @return
	 */
	public void recieveAnytimeUpMonotonic(Message msg) {
		MessageAnyTimeUp mau = (MessageAnyTimeUp) msg;

		Permutation p = mau.getMessageInformation();
		Set<Permutation> belowCoherentWithMessage = combinePermutationFromMsgWithOtherPermutationsOfReceiverSon(p);

		Set<Permutation> pastCoherentWithMessage = combinePermutationFromMsgWithOtherPermutationsOfReceiverPast(p);

		if (belowCoherentWithMessage.isEmpty() || pastCoherentWithMessage.isEmpty()) {
			return;
		}
		combineBelowAndPast(belowCoherentWithMessage, pastCoherentWithMessage);

	}

	public Set<Permutation> combinePermutationFromMsgWithOtherPermutationsOfReceiverSon(Permutation msgPermutation) {
		boolean flag = false;
		Set<Permutation> pToAdd = new HashSet<Permutation>();
		Set<Permutation> pToRemove = new HashSet<Permutation>();

		for (Permutation sonsPermutation : sonsAnytimePermutations) {
			if (msgPermutation.isCoherent(sonsPermutation)) {
				flag = true;
				pToAdd.add(Permutation.combinePermutations(sonsPermutation, msgPermutation, this));
				pToRemove.add(sonsPermutation); // the un
			}
		}

		if (!flag) {
			this.sonsAnytimePermutations.add(msgPermutation);
			pToAdd.add(msgPermutation);
			return pToAdd;

		} else {
			this.sonsAnytimePermutations.removeAll(pToRemove);
			this.sonsAnytimePermutations.addAll(pToAdd);
		}

		// pToAdd will contain only permutations that are ready to be sent
		Iterator<Permutation> it = pToAdd.iterator();
		while (it.hasNext()) {
			Permutation itPermutation = it.next();
			if (!permutationContainAllSon(itPermutation)) {
				it.remove();
			}

		}
		return pToAdd;
	}

	private Set<Permutation> combinePermutationFromMsgWithOtherPermutationsOfReceiverPast(
			Permutation permutationFromMessage) {
		Set<Permutation> ans = new HashSet<Permutation>();
		for (Permutation pastPermutation : permutationsPast) {
			if (pastPermutation.isCoherent(permutationFromMessage)) {
				ans.add(pastPermutation);
			}
		}
		return ans;
	}

	private void combineBelowAndPast(Set<Permutation> belowCoherentWithMessage,
			Set<Permutation> pastCoherentWithMessage) {

		for (Permutation belowP : belowCoherentWithMessage) {
			for (Permutation aboveP : pastCoherentWithMessage) {
				if (belowP.isCoherent(aboveP)) {
					Permutation pToSend = Permutation.combinePermutations(belowP, aboveP);

					handlePToSend(pToSend);

				}
			}
		}

	}

	public void recieveAnytimeDownMonotonic(Message input) {
		//// maybe bug here

		MessageAnyTimeDown mad = (MessageAnyTimeDown) input;

		// if (mad.getDate() > this.currentAnyTimeDate) {
		this.msgDown = mad;
		doPermutationToSend(mad.getMessageInformation());
		// this.currentAnyTimeDate = mad.get;
		// }
	}

	public void recieveAnytimeUp(Message msg) {
		if (msg instanceof MessageAnyTimeUp) {

			MessageAnyTimeUp mau = (MessageAnyTimeUp) msg;
			Permutation msgP = mau.getMessageInformation();
			tryToCombinePermutation(msgP);

		} else {
			System.err.println("logical bug from recieveAnytimeUpBfs");
		}

	}

	// --------MGM-----
	public void mgmAsynchDecide() {

		if (Asynchrony.iter == 0) {
			firstValMgm();

		} else {
			// boolean didChange = false;
			if (waitingForValueStatuesFlag) {
				if (valueRecieveFlag) {
					waitForValMgm();
				}
			} // waitingForValueStatuesFlag = true
			else {
				if (rRcieveFlag) {
					waitForRMgm();
				}
			} // waitingForValueStatuesFlag = false
		}
	}

	public void mgmAsynchDecideNotWait() {

		if (Asynchrony.iter == 0) {
			firstValMgm();

		} else {
			// boolean didChange = false;
			// if (waitingForValueStatuesFlag) {
			if (valueRecieveFlag) {
				waitForValMgm();
			}
			// } // waitingForValueStatuesFlag = true
			// else {
			if (rRcieveFlag) {
				waitForRMgm();
			}
			// } // waitingForValueStatuesFlag = false
		}
	}

	private void waitForValMgm() {
		boolean didChange = false;
		valueRecieveFlag = false;
		didChange = setR();
		// if (didChange) {
		rCounter++;
		this.rRcieveFlag = true;

		// }
		az.createUnsynchMgmMsgs(this, false);
		waitingForValueStatuesFlag = false;

	}

	private void waitForRMgm() {
		/*
		 * boolean canGo = true; if (checkGoFirst) { canGo = checkGoFirst(); if (canGo)
		 * { checkGoFirst = false; } }
		 */

		// if (canGo) {
		boolean didChange = false;
		rRcieveFlag = false;
		didChange = this.mgmValueDecide();
		// if (didChange) {
		this.decisonCounter++;
		az.createUnsynchMgmMsgs(this, false);
		this.valueRecieveFlag = true;
		doAnytime();
		// }
		waitingForValueStatuesFlag = true;
	}

	// }

	private boolean checkGoFirst() {
		for (MessageRecieve m : neighborR.values()) {
			int r = m.getValue();
			if (r == -1) {
				return false;
			}
		}
		return true;
	}

	public void mgmSynchronicDecide() {
		if (Asynchrony.iter == 0) {
			firstValMgm();
		} else {
			if (waitingForValueStatuesFlag) {

				if (checkNeighborRecieveBoolean()) {
					waitForValMgmSynch();
					updateMapsAfterReset(this.neighborRecieveBoolean, this.neighborLaterMsgs, this.neighbor);

				}
			} // waitingForValueStatuesFlag = true
			else {

				if (checkNeighborRecieveRMsgsBoolean()) {
					waitForRMgmSynch();
					updateMapsAfterReset(this.neighborRecieveRMsgBoolean, this.neighborLaterRMsgs, this.neighborR);

				}
			} // waitingForValueStatuesFlag = false
		}

	}

	private void waitForRMgmSynch() {
		/*
		 * boolean canGo = true; if (checkGoFirst) { canGo = checkGoFirst(); if (canGo)
		 * { checkGoFirst = false; } }
		 */

		// if (canGo) {
		// boolean didChange = false;
		rRcieveFlag = false;
		// didChange =
		this.mgmValueDecide();
		// if (didChange) {
		this.decisonCounter++;
		az.createUnsynchMgmMsgs(this, false);
		this.valueRecieveFlag = true;
		doAnytime();
		// }
		waitingForValueStatuesFlag = true;
		// }

	}

	private void waitForValMgmSynch() {
		boolean didChange = false;
		valueRecieveFlag = false;
		setR();
		// if (didChange) {
		rCounter++;
		this.rRcieveFlag = true;
		az.createUnsynchMgmMsgs(this, false);
		// }
		waitingForValueStatuesFlag = false;

	}

	private void firstValMgm() {
		this.value = createRandFirstValue();
		this.decisonCounter++;
		az.createUnsynchMgmMsgs(this, false);
		waitingForValueStatuesFlag = true;

	}

	private Entry<Integer, MessageRecieve> getMaxRFromNeighbors() {
		Entry<Integer, MessageRecieve> max = null;
		boolean flag = false;
		for (Entry<Integer, MessageRecieve> nr : neighborR.entrySet()) {
			if (!flag) {
				max = nr;
				flag = true;
			}
			int maxR = max.getValue().getValue();
			int nrR = nr.getValue().getValue();
			if (nrR > maxR) {
				max = nr;
			}
			int idMax = max.getKey();
			int idNr = nr.getKey();
			if (nrR == maxR && idMax > idNr) {
				max = nr;
			}
		}
		return max;
	}

	public void changeValR() {

		for (Entry<Integer, MessageRecieve> n : neighborR.entrySet()) {
			n.setValue(new MessageRecieve(-1, -1));
		}

	}

	public boolean mgmValueDecide() {
		Entry<Integer, MessageRecieve> maxRInMap = getMaxRFromNeighbors();

		if (maxRInMap == null) {
			this.value = this.domain[0];
			return false;
		}
		int maxRVal = maxRInMap.getValue().getValue();
		if (this.r > maxRVal) {
			this.value = this.minPC.getValue();
			return true;
		}
		int maxRId = maxRInMap.getKey();
		if (this.r == maxRVal && this.id < maxRId) {
			this.value = this.minPC.getValue();
			return true;

		}
		return false;
	}

	public boolean setR() {
		List<PotentialCost> pCosts = findPotentialCost();
		PotentialCost minPotentialCost = Collections.min(pCosts);

		int currentCost = findCurrentCost(pCosts);
		int minCost = minPotentialCost.getCost();

		this.minPC = minPotentialCost;
		if (currentCost <= minCost) {
			int oldR = this.r;
			int newR = 0;
			this.r = newR;
			if (Asynchrony.iter == 1) {
				return true;
			}

			if (newR != oldR) {
				return true;
			} else {
				return false;
			}
		}

		if (currentCost > minCost) {
			int oldR = this.r;
			int newR = currentCost - minCost;
			this.r = newR;
			if (newR != oldR) {
				return true;
			} else {
				return false;
			}

		}
		return false;

	}

	public int getR() {
		return this.r;
	}

	// --------GETTERS SETTERS-----
	public void restartAnytimeValue() {
		this.anytimeValue = -1;

	}

	public boolean isNeighbor(int inputId) {
		// TODO Auto-generated method stub
		return this.neighbor.containsKey(inputId);
	}

	public void setValueRecieveFlag(boolean b) {
		this.valueRecieveFlag = b;

	}

	public void setRRecieveFlag(boolean b) {
		this.rRcieveFlag = b;

	}

	public void restartPermutationsSets() {
		this.permutationsPast = new HashSet<Permutation>();
		permutationComplete = new HashSet<Permutation>();
		this.permutationsToSend = new HashSet<Permutation>();

	}
	
	/*
	public void restartAnytimeToSend() {
		this.permutationsToSend = new HashSet<Permutation>();

	}
	*/

	public Set<Permutation> getPastPermutations() {
		// TODO Auto-generated method stub
		return this.permutationsPast;
	}

	public Map<Integer, Integer> getNeighborCounter() {
		return this.neigborCounter;
	}

	public void setAz(AgentZero az) {
		this.az = az;
	}

	public void setRCounter(int input) {
		this.rCounter = input;
	}

	public int getRCounter() {
		return rCounter;
	}

	public void setKnownCounter(boolean b) {
		this.personalKnownCounter = b;

	}

	public boolean isKnownCounter() {
		return personalKnownCounter;

	}

	public void setCounterToChangeKnownDate(double ratioOfNeighborsToChangeKnownDate) {
		this.counterToChangeKnownDate = (int) (this.getNieghborSize() * ratioOfNeighborsToChangeKnownDate);
		this.counterToChangeKnownDateDecreases = counterToChangeKnownDate;

	}

	public void setAnytimeValueLonleyNode() {
		this.anytimeValue = 0;
		
	}

	public void setCheatBestPermutation() {
		Map<Integer, Integer> m = new HashMap<Integer,Integer>();
		m.put(this.id, 0);
		this.bestPermuation = new Permutation(m, 0, this);
		
	}

}
