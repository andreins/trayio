package trayio.models;

import org.json.JSONObject;

public class Workflow {

    private final int id;
    private final int stepsNumber;

    public Workflow(int id, int stepsNumber){
        this.id = id;
        this.stepsNumber = stepsNumber;
    }

    public int getId() {return id;}

    public int getStepsNumber() {return stepsNumber;}

    public String toString(){
        return new JSONObject(this).toString();
    }

}
