package cis350.upenn.edu.sapling;

// @author: amenarde
// a goal is a conceptual daily task you can work toward; one whose success can be defined by a
// boolean. for example, "went to the gym" is a goal, because the answer is generally yes or no
// some things can be framed as both goals or metrics, for example "read a book" could have a
// boolean orientation, but someone could choose to put it as a metric, in order to log how much
// they felt as though they were reading, where a 1 is a way of describing a failure to read.

public class Goal {
    private final String name;
    private Boolean completed;

    public Goal(String name, Boolean completed) {
        if (name == null) {
            throw new IllegalArgumentException("Name can not be null");
        }

        this.name = name;
        this.completed = completed;
    }

    public String getName()
    {
        return name;
    }

    // goals default to false (uncompleted)
    // not that metrics do not default to false, as there is no default on a scale
    public boolean getCompleted() {
        if (completed == null) {
            return false;
        }

        return completed;
    }
}
