package trayio.models;

import org.json.JSONObject;

import java.time.LocalDateTime;

public class WorkflowExecution {

    private final int id;
    private int step;
    private Workflow workflow;
    private LocalDateTime created_at;

    public WorkflowExecution(int id, Workflow workflow){
        this.id = id;
        this.step = 0;
        this.created_at = LocalDateTime.now();
        this.workflow = workflow;
    }
    public int getId() {return id;}

    public int getStep() {return step;}

    public int getMaxSteps() { return workflow.getStepsNumber(); }

    public void setStep(int currentStep){ step = currentStep;}

    public void incrementStep(){ this.step += 1; }

    public LocalDateTime getCreatedAt() {return created_at;}

    public String toString(){ return new JSONObject(this).toString();}

}
