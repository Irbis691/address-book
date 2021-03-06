package com.epam.addressbook.repository;

import com.epam.addressbook.model.Accommodation;

import java.util.List;
import java.util.Optional;

public interface AccommodationRepository {
    Optional<Accommodation> create(Accommodation accommodation);

    Optional<Accommodation> getById(Long id);

    Optional<List<Accommodation>> findAll();

    Optional<Accommodation> update(Long id, Accommodation accommodation);

    void delete(Long id);
}