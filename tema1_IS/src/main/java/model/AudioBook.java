package model;

import java.time.LocalDate;

public class AudioBook extends PhysicalBook {

    private int runTime; // duration in minutes
    public int getRunTime() {
        return runTime;
    }

    public void setRunTime(int runTime) {
        this.runTime = runTime;
    }

    @Override
    public String toString() {
        return String.format("AudioBook author: %s | title: %s | Run Time: %d minutes | Published Date: %s.", getAuthor(), getTitle(), runTime, getPublishedDate());
    }
}
