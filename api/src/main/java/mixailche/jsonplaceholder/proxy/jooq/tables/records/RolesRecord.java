/*
 * This file is generated by jOOQ.
 */
package mixailche.jsonplaceholder.proxy.jooq.tables.records;


import mixailche.jsonplaceholder.proxy.jooq.enums.AccessLevel;
import mixailche.jsonplaceholder.proxy.jooq.tables.Roles;

import org.jooq.Record1;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class RolesRecord extends UpdatableRecordImpl<RolesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>public.roles.id</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.roles.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.roles.name</code>.
     */
    public void setName(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.roles.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.roles.posts_access_level</code>.
     */
    public void setPostsAccessLevel(AccessLevel value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.roles.posts_access_level</code>.
     */
    public AccessLevel getPostsAccessLevel() {
        return (AccessLevel) get(2);
    }

    /**
     * Setter for <code>public.roles.users_access_level</code>.
     */
    public void setUsersAccessLevel(AccessLevel value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.roles.users_access_level</code>.
     */
    public AccessLevel getUsersAccessLevel() {
        return (AccessLevel) get(3);
    }

    /**
     * Setter for <code>public.roles.albums_access_level</code>.
     */
    public void setAlbumsAccessLevel(AccessLevel value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.roles.albums_access_level</code>.
     */
    public AccessLevel getAlbumsAccessLevel() {
        return (AccessLevel) get(4);
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
     * Create a detached RolesRecord
     */
    public RolesRecord() {
        super(Roles.ROLES);
    }

    /**
     * Create a detached, initialised RolesRecord
     */
    public RolesRecord(Integer id, String name, AccessLevel postsAccessLevel, AccessLevel usersAccessLevel, AccessLevel albumsAccessLevel) {
        super(Roles.ROLES);

        setId(id);
        setName(name);
        setPostsAccessLevel(postsAccessLevel);
        setUsersAccessLevel(usersAccessLevel);
        setAlbumsAccessLevel(albumsAccessLevel);
        resetChangedOnNotNull();
    }
}
