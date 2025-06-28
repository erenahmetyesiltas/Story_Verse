package com.sweproject.storyVerse.controller;

import com.sweproject.storyVerse.entity.Contributor;
import com.sweproject.storyVerse.enums.GenreType;
import com.sweproject.storyVerse.request.*;
import com.sweproject.storyVerse.response.*;
import com.sweproject.storyVerse.security.auth.RegisterRequest;
import com.sweproject.storyVerse.service.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/contributors")
@AllArgsConstructor
public class ContributorController {
    private ContributorService contributorService;
    private UserService userService;
    private StoryService storyService;
    private BranchService branchService;
    private ReactionService reactionService;

    @GetMapping
    public List<Contributor> getAllContributors() {
        return contributorService.findAll();
    }

    @PostMapping
    public void postAContributor(@RequestBody RegisterRequest request){
        contributorService.addAContributor(request);
    }

    @GetMapping("/endpoint")
    public String getEnd(){
        return "Contributor Endpoint";
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public ResponseEntity<?> getAdminPage(@PathVariable String email) {
        return ResponseEntity.ok("Contributor page: " + email);
    }

    // OK
    @GetMapping("/{email}/selectedGenres")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public List<GenreResponse> getSelectedGenres(@PathVariable String email){
        return contributorService.getExistedGenres(email);
    }

    // OK // if send empty list, remove the all selected genres from the contributor
    @PutMapping("/{email}/selectedGenres")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public void updateGenresOfContributors(@PathVariable String email, @RequestBody GenreListRequest request){
        contributorService.updateGenresOfContributors(email, request);
    }

    // OK
    @GetMapping("/{email}/recommendedStories")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public List<StoryResponse> getStoriesAccordingToPreferences(@PathVariable String email){
        return userService.getStoriesAccordingToPreferences(email);
    }

    // OK // if post a review to a story then give 0 to {branchId}
    @GetMapping("/stories/{storyId}/branches/{branchId}/reviews")
    public List<ReviewResponse> getAllReviewsAboutTheBranchOrTheStory(@PathVariable Long storyId, @PathVariable Long branchId){
        return userService.getAssociatedReviews(storyId, branchId);
    }


    // OK
    @PostMapping("/{email}/stories/{storyId}/branches/{branchId}/reviews")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public ReviewResponse postReview(@PathVariable String email,@PathVariable Long branchId,@PathVariable Long storyId,@RequestBody ReviewAddRequest request){
        return userService.postReview(email,branchId,storyId,request);
    }

    // OK
    @PutMapping("/{email}/reviews/{reviewId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public ReviewResponse updateReview(@PathVariable String email,@PathVariable Long reviewId, @RequestBody ReviewAddRequest request){
        return userService.updateReview(email, reviewId, request);
    }

    // OK
    @DeleteMapping("/{email}/reviews/{reviewId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public void deleteReview(@PathVariable String email, @PathVariable Long reviewId){
        userService.deleteReviewById(email, reviewId);
    }

    // OK
    @GetMapping("/stories/{storyPrefix}")
    public List<StoryResponse> getStories(@PathVariable String storyPrefix){
        return storyService.getStoriesStartingWith(storyPrefix);
    }

    // OK
    @GetMapping("/stories/genres/{genreType}")
    public List<StoryResponse> getStoriesByGenres(@PathVariable GenreType genreType){
        return storyService.getStoriesByGenres(genreType);
    }

    // OK // if post a review to a story then give 0 to {branchId}
    @PostMapping("/{email}/stories/{storyId}/branches/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public BranchResponse postBranchToStory(@PathVariable String email, @PathVariable Long storyId, @PathVariable Long branchId, @RequestBody BranchAddRequest request){
        return contributorService.postBranchToStory(email, branchId, storyId, request);
    }

//    @PostMapping("/{email}/selectedGenres")
//    public void postGenresToContributor(@PathVariable String email, @RequestBody GenreListRequest request){
//        contributorService.postGenresToContributor(email,request);
//    }

    @GetMapping("/{email}/stories/readStory/{storyId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public StoryWithBranchesResponse getStoryWithBranches(@PathVariable String email, @PathVariable Long storyId){
        return storyService.getStoryWithBranches(storyId);
    }

    @GetMapping("/{email}/stories/readStory/{storyId}/branches/{branchId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('CONTRIBUTOR') and #email == authentication.principal.email)")
    public BranchWithChildBranchesDTO getBranchWithChildBranches(@PathVariable Long storyId, @PathVariable Long branchId){
        return branchService.getBranchWithChildBranches(branchId);
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
