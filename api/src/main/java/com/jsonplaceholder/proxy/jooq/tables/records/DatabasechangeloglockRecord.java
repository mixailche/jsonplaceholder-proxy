/*
 * This file is generated by jOOQ.
 */
package com.jsonplaceholder.proxy.jooq.tables.records;


import com.jsonplaceholder.proxy.jooq.tables.Databasechangeloglock;

import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DatabasechangeloglockRecord extends UpdatableRecordImpl<DatabasechangeloglockRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.databasechangeloglock.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.databasechangeloglock.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.databasechangeloglock.locked</code>.
     */
    public void setLocked(Boolean value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.databasechangeloglock.locked</code>.
     */
    public Boolean getLocked() {
        return (Boolean) get(1);
    }

    /**
     * Setter for <code>public.databasechangeloglock.lockgranted</code>.
     */
    public void setLockgranted(LocalDateTime value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.databasechangeloglock.lockgranted</code>.
     */
    public LocalDateTime getLockgranted() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>public.databasechangeloglock.lockedby</code>.
     */
    public void setLockedby(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.databasechangeloglock.lockedby</code>.
     */
    public String getLockedby() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DatabasechangeloglockRecord
     */
    public DatabasechangeloglockRecord() {
        super(Databasechangeloglock.DATABASECHANGELOGLOCK);
    }

    /**
     * Create a detached, initialised DatabasechangeloglockRecord
     */
    public DatabasechangeloglockRecord(Integer id, Boolean locked, LocalDateTime lockgranted, String lockedby) {
        super(Databasechangeloglock.DATABASECHANGELOGLOCK);

        setId(id);
        setLocked(locked);
        setLockgranted(lockgranted);
        setLockedby(lockedby);
        resetChangedOnNotNull();
    }
}
