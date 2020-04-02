package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

public class AdminUIControllerTest extends AbstractControllerTest {

    private static final String REST_URL = "/ajax/admin/users/";

    @Autowired
    private UserService userService;

    @Test
    void enable() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL + USER_ID)
                .param("enabled", "false")
                .content(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print());
        assertFalse(userService.get(USER_ID).isEnabled());
    }
}
