package com.sweproject.storyVerse.service;

import com.sweproject.storyVerse.entity.Author;
import com.sweproject.storyVerse.exception.AuthorCreationException;
import com.sweproject.storyVerse.exception.AuthorDeletionException;
import com.sweproject.storyVerse.exception.AuthorNotFoundException;
import com.sweproject.storyVerse.exception.AuthorUpdateException;
import com.sweproject.storyVerse.repository.AuthorRepository;
import com.sweproject.storyVerse.request.AuthorCreateRequest;
import com.sweproject.storyVerse.security.auth.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorService {

    private AuthorRepository authorRepository;
    private PasswordEncoder passwordEncoder;

    public List<Author> listAllAuthors() {
        return authorRepository.findAll();
    }

    public void addAnAuthor(RegisterRequest request){
        try {
            Author author = new Author();
            author.setGender(request.getGender());
            author.setEmail(request.getEmail());
            // default 0
            author.setTotalNumberOfStory(0);
            author.setPassword(passwordEncoder.encode(request.getPassword()));
            author.setBirthDate(request.getBirthDate());
            // default 0
            author.setFollowerCount(0);
            author.setFirstName(request.getFirstName());
            author.setLastName(request.getLastName());
            author.setDescription(request.getDescription());
            author.setStories(new ArrayList<>());
            author.setGenres(new ArrayList<>());
            author.setRole(request.getRole());

            authorRepository.save(author);

        } catch (DataIntegrityViolationException e) {
            throw new AuthorCreationException("Email already exists or constraint violation occurred.", e);
        }
        catch (Exception e){
            throw new AuthorCreationException("An unexpected error occurred while creating the author.", e);
        }
    }

    //It may return null
    public Optional<Author> getAuthorById(Long id) {
        return authorRepository.findById(id);
    }

    // If author is found & updated return true, else return false.
    public void updateAuthor(Long id, AuthorCreateRequest request) {
        if (getAuthorById(id).isPresent()) {
            try {
                Author author = getAuthorById(id).get();
                author.setGender(request.getGender());
                author.setEmail(request.getEmail());
                author.setTotalNumberOfStory(request.getTotalNumberOfStory());
                author.setEmail(request.getEmail());
                author.setPassword(request.getPassword());
                author.setBirthDate(request.getBirthDate());
                author.setFollowerCount(request.getFollowerCount());
                author.setFirstName(request.getFirstName());
                author.setLastName(request.getLastName());
                author.setDescription(request.getDescription());
                authorRepository.save(author);
            }
            catch (Exception e){
                throw new AuthorUpdateException("Failed to update author with ID " + id + ".", e);
            }
        }
        else{
            throw new AuthorNotFoundException(id);
        }
    }

    public void deleteAuthor(Long id) {
        try {
            if (!authorRepository.existsById(id)) {
                throw new AuthorNotFoundException(id);
            }
            authorRepository.deleteById(id);
        } catch (Exception e) {
            throw new AuthorDeletionException("Failed to delete author with ID " + id + ".", e);
        }
    }
}
