package keywords;

import java.io.IOException;
import java.util.List;

public class KeywordTester {

	public static void main(String[] args) throws IOException {
		String test = "In this paper a general technique for reducing processors in simulation without any increase in time is described. This results in an O(√logn) time algorithm for simulating one step of PRIORITY on TOLERANT with processor-time product of O(n log logn); the same as that for simulating PRIORITY on ARBITRARY. This is used to obtain anO(logn/log logn + √logn (log logm − log logn)) time algorithm for sortingn integers from the set {0,...,m − 1},m ≧n, with a processor-time product ofO(n log logm log logn) on a TOLERANT CRCW PRAM. New upper and lower bounds for ordered chaining problem on an allocated COMMON CRCW model are also obtained. The algorithm for ordered chaining takesO(logn/log logn) time on an allocated PRAM of sizen. It is shown that this result is best possible (upto a constant multiplicative factor) by obtaining a lower bound of Ω(r logn/(logr + log logn)) for finding the first (leftmost one) live processor on an allocated-COMMON PRAM of sizen ofr-slow virtual processors (one processor simulatesr processors of allocated PRAM). As a result, for ordered chaining problem, “processor-time product” has to be at least Ω(n logn/log logn) for any poly-logarithmic time algorithm.Algorithm for ordered-chaining problem results in anO(logN/log logN) time algorithm for (stable) sorting ofn integers from the set {0,...,m − 1} withn-processors on a COMMON CRCW PRAM; hereN = max(n, m). In particular if,m =n O(1), then sorting takes Θ(logn/log logn) time on both TOLERANT and COMMON CRCW PRAMs. Processor-time product for TOLERANT isO(n(log logn)2). Algorithm for COMMON usesn processors.";
		
		@SuppressWarnings("unused")
		List<Keyword> keywords = Utilities.guessFromString(test);
		System.out.println(keywords);
	}

}
