package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.web.meal.MealRestController.REST_URL;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.TestUtil.readListFromJsonMvcResult;

public class MealRestControllerTest extends AbstractControllerTest {

    @Autowired
    MealService mealService;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readListFromJsonMvcResult(result, MealTo.class))
                        .isEqualTo(getTos(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/" + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(MEAL1));
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter?startDate=2020-01-30&startTime=10:00&endDate=2020-01-31&endTime=13:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readListFromJsonMvcResult(result, MealTo.class))
                        .isEqualTo(List.of(createTo(MEAL5, true), createTo(MEAL1, false))));
    }

    @Test
    void getBetweenWithNullStartDateAndStartTime() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter?endDate=2020-01-30&endTime=12:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readListFromJsonMvcResult(result, MealTo.class))
                        .isEqualTo(List.of(createTo(MEAL1, false))));
    }

    @Test
    void getBetweenWithoutFilters() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readListFromJsonMvcResult(result, MealTo.class))
                        .isEqualTo(getTos(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    void getBetweenWithNullStartDateAndEndDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter?startTime=11:00&endTime=14:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readListFromJsonMvcResult(result, MealTo.class))
                        .isEqualTo(List.of(createTo(MEAL6, true), createTo(MEAL2, false))));
    }

    @Test
    void getBetweenWithNullStartTime() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter?startDate=2020-01-30&endDate=2020-01-31&endTime=12:00"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readListFromJsonMvcResult(result, MealTo.class))
                        .isEqualTo(List.of(createTo(MEAL5, true),
                                createTo(MEAL4, true),createTo(MEAL1, false))));
    }

    @Test
    void getBetweenWithStartDate() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "/filter?startDate=2020-01-31"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertThat(readListFromJsonMvcResult(result, MealTo.class))
                        .isEqualTo(List.of(createTo(MEAL7, true), createTo(MEAL6, true),
                                createTo(MEAL5, true), createTo(MEAL4, true))));
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = getNew();
        ResultActions actions = perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());
        Meal created = readFromJson(actions, Meal.class);
        int newId = created.id();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + "/" + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + "/" + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL1_ID, USER_ID));
    }
}