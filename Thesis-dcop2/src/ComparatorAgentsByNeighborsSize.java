import java.util.Comparator;

public class ComparatorAgentsByNeighborsSize implements Comparator<AgentField> {
private boolean alotNeighborStrongerFlag;
	
4
	public ComparatorAgentsByNeighborsSize(boolean alotNeighborStrongerFlag) {
	super();
	this.alotNeighborStrongerFlag = alotNeighborStrongerFlag;
}


	@Override
	public int compare(AgentField o1, AgentField o2) {
		if (o1.getNieghborSize()>o2.getNieghborSize()) {
			
			if (alotNeighborStrongerFlag) {
				return 1;
			}else {
				return -1;
			}
		}
		if (o1.getNieghborSize()<o2.getNieghborSize()) {
			if (alotNeighborStrongerFlag) {
				return -1;
			}else {
				return 1;
			}
		}else {
			if (o1.getId()<o2.getId()) {
				return 1;
			}
			if (o1.getId()>o2.getId()) {
				return -1;
			}
		}
		return 0;
	}

}
