package com.sweproject.storyVerse.service;

import com.sweproject.storyVerse.entity.*;
import com.sweproject.storyVerse.enums.GenreType;
import com.sweproject.storyVerse.repository.BranchRepository;
import com.sweproject.storyVerse.repository.GenreRepository;
import com.sweproject.storyVerse.repository.StoryRepository;
import com.sweproject.storyVerse.repository.UserRepository;
import com.sweproject.storyVerse.request.StoryCreateRequest;
import com.sweproject.storyVerse.request.StoryUpdateRequest;
import com.sweproject.storyVerse.response.BranchDTO;
import com.sweproject.storyVerse.response.StoryResponse;
import com.sweproject.storyVerse.response.StoryWithBranchesResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StoryService {

    private StoryRepository storyRepository;
    private UserService userService;
    private BranchRepository branchRepository;

    private UserRepository userRepository;
    private GenreRepository genreRepository;

    public void saveStoryAsDraft(StoryCreateRequest request, String email) {
        try {
            Story story = new Story();
            saveStory(story, request, email);
            story.setDraft(true);
            storyRepository.save(story);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveStoryDirectly(StoryCreateRequest request, String email) {
        try {
            Story story = new Story();
            saveStory(story, request, email);
            story.setDraft(false);
            storyRepository.save(story);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveStory(Story story, StoryCreateRequest request, String email) {
        try {
            // get the author
            User user = userService.getUserByEmail(email);

            // Story configuration without drafting
            story.setAuthor((Author) user);
            story.setText(request.getText());
            story.setTitle(request.getTitle());
            story.setDescription(request.getDescription());

            // must be error handling
            story.setGenres(request.getGenres());

            story.setTotalContributorsNumber(0);
            story.setContributors(new ArrayList<>());
            storyRepository.save(story);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateStory(Long storyId, StoryUpdateRequest request, String email) {
        try {
            // get the author
            User user = userService.getUserByEmail(email);

            Story updatedstory = storyRepository.findStoryByAuthorIdAndId(user.getId(), storyId);

            if (updatedstory.isDraft()) {
                // Story configuration without drafting
                updatedstory.setAuthor((Author) user);
                updatedstory.setText(request.getText());
                updatedstory.setTitle(request.getTitle());
                updatedstory.setDescription(request.getDescription());
                updatedstory.setDraft(request.isDraft());

                // must be error handling
                updatedstory.setGenres(request.getGenres());
                storyRepository.save(updatedstory);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public List<StoryResponse> listDraftedStories(String email) {
        try {

            User user = obtainUserByEmail(email);
            List<Story> stories = storyRepository.findStoryByAuthorIdAndIsDraft(user.getId(), true);

            List<StoryResponse> results = stories.stream().map(story -> new StoryResponse(story)).collect(Collectors.toList());
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private User obtainUserByEmail(String email) {
        try {
            return userService.getUserByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setDraftStoryAsPublished(String email, Long storyId) {
        try {
            User user = userService.getUserByEmail(email);
            Story story = storyRepository.findStoryByAuthorIdAndId(user.getId(), storyId);
            story.setDraft(false);
            storyRepository.save(story);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getStoryDescription(Long storyId) {
        try {
            Story story = storyRepository.findStoryById(storyId);
            return story.getDescription();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Will be rearranged
    public List<StoryResponse> getStoriesStartingWith(String storyPrefix) {
        try {
            List<Story> storyList = storyRepository.findByTitleStartingWith(storyPrefix);
            List<StoryResponse> responseList = storyList.stream().map(p -> new StoryResponse(p)).collect(Collectors.toList());

            return responseList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Will be rearranged
    public List<StoryResponse> getStoriesByGenres(GenreType genreType) {
        try {

            System.out.println("Ahmet");

            Genre genre = genreRepository.findByName(genreType).orElse(null);
            List<Story> storyList = new ArrayList<>();

            System.out.println("Eren");
            System.out.println(genre.getName());

            storyList.addAll(genre.getStories());

            List<StoryResponse> responseList = storyList.stream().map(p -> new StoryResponse(p)).collect(Collectors.toList());
            return responseList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //List<Story> storyList = storyRepository.findByGenreId(genre.getId());
        //return storyList.stream().map(p->new RecommendedStoryResponse(p)).collect(Collectors.toList());
    }

    public StoryWithBranchesResponse getStoryWithBranches(Long storyId) {
        try {
            Story story = storyRepository.findById(storyId).orElseThrow(() -> new RuntimeException("Story not found"));

            List<Branch> branches = branchRepository._findByStoryId(storyId);

            List<BranchDTO> branchDtos = branches.stream().map(branch -> new BranchDTO(
                    branch.getId(),
                    branch.getText(),
                    branch.getRate(),
                    branch.getParentBranch() != null ? branch.getParentBranch().getId() : null,
                    branch.getLikeCounter(),
                    branch.getDislikeCounter()
            )).toList();

            return new StoryWithBranchesResponse(
                    story.getId(),
                    story.getTitle(),
                    story.getDescription(),
                    story.getText(),
                    story.isDraft(),
                    story.getTotalContributorsNumber(),
                    story.getRate(),
                    branchDtos
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
