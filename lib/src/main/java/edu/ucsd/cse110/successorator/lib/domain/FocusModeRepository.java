package edu.ucsd.cse110.successorator.lib.domain;

import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;

public class FocusModeRepository {
    private final SimpleSubject<GoalContext> focusContext;

    public FocusModeRepository() {
        focusContext = new SimpleSubject<>();
        focusContext.setValue(null);
    }

    public void setFocusContext(GoalContext focusContext) {
        this.focusContext.setValue(focusContext);
    }

    public Subject<GoalContext> getFocusContext() {
        return focusContext;
    }
}
