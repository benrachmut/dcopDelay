import java.util.Comparator;

public class ComparatorMsgSenderId implements Comparator<Message> {

	@Override
	public int compare(Message o1, Message o2) {
		return o1.getSender().getId()-o2.getSender().getId();
	}

}
