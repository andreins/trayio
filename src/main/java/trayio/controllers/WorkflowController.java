package trayio.controllers;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import trayio.models.Workflow;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class WorkflowController {

    private static final AtomicInteger workflowId = new AtomicInteger();
    private static ArrayList<Workflow> workflows = new ArrayList<>();

    public static Workflow getWorkflow(int id){
        try {
            return workflows.get(id);
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }
    }

    @RequestMapping("/create-workflow")
    public Workflow createWorkflow(@RequestParam(value="steps") int steps){
        Workflow workflow = new Workflow(workflowId.incrementAndGet(), steps);
        workflows.add(workflow);
        return workflow;
    }

    @RequestMapping("/get-workflow")
    public String getWorkflowRouter(@RequestParam(value="id") int id){
        Workflow workflow = getWorkflow(id - 1);
        return workflow == null ? "{\"error\": \"No workflow with such ID\"}" : workflow.toString();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public String handleTypeMismatch(MethodArgumentTypeMismatchException ex){
        String name = ex.getName();
        String type = ex.getRequiredType().getSimpleName();
        Object value = ex.getValue();
        return String.format("{\"error\": \"%s should be a valid %s, currently %s\"}",
                name, type, value);
    }

    public static void reset(){
        workflows = new ArrayList<>();
        workflowId.set(0);
    }
}
