package com.sweproject.storyVerse.service;

import com.sweproject.storyVerse.entity.Branch;
import com.sweproject.storyVerse.exception.BranchNotFoundException;
import com.sweproject.storyVerse.repository.BranchRepository;
import com.sweproject.storyVerse.response.BranchDTO;
import com.sweproject.storyVerse.response.BranchWithChildBranchesDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BranchService {

    private BranchRepository branchRepository;

    public BranchWithChildBranchesDTO getBranchWithChildBranches(Long branchId) {
        try {
            Branch branch = branchRepository.findById(branchId)
                    .orElseThrow(() -> new BranchNotFoundException("Branch with ID " + branchId + " not found."));

            List<Branch> childBranches = branchRepository.findByParentBranch_Id(branchId);

            List<BranchDTO> branchDtos = childBranches.stream()
                    .map(childBranch -> new BranchDTO(
                            childBranch.getId(),
                            childBranch.getText(),
                            childBranch.getRate(),
                            childBranch.getParentBranch() != null ? childBranch.getParentBranch().getId() : null,
                            childBranch.getLikeCounter(),
                            childBranch.getDislikeCounter()
                    ))
                    .toList();

            return new BranchWithChildBranchesDTO(
                    branch.getId(),
                    branch.getText(),
                    branch.getRate(),
                    branch.getParentBranch() != null ? branch.getParentBranch().getId() : null,
                    branch.getLikeCounter(),
                    branch.getDislikeCounter(),
                    branchDtos
            );

        } catch (BranchNotFoundException e) {
            throw e; // Let it bubble up, to be caught by global exception handler if any
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve branch data for ID " + branchId, e);
        }
    }
}
