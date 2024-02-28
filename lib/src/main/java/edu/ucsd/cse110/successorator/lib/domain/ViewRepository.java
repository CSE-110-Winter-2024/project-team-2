package edu.ucsd.cse110.successorator.lib.domain;

import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.util.views.ViewOptions;
import edu.ucsd.cse110.successorator.lib.util.views.ViewProvider;

public class ViewRepository {
    private final SimpleSubject<ViewOptions> view;

    public ViewRepository(ViewProvider viewProvider) {
        view = new SimpleSubject<>();
        view.setValue(viewProvider.getDefaultView());
    }

    public void setView(ViewOptions view) {
        this.view.setValue(view);
    }

    public Subject<ViewOptions> getView(){
        return view;
    }
}
