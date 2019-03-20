package com.epam.addressbook.controller;

import com.epam.addressbook.model.Accommodation;
import com.epam.addressbook.repository.AccommodationRepository;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accommodations")
public class AccommodationController {

    private AccommodationRepository accommodationRepository;
    private final MeterRegistry meterRegistry;

    public AccommodationController(AccommodationRepository accommodationRepository, MeterRegistry meterRegistry) {
        this.accommodationRepository = accommodationRepository;
        this.meterRegistry = meterRegistry;
    }

    @PostMapping
    public ResponseEntity<Accommodation> create(@RequestBody Accommodation accommodation) {
        return accommodationRepository.create(accommodation)
                .map(createdAccommodation -> {
                    meterRegistry.counter("Accommodation.created").increment();
                    meterRegistry.gauge("Accommodation.count", accommodationRepository.findAll().get().size());

                    return createdAccommodation;
                })
                .map(createdAccommodation -> new ResponseEntity<>(createdAccommodation, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @GetMapping("{id}")
    public ResponseEntity<Accommodation> getById(@PathVariable Long id) {
        return accommodationRepository.getById(id)
                .map(accommodation -> {
                    meterRegistry.counter("Accommodation.getById");

                    return accommodation;
                })
                .map(accommodation -> new ResponseEntity<>(accommodation, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Accommodation>> findAll() {
        return accommodationRepository.findAll()
                .map(accommodations -> {
                    meterRegistry.counter("Accommodation.findAll").increment();

                    return accommodations;
                })
                .map(accommodations -> new ResponseEntity<>(accommodations, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("{id}")
    public ResponseEntity<Accommodation> update(@PathVariable Long id, @RequestBody Accommodation accommodation) {
        return accommodationRepository.update(id, accommodation)
                .map(updatedAccommodation -> {
                    meterRegistry.counter("Accommodation.update").increment();

                    return updatedAccommodation;
                })
                .map(updatedAccommodation -> new ResponseEntity<>(updatedAccommodation, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Accommodation> delete(@PathVariable Long id) {
        accommodationRepository.delete(id);

        meterRegistry.counter("Accommodation.delete").increment();
        meterRegistry.gauge("Accommodation.count", accommodationRepository.findAll().get().size());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}