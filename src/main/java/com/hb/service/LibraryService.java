package com.hb.service;

import com.hb.entity.Author;
import com.hb.entity.Book;
import com.hb.repository.AuthorRepository;
import com.hb.repository.BookRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.mapper.orm.session.SearchSession;

import java.util.List;

@Slf4j
@ApplicationScoped
@Transactional
public class LibraryService {
    @Inject
    BookRepository bookRepository;
    @Inject
    AuthorRepository authorRepository;
    @Inject
    SearchSession searchSession;

    void onStart(@Observes StartupEvent ev) throws InterruptedException {
        log.info("Bulk import started...");
        // only reindex if we imported some content
        if (bookRepository.count() > 0) {
            searchSession.scope(Object.class)
                    .massIndexer()
                    .startAndWait();
        }
    }

    public Book searchBook(String pattern) {
        pattern = "%"+pattern+"%";
        return bookRepository.find("title ilike :pattern", Parameters.with("pattern", pattern)).firstResult();
    }

    public List<Book> searchBooksByTitle(String title) {
        return searchSession.search(Book.class)
                .where(f -> f.simpleQueryString()
                        .fields("title")
                        .matching(title))
                .fetchHits(10);
    }

    public void addBook(String title, Long authorId) {
        Author author = authorRepository.findById(authorId);
        if (author == null) {
            return;
        }

        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        bookRepository.persist(book);
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id);
        if (book != null) {
            book.getAuthor().books.remove(book);
            bookRepository.delete(book);
        }
    }

    public void addAuthor(String firstName, String lastName) {
        Author author = Author.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        authorRepository.persist(author);
    }

    public void deleteAuthor(Long id) {
        Author author = authorRepository.findById(id);
        if (author != null) {
            authorRepository.delete(author);
        }
    }
}
