/*
 * This file is generated by jOOQ.
*/
package ru.highloadcup.generated;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import ru.highloadcup.generated.tables.Location;
import ru.highloadcup.generated.tables.User;
import ru.highloadcup.generated.tables.Visit;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = -507281151;

    /**
     * The reference instance of <code></code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>LOCATION</code>.
     */
    public final Location LOCATION = ru.highloadcup.generated.tables.Location.LOCATION;

    /**
     * The table <code>USER</code>.
     */
    public final User USER = ru.highloadcup.generated.tables.User.USER;

    /**
     * The table <code>VISIT</code>.
     */
    public final Visit VISIT = ru.highloadcup.generated.tables.Visit.VISIT;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Location.LOCATION,
            User.USER,
            Visit.VISIT);
    }
}
