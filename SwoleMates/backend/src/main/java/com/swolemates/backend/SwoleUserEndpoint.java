package com.swolemates.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "swoleUserApi",
        version = "v1",
        resource = "swoleUser",
        namespace = @ApiNamespace(
                ownerDomain = "backend.swolemates.com",
                ownerName = "backend.swolemates.com",
                packagePath = ""
        )
)
public class SwoleUserEndpoint {

    private static final Logger logger = Logger.getLogger(SwoleUserEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(SwoleUser.class);
    }

    /**
     * Returns the {@link SwoleUser} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code SwoleUser} with the provided ID.
     */
    @ApiMethod(
            name = "get",
            path = "swoleUser/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public SwoleUser get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting SwoleUser with ID: " + id);
        SwoleUser swoleUser = ofy().load().type(SwoleUser.class).id(id).now();
        if (swoleUser == null) {
            throw new NotFoundException("Could not find SwoleUser with ID: " + id);
        }
        return swoleUser;
    }

    /**
     * Inserts a new {@code SwoleUser}.
     */
    @ApiMethod(
            name = "insert",
            path = "swoleUser",
            httpMethod = ApiMethod.HttpMethod.POST)
    public SwoleUser insert(SwoleUser swoleUser) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that swoleUser.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(swoleUser).now();
        logger.info("Created SwoleUser with ID: " + swoleUser.getId());

        return ofy().load().entity(swoleUser).now();
    }

    /**
     * Updates an existing {@code SwoleUser}.
     *
     * @param id        the ID of the entity to be updated
     * @param swoleUser the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code SwoleUser}
     */
    @ApiMethod(
            name = "update",
            path = "swoleUser/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public SwoleUser update(@Named("id") Long id, SwoleUser swoleUser) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(swoleUser).now();
        logger.info("Updated SwoleUser: " + swoleUser);
        return ofy().load().entity(swoleUser).now();
    }

    /**
     * Deletes the specified {@code SwoleUser}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code SwoleUser}
     */
    @ApiMethod(
            name = "remove",
            path = "swoleUser/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(SwoleUser.class).id(id).now();
        logger.info("Deleted SwoleUser with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "list",
            path = "swoleUser",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<SwoleUser> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<SwoleUser> query = ofy().load().type(SwoleUser.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<SwoleUser> queryIterator = query.iterator();
        List<SwoleUser> swoleUserList = new ArrayList<SwoleUser>(limit);
        while (queryIterator.hasNext()) {
            swoleUserList.add(queryIterator.next());
        }
        return CollectionResponse.<SwoleUser>builder().setItems(swoleUserList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(SwoleUser.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find SwoleUser with ID: " + id);
        }
    }
}