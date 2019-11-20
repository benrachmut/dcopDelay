import java.util.Comparator;

public class ComparatorMsgCounter implements Comparator<MessageRecieve> {

	@Override
	public int compare(MessageRecieve m1, MessageRecieve m2) {
		if (m1.getCounter() > m2.getCounter()) {
			return 1;
		}
		if (m1.getCounter() < m2.getCounter())  {
			return -1;
		}
		return 0;
	}

}
