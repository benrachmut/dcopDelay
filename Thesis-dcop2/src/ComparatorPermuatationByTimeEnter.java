import java.util.Comparator;

public class ComparatorPermuatationByTimeEnter implements Comparator<Permutation> {

	@Override
	public int compare(Permutation p1, Permutation p2) {
		if (p1.getTimeEnter()>p2.getTimeEnter()) {
			return 1;
		}
		if (p1.getTimeEnter()<p2.getTimeEnter()) {
			return -1;
		}
		return 0;
	}

}
