package edu.ucsd.cse110.successorator.app.util;

import androidx.lifecycle.MutableLiveData;

import edu.ucsd.cse110.successorator.lib.util.MutableSubject;

/**
 * Adapter class that extends the LiveDataSubjectAdapter class,
 * providing mutability to the underlying LiveData.
 */
public class MutableLiveDataSubjectAdapter<T> extends LiveDataSubjectAdapter<T> implements MutableSubject<T> {
    private final MutableLiveData<T> mutableAdaptee;

    public MutableLiveDataSubjectAdapter(MutableLiveData<T> adaptee) {
        super(adaptee);
        this.mutableAdaptee = adaptee;
    }

    @Override
    public void setValue(T value) {
        mutableAdaptee.setValue(value);
    }
}
