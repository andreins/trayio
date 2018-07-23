package trayio.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import trayio.models.Workflow;
import trayio.models.WorkflowExecution;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class WorkflowExecutionController {
    private ArrayList<WorkflowExecution> workflowExecutions = new ArrayList<>();
    private final AtomicInteger workflowExecutionId = new AtomicInteger();

    @RequestMapping("/create-workflow-execution")
    public String createWorkflowExecution(@RequestParam(value="workflow-id") int workflowId,
                                                     @RequestParam(value="step", defaultValue = "0") String step){
        Workflow workflow = WorkflowController.getWorkflow(workflowId - 1);
        if (workflow == null)
            return "{\"error\": \"Workflow with id " + workflowId + " does not exist\"}" ;
        WorkflowExecution workflowExecution = new WorkflowExecution(workflowExecutionId.incrementAndGet(), workflow);
        if (!step.equals("")) workflowExecution.setStep(Integer.valueOf(step));
        workflowExecutions.add(workflowExecution);
        return workflowExecution.toString();
    }

    private WorkflowExecution getWorkflowExecution(int id){
        try {
            return workflowExecutions.get(id);
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    @RequestMapping("/get-workflow-execution")
    public String getWorkflowExecutionRoute(@RequestParam(value="id") int id){
        WorkflowExecution we = getWorkflowExecution(id - 1);
        return we == null ? "{\"error\": \"No workflow execution with such ID:" + id + "\"}" : we.toString();
    }

    @RequestMapping("/increment-workflow-execution-step")
    public String incrementStep(@RequestParam(value="id") int id){
        WorkflowExecution we = getWorkflowExecution(id - 1);
        if (we == null) return "{\"error\": \"No workflow execution with such ID:" + id  + "\"}";
        if (we.getStep() == we.getMaxSteps())
            return "{\"warn\": \"Workflow execution already reached end. Step was not incremented further\"}";
        we.incrementStep();
        return we.toString();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        return String.format("{\"error\": \"%s should be a valid %s, current value: %s\"}",
                name, type, value);
    }
}
