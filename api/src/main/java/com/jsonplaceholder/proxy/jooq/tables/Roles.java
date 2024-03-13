/*
 * This file is generated by jOOQ.
 */
package com.jsonplaceholder.proxy.jooq.tables;


import com.jsonplaceholder.proxy.jooq.Keys;
import com.jsonplaceholder.proxy.jooq.Public;
import com.jsonplaceholder.proxy.jooq.enums.AccessLevel;
import com.jsonplaceholder.proxy.jooq.tables.UserRoles.UserRolesPath;
import com.jsonplaceholder.proxy.jooq.tables.records.RolesRecord;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Roles extends TableImpl<RolesRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.roles</code>
     */
    public static final Roles ROLES = new Roles();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RolesRecord> getRecordType() {
        return RolesRecord.class;
    }

    /**
     * The column <code>public.roles.id</code>.
     */
    public final TableField<RolesRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>public.roles.name</code>.
     */
    public final TableField<RolesRecord, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.roles.posts_access_level</code>.
     */
    public final TableField<RolesRecord, AccessLevel> POSTS_ACCESS_LEVEL = createField(DSL.name("posts_access_level"), SQLDataType.VARCHAR.nullable(false).asEnumDataType(AccessLevel.class), this, "");

    /**
     * The column <code>public.roles.users_access_level</code>.
     */
    public final TableField<RolesRecord, AccessLevel> USERS_ACCESS_LEVEL = createField(DSL.name("users_access_level"), SQLDataType.VARCHAR.nullable(false).asEnumDataType(AccessLevel.class), this, "");

    /**
     * The column <code>public.roles.albums_access_level</code>.
     */
    public final TableField<RolesRecord, AccessLevel> ALBUMS_ACCESS_LEVEL = createField(DSL.name("albums_access_level"), SQLDataType.VARCHAR.nullable(false).asEnumDataType(AccessLevel.class), this, "");

    private Roles(Name alias, Table<RolesRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Roles(Name alias, Table<RolesRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>public.roles</code> table reference
     */
    public Roles(String alias) {
        this(DSL.name(alias), ROLES);
    }

    /**
     * Create an aliased <code>public.roles</code> table reference
     */
    public Roles(Name alias) {
        this(alias, ROLES);
    }

    /**
     * Create a <code>public.roles</code> table reference
     */
    public Roles() {
        this(DSL.name("roles"), null);
    }

    public <O extends Record> Roles(Table<O> path, ForeignKey<O, RolesRecord> childPath, InverseForeignKey<O, RolesRecord> parentPath) {
        super(path, childPath, parentPath, ROLES);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class RolesPath extends Roles implements Path<RolesRecord> {
        public <O extends Record> RolesPath(Table<O> path, ForeignKey<O, RolesRecord> childPath, InverseForeignKey<O, RolesRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private RolesPath(Name alias, Table<RolesRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public RolesPath as(String alias) {
            return new RolesPath(DSL.name(alias), this);
        }

        @Override
        public RolesPath as(Name alias) {
            return new RolesPath(alias, this);
        }

        @Override
        public RolesPath as(Table<?> alias) {
            return new RolesPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<RolesRecord, Integer> getIdentity() {
        return (Identity<RolesRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<RolesRecord> getPrimaryKey() {
        return Keys.ROLES_PKEY;
    }

    @Override
    public List<UniqueKey<RolesRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.ROLES_NAME_KEY);
    }

    private transient UserRolesPath _userRoles;

    /**
     * Get the implicit to-many join path to the <code>public.user_roles</code>
     * table
     */
    public UserRolesPath userRoles() {
        if (_userRoles == null)
            _userRoles = new UserRolesPath(this, null, Keys.USER_ROLES__USER_ROLES_ROLE_ID_FKEY.getInverseKey());

        return _userRoles;
    }

    @Override
    public Roles as(String alias) {
        return new Roles(DSL.name(alias), this);
    }

    @Override
    public Roles as(Name alias) {
        return new Roles(alias, this);
    }

    @Override
    public Roles as(Table<?> alias) {
        return new Roles(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Roles rename(String name) {
        return new Roles(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Roles rename(Name name) {
        return new Roles(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Roles rename(Table<?> name) {
        return new Roles(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Roles where(Condition condition) {
        return new Roles(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Roles where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Roles where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Roles where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Roles where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Roles where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Roles where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Roles where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Roles whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Roles whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
