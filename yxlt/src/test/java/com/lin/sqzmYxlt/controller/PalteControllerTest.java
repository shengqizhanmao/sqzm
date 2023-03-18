package com.lin.sqzmYxlt.controller;

import com.lin.common.pojo.Palte;
import com.lin.common.service.PalteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PalteController.class)
class PalteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PalteService mockPalteService;

    @Test
    void testGetPalte() throws Exception {
        // Setup
        // Configure PalteService.list(...).
        final Palte palte = new Palte();
        palte.setId("id");
        palte.setName("name");
        palte.setIcon("icon");
        palte.setPath("path");
        final List<Palte> paltes = Arrays.asList(palte);
        when(mockPalteService.list()).thenReturn(paltes);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/palte/get")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }

    @Test
    void testGetPalte_PalteServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockPalteService.list()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/palte/get")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("expectedResponse");
    }
}
