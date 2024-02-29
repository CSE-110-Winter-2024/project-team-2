package edu.ucsd.cse110.successorator.lib.util.views;

/**
 * Interface for Providing the current goals list view
 */
public interface ViewProvider {
  ViewOptions getDefaultView();

  ViewOptions getTodayView();

  ViewOptions getTomorrowView();

  ViewOptions getRecurringView();

  ViewOptions getPendingView();
}
