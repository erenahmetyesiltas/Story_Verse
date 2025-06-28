package com.sweproject.storyVerse.controller;

import com.sweproject.storyVerse.entity.Author;
import com.sweproject.storyVerse.exception.AuthorNotFoundException;
import com.sweproject.storyVerse.request.*;
import com.sweproject.storyVerse.response.ReviewResponse;
import com.sweproject.storyVerse.security.auth.RegisterRequest;
import com.sweproject.storyVerse.service.AuthorService;
import com.sweproject.storyVerse.service.ReactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.sweproject.storyVerse.response.StoryResponse;
import com.sweproject.storyVerse.service.StoryService;
import com.sweproject.storyVerse.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

    @RestController
    @RequestMapping("/api/v1/writers")
    @AllArgsConstructor
    public class AuthorController {

    private AuthorService authorService;
    private UserService userService;
    private StoryService storyService;
    private ReactionService reactionService;

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.listAllAuthors();
        return ResponseEntity.ok(authors);
    }
    @PostMapping
    public ResponseEntity<String> postAnAuthor(@RequestBody RegisterRequest request) {
        authorService.addAnAuthor(request);
        return new ResponseEntity<>("Author created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable Long id) {
        Optional<Author> author = authorService.getAuthorById(id);
        return author.map(ResponseEntity::ok)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateAnAuthor(@PathVariable Long id, @RequestBody AuthorCreateRequest request) {
        if (authorService.getAuthorById(id).isEmpty()) {
            throw new AuthorNotFoundException(id);
        }
        authorService.updateAuthor(id, request);
        return ResponseEntity.ok("Author updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAnAuthor(@PathVariable Long id) {
        if (authorService.getAuthorById(id).isEmpty()) {
            throw new AuthorNotFoundException(id);
        }
        authorService.deleteAuthor(id);
        return ResponseEntity.ok("Author deleted successfully");
    }

    @GetMapping("/endpoint")
    public ResponseEntity<String> getEnd() {
        return ResponseEntity.ok("Author Endpoint");
    }

    // OK
    @GetMapping("/personal/{email}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AUTHOR') and #email == authentication.principal.email)")
    public ResponseEntity<?> getAdminPage(@PathVariable String email) {
        return ResponseEntity.ok("Author page: " + email);
    }

    // Author use case scenario
    // OK
    @PostMapping("/{email}/storyDraft")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AUTHOR') and #email == authentication.principal.email)")
    public ResponseEntity<String> addStoryAsDraft(@RequestBody StoryCreateRequest request, @PathVariable String email){
        storyService.saveStoryAsDraft(request, email);
        return new ResponseEntity<>("The story is saved as draft.",HttpStatus.OK);
    }

    // OK
    @PostMapping("/{email}/storyDirectPublish")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AUTHOR') and #email == authentication.principal.email)")
    public ResponseEntity<String> addStoryDirectly(@RequestBody StoryCreateRequest request, @PathVariable String email){
        storyService.saveStoryDirectly(request, email);
        return new ResponseEntity<>("The story is published.",HttpStatus.OK);
    }

    // If the story is draft, it can be changed. OK
    @PutMapping("/{email}/updateStory/{storyId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AUTHOR') and #email == authentication.principal.email)")
    public ResponseEntity<String> updateStoryThings(@RequestBody StoryUpdateRequest request, @PathVariable String email, @PathVariable Long storyId){
        storyService.updateStory(storyId, request, email);
        return new ResponseEntity<>("The story is updated successfully.",HttpStatus.OK);
    }

    // OK
    @GetMapping("/{email}/getDraftStories")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AUTHOR') and #email == authentication.principal.email)")
    public ResponseEntity<List<StoryResponse>> getDraftedStories(@PathVariable String email){
        return new ResponseEntity<List<StoryResponse>>(storyService.listDraftedStories(email),HttpStatus.OK);
    }

    // OK
    @PostMapping("/{email}/getDraftStories/{storyId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AUTHOR') and #email == authentication.principal.email)")
    public ResponseEntity<String> publishDraftStory(@PathVariable String email, @PathVariable Long storyId){
        storyService.setDraftStoryAsPublished(email, storyId);
        return new ResponseEntity<String>("The story is published successfully.",HttpStatus.OK);
    }

    // Reviews
    // OK // if post a review to a story then give 0 to {branchId}
    @GetMapping("/stories/{storyId}/branches/{branchId}/reviews")
    public List<ReviewResponse> getAllReviewsAboutTheBranchOrTheStory(@PathVariable Long storyId, @PathVariable Long branchId){
        return userService.getAssociatedReviews(storyId, branchId);
    }


    // OK
    @PostMapping("/{email}/stories/{storyId}/branches/{branchId}/reviews")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AUTHOR') and #email == authentication.principal.email)")
    public ReviewResponse postReview(@PathVariable String email,@PathVariable Long branchId,@PathVariable Long storyId,@RequestBody ReviewAddRequest request){
        return userService.postReview(email,branchId,storyId,request);
    }

    // OK
    @PutMapping("/{email}/reviews/{reviewId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AUTHOR') and #email == authentication.principal.email)")
    public ReviewResponse updateReview(@PathVariable String email,@PathVariable Long reviewId, @RequestBody ReviewAddRequest request){
        return userService.updateReview(email, reviewId, request);
    }

    // OK
    @DeleteMapping("/{email}/reviews/{reviewId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('AUTHOR') and #email == authentication.principal.email)")
    public void deleteReview(@PathVariable String email, @PathVariable Long reviewId){
        userService.deleteReviewById(email, reviewId);
    }


    // reaction request
    // this method is both used add a reaction or update a reaction
    @PostMapping("/{email}/stories/{storyId}/branches/{branchId}/reaction")
    public void postAReaction(@PathVariable String email, @PathVariable Long storyId, @PathVariable Long branchId, @RequestBody ReactionAddRequest request){
        reactionService.addAReaction(email,storyId,branchId,request);
    }

    @DeleteMapping("/{email}/reactions/{reactionId}")
    public void deleteReaction(@PathVariable String email, @PathVariable Long reactionId){
        reactionService.deleteReaction(email,reactionId);
    }
}