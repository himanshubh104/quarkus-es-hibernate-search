package com.hb.resource;

import com.hb.entity.Book;
import com.hb.service.LibraryService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.jboss.resteasy.reactive.RestForm;

import java.util.List;

@Path("/library")
@Slf4j
public class HomeResource {

    @Inject
    LibraryService libraryService;

    @GET
    @Path("/search-by-title")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Book> search(@QueryParam("title") String titlePattern) {
        log.info("Request received.");
        return libraryService.searchBooksByTitle(titlePattern);
    }

    @POST
    @Path("/book")
    public void addBook(@QueryParam("title") String title, @QueryParam("authorId") Long authorId) {
        libraryService.addBook(title, authorId);
    }

    @DELETE
    @Path("/book/{id}")
    public void deleteBook(Long id) {
        libraryService.deleteBook(id);
    }

    @POST
    @Path("/author")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void addAuthor(@RestForm String firstName, @RestForm String lastName) {
        libraryService.addAuthor(firstName, lastName);
    }

    @DELETE
    @Path("/author/{id}")
    public void deleteAuthor(Long id) {
        libraryService.deleteAuthor(id);
    }

}
