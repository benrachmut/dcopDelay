import java.util.Comparator;

public class ComparatorAgentsByNeighborsSize implements Comparator<AgentField> {

	

	@Override
	public int compare(AgentField o1, AgentField o2) {
		if (o1.getNieghborSize()>o2.getNieghborSize()) {
			return 1;
		}
		if (o1.getNieghborSize()<o2.getNieghborSize()) {
			return -1;
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
