package com.sweproject.storyVerse.service;

import com.sweproject.storyVerse.entity.*;
import com.sweproject.storyVerse.exception.EmailNotFoundException;
import com.sweproject.storyVerse.repository.*;
import com.sweproject.storyVerse.request.ReviewAddRequest;
import com.sweproject.storyVerse.response.ReviewResponse;
import com.sweproject.storyVerse.response.StoryResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private ReviewRepository reviewRepository;
    private BranchRepository branchRepository;
    private StoryRepository storyRepository;
    private GenreRepository genreRepository;

    public User getUserByEmail(String email){
        try {
            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                throw new EmailNotFoundException("There is no such an email registered in the system.");
            }
            return user;
        } catch (EmailNotFoundException e) {
            throw new EmailNotFoundException("There is no such an email registered in the system.");
        }
    }

    public ReviewResponse postReview(String email, Long branchId, Long storyId, ReviewAddRequest request){
        try {
            if (isUserAlreadyMadeAReviewAboutContent(email, branchId, storyId)) {
                throw new RuntimeException("The user has already made a review about this content.");
            }

            // Create a new review
            Review newReview = new Review();
            User user = userRepository.findByEmail(email).orElse(null);
            Branch branch = branchRepository.findById(branchId).orElse(null);
            Story story = storyRepository.findById(storyId).orElse(null);

            newReview.setUser(user);
            newReview.setText(request.getText());
            newReview.setBranch(branch);
            newReview.setStory(story);
            newReview.setPoint(request.getPoint());
            newReview.setTitle(request.getTitle());

            reviewRepository.save(newReview);

            if (branchId == 0) {
                updateStoryRate(storyId);
            } else {
                updateBranchRate(branchId);
            }

            ReviewResponse response = new ReviewResponse(newReview);
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // OK
    public ReviewResponse updateReview(String email, Long reviewId, ReviewAddRequest request){
        try {
            Review updatedReview = reviewRepository.findById(reviewId).orElse(null);

            if (updatedReview.getUser().getEmail().equals(email)) {
                updatedReview.setText(request.getText());
                updatedReview.setPoint(request.getPoint());
                updatedReview.setCreatedDate(new Date());
                updatedReview.setTitle(request.getTitle());

                reviewRepository.save(updatedReview);

                if (updatedReview.getBranch() == null) {
                    updateStoryRate(updatedReview.getStory().getId());
                } else if (updatedReview.getStory() == null) {
                    updateBranchRate(updatedReview.getBranch().getId());
                }

                ReviewResponse response = new ReviewResponse(updatedReview);
                return response;
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    // OK
    public void deleteReviewById(String email, Long reviewId){

        try {
            Review review = reviewRepository.findById(reviewId).orElse(null);

            Long storyId = review.getStory().getId();

            Long branchId;

            if (review.getBranch() == null) {
                branchId = null;
            } else {
                branchId = review.getBranch().getId();
            }


            if (review.getUser().getEmail().equals(email)) {

                reviewRepository.deleteById(reviewId);

                if (review.getBranch() == null) {
                    updateStoryRate(storyId);
                } else if (review.getStory() == null) {
                    updateBranchRate(branchId);
                }

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ReviewResponse> getAssociatedReviews(Long storyId, Long branchId) {
        try {
            if (branchId == 0) {
                branchId = null;
            }

            List<Review> reviews = reviewRepository.findByStoryIdAndBranchId(storyId, branchId);

            List<ReviewResponse> reviewResponseList = reviews.stream().map(p -> new ReviewResponse(p)).collect(Collectors.toList());

            return reviewResponseList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<StoryResponse> getStoriesAccordingToPreferences(String email){
        try {
//        List<Long> userGenres = getUserGenres(email);
//
//        List<Story> getRecStoryResponseList = new ArrayList<>();
//
//        for (int i = 0; i < userGenres.size(); i++) {
//            getRecStoryResponseList.addAll(storyRepository.findTop10ByGenreIdOrderByCreatedAtDesc(userGenres.get(i)));
//        }
//
//        List<RecommendedStoryResponse> resultList = getRecStoryResponseList.stream().map(p->new RecommendedStoryResponse(p)).collect(Collectors.toList());
//
//        return resultList;
            User user = userRepository.findByEmail(email).orElse(null);
            List<Story> allStoryList = storyRepository.findAll();

            List<Story> resultStoryList = new ArrayList<>();

            for (int i = 0; i < user.getGenres().size(); i++) {
                //storyList.addAll(storyRepository.findTop10ByGenreIdOrderByCreatedAtDesc(user.getGenres().get(i).getId()));

                for (int j = 0; j < allStoryList.size(); j++) {

                    for (int k = 0; k < allStoryList.get(j).getGenres().size(); k++) {

                        if (allStoryList.get(j).getGenres().get(k).getName().equals(user.getGenres().get(i).getName())) {
                            resultStoryList.add(allStoryList.get(j));
                            break;
                        }

                    }

                }

            }

            Set<Story> storySet = new HashSet<>(resultStoryList);
            List<Story> resultArList = new ArrayList<>(storySet);

            // resultStoryList = new ArrayList<>(new LinkedHashSet<>());

            List<StoryResponse> resultList = resultArList.stream().map(p -> new StoryResponse(p)).collect(Collectors.toList());
            return resultList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Long> getUserGenres(String email){
        try {
            Genre genre;
            List<Long> genreList = new ArrayList<>();

            List<Genre> genreList1 = genreRepository.findAll();


            for (int i = 0; i < genreList1.size(); i++) {
                genre = genreRepository.findById(Long.valueOf(i)).orElse(null);

                for (int j = 0; j < genre.getUsers().size(); j++) {
                    if (genre.getUsers().get(j).getEmail().equals(email)) {
                        genreList.add(genre.getId());
                        break;
                    }
                }

            }

            return genreList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void updateStoryRate(Long storyId){
        try {
            List<Review> reviewList = reviewRepository.findByStoryIdAndBranchId(storyId, null);

            double totalPoint = 0;
            double totalReview = 0;

            for (int i = 0; i < reviewList.size(); i++) {
                totalPoint += reviewList.get(i).getPoint();
            }

            totalReview = reviewList.size();

            Story story = storyRepository.findById(storyId).orElse(null);

            if (reviewList.size() == 0) {
                story.setRate(0);
                storyRepository.save(story);
            } else {
                double resultRate = totalPoint / totalReview;

                if (story != null) {
                    story.setRate(resultRate);
                    storyRepository.save(story);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void updateBranchRate(Long branchId){
        try {
            List<Review> reviewList = reviewRepository.findByBranchId(branchId);

            double totalPoint = 0;
            double totalReview = 0;

            for (int i = 0; i < reviewList.size(); i++) {
                totalPoint += reviewList.get(i).getPoint();
            }

            Branch branch = branchRepository.findById(branchId).orElse(null);

            totalReview += reviewList.size();

            double resultRate = totalPoint / totalReview;

            if (reviewList.size() == 0) {
                branch.setRate(0);
                branchRepository.save(branch);
            } else {
                if (branch != null) {
                    branch.setRate(resultRate);
                    branchRepository.save(branch);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isUserAlreadyMadeAReviewAboutContent(String email, Long branchId, Long storyId){
        try {
            User user = userRepository.findByEmail(email).orElse(null);

            Branch branch = branchRepository.findById(branchId).orElse(null);

            Story story = storyRepository.findById(storyId).orElse(null);

            Review review = null;

            if (branch == null & user != null) {
                review = reviewRepository.findByStoryIdAndUserId(storyId, user.getId());
            } else {
                if (user != null) {
                    review = reviewRepository.findByBranchIdAndUserId(branchId, user.getId());
                }
            }

            if (review != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
