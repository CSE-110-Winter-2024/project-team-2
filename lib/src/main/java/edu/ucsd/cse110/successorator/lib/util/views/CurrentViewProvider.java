package edu.ucsd.cse110.successorator.lib.util.views;

public class CurrentViewProvider implements ViewProvider {
    @Override
    public ViewOptions getDefaultView() {
      return getTodayView();
    }

    public ViewOptions getTodayView() {
        return ViewOptions.TODAY;
    }

    public ViewOptions getTomorrowView() {
        return ViewOptions.TOMORROW;
    }

    public ViewOptions getRecurringView() {
        return ViewOptions.RECURRING;
    }

    public ViewOptions getPendingView() {
        return ViewOptions.PENDING;
    }
}
