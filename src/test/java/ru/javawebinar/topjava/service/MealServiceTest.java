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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
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
        service.delete(0, 0);
    }

    @Test
    public void get() throws Exception {
        Meal meal = USER_MEAL.get(0);
        Meal newMeal = service.get(meal.getId(), USER_ID);
        assertMatch(meal, newMeal);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUserMeal() {
        Meal meal = USER_MEAL.get(0);
        service.get(meal.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnotherUserMeal() {
        Meal meal = USER_MEAL.get(0);
        service.delete(meal.getId(), ADMIN_ID);
    }

    @Test(expected = NotFoundException.class)
    public void updateAnotherUserMeal() {
        Meal meal = USER_MEAL.get(0);
        meal.setCalories(2007);
        meal.setDescription("New description");
        service.update(meal, ADMIN_ID);
    }

    @Test
    public void update() {
        Meal meal = USER_MEAL.get(0);
        meal.setDescription("New description");
        meal.setCalories(1000);
        service.update(meal, USER_ID);
        assertMatch(service.get(USER_MEAL.get(0).getId(), USER_ID), meal);
    }

    @Test
    public void getAll() {
        List<Meal> meals = service.getAll(USER_ID);
        assertMatch(meals, USER_MEAL);
    }

    @Test(expected = DataAccessException.class)
    public void duplicateDateTime() {
        Meal meal = new Meal(null, LocalDateTime.of(2020, Month.FEBRUARY, 24, 18, 0, 0), "ужин", 700);
        service.create(meal, USER_ID);
    }
}
