import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;


public class AgentZero {

	private List<Message> messageBox;
	// private List<MessageNormal> rMessageBox;
	private List<MessageValue> timeStempMessageBox;

	private Set<Neighbors> neighbors;
	private int iteration;
	private double p3;
	private double p4;
	private int delayUb;
	private AgentField[] agents;

	public AgentZero(int iteration, Set<Neighbors> neighbors, AgentField[] agents) {
		this.agents = agents;
		this.messageBox = new ArrayList<Message>();
		// rMessageBox = new ArrayList<MessageNormal>();
		timeStempMessageBox = new ArrayList<MessageValue>();
		this.neighbors = neighbors;
		this.iteration = iteration;
	}
/*
	public void createMsgs(int currentIteration) {
		for (Neighbors n : this.neighbors) {

			AgentField a1 = (AgentField) n.getA1();

			AgentField a2 = (AgentField) n.getA2();

			int delay12 = createDelay(false);
			int delay21 = createDelay(false);

			// int delay12 = n.getDelay12(currentIteration);
			// int delay21 = n.getDelay21(currentIteration);

			MessageValue msg12 = new MessageValue(a1, a2, a1.getValue(), delay12);
			MessageValue msg21 = new MessageValue(a2, a1, a2.getValue(), delay21);

			this.messageBox.add(msg12);
			this.messageBox.add(msg21);
		}

	}
	*/
	
	/*

	public void createRiMsgs(int currentIteration) {
		for (Neighbors n : this.neighbors) {
			AgentField a1 = (AgentField) n.getA1();
			AgentField a2 = (AgentField) n.getA2();
			int delay12 = createDelay(false);
			int delay21 = createDelay(false); // int delay12 =
			//n.getDelay12(currentIteration); // int delay21 =
			//n.getDelay21(currentIteration);

			Message msg12 = new MessageR(a1, a2, a1.getR(), delay12);
			Message msg21 = new MessageR(a2, a1, a2.getR(), delay21);

			this.messageBox.add(msg12);
			this.messageBox.add(msg21);
		}

	}

*/
	/*
	 * public void emptyRMessageBox() { this.rMessageBox.clear();
	 * 
	 * }
	 */
	private List<Message> handleDelay(List<Message> input) {
		Collections.sort(input);
		List<Message> msgToSend = new ArrayList<Message>();
		Iterator it = input.iterator();
		while (it.hasNext()) {
			Message msg = (Message) it.next();
			if (msg.getDelay() == 0) {
				msgToSend.add(msg);
				it.remove();
			} else {
				msg.setDelay(msg.getDelay() - 1);
			}
		}
		return msgToSend;
	}

	public List<Message> handleDelay() {
		Collections.sort(this.messageBox);
		List<Message> msgToSend = new ArrayList<Message>();
		Iterator it = this.messageBox.iterator();

		while (it.hasNext()) {
			Message msg = (Message) it.next();	
			if (msg.getDelay() == 0) {
				msgToSend.add(msg);
				it.remove();
			} else {
				msg.setDelay(msg.getDelay() - 1);
			}
		}
		return msgToSend;
	}

	public void changeCommunicationProtocol(double p3Input, int delayUbInput, Double p4Input) {
		this.p3 = p3Input;
		this.delayUb = delayUbInput;
		this.p4 = p4Input;

		for (Neighbors n : this.neighbors) {
			n.createFluds(p3, delayUb, p4);
		}

	}

/*
	public void sendMsgs(boolean r) {
		
		List<Message> tMessage = new ArrayList<Message>();
		for (Message m : this.messageBox) {
			if (r) {
				tMessage.add(m);
			}
			else {
				tMessage.add(m);
			}
		}
		List<Message> msgToSend = handleDelay(tMessage);
		for (Message msg : msgToSend) {
			int senderId = msg.getSender().getId();
			AgentField reciver = msg.getReciever();
			if (msg instanceof MessageValue) {
				MessageValue mmm = (MessageValue) msg;
				int senderValue = mmm.getMessageInformation();
				int msgCounter = mmm.getDecisionCounter();
				reciver.reciveMsg(senderId, senderValue,  msgCounter);
			}
		}

	}
	*/

	public void emptyMessageBox() {
		this.messageBox.clear();
	}

	public int getUb() {
		return this.delayUb;
	}

	public double getP3() {
		return this.p3;
	}

	public void emptyTimeStempBoxMessage() {
		this.timeStempMessageBox.clear();

	}

	private int createDelay(boolean anytimeFlag) {
		int rndDelay;
		rndDelay = 0;

		Random r = whichRandom(anytimeFlag);

		double rnd = Main.rP3.nextDouble();
		if (rnd < Main.currentP3) {

			rndDelay = Main.getRandomInt(r, 1, Main.currentUb);
			rnd = Main.rP4.nextDouble();
			if (rnd < Main.currentP4) {
				rndDelay = Integer.MAX_VALUE;
			}

		}
		return rndDelay;
	}

	private Random whichRandom(boolean anytimeFlag) {
		if (anytimeFlag) {
			return Main.rDelayAnytime;
		}
		return Main.rDelay;
	}

	private List<AgentField> getNeighborsAgents(AgentField a) {
		List<AgentField> ans = new ArrayList<AgentField>();
		for (Integer neigborNumber : a.getNeighborIds()) {
			for (AgentField aTemp : agents) {
				if (aTemp.getId() == neigborNumber) {
					ans.add(aTemp);
				}
			}
		}
		return ans;
	}

	// -------------- Unsynch Monotonic-------------

	public void sendMonotonicMsgs(List<Message> msgToSend) {
		for (Message msg : msgToSend) {
			sendUnsynchMonotonicMsg(msg);
		}
	}

	private void sendUnsynchMonotonicMsg(Message msg) {
		int senderId = msg.getSender().getId();
		AgentField reciever = msg.getReciever();

		if ((msg instanceof MessageValue)) {
			MessageValue msg1 = (MessageValue) msg;
			int senderValue = msg1.getMessageInformation();
			reciever.reciveMsgMonotonic(senderId, senderValue, msg1.getDecisionCounter());
		}
	}

	/*
	 * private Map<Integer, SortedSet<MessageValue>>
	 * getMsgMapByReciever(List<MessageValue> msgToSend) {
	 * 
	 * Map<Integer, SortedSet<MessageValue>> ans = new HashMap<Integer,
	 * SortedSet<MessageValue>>(); for (MessageValue msg : msgToSend) { int
	 * recieverId = msg.getReciever().getId(); if (!ans.containsKey(recieverId)) {
	 * ans.put(recieverId, new TreeSet<MessageValue>(new ComparatorMsgDate())); }
	 * ans.get(recieverId).add(msg); } return ans; }
	 */
	
	/*
	private void anytimeMechanismAfterRecieveMsg(Set<AgentField> agentsRecieved) {
		

		for (AgentField reciever : agentsRecieved) {
			Permutation currPermutation = reciever.createCurrentPermutationNonMonotonic();
			List<Permutation> toSend = new ArrayList<Permutation>();
			String reason = "";
			boolean leafFlag = false;
			if (reciever.isAnytimeLeaf()) {
				reciever.addToPermutationToSendUnsynchNonMonoByValue(currPermutation);
				reason = "leaf a" + reciever.getId() + "creates a message because counters change";
				leafFlag = true;
			} else {
				// reciever.addToPermutationPast(currPermutation);
				toSend = reciever.tryToCombinePermutation(currPermutation);
				reason = "combine between permutations " + currPermutation;
			}
			
			reciever.addToPermutationPast(currPermutation);
		} // for msgs

	}
*/

	private Set<AgentField> getAgents(Set<Integer> input) {
		Set<AgentField> ans = new HashSet<AgentField>();
		for (Integer i : input) {
			for (AgentField a : agents) {
				if (a.getId() == i) {
					ans.add(a);
					break;
				}
			}
		}
		return ans;
	}

	private void sendUnsynchNonMonotonicByValueMsgMgm(Message msg) {
		//---	
		int senderId = msg.getSender().getId();
		AgentField reciever = msg.getReciever();
		if (msg instanceof MessageValue) {
			MessageValue mmm = (MessageValue)msg;
			int senderValue = mmm.getMessageInformation();
			int counter = mmm.getDecisionCounter();
			reciever.reciveMsgValueFlag(senderId, senderValue, counter);
			if (Main.anytime) {
				Permutation p = reciever.createCurrentPermutationByValue();
				reciever.updateRecieverUponPermutationOneByOne(p, reciever);
			}
		} // normal message
		
		if (msg instanceof MessageR) {
			MessageR mrr = (MessageR) msg;
			int senderR = mrr.getMessageInformation();
			int counter = mrr.getRCounter();
			reciever.reciveRMsg(senderId, senderR, counter);
		} // normal message
		if (msg instanceof MessageAnyTimeUp) {
			reciever.recieveAnytimeUpBfs(msg);
		}
		if (msg instanceof MessageAnyTimeDown) {
			reciever.recieveAnytimeDownNonMonotonicByValue(msg);
		}

	}
	
	
	
	
	


	

	public void afterDecideTakeActionUnsynchMonotonic(Collection<AgentField> agentsThatChanged, int currentIteration) {
		for (AgentField a : agentsThatChanged) {
			//a.setDecisionCounterMonotonic(a.getDecisonCounter() + 1);
			a.setCounterAndValueHistory();
			createUnsynchMsgs(a,  false);
		}
	}

	private void addPermutatioToAnytimeMechanism(AgentField a, Permutation p) {

		if (a.isAnytimeLeaf()) {
			a.addToPermutationToSendUnsynchNonMonoByValue(p);
		}

		else {
			a.tryToCombinePermutation(p);
		}
	}

	private void printDebugAnytimePermutation(boolean leaf, AgentField reciever, Permutation currPermutation,
			List<Permutation> toSend, String reason) {
		if (leaf) {

			System.out.println("from: a" + reciever.getId() + ", to: a" + reciever.getAnytimeFather().getId() + ", "
					+ currPermutation + " " + ", reason: " + reason);
		} else {
			for (Permutation toAdd : toSend) {
				reason = reason + "and combined with " + toAdd;
				if (!reciever.isAnytimeTop()) {
					System.out.println("from: a" + reciever.getId() + ", to: a" + reciever.getAnytimeFather().getId()
							+ ", " + toAdd + " " + ", reason: combine between Permutations");
				} else {
					System.out.println("from: a" + reciever.getId() + ", to: ax, " + toAdd
							+ ", reason: combine between Permutations");
				}
			}
		}
	}

	public void createUnsynchMsgs(AgentField currentAgent, boolean anytimeFlag) {

		List<AgentField> neighborsAgents = getNeighborsAgents(currentAgent);
		for (AgentField n : neighborsAgents) {
			Message m = createUnsynchOneMsg(currentAgent, n,  anytimeFlag);
			this.messageBox.add(m);
		}
	}

	private Message createUnsynchOneMsg(AgentField sender, AgentField reciever, boolean anytimeFlag) {
		int decisionCounter = sender.getDecisonCounter();
		int senderValue = sender.getValue();
		int delay = this.createDelay(anytimeFlag);
		return new MessageValue(sender, reciever, senderValue, delay, decisionCounter);
	}


	public void createAnyTimeUpUnsynchMono(int currentIteration) {
		// List<AgentField> agentsSendAnytime = new ArrayList<AgentField>()
		for (AgentField a : agents) {
			boolean isHead = a.getAnytimeFather() == null;
			if (a.hasAnytimeUpToSend() && !isHead) {
				Set<Permutation> pToSendA = a.getPermutationsToSend();
				for (Permutation p : pToSendA) {
					int delay = this.createDelay(true);
					Message m = new MessageAnyTimeUp(a, a.getAnytimeFather(), p, delay);
					this.messageBox.add(m);
				}
				a.removeAllPermutationToSend();
			} // if not had and have something to send
		}
	}

	public void createAnyTimeUpUnsynchNonMonotonic(int currentIteration) {
		// List<AgentField> agentsSendAnytime = new ArrayList<AgentField>()
		int anytimeUpCreated = 0;
		for (AgentField a : agents) {
			boolean isHead = a.getAnytimeFather() == null;
			if (a.hasAnytimeUpToSend() && !isHead) {
				Set<Permutation> pToSendA = a.getPermutationsToSend();
				for (Permutation p : pToSendA) {
					// p.createdIncluded(a);
					int delay = this.createDelay(true);
					Message m = new MessageAnyTimeUp(a, a.getAnytimeFather(), p, delay);
					this.messageBox.add(m);
					anytimeUpCreated++;
					
				}
				a.removeAllPermutationToSend();
			} // if not had and have something to send
		}
		//System.out.println("anytimeUpCreated:"+anytimeUpCreated);
	}

	public void createAnyTimeDownUnsynchMono(int date) {

		for (AgentField a : this.agents) {
			if (a.isAnytimeTop()) {
				if (a.isTopHasAnytimeNews()) {
					a.resettopHasAnytimeNews();
					placeAnytimeDownMessageInBox(a, date);
				}
			} else {
				MessageAnyTimeDown m = a.moveDownToSend(); // take msg from msg to send down and deliever it
				if (!a.isAnytimeLeaf()) {
					if (m != null) {
						placeAnytimeDownMessageInBox(a, date);
					}
				}
			}
		}

		/*
		 * for (AgentField top : fathers) { if (top.isTopHasAnytimeNews()) {
		 * top.resettopHasAnytimeNews(); placeAnytimeDownMessageInBox(top, date); } }
		 * 
		 * for (AgentField a : this.agents) { if (!a.isAnytimeTop()) {
		 * MessageAnyTimeDown m = a.moveDownToSend(); if (m != null) {
		 * placeAnytimeDownMessageInBox(a, date); } } }
		 */
	}

	private void placeAnytimeDownMessageInBox(AgentField from, int date) {
		for (AgentField son : from.getAnytimeSons()) {
			AgentField sender = from;
			AgentField reciever = son;
			int delay = this.createDelay(true);
			int currentIteration = date;
			Permutation permutationToSend = from.getBestPermutation();
			Message m = new MessageAnyTimeDown(sender, reciever, permutationToSend, delay);
			this.messageBox.add(m);
		}

	}

	public List<Message> getMsgBox() {
		return this.messageBox;
	}

	

	public void afterDecideTakeActionUnsynchNonMonotonicByValue(SortedSet<AgentField> agentsThatChanged, int date) {
		//----
		
		for (AgentField a : agentsThatChanged) {
			createUnsynchMsgs(a,  false);
			if (Main.anytime) {
				Permutation myPermutation = a.createCurrentPermutationByValue();
				addPermutatioToAnytimeMechanism(a, myPermutation);
			}
		}

	}
	
	public void afterDecideTakeActionUnsynchNonMonotonicByValueMgm(SortedSet<AgentField> agentsThatChanged, int date) {
		//----
		
		for (AgentField a : agentsThatChanged) {
			createUnsynchMgmMsgs(a, false);
			if (Main.anytime) {
				Permutation myPermutation = a.createCurrentPermutationByValue();
				addPermutatioToAnytimeMechanism(a, myPermutation);
			}
		}

	}

	void createUnsynchMgmMsgs(AgentField currentAgent, boolean anytimeFlag) {
		List<AgentField> neighborsAgents = getNeighborsAgents(currentAgent);
		for (AgentField n : neighborsAgents) {
			Message m = null;
			if (Asynchrony.iter==0) {
				 m = createUnsynchOneMsg(currentAgent, n,  anytimeFlag);
			}
			else if (currentAgent.isWaitingForValueStatuesFlag()) {
				m = createUnsynchROneMsg(currentAgent, n,  anytimeFlag);
			}else {
				 m = createUnsynchOneMsg(currentAgent, n,  anytimeFlag);
			}
			this.messageBox.add(m);
		}
	}

	private Message createUnsynchROneMsg(AgentField sender, AgentField reciever, 
			boolean anytimeFlag) {
		int senderR = sender.getR();
		int counter = sender.getRCounter();
		int delay = this.createDelay(anytimeFlag);
		return new MessageR(sender, reciever, senderR, delay, counter);
	}

	public void sendAsynchronyDsa(List<Message> msgToSend) {
		Collections.sort(msgToSend, new ComparatorMsgSenderId());
		Collections.reverse(msgToSend);
		for (Message msg : msgToSend) {
			sendASingleMsgDsaAsynchrony(msg);
		}
	}
	
	private void sendASingleMsgDsaAsynchrony(Message msg) {
		//---	
		int senderId = msg.getSender().getId();
		AgentField reciever = msg.getReciever();
		if (msg instanceof MessageValue) {
			MessageValue mv =(MessageValue) msg; 
			int senderValue = mv.getMessageInformation();
			int counter = mv.getDecisionCounter();
			reciever.reciveMsgValueFlag(senderId, senderValue, counter);
			if (Main.anytime) {
				Permutation p = reciever.createCurrentPermutationByValue();
				reciever.updateRecieverUponPermutationOneByOne(p, reciever);
			}
		} // normal message
		
		if (msg instanceof MessageAnyTimeUp) {
			reciever.recieveAnytimeUpBfs(msg);
		}
		if (msg instanceof MessageAnyTimeDown) {
			reciever.recieveAnytimeDownNonMonotonicByValue(msg);
		}

	}
	
	
	public void sendSynchronicDsa(List<Message> msgToSend) {
		Collections.sort(msgToSend, new ComparatorMsgSenderId());
		Collections.reverse(msgToSend);
		for (Message msg : msgToSend) {
			sendASingleMsgDsaSynchronic(msg);
		}
	}
	
	
	
	private void sendASingleMsgDsaSynchronic(Message msg) {
		//---	
		int senderId = msg.getSender().getId();
		AgentField reciever = msg.getReciever();
		if (msg instanceof MessageValue) {
			MessageValue mv =(MessageValue) msg; 
			int senderValue = mv.getMessageInformation();
			int counter = mv.getDecisionCounter();
			reciever.reciveMsgValueMap(senderId, senderValue, counter);
			if (Main.anytime) {
				Permutation p = reciever.createCurrentPermutationByValue();
				reciever.updateRecieverUponPermutationOneByOne(p, reciever);
			}
		} // normal message
		
		if (msg instanceof MessageAnyTimeUp) {
			reciever.recieveAnytimeUpBfs(msg);
		}
		if (msg instanceof MessageAnyTimeDown) {
			reciever.recieveAnytimeDownNonMonotonicByValue(msg);
		}

	}
	
	
	
	public void sendUnsynchNonMonotonicByValueMsgsMgm(List<Message> msgToSend) {
		Collections.sort(msgToSend, new ComparatorMsgSenderId());
		Collections.reverse(msgToSend);
		for (Message msg : msgToSend) {
			sendUnsynchNonMonotonicByValueMsgMgm(msg);
		}
	}
/*
	private void debugToSeeSequence(List<MessageValue> msgToSend) {
		int minDate = 0;

		if (!msgToSend.isEmpty()) {
			MessageValue m = Collections.min(msgToSend, new ComparatorMsgSenderId());
			minDate = m.getDate();
		}

		if (msgToSend.size() > 2 && minDate == 2) {
			for (int i = 0; i < msgToSend.size(); i++) {
				System.out.println("location: " + i + ", date:" + msgToSend.get(i).getDate());

			}
		}

	}
	*/
/*
	private void anytimeMechanismAfterRecieveMsgByValue(Set<AgentField> agentsRecieved) {
		for (AgentField reciever : agentsRecieved) {
			Permutation currPermutation = reciever.createCurrentPermutationByValue();
			updateRecieverUponPermutationOneByOne(currPermutation, reciever);
		} // for msgs
	}
	*/
/*
	public void sendUnsynchNonMonotonicMsgs(List<Message> msgToSend) {
		Set<Integer> integerRecieved = new HashSet<Integer>();

		for (Message msg : msgToSend) {
			sendUnsynchNonMonotonicMsg(msg);
			integerRecieved.add(msg.getReciever().getId());
		}
		/*
		 * if (Main.tryAllMailBox) { Set<AgentField> agentsRecieved =
		 * getAgents(integerRecieved); anytimeMechanismAfterRecieveMsg(agentsRecieved);
		 * }
		 */
	//}

	/*
	 * private void updateCounterOfReciever(AgentField reciever, int senderId,
	 * MessageNormal msg) { if (Main.trySendSelfCounter) {
	 * reciever.updateCounterNonMonoWithSelfCounterSent(senderId,
	 * msg.getSenderSelfCounter()); } else {
	 * reciever.updateCounterNonMono(senderId); }
	 * 
	 * }
	 */
	
	
	
	/*
	private void sendUnsynchNonMonotonicMsg(Message msg) {
		int senderId = msg.getSender().getId();
		AgentField reciever = msg.getReciever();

		if (msg instanceof MessageValue) {

			MessageValue m = (MessageValue) msg;
			int senderValue = m.getMessageInformation();

			reciever.reciveMsg(senderId, senderValue, msg.getDate());

			// updateCounterOfReciever(reciever, senderId, msg);

			// if (!Main.tryAllMailBox) {
			Permutation currPermutation = reciever.createCurrentPermutationNonMonotonic();
			updateRecieverUponPermutationOneByOne(currPermutation, reciever);
			// }
			/*
			 * if (Main.tryAgentRememberSequence) {
			 * reciever.recieveMsgAndPlaceItInSequence(msg); }
			 */

		//} // normal message

	/*
		if (msg instanceof MessageAnyTimeUp) {
			reciever.recieveAnytimeUpBfs(msg);
		}
		if (msg instanceof MessageAnyTimeDown) {
			// still need to do
		}

	}
*/
	
	
	/*
	 * public void selfChangeReport(Set<AgentField> didDecide) { for (AgentField a :
	 * didDecide) { a.get }
	 * 
	 * }
	 */
/*
	public void afterDecideTakeActionUnsynchNonMonotonicByCounter(Collection<AgentField> agentsThatChanged, int date) {
		for (AgentField a : agentsThatChanged) {
			a.setDecisionCounterNonMonotonic(a.getDecisonCounter() + 1);
			a.setCounterAndValueHistory(); // //record in map of agent its current value and self counter
			Permutation myPermutation = a.createCurrentPermutationNonMonotonic();
			a.addToPermutationPast(myPermutation);
			/*
			 * if (Main.tryAgentRememberSequence) { a.setDateAndValueHistory(date); //record
			 * in map of agent its current value and date
			 * a.combineCurrentValueWithOtherMessages(); //combine current with latest }
			 * else {
			 * 
			 * }
			 * 
			 */
/*
	createUnsynchMsgs(a, date, false);
			addPermutatioToAnytimeMechanism(a, myPermutation);
		}
	}
	*/
}// class
