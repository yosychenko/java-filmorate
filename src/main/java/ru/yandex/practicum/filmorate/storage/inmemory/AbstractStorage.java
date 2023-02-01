package ru.yandex.practicum.filmorate.storage.inmemory;

import java.util.*;

public abstract class AbstractStorage<T> {
    private final Map<Long, T> storage = new HashMap<>();
    protected long idCounter = 0;

    public T createRecord(long id, T newRecord) {
        storage.put(id, newRecord);
        return newRecord;
    }

    public T updateRecord(long id, T newRecord, RuntimeException ex) {
        T existingRecord = storage.get(id);
        if (existingRecord == null) {
            throw ex;
        }
        storage.put(id, newRecord);
        return newRecord;
    }

    public T getRecordById(long id, RuntimeException ex) {
        T record = storage.get(id);
        if (record == null) {
            throw ex;
        }
        return record;
    }

    public Collection<T> getAllRecords() {
        return new ArrayList<>(storage.values());
    }
}
