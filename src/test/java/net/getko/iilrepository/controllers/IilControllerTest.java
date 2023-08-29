package net.getko.iilrepository.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.models.dto.IilDto;
import net.getko.iilrepository.services.IilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class IilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IilService iilService;

    @Mock
    private DomainDtoMapper<Iil, IilDto> iilDomainToDtoMapper;

    @Mock
    private DomainDtoMapper<IilDto, Iil> iilDtoToDomainMapper;

    @InjectMocks
    private IilController iilController;

    private final UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(iilController).build();
    }

    @Test
    void getIils() throws Exception {
        // Arrange
        Iil iil1 = new Iil();
        Iil iil2 = new Iil();
        List<Iil> iils = Arrays.asList(iil1, iil2);
/*
        IilDto iilDto1 = new IilDto(iil1.getId(), iil1.getName());
        IilDto iilDto2 = new IilDto(iil2.getId(), iil2.getName());
        List<IilDto> iilDtos = Arrays.asList(iilDto1, iilDto2);

        when(iilService.findAll()).thenReturn(iils);
        when(iilDomainToDtoMapper.convertToList(any(), eq(IilDto.class))).thenReturn(iilDtos);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/iils")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(iilDto1.getId().toString()))
                .andExpect(jsonPath("$[0].name").value(iilDto1.getName()))
                .andExpect(jsonPath("$[1].id").value(iilDto2.getId().toString()))
                .andExpect(jsonPath("$[1].name").value(iilDto2.getName()));
    }

    @Test
    public void testGetIil() throws Exception {
        UUID id = UUID.randomUUID();
        Iil iil = new Iil();
        iil.setId(id);
        iil.setName("Test Iil");
        iil.setDescription("Test description");
        iil.setHelp(new HashMap<>());
        iil.setAbout(new HashMap<>());

        Mockito.when(iilService.findOne(id)).thenReturn(iil);

        mockMvc.perform(get("/api/iils/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.name", is("Test Iil")))
                .andExpect(jsonPath("$.description", is("Test description")))
                .andExpect(jsonPath("$.help", isA(Map.class)))
                .andExpect(jsonPath("$.about", isA(Map.class)));

        Mockito.verify(iilService, times(1)).findOne(id);
        Mockito.verifyNoMoreInteractions(iilService);
    }

    @Test
    public void testDeleteIil() throws Exception {
        doNothing().when(iilService).delete(uuid);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/iils/{id}", uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(iilService, times(1)).delete(uuid);

 */
    }
}
