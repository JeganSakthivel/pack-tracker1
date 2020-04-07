package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    Map<Long, TimeEntry> timeEntries = new HashMap<Long,TimeEntry>();
    long i=0L;

    public InMemoryTimeEntryRepository() {
        super();
    }

    @Override
    public TimeEntry create(TimeEntry timeEntry) {
        i++;
        timeEntry.setId(i);
        timeEntries.put(i, timeEntry );
        return timeEntry;
    }

    @Override
    public TimeEntry find(long timieEntryId) {
        return timeEntries.get(timieEntryId);
    }

    @Override
    public List<TimeEntry> list() {
        List<TimeEntry> timeEntryList = new ArrayList<>();


        for(Long id: timeEntries.keySet()) {
            timeEntryList.add(timeEntries.get(id));
        }

        return timeEntryList;
    }

    @Override
    public TimeEntry update(long eq, TimeEntry timeEntry) {
        if(timeEntries.get(eq)==null){
            return null;
        }

        timeEntry.setId(eq);
        timeEntries.put(eq, timeEntry);
        return timeEntry;
    }

    @Override
    public void delete(long timeEntryId) {
        timeEntries.remove(timeEntryId);
    }
}
