package com.ratnesh.junit5.health;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

class DietPlannerTest {
	private DietPlanner dietPlanner;

	@BeforeEach
	void berforEach() {
		this.dietPlanner = new DietPlanner(20, 30, 50);
	}

	@AfterEach
	void afterEach() {
		System.out.println("A unit test was finished");
	}

	// @Disabled
	// @DisabledOnOs(value = OS.LINUX)
	@RepeatedTest(value = 10, name = RepeatedTest.LONG_DISPLAY_NAME) // run same test 10 times
	@DisplayName("example test name")
	void should_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty() {
		// given
		Coder coder = new Coder(1.82, 75.0, 26, Gender.MALE);
		DietPlan expected = new DietPlan(2202, 110, 73, 275);

		// when
		DietPlan actual = dietPlanner.calculateDiet(coder);

		// then
		assertAll(() -> assertEquals(expected.getCalories(), actual.getCalories()),
				() -> assertEquals(expected.getProtein(), actual.getProtein()),
				() -> assertEquals(expected.getFat(), actual.getFat()),
				() -> assertEquals(expected.getCarbohydrate(), actual.getCarbohydrate()));
	}

}
