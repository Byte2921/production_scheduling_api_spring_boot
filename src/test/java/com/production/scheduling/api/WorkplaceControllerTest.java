package com.production.scheduling.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.production.scheduling.exceptions.WorkplaceNotFoundException;
import com.production.scheduling.model.Workplace;
import com.production.scheduling.repository.WorkplaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = WorkplaceController.class)
class WorkplaceControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private WorkplaceRepository workplaceRepository;

    private Workplace workplace1 = new Workplace("Building machine");
    private Workplace workplace2 = new Workplace("Packing machine");

    @Test
    void gettingAllWorkplacesReturnsTheProperJSONString() throws Exception {
        List<Workplace> workplaces = new ArrayList<>();
        workplaces.add(workplace1);
        workplaces.add(workplace2);
        String expected = "[{\"id\":1,\"name\":\"Building machine\"},{\"id\":2,\"name\":\"Packing machine\"}]";

        Mockito.when(workplaceRepository
                        .findAll())
                .thenReturn(workplaces);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/workplace/tasks/workplaces")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[1].name", is("Packing machine")));

    }

    @Test
    void searchingWithExistingIdReturnsWorkplace() throws Exception {
        Mockito.when(workplaceRepository
                        .findById(1L))
                .thenReturn(Optional.of(workplace1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/workplace/tasks/workplaces/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()));
    }

    @Test
    void searchingWithNotExistingIdReturnsNotFound() throws Exception {
        Mockito.when(workplaceRepository
                        .findById(5L))
                .thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/workplace/tasks/workplaces/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(mvcResult -> assertTrue(mvcResult.getResolvedException() instanceof WorkplaceNotFoundException));
    }

    @Test
    void creatingNewWorkplaceReturnsSuccess() throws Exception {
        Mockito.when(workplaceRepository
                        .save(workplace1))
                .thenReturn(workplace1);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/workplace/tasks/workplaces/new")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(workplace1));

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Building machine")));
    }

    @Test
    void deletingWorkplaceReturnsSuccess() throws Exception {
        Mockito.when(workplaceRepository
                        .findById(workplace1.getId()))
                .thenReturn(Optional.of(workplace1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/workplace/tasks/workplaces/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}