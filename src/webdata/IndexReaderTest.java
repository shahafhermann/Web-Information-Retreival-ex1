package webdata;

import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class IndexReaderTest {
	final static String indexDir = "/Users/shahaf/Documents/UNI/אחזור מידע באינטרנט/ex1/indexFiles";
	final static String inputFile = "1000.txt";
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
	class TestReviewIdFunction{
		@Test
		@DisplayName("Testing getProductId - valid input")
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
		@DisplayName("Testing getReviewScore - valid inputs")
		void getReviewScore() {
			int[] inputs = {1,2,3,4,20,999,1000};
			int[] expectedOutputs = {5,1,4,2,5,1,2};
			validateArrayWithFunction(inputs, expectedOutputs, indexReader::getReviewScore);
		}

		@Test
		@DisplayName("Testing getReviewHelpfulnessNumerator - valid inputs")
		void getReviewHelpfulnessNumerator() {
			int[] inputs = {1,20,999,1000};
			int[] expectedOutputs = {1, 0, 1, 2};
			validateArrayWithFunction(inputs, expectedOutputs, indexReader::getReviewHelpfulnessNumerator);
		}

		@Test
		@DisplayName("Testing getReviewHelpfulnessDenominator - valid inputs")
		void getReviewHelpfulnessDenominator() {
			int[] inputs = {1,20,999,1000};
			int[] expectedOutputs = {1, 0, 2, 5};
			validateArrayWithFunction(inputs, expectedOutputs, indexReader::getReviewHelpfulnessDenominator);
		}

		@Test
		@DisplayName("Testing getReviewReviewLen - valid inputs")
		void getReviewLen(){
			int[] inputs = {1, 2, 3, 20, 999, 1000};
			int[] expectedOutputs = {48, 32, 93, 29, 57, 102};
			validateArrayWithFunction(inputs, expectedOutputs, indexReader::getReviewLength);
		}


		@Test
		@DisplayName("Testing getProductId - reviewId doesn't exists")
		void getProductIdNotExists() {
			assertAll(
					() -> assertNull(indexReader.getProductId(-1), () -> String.format(msgInt, -1)),
					() -> assertNull(indexReader.getProductId(0), () -> String.format(msgInt, 0)),
					() -> assertNull(indexReader.getProductId(1001), () -> String.format(msgInt, 1001))
			);
		}

		@Test
		@DisplayName("Testing getReviewScore - reviewId doesn't exists")
		void getReviewScoreNotExists() {
			int[] inputs = {-1, 0, 1001};
			int[] expectedOutput = {-1, -1, -1};
			validateArrayWithFunction(inputs, expectedOutput, indexReader::getReviewScore);
		}

		@Test
		@DisplayName("Testing getReviewHelpfulnessNumerator - reviewId doesn't exists")
		void getReviewHelpfulnessNumeratorNotExists() {
			int[] inputs = {-1, 0, 1001};
			int[] expectedOutput = {-1, -1, -1};
			validateArrayWithFunction(inputs, expectedOutput, indexReader::getReviewHelpfulnessNumerator);
		}

		@Test
		@DisplayName("Testing getReviewHelpfulnessDenominator - reviewId doesn't exists")
		void getReviewHelpfulnessDenominatorNotExists() {
			int[] inputs = {-1, 0, 1001};
			int[] expectedOutput = {-1, -1, -1};
			validateArrayWithFunction(inputs, expectedOutput, indexReader::getReviewHelpfulnessDenominator);
		}

		@Test
		@DisplayName("Testing getReviewLength - reviewId doesn't exists")
		void getReviewLenNotExists() {
			int[] inputs = {-1, 0, 1001};
			int[] expectedOutput = {-1, -1, -1};
			validateArrayWithFunction(inputs, expectedOutput, indexReader::getReviewLength);
		}


		/** helper for testing Helpfulness, Score, and ReviewLen on several inputs */
		private void validateArrayWithFunction(int[] inputs, int[] expectedOutputs,
											   Function<Integer, Integer> func){
			for (int i = 0; i < inputs.length; i++) {
				assertEquals(expectedOutputs[i], (int)func.apply(inputs[i]), String.format(msgInt, inputs[i]));
			}
		}

	}

	@Nested
	@DisplayName("Testing Token as parameter")
	class TestTokenFunctions{

		@Test
		@DisplayName("Testing getTokenFrequency - valid inputs")
		void getTokenFrequency() {
			String[] inputs = {"Greatest", "Buttermilk", "Lord"};
			int[] expectedOutputs = {2, 3, 2};
			testTokenFrequency(inputs, expectedOutputs, indexReader::getTokenFrequency);
		}

		@Test
		@DisplayName("Testing getTokenCollectionFrequency - valid inputs")
		void getTokenCollectionFrequency() {
			String[] inputs = {"to", "thing", "Greatest", "ZuCchini"};
			int[] expectedOutputs = {1522, 45, 2, 5};
			testTokenFrequency(inputs, expectedOutputs, indexReader::getTokenCollectionFrequency);
		}

		@Test
		@DisplayName("Testing getReviewsWithToken - valid inputs")
		void getReviewsWithToken(){
			String[] inputs = {"ZuCchini", "taffy", "addition", "bEEr", "sucKer"};
			int[][] expectedOutputs = {
					{902, 2, 932, 1, 942, 1, 944, 1},
					{5, 3, 6, 3, 7, 1, 8, 1, 741, 1},
					{4, 1, 357, 1, 498, 1, 713, 1, 756, 1, 778, 1, 794, 1, 904, 2},
					{4, 1, 6, 1, 270, 1, 452, 1, 467, 1, 468, 2, 500, 1, 575, 1, 603, 2, 604, 1},
					{1000, 1},
			};
			testEnumerations(inputs, expectedOutputs, indexReader::getReviewsWithToken);
		}

		@Test
		@DisplayName("Testing getTokenFrequency - token doesn't exists")
		void getTokenFrequencyNotExists() {
			assertEquals(0, indexReader.getTokenFrequency("tokenThatDoesNotExists"));
		}

		@Test
		@DisplayName("Testing getTokenCollectionFrequency - token doesn't exists")
		void getTokenCollectionFrequencyNotExists() {
			assertEquals(0, indexReader.getTokenCollectionFrequency("tokenThatDoesNotExists"));
		}

		@Test
		@DisplayName("Testing getReviewsWithToken - token doesn't exists")
		void getReviewsWithTokenNotExists(){
			ArrayList<Integer> arr = Collections.list(indexReader.getReviewsWithToken("Sagiv"));
			int[] actual =  arr.stream().mapToInt(Integer::intValue).toArray();
			int[] expected = {};
			assertArrayEquals(expected, actual, () -> String.format(msgStr, "Sagiv"));
		}

		/** helper for testing Frequencies of Tokens on several valid inputs */
		private void testTokenFrequency(String[] inputs, int[] expectedOutputs, Function<String, Integer> func){
			for (int i = 0; i < inputs.length; i++) {
				assertEquals(expectedOutputs[i], (int)func.apply(inputs[i]), String.format(msgStr, inputs[i]));
			}
		}
	}

	@Nested
	@DisplayName("Testing ProductId as parameter")
	class TestProductIdFunctions{

		@Test
		@DisplayName("Testing getProductReviews - valid inputs")
		void getProductReviews(){
			String[] inputs = {"B006K2ZZ7K", "B001GVISJM", "B0048IACB2", "B006F2NYI2"};
			int[][] expectedOutputs = {
					{5, 6, 7, 8},
					{14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28},
					{987},
					{988, 989, 990, 991, 992, 993, 994, 995, 996, 997, 998, 999, 1000},
			};
			testEnumerations(inputs, expectedOutputs, indexReader::getProductReviews);
		}

		@Test
		@DisplayName("Testing getProductReviews - productId doesn't exists")
		void  getProductReviewsNotExists(){
			ArrayList<Integer> arr = Collections.list(indexReader.getProductReviews("B000002399839829"));
			int[] actual =  arr.stream().mapToInt(Integer::intValue).toArray();
			int[] expected = {};
			assertArrayEquals(expected, actual, () -> String.format(msgStr, "B000002399839829")); }
	}


	@Nested
	@DisplayName("Testing no parameter")
	class TestFunctionsWithNoParams{

		@Test
		@DisplayName("Testing getNumberOfReviews")
		void getNumberOfReviews(){
			assertEquals(1000, indexReader.getNumberOfReviews(), "Should returns the total number of reviews");
		}

		@Test
		@DisplayName("Testing getTokenSizeOfReviews")
		void getTokenSizeOfReviews(){
			assertEquals(75447, indexReader.getTokenSizeOfReviews(),
					"Should returns the total number of tokens include repetitions");
		}
	}


	/** helper for testing Enumerations returned values */
	private void testEnumerations(String[] inputs, int[][] expectedOutputs,
								  Function<String, Enumeration<Integer>> func) {
		for (int i = 0; i < inputs.length; i++) {
			ArrayList<Integer> arr = Collections.list(func.apply(inputs[i]));
			int[] actual =  arr.stream().mapToInt(Integer::intValue).toArray();
			assertArrayEquals(expectedOutputs[i], actual, String.format(msgStr, inputs[i]));
		}
	}


	/** comment for not checking removeIndex method of slowWriter!. */
	@AfterAll
	static void removeDir(){
		indexWriter.removeIndex(indexDir);
		File directory = new File(indexDir);
		assertFalse(directory.exists());
	}
}