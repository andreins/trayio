package trayio;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class WorkflowControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createWorkflow() throws Exception {
        this.mockMvc.perform(get("/reset")); // needed because there is no database 
        this.mockMvc.perform(get("/create-workflow?steps=5")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stepsNumber").value(5));
        this.mockMvc.perform(get("/create-workflow?steps=8")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.stepsNumber").value(8));
        this.mockMvc.perform(get("/create-workflow?steps=test")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("steps should be a valid int, currently test"));
    }

    @Test
    public void getWorkflow() throws Exception {
        this.mockMvc.perform(get("/reset")); // needed because there is no database
        this.mockMvc.perform(get("/create-workflow?steps=5"));
        this.mockMvc.perform(get("/create-workflow?steps=8"));

        this.mockMvc.perform(get("/get-workflow?id=2")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.stepsNumber").value(8));
        this.mockMvc.perform(get("/get-workflow?id=test")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("id should be a valid int, currently test"));
        this.mockMvc.perform(get("/get-workflow?id=5")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("No workflow with such ID"));
    }

}
