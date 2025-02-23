package com.biblioteca.resources;

import java.util.List;

import javax.validation.Valid;

import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.entities.Book;
import com.biblioteca.entities.BooksStatistics;
import com.biblioteca.enums.StatusBook;
import com.biblioteca.mapper.BookMapper;
import com.biblioteca.requests.BookGetRequestBody;
import com.biblioteca.requests.BookPostRequestBody;
import com.biblioteca.requests.BookPutRequestBody;
import com.biblioteca.services.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/books")
@RequiredArgsConstructor
public class BookResources {

	private final BookService serviceBook;

	@GetMapping(value = "/all")
	@Operation(summary = "find all books non paginated")
	public ResponseEntity<List<BookGetRequestBody>> findAllNonPageable() {
		return ResponseEntity.ok(BookMapper.INSTANCE.toListOfBookGetRequetBody(serviceBook.findAllNonPageable()));
	}

	@GetMapping
	@Operation(summary = "find all books paginated", description = "the default size is 20, use the parameter to change the default value")
	public ResponseEntity<Page<BookGetRequestBody>> findAll(@ParameterObject Pageable pageable) {

		Page<Book> book = serviceBook.findAll(pageable);

		PageImpl<BookGetRequestBody> bookGetPage = new PageImpl<>(
				BookMapper.INSTANCE.toListOfBookGetRequetBody(book.toList()), pageable, book.getTotalElements());

		return ResponseEntity.ok(bookGetPage);
	}

	@GetMapping(value = "/{id}")
	@Operation(summary = "find book by id")
	public ResponseEntity<BookGetRequestBody> findById(@PathVariable Long id) {
		return ResponseEntity.ok(
				BookMapper.INSTANCE.toBookGetRequetBody(serviceBook.findByIdOrElseThrowResourceNotFoundException(id)));
	}

	@GetMapping(value = "/find-by-title")
	@Operation(summary = "find book by title")
	public ResponseEntity<Page<BookGetRequestBody>> findByTitle(@RequestParam String title,@ParameterObject Pageable pageable) {
		Page<Book> books = serviceBook.findByTitle(title,pageable);
		
		PageImpl<BookGetRequestBody> bookGetPage = new PageImpl<>(BookMapper.INSTANCE.toListOfBookGetRequetBody(books.toList())
				,pageable
				,books.getTotalElements());
		
		return ResponseEntity.ok(bookGetPage);
	}

	@GetMapping(value = "/find-by-Status")
	@Operation(summary = "find book by status")
	public ResponseEntity<Page<BookGetRequestBody>> findBookByStatus(@RequestParam String statusBook,@ParameterObject Pageable pageable) {
		Page<Book> books = serviceBook.findAllBooksByStatus(StatusBook.valueOf(statusBook),pageable);
		
		PageImpl<BookGetRequestBody> bookGetPage = new PageImpl<>(BookMapper.INSTANCE.toListOfBookGetRequetBody(books.toList()),pageable,books.getTotalElements());
		
		return ResponseEntity.ok(bookGetPage);
	}

	@GetMapping(value = "/find-by-author")
	@Operation(summary = "find book by author")
	public ResponseEntity<Page<BookGetRequestBody>> findByAuthor(@RequestParam String author,@ParameterObject Pageable pageable) {
		Page<Book> books =serviceBook.findByAuthors(author,pageable);
		
		PageImpl<BookGetRequestBody> bookGetPage = new PageImpl<>(BookMapper.INSTANCE.toListOfBookGetRequetBody(books.toList())
				,pageable
				,books.getTotalElements());
		
		return ResponseEntity.ok(bookGetPage);
	}

	@GetMapping(value = "/find-by-genrer")
	@Operation(summary = "find book by genrer")
	public ResponseEntity<Page<BookGetRequestBody>> findByGenrer(@RequestParam String genrer,@ParameterObject Pageable pageable) {
		Page<Book> books = serviceBook.findByGenrer(genrer,pageable);
		
		PageImpl<BookGetRequestBody> bookGetPage = new PageImpl<>(BookMapper.INSTANCE.toListOfBookGetRequetBody(books.toList())
				,pageable
				,books.getTotalElements());
		return ResponseEntity.ok(bookGetPage);
	}

	@PostMapping
	@Operation()
	public ResponseEntity<BookGetRequestBody> save(@RequestBody @Valid BookPostRequestBody bookPostRequestBody) {
		Book book = BookMapper.INSTANCE.toBook(bookPostRequestBody);

		return new ResponseEntity<BookGetRequestBody>(BookMapper.INSTANCE.toBookGetRequetBody(serviceBook.save(book)),
				HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/{id}")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "successful operation"),
			@ApiResponse(responseCode = "400", description = "when publisher does not exist in the dataBase") })
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		serviceBook.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping
	@Operation(description = "for the book to be made, the user Id,the publisher Id and the author Id are required")
	public ResponseEntity<Void> update(@RequestBody @Valid BookPutRequestBody bookPutRequestBody) {
		serviceBook.update(bookPutRequestBody);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping(value = "/get-books-statistics")
	@Operation(summary = "statistics about books")
	public ResponseEntity<BooksStatistics> getBooksStatistics(){
		return ResponseEntity.ok(serviceBook.getBooksStatistics());
	}
}
