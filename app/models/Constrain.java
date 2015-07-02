package models;

/**
 * Created by anthony on 7/2/15.
 */
public class Constrain {
    private int time;
    private int memory;

    public Constrain(int time, int memory) {
        this.time = time;
        this.memory = memory;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }
}
