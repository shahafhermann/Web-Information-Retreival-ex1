package webdata;

import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class IndexReaderEdgeTest {
	final static String indexDir = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/indexFiles";
	final static String inputFile = "2.txt";
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


	@Nested
	@DisplayName("Testing ReviewId as parameter")
	class TestReviewIdFunction {


		@Test
		@DisplayName("Testing helpfulnessDenominator")
		void helpfulness1() {
			int[] inputs = {2, 5};
			int[] expectedOutputs = {990, 550};
			validateArrayWithFunction(inputs, expectedOutputs, indexReader::getReviewHelpfulnessDenominator);
		}

		@Test
		@DisplayName("Testing helpfulnessNumerator")
		void helpfulness2() {
			int[] inputs = {2, 5};
			int[] expectedOutputs = {670, 300};
			validateArrayWithFunction(inputs, expectedOutputs, indexReader::getReviewHelpfulnessNumerator);
		}


		@Test
		@DisplayName("Testing getReviewReviewLen - valid inputs")
		void getReviewLen() {
			int[] inputs = {1, 2, 3, 4, 5, 6};
			int[] expectedOutputs = {9, 0, 9, 10, 0, -1};
			validateArrayWithFunction(inputs, expectedOutputs, indexReader::getReviewLength);
		}


		/**
		 * helper for testing Helpfulness, Score, and ReviewLen on several inputs
		 */
		private void validateArrayWithFunction(int[] inputs, int[] expectedOutputs,
											   Function<Integer, Integer> func) {
			for (int i = 0; i < inputs.length; i++) {
				assertEquals(expectedOutputs[i], (int) func.apply(inputs[i]), String.format(msgInt, inputs[i]));
			}
		}

	}


	@Nested
	@DisplayName("Testing Token as parameter")
	class TestTokenFunctions {


		@Test
		@DisplayName("Testing getTokenFrequency - valid inputs")
		void getTokenFrequency() {
			String[] inputs = {"a", "k", "p", "z"};
			int[] expectedOutputs = {1, 2, 1, 1};
			testTokenFrequency(inputs, expectedOutputs, indexReader::getTokenFrequency);
		}


		@Test
		@DisplayName("Testing getReviewsWithToken - valid inputs")
		void getReviewsWithToken() {
			String[] inputs = {"d"};
			int[][] expectedOutputs = {
					{1, 1},
			};
			testEnumerations(inputs, expectedOutputs, indexReader::getReviewsWithToken);
		}


		/**
		 * helper for testing Frequencies of Tokens on several valid inputs
		 */
		private void testTokenFrequency(String[] inputs, int[] expectedOutputs, Function<String, Integer> func) {
			for (int i = 0; i < inputs.length; i++) {
				assertEquals(expectedOutputs[i], (int) func.apply(inputs[i]), String.format(msgStr, inputs[i]));
			}
		}
	}

	@Nested
	@DisplayName("Testing ProductId as parameter")
	class TestProductIdFunctions {

		@Test
		@DisplayName("Testing getProductReviews - valid inputs")
		void getProductReviews() {
			String[] inputs = {"B001E4KFG0", "B00813GRG4", "B000LQOCH0"};
			int[][] expectedOutputs = {
					{1},
					{2},
					{3},
			};
			testEnumerations(inputs, expectedOutputs, indexReader::getProductReviews);
		}

	}


	@Nested
	@DisplayName("Testing  no parameter")
	class TestFunctionsWithNoParams {

		@Test
		@DisplayName("Testing getNumberOfReviews")
		void getNumberOfReviews() {
			assertEquals(5, indexReader.getNumberOfReviews(), "Should returns the total number of reviews");
		}

		@Test
		@DisplayName("Testing getTokenSizeOfReviews")
		void getTokenSizeOfReviews() {
			assertEquals(28, indexReader.getTokenSizeOfReviews(),
					"Should returns the total number of tokens include repetitions");
		}
	}


	/**
	 * helper for testing Enumerations returned values
	 */
	private void testEnumerations(String[] inputs, int[][] expectedOutputs,
								  Function<String, Enumeration<Integer>> func) {
		for (int i = 0; i < inputs.length; i++) {
			ArrayList<Integer> arr = Collections.list(func.apply(inputs[i]));
			int[] actual = arr.stream().mapToInt(Integer::intValue).toArray();
			assertArrayEquals(expectedOutputs[i], actual, String.format(msgStr, inputs[i]));
		}
	}


	/**
	 * comment for not checking removeIndex method of slowWriter!.
	 */
	@AfterAll
	static void removeDir() {
		indexWriter.removeIndex(indexDir);
		File directory = new File(indexDir);
		assertFalse(directory.exists());
	}
}
