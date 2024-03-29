package com.damoacon.domain.event.controller;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("getMainEvents() Test")
    public void getMainEvents() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/event"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("getDetailEvent() Test")
    public void getDetailEvent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/event/118"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}