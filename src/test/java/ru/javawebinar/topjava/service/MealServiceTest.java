package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml",
        "classpath:spring/spring-jdbc.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void create() throws Exception {
        Meal newMeal = getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test(expected = NotFoundException.class)
    public void delete() throws Exception {
        service.delete(MEAL_ID, USER_ID);
        service.get(MEAL_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFound() throws Exception {
        service.delete(0, USER_ID);
    }

    @Test
    public void get() throws Exception {
        Meal newMeal = service.get(USER_MEAL_1.getId(), USER_ID);
        assertMatch(USER_MEAL_1, newMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUserMeal() {
        service.get(USER_MEAL_1.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnotherUserMeal() {
        service.delete(USER_MEAL_1.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnotherUserMeal() {
        USER_MEAL_1.setCalories(2007);
        USER_MEAL_1.setDescription("New description");
        service.update(USER_MEAL_1, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal meal = USER_MEAL_1;
        meal.setDescription("New description");
        meal.setCalories(1000);
        service.update(meal, USER_ID);
        assertMatch(service.get(USER_MEAL_1.getId(), USER_ID), meal);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, USER_MEALS);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTime() {
        Meal meal = new Meal(null, LocalDateTime.of(2020, Month.FEBRUARY, 24, 18, 0, 0), "ужин", 700);
        service.create(meal, USER_ID);
    }

    @Test
    public void getMealsBetweenDates() {
        LocalDate startDate = LocalDate.of(2020, Month.FEBRUARY, 24);
        LocalDate endDate = LocalDate.of(2020, Month.FEBRUARY, 25);
        List<Meal> filteredUserMeals = service.getBetweenHalfOpen(startDate, endDate, USER_ID);
        assertMatch(filteredUserMeals, Arrays.asList(USER_MEAL_7, USER_MEAL_6, USER_MEAL_5));
    }

    @Test
    public void getMealsBetweenDatesWithNullStartDate() {
        LocalDate endDate = LocalDate.of(2020, Month.FEBRUARY, 23);
        List<Meal> filteredUserMeals = service.getBetweenHalfOpen(null, endDate, USER_ID);
        assertMatch(filteredUserMeals, Arrays.asList(USER_MEAL_4, USER_MEAL_3, USER_MEAL_2, USER_MEAL_1));
    }

    @Test
    public void getMealsBetweenDatesWithNullEndDate() {
        LocalDate startDate = LocalDate.of(2020, Month.FEBRUARY, 24);
        List<Meal> filteredUserMeals = service.getBetweenHalfOpen(startDate, null, USER_ID);
        assertMatch(filteredUserMeals, Arrays.asList(USER_MEAL_7, USER_MEAL_6, USER_MEAL_5));
    }

    @Test
    public void getMealsBetweenNullDates() {
        List<Meal> filteredUserMeals = service.getBetweenHalfOpen(null, null, USER_ID);
        assertMatch(filteredUserMeals, USER_MEALS);
    }

    @Test
    public void getMealsBetweenIrrelevantDates() {
        LocalDate startDate = LocalDate.of(2020, Month.FEBRUARY, 1);
        LocalDate endDate = LocalDate.of(2020, Month.FEBRUARY, 3);
        List<Meal> filteredUserMeals = service.getBetweenHalfOpen(startDate, endDate, USER_ID);
        assertMatch(filteredUserMeals, Collections.emptyList());
    }
}