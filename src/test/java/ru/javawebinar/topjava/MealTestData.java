package ru.javawebinar.topjava;


import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ + 2;

    public static final Meal USER_MEAL_1 =
            new Meal(MEAL_ID, LocalDateTime.of(2020, Month.FEBRUARY, 23, 10, 0, 0), "завтрак", 1000);
    public static final Meal USER_MEAL_2 =
            new Meal(MEAL_ID + 1, LocalDateTime.of(2020, Month.FEBRUARY, 23, 14, 0, 0), "обед", 500);
    public static final Meal USER_MEAL_3 =
            new Meal(MEAL_ID + 2, LocalDateTime.of(2020, Month.FEBRUARY, 23, 18, 0, 0), "ужин", 500);
    public static final Meal USER_MEAL_4 =
            new Meal(MEAL_ID + 3, LocalDateTime.of(2020, Month.FEBRUARY, 23, 20, 0, 0), "Еда на граничное значение", 100);
    public static final Meal USER_MEAL_5 =
            new Meal(MEAL_ID + 4, LocalDateTime.of(2020, Month.FEBRUARY, 24, 10, 0, 0), "завтрак", 1000);
    public static final Meal USER_MEAL_6 =
            new Meal(MEAL_ID + 5, LocalDateTime.of(2020, Month.FEBRUARY, 24, 13, 0, 0), "обед", 300);
    public static final Meal USER_MEAL_7 =
            new Meal(MEAL_ID + 6, LocalDateTime.of(2020, Month.FEBRUARY, 24, 18, 0, 0), "ужин", 700);

    public static final Meal ADMIN_MEAL_1 =
            new Meal(MEAL_ID + 7, LocalDateTime.of(2020, Month.FEBRUARY, 24, 10, 0, 0), "завтрак admin", 1000);
    public static final Meal ADMIN_MEAL_2 =
            new Meal(MEAL_ID + 8, LocalDateTime.of(2020, Month.FEBRUARY, 24, 13, 0, 0), "обед admin", 300);
    public static final Meal ADMIN_MEAL_3 =
            new Meal(MEAL_ID + 9, LocalDateTime.of(2020, Month.FEBRUARY, 24, 18, 0, 0), "ужин admin", 700);

    public static final List<Meal> USER_MEALS =
            Arrays.asList(USER_MEAL_7, USER_MEAL_6, USER_MEAL_5, USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1);

    public static final List<Meal> ADMIN_MEALS = Arrays.asList(ADMIN_MEAL_3, ADMIN_MEAL_2, ADMIN_MEAL_1);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2020, Month.FEBRUARY, 23, 13, 0,0), "test", 400);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
