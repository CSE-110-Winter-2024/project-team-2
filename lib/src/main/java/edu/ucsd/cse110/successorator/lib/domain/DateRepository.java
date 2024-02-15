package edu.ucsd.cse110.successorator.lib.domain;

import java.util.Calendar;

import edu.ucsd.cse110.successorator.lib.util.SimpleSubject;
import edu.ucsd.cse110.successorator.lib.util.Subject;
import edu.ucsd.cse110.successorator.lib.util.date.DateProvider;

public class DateRepository {
    public DateProvider dateProvider;
    private final SimpleSubject<Calendar> calendar;
    public DateRepository(DateProvider dateProvider) {
        this.dateProvider = dateProvider;
        calendar = new SimpleSubject<>();
        calendar.setValue(dateProvider.getCurrentDate());
    }

    public void advanceDateOneDayForward() {
        Calendar value = calendar.getValue();
        if (value == null) {
            return;
        }

        value.add(Calendar.DATE,1);
        calendar.setValue(value);
    }

    public Subject<Calendar> getDate(){
        return calendar;
    }
}
