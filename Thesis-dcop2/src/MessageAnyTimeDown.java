
public class MessageAnyTimeDown extends Message<Permutation> {

	//private Permutation permutationSent;
	public MessageAnyTimeDown(AgentField sender, AgentField reciever, Permutation bestPermutation, int delay) {
		super(sender, reciever, bestPermutation, delay);
		//this.permutationSent = bestPermutation;
	}
	/*
	public Permutation getPermutationSent() {
		return this.permutationSent;
	}
*/
}
