package pro.sky.sockswarehouse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pro.sky.sockswarehouse.controller.SocksController;
import pro.sky.sockswarehouse.dto.SocksDto;
import pro.sky.sockswarehouse.entity.Socks;
import pro.sky.sockswarehouse.exception.NoNumberOfSocksException;
import pro.sky.sockswarehouse.repository.SocksRepository;
import pro.sky.sockswarehouse.service.SocksService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pro.sky.sockswarehouse.operation.ComparisonOperator.equal;
import static pro.sky.sockswarehouse.operation.ComparisonOperator.lessThan;

@WebMvcTest
public class SocksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SocksRepository socksRepository;

    @SpyBean
    private SocksService socksService;

    @InjectMocks
    private SocksController socksController;

    @Autowired
    private ObjectMapper objectMapper;

    private Socks getMockSocks() {
        Socks socks = new Socks();
        socks.setId(1L);
        socks.setColor("red");
        socks.setCottonPart(50);
        socks.setQuantity(20);
        return socks;
    }

    @Test
    void contextLoads() {
        Assertions.assertThat(socksController).isNotNull();
    }

    @Test
    public void incomeSocksTest() throws Exception {
        Socks socks = getMockSocks();

        SocksDto socksDto = new SocksDto();
        socksDto.setColor(socks.getColor());
        socksDto.setCottonPart(socks.getCottonPart());
        socksDto.setQuantity(5);
        String json = objectMapper.writeValueAsString(socksDto);

        Socks updateSocks = getMockSocks();;
        updateSocks.setQuantity(25);

        when(socksRepository.findSocksByColorAndCottonPart(socks.getColor(), socks.getCottonPart()))
                .thenReturn(Optional.of(socks));
        when(socksRepository.save(any(Socks.class))).thenReturn(updateSocks);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.color").value("red"))
                .andExpect(jsonPath("$.cottonPart").value(50))
                .andExpect(jsonPath("$.quantity").value(25));

        socksDto.setQuantity(-5);
        json = objectMapper.writeValueAsString(socksDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        assertThrows(IllegalArgumentException.class,
                () -> socksService.incomeSocks(socksDto));

        socksDto.setQuantity(5);
        socksDto.setCottonPart(-5);
        json = objectMapper.writeValueAsString(socksDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        assertThrows(IllegalArgumentException.class,
                () -> socksService.outcomeSocks(socksDto));

        socksDto.setCottonPart(105);
        json = objectMapper.writeValueAsString(socksDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/income")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        assertThrows(IllegalArgumentException.class,
                () -> socksService.outcomeSocks(socksDto));
    }


    @Test
    public void outcomeSocksTest() throws Exception {
        Socks socks = getMockSocks();

        SocksDto socksDto = new SocksDto();
        socksDto.setColor(socks.getColor());
        socksDto.setCottonPart(socks.getCottonPart());
        socksDto.setQuantity(5);
        String json = objectMapper.writeValueAsString(socksDto);

        Socks updateSocks = getMockSocks();;
        updateSocks.setQuantity(15);

        when(socksRepository.findSocksByColorAndCottonPart(socks.getColor(), socks.getCottonPart()))
                .thenReturn(Optional.of(socks));
        when(socksRepository.save(any(Socks.class))).thenReturn(updateSocks);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.color").value("red"))
                .andExpect(jsonPath("$.cottonPart").value(50))
                .andExpect(jsonPath("$.quantity").value(15));

        socksDto.setQuantity(-5);
        json = objectMapper.writeValueAsString(socksDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        assertThrows(IllegalArgumentException.class,
                () -> socksService.outcomeSocks(socksDto));

        socksDto.setQuantity(16);
        json = objectMapper.writeValueAsString(socksDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        assertThrows(NoNumberOfSocksException.class,
                () -> socksService.outcomeSocks(socksDto));

        socksDto.setQuantity(14);
        socksDto.setCottonPart(-5);
        json = objectMapper.writeValueAsString(socksDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        assertThrows(IllegalArgumentException.class,
                () -> socksService.outcomeSocks(socksDto));

        socksDto.setCottonPart(105);
        json = objectMapper.writeValueAsString(socksDto);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/socks/outcome")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        assertThrows(IllegalArgumentException.class,
                () -> socksService.outcomeSocks(socksDto));
    }


    @Test
    public void getSocksTest() throws Exception {

        when(socksRepository.getQuantitySocksByColorAndCottonPartLessThan(anyString(), anyInt()))
                .thenReturn(Optional.of(1000));
        when(socksRepository.getQuantitySocksByColorAndCottonPartEqual(anyString(), anyInt()))
                .thenReturn(Optional.of(2000));
        when(socksRepository.getQuantitySocksByColorAndCottonPartMoreThan(anyString(), anyInt()))
                .thenReturn(Optional.of(3000));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .queryParam("color", "black")
                        .queryParam("comparisonOperator", "lessThan")
                        .queryParam("cottonPart","100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1000"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .queryParam("color", "black")
                        .queryParam("comparisonOperator", "equal")
                        .queryParam("cottonPart","100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("2000"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .queryParam("color", "black")
                        .queryParam("comparisonOperator", "moreThan")
                        .queryParam("cottonPart","100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("3000"));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .queryParam("color", "black")
                        .queryParam("comparisonOperator", "non-existent")
                        .queryParam("cottonPart", "100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .queryParam("color", "black")
                        .queryParam("comparisonOperator", "lessThan")
                        .queryParam("cottonPart","-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        assertThrows(IllegalArgumentException.class,
                () -> socksService.getSocks("black", lessThan, -1));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .queryParam("color", "black")
                        .queryParam("comparisonOperator", "equal")
                        .queryParam("cottonPart","101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
        assertThrows(IllegalArgumentException.class,
                () -> socksService.getSocks("black", equal, 101));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/socks")
                        .queryParam("color", "black")
                        .queryParam("comparisonOperator", "moreThan")
                        .queryParam("cottonPart","notNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));

    }
}
