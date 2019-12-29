import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Main {

	// ------- VARIABLES TO CHECK BEFORE STARTING A RUN
	// -- variables of dcop problem
	static int A = 50;// 50;//35; // number of agents
	static int D = 10; // size of domain for each agent
	static int costMax = 100; // the max value of cost
	// -- Experiment time

	static int meanRepsStart = 0;
	static int meanRepsEnd = 100;

	static int currMeanRun = 0;
	static int iterations = 4000;// 15000;//10000;// 1000;//10000;//10000;//5000;//10000, 2000;
	// versions

	/*
	 * 
	 * stochastic, knownRatio, changeFlag AsynchronyDSA(0.7,1,0.5)
	 * AsynchronyDSA(0.7,1,0.75) AsynchronyDSA(0.7,1,1) AsynchronyDSA(0.7,1,1.5)
	 * AsynchronyDSA(0.7,1,2) AsynchronyDSA(0.7,1,5)
	 */
	// AsynchronyMGMNoPhase
	// AsynchronyMGMPhase
	// SynchronicMGM;
	// SynchronicDSA;
	// AsynchronyDSA
	// Monotonic

	// AsynchronyDSA_SDP(k=10)
	// AsynchronyDSA_SDP(k=20)
	// AsynchronyDSA_SDP(k=30)
	// AsynchronyDSA_SDP(k=40)
	// AsynchronyDSA_SDP(k=50)
	// AsynchronyDSA_SDP(k=60)
	// AsynchronyDSA_SDP(k=70)
	// AsynchronyDSA_SDP(k=80)

	// "AsynchronyDSA_SDP(k=10,date=0)");

	
//"AsynchronyDSA_SDP(k=40,r=05)"
	//"AsynchronyDSA_SDP(k=40,r=15)";
	//"AsynchronyDSA_SDP(k=40,r=15)";
	//"AsynchronyDSA_SDP(k=40,r=3)";
	///"AsynchronyDSA_SDP(k=40,r=5"
	static String algo = "AsynchronyDSA_SDP(k=40,r=5)";
	static boolean sendOnce = false;

	static int[] dcopVersions = { 1 }; // 1= Uniformly random DCOPs, 2= Graph coloring problems, 3= Scale-free
	// -- memory
	static int[] memoryVersions = { 1 }; // 1=exp, 2= constant, 3= reasonable
	static double[] constantsPower = { 2 }; // { 1.69897, 2, 2.1761,2.47712,2.6989,2.8751,3 };//
											// {1.8};//{0.8,1,1.4,1.8,2,2.4};//{0.8,1,1.4,1.8,2,2.4};//{1.8,2,2.2,2.8,3,3.2,3.5};//{2};//{2,2.2,2.5,2.8,3,3.2,3.5};//{2};//{2,2.3,2.5,2.7,3,3.3,3.5};//{2.75};//{}{0.8,1,2,3,4};//{2,4,6,8};//{0.8,1,2,3,4};//{1,2,3,4,5};

	static int[] comparatorsForMemory = { 1 };
	// -- synch
	// static boolean synch = false;
	static boolean anytime = false;
	static boolean orgenizeDfs = false;
	static boolean orgenizeBfs = false;

	/*
	 * static boolean enableBfs = true; static boolean enableDfs = false;
	 * 
	 */

	// static boolean anytimeVector = false;

	static String fileName;

	// -- uniformly random dcop
	static double[] p1sUniform = { 0.2 }; // {0.2 }; // 0.1,0.7
	static double[] p2sUniform = { 1 };
	// -- color dcop
	static final double[] p1sColor = { 0.05 }; // 0.1,0.7
	// -- scale free AB
	static int[] hubs; // 5, 10
	static int[] numOfNToNotHubs = { 3 };
	static double[] p2sScaleFree = { 1 };
	// -- communication protocol
	static double[] p3s = { 0,1 };// {0,1};
	// static boolean[] dateKnowns = { true };
	static int[] delayUBs = {5,10,25,50};//{2,5,10,25};// {2,5,10,25};//{5,10,25,50,75,100};//{10,25,50};//{5,10,25,50,75,100};//{2,5,10,25,50,75,100};
	static double[] p4s = { 0 };// { 0,0.05,0.1,0.15,0.2,0.25,0.3 };
	public static boolean useCounterToChangeTrans = false;
	public static boolean secondBest = false;
	// ------- GENERAL VARIABLES NO NEED TO CHANGE
	// -- characters
	static AgentField[] agents;
	static AgentZero agentZero;
	// -- other
	static List<String> solutions = new ArrayList<String>();
	static List<String> fatherSolutions = new ArrayList<String>();
	// -- random variables
	static Random rFirstValue = new Random();
	static Random rCost = new Random();

	// static Random rDsa = new Random();
	static Dcop dcop;
	static int dcopVersion;
	// -- memory version
	static int memoryVersion;
	static long memoryMaxConstant;
	static int currentComparatorForMemory;
	// public static boolean trySendValueAsPermutation = true;
	// -- uniformly random dcop
	static Double currentP1Uniform;
	static Double currentP2Uniform;
	static Random rP1Uniform = new Random();
	static Random rP2Uniform = new Random();
	// -- color dcop
	static Double currentP1Color;
	static Random rP1Color = new Random();
	// -- scale free AB
	static double currentP2ScaleFree;
	static int currentHub;
	static int currentNumOfNToNotHub;
	static Random rHub = new Random();
	static Random rNotHub = new Random();
	static Random rP2ScaleFree = new Random();
	// -- communication protocol
	static Random rP3 = new Random();
	static Random rP4 = new Random();
	static Random rDelay = new Random();
	static boolean dateKnown;
	static Double currentP3;
	static Double currentP4;
	static int currentUb;
	static Random rDelayAnytime = new Random();

	public static void main(String[] args) {
		fileName = getFileName();

		System.out.println(fileName);

		for (int i : dcopVersions) {
			dcopVersion = i;
			if (memoryVersions[0] == 2) {
				for (int j : memoryVersions) {
					memoryVersion = j;
					for (int k : comparatorsForMemory) {
						currentComparatorForMemory = k;
						runDifferentMemoryVersions();
					}
				}
			} else {
				if (memoryVersions[0] == 1) {
					memoryVersion = 1;
				}
				if (memoryVersions[0] == 3) {
					memoryVersion = 3;
				}
				runDifferentDcop();
			}
			printDcops();
		}
	}

	private static String getFileName() {

		String dcopV = getDcopName();
		String param = getDcopParam();
		String ans = "algo_" + algo + ",A_" + A + ",dcop_" + dcopV + ",param_" + param + ",anytime_" + anytime;
		String meanRunRange = ",start_" + meanRepsStart + ",end_" + meanRepsEnd + ",iteration_" + iterations + ",p4_"
				+ p4s[0];
		ans = ans + meanRunRange;
		if (memoryVersions[0] == 2) {
			String heurstic = ",huerstic_" + comparatorsForMemory[0];
			ans = ans + heurstic;
		}
		return ans;
	}

	private static String getDcopParam() {
		if (dcopVersions[0] == 1) {
			return Double.toString(p1sUniform[0]);
		}
		if (dcopVersions[0] == 2) {
			return Double.toString(p1sColor[0]);
		} else {
			return Integer.toString(A / 5);
		}
	}

	private static String getDcopName() {
		if (dcopVersions[0] == 1) {
			return "random";
		}
		if (dcopVersions[0] == 2) {
			return "color";
		} else {
			return "scaleFree";
		}
	}

	private static String addOnPerProb() {
		String addOn = "";
		if (dcopVersions[0] == 1) {
			addOn = "uniform,p1_" + p1sUniform[0] + ",p2_" + p2sUniform[0];
		}
		if (dcopVersions[0] == 2) {
			addOn = "color,p1_" + p1sColor[0];
		}
		if (dcopVersions[0] == 3) {
			addOn = "scaleFree,hub_" + A / 5 + ", links_" + numOfNToNotHubs[0] + ",p2_" + p2sScaleFree[0];
		}
		return addOn;
	}

	private static void runDifferentMemoryVersions() {
		if (memoryVersion == 1) {
			runDifferentDcop();
		}

		if (memoryVersion == 2) {
			for (double i : constantsPower) { // for parameter tuning
				memoryMaxConstant = (long) Math.pow(10, i);
				runDifferentDcop();
			}
		}

		printDcops();

	}

	private static void runDifferentDcop() {
		if (dcopVersion == 1) {
			D = 10;
			costMax = 100;
			runUniformlyRandomDcop();
		}
		if (dcopVersion == 2) {
			D = 3;
			costMax = 10;
			runColorDcop();
		}
		if (dcopVersion == 3) {
			D = 10;
			costMax = 100;
			int numOfHubs = A / 5;
			int[] hubs_t = { numOfHubs };
			hubs = hubs_t;
			runScaleFreeDcop();
		}
		printDcops();

	}

	private static void runScaleFreeDcop() {

		for (int i : hubs) {
			currentHub = i;
			for (int j : numOfNToNotHubs) {
				currentNumOfNToNotHub = j;
				for (double k : p2sScaleFree) {
					currentP2ScaleFree = k;

					for (int meanRun = meanRepsStart; meanRun < meanRepsEnd; meanRun++) {
						currMeanRun = meanRun;
						dcopSeeds(meanRun);
						dcop = createDcop();
						differentCommunicationProtocols(dcop, meanRun);

					} // means run
				}
			}
		}
	}
	/*
	 * private static void setSynchBoolean() { boolean unsynchMono =
	 * algo.equals("unsynchMono");
	 * 
	 * if (unsynchMono) { synch = false; } else { synch = true; }
	 * 
	 * }
	 */

	private static void printDcops() {
		BufferedWriter out = null;
		try {
			FileWriter s = new FileWriter(fileName + ".csv");
			out = new BufferedWriter(s);
			String header = "dcop,p3,date_known,ub,p4,algo,p1,p2,mean_run,iteration,bird_eye_cost,anytime_bird_eye,known_ratio";
			// if (!synch) {

			String anytimeString = ",cost_change_counter,top_cost_not_best,top_change_counter,top_change_ratio,anytime_cost,top_cost";
			String huerstic = ",hyper_parametr,Heuristics";

			if (Main.anytime) {
				header = header + anytimeString;
			}
			if (memoryVersion == 2) {
				header = header + huerstic;
			}

			// }
			out.write(header);
			out.newLine();

			for (String o : solutions) {
				out.write(o);
				out.newLine();
			}

			out.close();
		} catch (Exception e) {
			System.err.println("Couldn't open the file");
		}

	}

	private static void runColorDcop() {

		for (Double p1 : p1sColor) {
			currentP1Color = p1;

			for (int meanRun = meanRepsStart; meanRun < meanRepsEnd; meanRun++) {
				currMeanRun = meanRun;
				dcopSeeds(meanRun);
				dcop = createDcop();
				differentCommunicationProtocols(dcop, meanRun);

			} // means run
		} // p1

	}

	private static void runUniformlyRandomDcop() {

		for (Double p1 : p1sUniform) {
			currentP1Uniform = p1;
			for (Double p2 : p2sUniform) {
				currentP2Uniform = p2;

				for (int meanRun = meanRepsStart; meanRun < meanRepsEnd; meanRun++) {
					currMeanRun = meanRun;
					dcopSeeds(meanRun);
					dcop = createDcop();
					differentCommunicationProtocols(dcop, meanRun);

				} // means run
			} // p2
		} // p1

	}

	private static void dcopSeeds(int meanRun) {
		rP1Uniform.setSeed(meanRun);
		rP2Uniform.setSeed(meanRun);
		rP1Color.setSeed(meanRun);
		rP2ScaleFree.setSeed(meanRun);
		rHub.setSeed(meanRun);
		rNotHub.setSeed(meanRun);
		rCost.setSeed(meanRun);
		rFirstValue.setSeed(meanRun);

	}

	private static void differentCommunicationProtocols(Dcop dcop, int meanRun) {

		int communicationSeed = 0;

		for (Double p3 : p3s) {
			currentP3 = p3;
			if (p3 == 0) {
				currentP4 = p4s[0];
				// for (AgentField a : agents) {
				// a.setDsaSeed(meanRun);
				// }
				communicationSeeds(meanRun);
				afterHavingAllPrameters(p3, true, 0, 0.0, dcop, meanRun);
			} else {
				diffCommunicationGivenP3(dcop, meanRun, p3);
			}
		} // p3
			// if (currMeanRun % 10 == 0) {
		printDcops();

		// }

	}

	private static void afterHavingAllPrameters(Double p3, Boolean dK, Integer delayUB, Double p4, Dcop dcop,
			int meanRun) {
		// ---- protocol ----
		String protocol = "p3=" + currentP3 + ", p4=" + currentP4 + ", ub=" + currentUb + ", mean run=" + meanRun;
		// ---- find solution ----

		Solution algo = selectedAlgo(dcop, meanRun);
		printHeader(protocol);
		// ---- restart ----
		String useInExcel = p3 + "," + dK + "," + delayUB + "," + p4;

		restartBetweenAlgo(algo, useInExcel);
	}

	private static void printHeader(String protocol) {

		String toPrint = "";
		if (dcopVersion == 1) {
			toPrint = algo + "_" + dcop + ", p1=" + currentP1Uniform + ", p2=" + currentP2Uniform + ", " + protocol;
		}
		if (dcopVersion == 2) {
			toPrint = algo + "_" + dcop + ", p1=" + currentP1Color + ", " + protocol;
		}
		if (dcopVersion == 3) {
			toPrint = algo + "_" + dcop + ", hubs=" + currentHub + ", p2=" + currentP2ScaleFree + ", links="
					+ currentNumOfNToNotHub + protocol;
		}
		if (algo.equals("dsaUnsynch7")) {
			toPrint = toPrint + ", comparator: " + currentComparatorForMemory + ", parameter:" + memoryMaxConstant;
		}
		System.out.println(toPrint);

	}

	private static void diffCommunicationGivenP3(Dcop dcop, int meanRun, Double p3) {
		// for (boolean dK : dateKnowns) {
		// dateKnown = dK;
		for (Integer delayUB : delayUBs) {
			currentUb = delayUB;
			for (Double p4 : p4s) {
				// communicationSeed = communicationSeed + 1;
				// System.out.println("communicationSeed");

				communicationSeeds(meanRun);
				currentP4 = p4;

				afterHavingAllPrameters(p3, false, delayUB, p4, dcop, meanRun);

			} // p4
		} // ub
			// } // date known

	}

	private static void communicationSeeds(int meanRun) {
		// communicationSeed =0;
		rP3.setSeed(meanRun);
		rP4.setSeed(meanRun);
		rDelay.setSeed(meanRun);
		rDelayAnytime.setSeed(meanRun);
		rFirstValue.setSeed(meanRun);

		for (AgentField a : agents) {
			a.setDsaSeed(meanRun);
		}

	}

	private static Solution selectedAlgo(Dcop dcop, int meanRun) {
		Solution ans = null;

		boolean AsynchronyDSA_SDP_10 = algo.equals("AsynchronyDSA_SDP(k=10)");
		boolean AsynchronyDSA_SDP_20 = algo.equals("AsynchronyDSA_SDP(k=20)");
		boolean AsynchronyDSA_SDP_40 = algo.equals("AsynchronyDSA_SDP(k=40)");
		boolean AsynchronyDSA_SDP_60 = algo.equals("AsynchronyDSA_SDP(k=60)");
		boolean AsynchronyDSA_SDP_80 = algo.equals("AsynchronyDSA_SDP(k=80)");
		
		
		
		
		
		if (AsynchronyDSA_SDP_10) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 10,1);
		}
		
		if (AsynchronyDSA_SDP_20) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 20,1);
		}
		
		if (AsynchronyDSA_SDP_40) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 40,1);
		}
		if (AsynchronyDSA_SDP_60) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 60,1);
		}
		
		if (AsynchronyDSA_SDP_80) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 80,1);
		}
		

		boolean AsynchronyDSA_SDP_40_05 = algo.equals("AsynchronyDSA_SDP(k=40,r=05)");
		boolean AsynchronyDSA_SDP_40_15 = algo.equals("AsynchronyDSA_SDP(k=40,r=15)");
		boolean AsynchronyDSA_SDP_40_2 = algo.equals("AsynchronyDSA_SDP(k=40,r=2)");
		boolean AsynchronyDSA_SDP_40_3 = algo.equals("AsynchronyDSA_SDP(k=40,r=3)");
		boolean AsynchronyDSA_SDP_40_5 = algo.equals("AsynchronyDSA_SDP(k=40,r=5)");

		
		if (AsynchronyDSA_SDP_40_05) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 40,0.5);
		}
		
		if (AsynchronyDSA_SDP_40_15) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 40,1.5);
		}
		if (AsynchronyDSA_SDP_40_2) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 40,2);
		}
		if (AsynchronyDSA_SDP_40_3) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 40,2);
		}
		if (AsynchronyDSA_SDP_40_5) {
			ans = new AsynchronyDSA_SDP(dcop, agents, agentZero, meanRun, 40,2);
		}
		
		
		// AsynchronyDSA(0.1);AsynchronyDSA(0.3);AsynchronyDSA(0.5);AsynchronyDSA(0.7);AsynchronyDSA(0.9);AsynchronyDSA(1);
		boolean AsynchronyMGMNoPhase = algo.equals("AsynchronyMGMNoPhase");

		if (AsynchronyMGMNoPhase) {
			ans = new AsynchronyMGMNoPhase(dcop, agents, agentZero, meanRun);
		}
		boolean AsynchronyDSA01 = algo.equals("AsynchronyDSA(0.1)");
		boolean AsynchronyDSA03 = algo.equals("AsynchronyDSA(0.3)");
		boolean AsynchronyDSA05 = algo.equals("AsynchronyDSA(0.5)");
		boolean AsynchronyDSA07 = algo.equals("AsynchronyDSA(0.7)");
		boolean AsynchronyDSA09 = algo.equals("AsynchronyDSA(0.9)");
		boolean AsynchronyDSA1 = algo.equals("AsynchronyDSA(1)");

		if (AsynchronyDSA01) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.1, sendOnce);
		}
		if (AsynchronyDSA03) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.3, sendOnce);
		}
		if (AsynchronyDSA05) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.5, sendOnce);
		}

		if (AsynchronyDSA07) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.7, sendOnce);
		}

		if (AsynchronyDSA09) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.9, sendOnce);
		}
		if (AsynchronyDSA1) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1.0, sendOnce);
		}

		boolean AsynchronyDSA07_0 = algo.equals("AsynchronyDSA(0.7,0)");
		boolean AsynchronyDSA07_01 = algo.equals("AsynchronyDSA(0.7,0.1)");
		boolean AsynchronyDSA07_02 = algo.equals("AsynchronyDSA(0.7,0.2)");
		boolean AsynchronyDSA07_03 = algo.equals("AsynchronyDSA(0.7,0.3)");
		boolean AsynchronyDSA07_04 = algo.equals("AsynchronyDSA(0.7,0.4)");
		boolean AsynchronyDSA07_05 = algo.equals("AsynchronyDSA(0.7,0.5)");
		boolean AsynchronyDSA07_06 = algo.equals("AsynchronyDSA(0.7,0.6)");
		boolean AsynchronyDSA07_07 = algo.equals("AsynchronyDSA(0.7,0.7)");
		boolean AsynchronyDSA07_08 = algo.equals("AsynchronyDSA(0.7,0.8)");
		boolean AsynchronyDSA07_09 = algo.equals("AsynchronyDSA(0.7,0.9)");
		boolean AsynchronyDSA07_1 = algo.equals("AsynchronyDSA(0.7,1)");

		if (AsynchronyDSA07_0) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0, 0.7, sendOnce);
		}

		if (AsynchronyDSA07_01) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.1, 0.7, sendOnce);
		}

		if (AsynchronyDSA07_02) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.2, 0.7, sendOnce);
		}
		if (AsynchronyDSA07_03) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.3, 0.7, sendOnce);
		}
		if (AsynchronyDSA07_04) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.4, 0.7, sendOnce);
		}
		if (AsynchronyDSA07_05) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.5, 0.7, sendOnce);
		}
		if (AsynchronyDSA07_06) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.6, 0.7, sendOnce);
		}
		if (AsynchronyDSA07_07) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.7, 0.7, sendOnce);
		}
		if (AsynchronyDSA07_08) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.8, 0.7, sendOnce);
		}
		if (AsynchronyDSA07_09) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.9, 0.7, sendOnce);
		}
		if (AsynchronyDSA07_1) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 0.7, sendOnce);
		}

		boolean AsynchronyDSA = algo.equals("AsynchronyDSA");
		boolean SynchronicDSA = algo.equals("SynchronicDSA");

		boolean AsynchronyMGMPhase = algo.equals("AsynchronyMGMPhase");
		boolean SynchronicMGM = algo.equals("SynchronicMGM");

		boolean Monotonic = algo.equals("Monotonic");

		if (Monotonic) {
			ans = new AsynchronyMonotonic(dcop, agents, agentZero, meanRun);
		}

		if (SynchronicMGM) {
			ans = new SynchronicMGM(dcop, agents, agentZero, meanRun);
		}

		if (AsynchronyMGMPhase) {
			ans = new AsynchronyMGMPhase(dcop, agents, agentZero, meanRun);
		}

		if (AsynchronyDSA) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 0.7, sendOnce);
		}

		if (SynchronicDSA) {
			ans = new SynchronicDSA(dcop, agents, agentZero, meanRun, 0.7);
		}

		boolean AsynchronyDSA07_1_10 = algo.equals("AsynchronyDSA(0.7,1,10)");
		if (AsynchronyDSA07_1_10) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 10, 0.7);
		}

		boolean AsynchronyDSA07_1_150 = algo.equals("AsynchronyDSA(0.7,1,15)");
		if (AsynchronyDSA07_1_150) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 15, 0.7);
		}

		boolean AsynchronyDSA07_1_8 = algo.equals("AsynchronyDSA(0.7,1,8)");
		if (AsynchronyDSA07_1_8) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 8, 0.7);
		}

		boolean AsynchronyDSA07_1_20 = algo.equals("AsynchronyDSA(0.7,1,20)");
		if (AsynchronyDSA07_1_20) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 20, 0.7);
		}

		boolean AsynchronyDSA07_1_05 = algo.equals("AsynchronyDSA(0.7,1,0.5)");
		if (AsynchronyDSA07_1_05) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 0.5, 0.7);
		}

		boolean AsynchronyDSA07_1_075 = algo.equals("AsynchronyDSA(0.7,1,0.75)");
		if (AsynchronyDSA07_1_075) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 0.75, 0.7);
		}

		boolean AsynchronyDSA07_1_1 = algo.equals("AsynchronyDSA(0.7,1,1)");
		if (AsynchronyDSA07_1_1) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 1, 0.7);
		}

		boolean AsynchronyDSA07_1_15 = algo.equals("AsynchronyDSA(0.7,1,1.5)");
		if (AsynchronyDSA07_1_15) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 1.5, 0.7);
		}

		boolean AsynchronyDSA07_1_2 = algo.equals("AsynchronyDSA(0.7,1,2)");
		if (AsynchronyDSA07_1_2) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 2, 0.7);
		}

		boolean AsynchronyDSA07_1_5 = algo.equals("AsynchronyDSA(0.7,1,5)");
		if (AsynchronyDSA07_1_5) {
			ans = new AsynchronyDSA(dcop, agents, agentZero, meanRun, 1, 5, 0.7);
		}

		ans.solve();

		return ans;

	}

	private static int countTrue(Collection<Boolean> values) {
		int ans = 0;
		for (Boolean b : values) {
			if (b) {
				ans++;
			}
		}
		return ans;
	}

	private static void addToSolutionString(Solution sol, String protocol) {
		for (int i = 0; i < iterations; i++) {
			String s = dcop.toString() + "," + protocol + "," + sol.toString() + "," + i + "," + sol.getRealCost(i)
					+ "," + sol.getBirdeyeAnytime(i) + "," + ((Asynchrony) sol).getKnownRatio();
			if (anytime) {
				String anytimeString = "," + sol.getCounterChanges(i) + "," + ((Asynchrony) sol).getTopCostNotBest(i)
						+ "," + ((Asynchrony) sol).getCounterTop(i) + "," + ((Asynchrony) sol).getCounterRatio(i) + ","
						+ sol.getAnytimeCost(i) + "," + sol.getTopCost(i);
				s = s + anytimeString;
			}
			if (memoryVersion == 2) {
				String huersticString = "," + memoryMaxConstant + "," + currentComparatorForMemory;
				s = s + huersticString;

			}
			/*
			 * s = s + sol.getCounterChanges(i) + "," + ((Unsynch) sol).getTopCostNotBest(i)
			 * + "," + ((Unsynch) sol).getCounterTop(i) + "," + ((Unsynch)
			 * sol).getCounterRatio(i) + "," + sol.getAnytimeCost(i) + "," +
			 * sol.getTopCost(i) + "," + memoryVersion ;
			 * 
			 * if (memoryVersion == 1) { s = s ; } if (memoryVersion == 2) { s = s +
			 * memoryMaxConstant + "," + currentComparatorForMemory; }
			 */

			// }
			solutions.add(s);
		}
	}

	private static Dcop createDcop() {
		agents = initAgentsFieldArray();
		Dcop dcop = new Dcop(agents, D, iterations);
		for (AgentField a : agents) {
			a.restartNeighborCounter();

		}
		agentZero = new AgentZero(iterations, dcop.getNeighbors(), agents);

		orgenizeTrees();
		restartAfterNeighborsKnown();
		fixLonleyNodes();
		return dcop;
	}

	private static void fixLonleyNodes() {
		for (AgentField a : agents) {
			if (a.isAnytimeLeaf() && a.getAnytimeFather() == null) {
				a.setAnytimeValueLonleyNode();
				a.setCheatBestPermutation();
			}
		}

	}

	private static void restartAfterNeighborsKnown() {
		for (AgentField a : agents) {
			a.restartLastPCreatedBy();
			a.restartPCreatedByLists();
			a.setAz(agentZero);
			a.restartForSynchronicAlgos();
			// a.setNeighborAndCounterRecieve();
		}
	}

	private static void orgenizeTrees() {

		if (algo.equals("Monotonic") || algo.equals("dsa7") || algo.equals("mgm")) {
			orgenizeDfs = false;
			orgenizeBfs = false;
		}
		if (algo.equals("Monotonic") || orgenizeDfs) {
			Tree psaduoTree = new Tree(agents);
			psaduoTree.dfs();
			psaduoTree.setIsAboveBelow();

			for (AgentField a : agents) {
				a.setAnytimeFather(a.getDfsFather());
				a.setAnytimeSons(a.getDfsSons());

			}

		}

		else if (orgenizeBfs) {
			Tree bfs = new Tree(agents);
			bfs.bfs();
			List<AgentField> leaves = findLeaves();
			for (AgentField a : leaves) {
				a.setLevelInTree(0);
			}
		}
		/*
		 * else if (anytimeDfs) { Tree psaduoTree = new Tree(agents); psaduoTree.dfs();
		 * 
		 * for (AgentField a : agents) { a.setAnytimeFather(a.getDfsFather());
		 * a.setAnytimeSons(a.getDfsSons()); } }
		 */
	}

	private static List<AgentField> findLeaves() {
		List<AgentField> ans = new ArrayList<AgentField>();

		for (AgentField a : agents) {
			if (a.getAnytimeSons().size() == 0) {
				ans.add(a);
			}
		}
		return ans;
	}

	private static void restartBetweenAlgo(Solution sol, String protocol) {

		addToSolutionString(sol, protocol);
		restartOther();
	}

	private static void restartOther() {
		restartAgent();
		fixLonleyNodes();

		agentZero.emptyMessageBox();
		// agentZero.emptyRMessageBox();
		agentZero.emptyTimeStempBoxMessage();

	}

	private static void restartAgent() {
		for (int i = 0; i < agents.length; i++) {
			agents[i].changeValOfAllNeighbor();
			agents[i].changeValR();
			agents[i].setFirstValueToValue();
			agents[i].setAllBelowMap(0);
			agents[i].setAllAboveMap(0);
			agents[i].resetMsgUpAndDown();
			// agents[i].setDecisionCounterNonMonotonic(0);
			agents[i].setDecisionCounter(0);
			agents[i].setRCounter(0);

			agents[i].initSonsAnytimeMessages();
			agents[i].resetCounterAndValue();
			agents[i].resetBestPermutation();
			agents[i].resettopHasAnytimeNews();
			agents[i].addFirstCoupleToCounterAndVal();
			agents[i].setValueRecieveFlag(false);
			agents[i].restartNeighborCounter();
			agents[i].restartAnytimeUpRecieved();
			agents[i].restartPermutationsSets();

			// agents[i].restartAnytimeToSend();
			agents[i].restartAnytimeValue();
			agents[i].resetFlagForMgm();
			agents[i].restartLastPCreatedBy();

			agents[i].restartForSynchronicAlgos();
			// agents[i].setCheckCanGoFirst(true);
			agents[i].setWorldChangeSynchFlag(true);
			agents[i].setKnownCounter(false);
			agents[i].resetWaitingForCounterSynch();
			agents[i].restartKsdpCounter();
			agents[i].restartLevelInTree();
			agents[i].initInfPermutations();
			// agents[i].setNeighborAndCounterRecieve();
		}

	}

	private static AgentField[] initAgentsFieldArray() {
		AgentField[] ans = new AgentField[A];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = new AgentField(D, i);
		}
		return ans;
	}

	public static int getRandomInt(Random r, int min, int max) {
		return r.nextInt(max - min + 1) + min;
	}

}
