package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository, MeterRegistry meterRegistry) {
        this.timeEntryRepository=timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate){

        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());

        ResponseEntity response = new ResponseEntity(createdTimeEntry, HttpStatus.CREATED);

        return response;
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> read(@PathVariable long timeEntryId){

        ResponseEntity<TimeEntry> response;

        TimeEntry timeEntry = timeEntryRepository.find(timeEntryId);
        if(timeEntry==null){
            response = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            actionCounter.increment();
            response = new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list(){

        List<TimeEntry> timeEntries = timeEntryRepository.list();
        actionCounter.increment();
        return new ResponseEntity<>(timeEntries, HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId ,@RequestBody TimeEntry expected ){
        ResponseEntity response;
        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId, expected);
        if(timeEntry==null){
            response = new ResponseEntity(null, HttpStatus.NOT_FOUND);
        } else {
            actionCounter.increment();
            response = new ResponseEntity(timeEntry, HttpStatus.OK);
        }

        return response;
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId){

        ResponseEntity response;
        timeEntryRepository.delete(timeEntryId);
        actionCounter.increment();
        timeEntrySummary.record(timeEntryRepository.list().size());
        response = new ResponseEntity(HttpStatus.NO_CONTENT);
        return response;
    }
}
