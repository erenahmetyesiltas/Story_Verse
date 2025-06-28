package com.sweproject.storyVerse.service;

import com.sweproject.storyVerse.entity.Branch;
import com.sweproject.storyVerse.entity.Reaction;
import com.sweproject.storyVerse.entity.Story;
import com.sweproject.storyVerse.entity.User;
import com.sweproject.storyVerse.enums.StatusType;
import com.sweproject.storyVerse.repository.BranchRepository;
import com.sweproject.storyVerse.repository.ReactionRepository;
import com.sweproject.storyVerse.repository.StoryRepository;
import com.sweproject.storyVerse.repository.UserRepository;
import com.sweproject.storyVerse.request.ReactionAddRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ReactionService {

    private UserRepository userRepository;
    private BranchRepository branchRepository;
    private StoryRepository storyRepository;
    private ReactionRepository reactionRepository;

    public void addAReaction(String email, Long storyId, Long branchId, ReactionAddRequest request){
        try {
            User user = getUser(email);

            Reaction newReaction;

            Branch branch = branchRepository.findById(branchId).orElse(null);
            Story story = storyRepository.findById(storyId).orElse(null);

            // There is no reaction for this user about the content (A story or a branch)
            // reaction adding
            if (!isThereAReaction(user.getId(), storyId, branchId)) {

                newReaction = new Reaction();
                newReaction.setBranch(branch);
                newReaction.setStory(story);
                newReaction.setStatus(request.getStatusType());
                newReaction.setUser(user);

                // story
                if (branchId == 0) {

                    if (request.getStatusType().equals(StatusType.LIKE)) {
                        story.setLikeCounter(story.getLikeCounter() + 1);
                    } else if (request.getStatusType().equals(StatusType.DISLIKE)) {
                        story.setDislikeCounter(story.getDislikeCounter() + 1);
                    }

                    storyRepository.save(story);

                }
                // branch
                else {

                    if (request.getStatusType().equals(StatusType.LIKE)) {
                        branch.setLikeCounter(branch.getLikeCounter() + 1);
                    } else if (request.getStatusType().equals(StatusType.DISLIKE)) {
                        branch.setDislikeCounter(branch.getDislikeCounter() + 1);
                    }

                    branchRepository.save(branch);

                }


                reactionRepository.save(newReaction);
            }

            // reaction updating
            else {

                // branch
                if (branchId != 0) {
                    newReaction = reactionRepository.findByUserIdAndBranchId(user.getId(), branchId);

                    if (newReaction.getStatus().equals(request.getStatusType())) {

                    } else {
                        newReaction.setStatus(request.getStatusType());
                        reactionRepository.save(newReaction);

                        if (request.getStatusType().equals(StatusType.DISLIKE)) {
                            branch.setLikeCounter(branch.getLikeCounter() - 1);
                            branch.setDislikeCounter(branch.getDislikeCounter() + 1);
                            branchRepository.save(branch);
                        } else if (request.getStatusType().equals(StatusType.LIKE)) {
                            branch.setDislikeCounter(branch.getDislikeCounter() - 1);
                            branch.setLikeCounter(branch.getLikeCounter() + 1);
                            branchRepository.save(branch);
                        }

                    }

                }
                //story
                else {
                    newReaction = reactionRepository.findByUserIdAndStoryId(user.getId(), storyId);

                    if (newReaction.getStatus().equals(request.getStatusType())) {

                    } else {
                        newReaction.setStatus(request.getStatusType());
                        reactionRepository.save(newReaction);

                        if (request.getStatusType().equals(StatusType.DISLIKE)) {
                            story.setLikeCounter(story.getLikeCounter() - 1);
                            story.setDislikeCounter(story.getDislikeCounter() + 1);
                            storyRepository.save(story);
                        } else if (request.getStatusType().equals(StatusType.LIKE)) {
                            story.setDislikeCounter(story.getDislikeCounter() - 1);
                            story.setLikeCounter(story.getLikeCounter() + 1);
                            storyRepository.save(story);
                        }

                    }

                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isThereAReaction(Long userId, Long storyId, Long branchId){
        try {
            User user = userRepository.findById(userId).orElse(null);

            List<Reaction> reactionList = reactionRepository.findAll();

            Reaction reaction = null;

            if (branchId == 0) {

                for (int i = 0; i < reactionList.size(); i++) {
                    reaction = reactionRepository.findByUserIdAndStoryId(userId, storyId);
                }

                if (reaction == null) {
                    return false;
                }
                return true;

            } else {

                for (int i = 0; i < reactionList.size(); i++) {
                    reaction = reactionRepository.findByUserIdAndBranchId(userId, branchId);
                }

                if (reaction == null) {
                    return false;
                }
                return true;

            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteReaction(String email, Long reactionId){
        try {
            User user = getUser(email);
            Reaction reaction = reactionRepository.findById(reactionId).orElse(null);

            Story story = reaction.getStory();
            Branch branch = reaction.getBranch();

            // story reaction
            if (reaction.getBranch().equals(null)) {

                if (reaction.getStatus().equals(StatusType.LIKE)) {
                    story.setLikeCounter(story.getLikeCounter() - 1);
                } else if (reaction.getStatus().equals(StatusType.DISLIKE)) {
                    story.setDislikeCounter(story.getDislikeCounter() - 1);
                }

                storyRepository.save(story);

            } else {

                if (reaction.getStatus().equals(StatusType.LIKE)) {
                    branch.setLikeCounter(branch.getLikeCounter() - 1);
                } else if (reaction.getStatus().equals(StatusType.DISLIKE)) {
                    branch.setDislikeCounter(branch.getDislikeCounter() - 1);
                }

                branchRepository.save(branch);

            }

            if (reaction.getUser().getEmail().equals(email)) {
                reactionRepository.deleteById(reactionId);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

}
