import java.util.Comparator;

public class ComparatorPermutationSimilarty implements Comparator<Permutation> {

	private Permutation currentP;
	private boolean maxSimilarityFlag;

	public ComparatorPermutationSimilarty(Permutation currentP, boolean maxSimilarityFlag) {
		this.currentP = currentP;
		this.maxSimilarityFlag = maxSimilarityFlag;
	}

	@Override
	public int compare(Permutation p1, Permutation p2) {
		
		if (currentP ==null) {
			return 0;
		}
		int p1SimilartyCounter = currentP.getSimilartyCounterTo(p1);
		int p2SimilartyCounter = currentP.getSimilartyCounterTo(p2);

		if (maxSimilarityFlag) {
			if (p1SimilartyCounter > p2SimilartyCounter) {
				return 1;
			}
			if (p1SimilartyCounter < p2SimilartyCounter) {
				return -1;
			} 
		}
		else {
			if (p1SimilartyCounter < p2SimilartyCounter) {
				return 1;
			}
			if (p1SimilartyCounter > p2SimilartyCounter) {
				return -1;
			} 
		}
		return 0;
	}


}
