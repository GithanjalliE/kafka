package com.task.verve;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class VerveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAcceptRequest_Success() throws Exception {
        mockMvc.perform(get("/api/verve/accept?id=125"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    public void testAcceptRequest_Failed_MissingId() throws Exception {
        mockMvc.perform(get("/api/verve/accept"))
                .andExpect(status().isBadRequest());  // Expecting 400 because 'id' is missing
    }

}
