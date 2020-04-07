package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    TimeEntryRepository timeEntryRepository;

    @Autowired
    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository=timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate){

        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
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
            response = new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }

        return response;
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list(){

        List<TimeEntry> timeEntries = timeEntryRepository.list();

        return new ResponseEntity<>(timeEntries, HttpStatus.OK);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId ,@RequestBody TimeEntry expected ){
        ResponseEntity response;
        TimeEntry timeEntry = timeEntryRepository.update(timeEntryId, expected);
        if(timeEntry==null){
            response = new ResponseEntity(null, HttpStatus.NOT_FOUND);
        } else {
            response = new ResponseEntity(timeEntry, HttpStatus.OK);
        }

        return response;
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity delete(@PathVariable long timeEntryId){

        ResponseEntity response;
        timeEntryRepository.delete(timeEntryId);
        response = new ResponseEntity(HttpStatus.NO_CONTENT);
        return response;
    }
}
