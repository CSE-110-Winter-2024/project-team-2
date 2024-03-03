package edu.ucsd.cse110.successorator.lib.domain;

import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;

public class ViewRepository {
    private final SimpleSubject<ViewOptions> view;

    public ViewRepository() {
        view = new SimpleSubject<>();
        view.setValue(ViewOptions.TODAY);
    }

    public void setView(ViewOptions view) {
        this.view.setValue(view);
    }

    public Subject<ViewOptions> getView(){
        return view;
    }
}
