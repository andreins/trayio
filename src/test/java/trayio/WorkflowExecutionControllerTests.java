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
public class WorkflowExecutionControllerTests {

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void createWorkflowExecution() throws Exception {
        this.mockMvc.perform(get("/reset"));

        this.mockMvc.perform(get("/create-workflow?steps=1")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.stepsNumber").value(1));
        this.mockMvc.perform(get("/create-workflow?steps=4")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.stepsNumber").value(4));
        this.mockMvc.perform(get("/create-workflow?steps=2")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.stepsNumber").value(2));

        // creation testing
        this.mockMvc.perform(get("/create-workflow-execution?workflow-id=1")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.step").value(0))
                .andExpect(jsonPath("$.maxSteps").value(1));
        this.mockMvc.perform(get("/create-workflow-execution?workflow-id=3")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.step").value(0))
                .andExpect(jsonPath("$.maxSteps").value(2));
        this.mockMvc.perform(get("/create-workflow-execution?workflow-id=2")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.step").value(0))
                .andExpect(jsonPath("$.maxSteps").value(4));

        //increment testing
        this.mockMvc.perform(get("/increment-workflow-execution-step?id=1")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.step").value(1))
                .andExpect(jsonPath("$.maxSteps").value(1));
        this.mockMvc.perform(get("/increment-workflow-execution-step?id=1")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.warn").value("Workflow execution already reached end. Step was not incremented further"));
        this.mockMvc.perform(get("/increment-workflow-execution-step?id=3"));
        this.mockMvc.perform(get("/increment-workflow-execution-step?id=3"));
        this.mockMvc.perform(get("/increment-workflow-execution-step?id=3"));
        this.mockMvc.perform(get("/increment-workflow-execution-step?id=3"));
        this.mockMvc.perform(get("/increment-workflow-execution-step?id=3")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.warn").value("Workflow execution already reached end. Step was not incremented further"));

        // error testing
        this.mockMvc.perform(get("/create-workflow-execution?workflow-id=6")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Workflow with id 6 does not exist"));
        this.mockMvc.perform(get("/create-workflow-execution?workflow-id=test")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("workflow-id should be a valid int, current value: test"));
        this.mockMvc.perform(get("/increment-workflow-execution-step?id=13")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("No workflow execution with such ID:13"));
        this.mockMvc.perform(get("/increment-workflow-execution-step?id=test")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("id should be a valid int, current value: test"));

    }
}
