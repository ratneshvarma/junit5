package com.ratnesh.junit5.health;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BMICalculatorTest {
	private String env = "env";

	@BeforeAll
	static void beforeAll() {
		// to initialize common objects
		System.out.println("before all");
	}

	@AfterAll
	static void afterAll() {
		// to close db connection, shut down server etc
		System.out.println("after all");
	}

	// organise same method test in same nested class
	@Nested
	class IsDietRecommended {
		// 1 example
		@Test
		void should_ReturnTrue_When_DietRecommended() {
			// given
			double weight = 89.0;
			double height = 1.72;

			// when
			boolean recommended = BMICalculator.isDietRecommended(weight, height);

			// then
			assertTrue(recommended);
		}

		// 2 example
		@ParameterizedTest
		@ValueSource(doubles = { 89.0, 90.0, 110.0 })
		void should_ReturnTrue_When_DietRecommended_UsingParameterizedTest(Double coderWeight) {
			// given
			double weight = coderWeight;
			double height = 1.72;

			// when
			boolean recommended = BMICalculator.isDietRecommended(weight, height);

			// then
			assertTrue(recommended);
		}

		// 3 example
		@ParameterizedTest(name = "weight={0}, height={1}")
		@CsvSource(value = { "89.0, 1.72", "90.0, 1.6", "110.0, 1.80" })
		void should_ReturnTrue_When_DietRecommended_UsingParameterizedTestCSV(Double coderWeight, Double coderHeight) {
			// given
			double weight = coderWeight;
			double height = coderHeight;

			// when
			boolean recommended = BMICalculator.isDietRecommended(weight, height);

			// then
			assertTrue(recommended);
		}

		// 4 example
		@ParameterizedTest(name = "weight={0}, height={1}")
		@CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
		void should_ReturnTrue_When_DietRecommended_UsingParameterizedTestCSVFile(Double coderWeight,
				Double coderHeight) {
			// given
			double weight = coderWeight;
			double height = coderHeight;

			// when
			boolean recommended = BMICalculator.isDietRecommended(weight, height);

			// then
			assertTrue(recommended);
		}

		@Test
		void should_ReturnFalse_When_DietRecommended() {
			// given
			double weight = 40.0;
			double height = 1.72;

			// when
			boolean recommended = BMICalculator.isDietRecommended(weight, height);

			// then
			assertFalse(recommended);
		}

		@Test
		void should_ThrowArithmeticException_When_HeightZero() {
			// given
			double weight = 90.0;
			double height = 0.0;

			// when
			Executable executable = () -> BMICalculator.isDietRecommended(weight, height);

			// then
			assertThrows(ArithmeticException.class, executable);
		}
	}

	@Nested
	class FindCoderWithWorstBMI {
		@Test
		void should_ReturnCoderWithWorstBMI_When_CoderListNotEmpty() {
			// given
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.82, 74.8));

			// when
			Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

			// then
			assertAll(() -> assertEquals(1.82, coderWorstBMI.getHeight()),
					() -> assertEquals(98.0, coderWorstBMI.getWeight()));
		}

		@Test
		void should_ReturnNullWorstBMI_When_CoderListEmpty() {
			// given
			List<Coder> coders = new ArrayList<>();

			// when
			Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);

			// then
			assertNull(coderWorstBMI);
		}

		@Test
		void should_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty() {
			// given
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.82, 64.7));
			double[] expected = { 18.52, 29.59, 19.53 };

			// when
			double[] bmiScores = BMICalculator.getBMIScores(coders);

			// then
			assertArrayEquals(expected, bmiScores);
		}
	}

	@Nested
	class GetBMIScores {
		@Test
		void should_ReturnCorrectBMIScoreArrayInMs_When_CoderListNotEmpty() {
			// given
			List<Coder> coders = new ArrayList<>();
			for (int i = 0; i < 10000; i++) {
				coders.add(new Coder(1.0 + i, 10.0 + i));
			}
			// when
			Executable executable = () -> BMICalculator.getBMIScores(coders);

			// then
			assertTimeout(Duration.ofMillis(100), executable);
		}

		// run test in specific env
		@Test
		void should_ReturnCorrectBMIScoreArrayInMsInPro_When_CoderListNotEmpty() {
			// given
			assumeTrue(BMICalculatorTest.this.env.equals("prod"));
			List<Coder> coders = new ArrayList<>();
			for (int i = 0; i < 10000; i++) {
				coders.add(new Coder(1.0 + i, 10.0 + i));
			}
			// when
			Executable executable = () -> BMICalculator.getBMIScores(coders);

			// then
			assertTimeout(Duration.ofMillis(100), executable);
		}

	}

}
