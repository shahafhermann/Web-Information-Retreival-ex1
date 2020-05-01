package webdata;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexReaderTest {
	final static String indexDir = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/indexFiles";
	final static String inputFile = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/1000.txt";
	static private IndexReader indexReader;
	static private SlowIndexWriter indexWriter;
	private final String msgInt = "fail on input: %d";
	private final String msgStr = "fail on input: %s";

	@BeforeAll
	static void before() {
		indexWriter = new SlowIndexWriter();
		indexWriter.slowWrite(inputFile, indexDir);
		indexReader = new IndexReader(indexDir);
	}

	@Test
	void getProductId() {
		int[] inputs = {1, 2, 3, 7, 99, 100, 999, 1000};
		String[] expectedOutputs = {"B001E4KFG0", "B00813GRG4", "B000LQOCH0", "B006K2ZZ7K","B0019CW0HE",
				"B0019CW0HE", "B006F2NYI2", "B006F2NYI2"};
		for (int i = 0; i < inputs.length; i++) {
			assertEquals(expectedOutputs[i], indexReader.getProductId(inputs[i]),
					String.format(msgInt, inputs[i]));
		}
	}

	@Test
	void getProductIdNotExists() {
		int[] inputs = {-1, 0, 1001};
		for(int input: inputs){
			assertNull(indexReader.getProductId(input), String.format(msgInt, input));
		}
	}

	@Test
	void getReviewScore() {
		int[] inputs = {1,2,3,4,20,999,1000};
		int[] expectedOutputs = {5,1,4,2,5,1,2};
		testHelpfulnessScoreReviewLen(inputs, expectedOutputs, indexReader::getReviewScore);
	}

	@Test
	void getReviewHelpfulnessNumerator() {
		int[] inputs = {1,20,999,1000};
		int[] expectedOutputs = {1, 0, 1, 2};
		testHelpfulnessScoreReviewLen(inputs, expectedOutputs, indexReader::getReviewHelpfulnessNumerator);
	}

	@Test
	void getReviewHelpfulnessDenominator() {
		int[] inputs = {1,20,999,1000};
		int[] expectedOutputs = {1, 0, 2, 5};
		testHelpfulnessScoreReviewLen(inputs, expectedOutputs, indexReader::getReviewHelpfulnessDenominator);
	}

	@Test
	void getReviewReviewLen(){
		int[] inputs = {1, 2, 3, 20, 999, 1000};
		int[] expectedOutputs = {48, 32, 93, 29, 57, 102};
		testHelpfulnessScoreReviewLen(inputs, expectedOutputs, indexReader::getReviewLength);
	}

	private void testHelpfulnessScoreReviewLen(int[] inputs, int[] expectedOutputs,
											   Function<Integer, Integer> func){
		for (int i = 0; i < inputs.length; i++) {
			assertEquals(expectedOutputs[i], func.apply(inputs[i]), String.format(msgInt, inputs[i]));
		}
	}

	@Test
	void getReviewScoreNotExists() {
		testHelpfulnessScoreReviewLenNotExists(indexReader::getReviewScore);
	}

	@Test
	void getReviewHelpfulnessNumeratorNotExists() {
		testHelpfulnessScoreReviewLenNotExists(indexReader::getReviewHelpfulnessNumerator);
	}

	@Test
	void getReviewHelpfulnessDenominatorNotExists() {
		testHelpfulnessScoreReviewLenNotExists(indexReader::getReviewHelpfulnessDenominator);
	}

	@Test
	void getReviewReviewLenNotExists() {
		testHelpfulnessScoreReviewLenNotExists(indexReader::getReviewLength);
	}


	private void testHelpfulnessScoreReviewLenNotExists(Function<Integer, Integer> func){
		int[] inputs = {-1, 0, 1001};
		int expectedOutput = -1;
		for (int input : inputs) {
			assertEquals(expectedOutput, func.apply(input), String.format(msgInt, input));
		}
	}

	@Test
	void getTokenFrequencyNotExists() {
		assertEquals(0, indexReader.getTokenFrequency("tokenThatDoesNotExists"));
	}

	@Test
	void getTokenCollectionFrequencyNotExists() {
		assertEquals(0, indexReader.getTokenCollectionFrequency("tokenThatDoesNotExists"));
	}

	@Test
	void getTokenFrequency() {
		String[] inputs = {"Greatest", "Buttermilk", "Lord"};
		int[] expectedOutputs = {2, 3, 2};
		testTokenFrequency(inputs, expectedOutputs, indexReader::getTokenFrequency);
	}

	@Test
	void getTokenCollectionFrequency() {
		String[] inputs = {"to", "thing", "Greatest", "zzbrarara", "ZuCchini"};
		int[] expectedOutputs = {1522, 45, 2, 0, 5}; // check if {2942, 231, 2, 0, 5} is the right one.
		testTokenFrequency(inputs, expectedOutputs, indexReader::getTokenCollectionFrequency);
	}

	private void testTokenFrequency(String[] inputs, int[] expectedOutputs, Function<String, Integer> func){
		for (int i = 0; i < inputs.length; i++) {
			assertEquals(expectedOutputs[i], func.apply(inputs[i]), String.format(msgStr, inputs[i]));
		}
	}

	@Test
	void  getProductReviews(){
		String[] inputs = {"B006K2ZZ7K", "B001GVISJM", "B0048IACB2", "B006F2NYI2", "B000002399839829"};
		int[][] expectedOutputs = {
				{5, 6, 7, 8},
				{14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28},
				{987},
				{988, 989, 990, 991, 992, 993, 994, 995, 996, 997, 998, 999, 1000},
				{}
		};
		testEnumerations(inputs, expectedOutputs, indexReader::getProductReviews);
	}

	@Test
	void getReviewsWithToken(){
		String[] inputs = {"ZuCchini", "taffy", "addition", "bEEr", "sucKer", "Sagiv"};
		int[][] expectedOutputs = {
				{902, 2, 932, 1, 942, 1, 944, 1},
				{5, 3, 6, 3, 7, 1, 8, 1, 741, 1},
				{4, 1, 357, 1, 498, 1, 713, 1, 756, 1, 778, 1, 794, 1, 904, 2},
				{4, 1, 6, 1, 270, 1, 452, 1, 467, 1, 468, 2, 500, 1, 575, 1, 603, 2, 604, 1},
				{1000, 1},
				{}
		};
		testEnumerations(inputs, expectedOutputs, indexReader::getReviewsWithToken);

	}

	private void testEnumerations(String[] inputs, int[][] expectedOutputs,
								  Function<String, Enumeration<Integer>> func) {
		for (int i = 0; i < inputs.length; i++) {
			ArrayList<Integer> arr = Collections.list(func.apply(inputs[i]));
			int[] ans =  arr.stream().mapToInt(Integer::intValue).toArray();
			assertArrayEquals(expectedOutputs[i], ans, String.format(msgStr, inputs[i]));
		}
	}

	@Test
	void getNumberOfReviews(){
		assertEquals(1000, indexReader.getNumberOfReviews());
	}

	@Test
	void getTokenSizeOfReviews(){
		assertEquals(75447, indexReader.getTokenSizeOfReviews());
	}

//	/** uncomment for checking removeIndex method of slowWriter!. */
//	@AfterAll
//	static void removeDir(){
//		indexWriter.removeIndex(indexDir);
//		File directory = new File(indexDir);
//		assertFalse(directory.exists());
//	}
}