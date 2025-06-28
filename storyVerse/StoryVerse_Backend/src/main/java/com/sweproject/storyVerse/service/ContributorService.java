package com.sweproject.storyVerse.service;

import com.sweproject.storyVerse.entity.*;
import com.sweproject.storyVerse.enums.GenreType;
import com.sweproject.storyVerse.exception.BranchPostException;
import com.sweproject.storyVerse.exception.ContributorCreationException;
import com.sweproject.storyVerse.exception.GenreNotFoundException;
import com.sweproject.storyVerse.exception.GenrePostException;
import com.sweproject.storyVerse.repository.*;
import com.sweproject.storyVerse.request.BranchAddRequest;
import com.sweproject.storyVerse.request.ContributorCreateRequest;
import com.sweproject.storyVerse.request.GenreListRequest;
import com.sweproject.storyVerse.response.BranchResponse;
import com.sweproject.storyVerse.response.GenreResponse;
import com.sweproject.storyVerse.security.auth.RegisterRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ContributorService {
    private final ContributorRepository contributorRepository;
    private PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    private GenreRepository genreRepository;
    private BranchRepository branchRepository;
    private StoryRepository storyRepository;

    public List<Contributor> findAll() {
        return contributorRepository.findAll();
    }

    public void addAContributor(RegisterRequest request) {
        try {
            Contributor contributor = new Contributor();
            contributor.setFirstName(request.getFirstName());
            contributor.setLastName(request.getLastName());
            contributor.setEmail(request.getEmail());
            contributor.setPassword(passwordEncoder.encode(request.getPassword()));
            contributor.setBirthDate(request.getBirthDate());
            contributor.setDescription(request.getDescription());
            contributor.setGender(request.getGender());
            contributor.setStories(new ArrayList<>());
            contributor.setGenres(new ArrayList<>());
            contributor.setRole(request.getRole());

            contributorRepository.save(contributor);
        } catch (Exception e) {
            throw new ContributorCreationException("Contributor could not be added: " , e);
        }
    }

    // get existed genres
    public List<GenreResponse> getExistedGenres(String email){
        try {
            User user = getUser(email);
            Genre genre;
            List<String> genreNames = new ArrayList<>();

            List<Genre> genreList = genreRepository.findAll();

            // Scan the genres preferred from the contributor before.
            for (int i = 0; i < genreList.size() + 1; i++) {
                genre = genreRepository.findById(Long.valueOf(i)).orElse(null);

                if (genre != null && genre.getUsers() != null) {
                    for (int j = 0; j < genre.getUsers().size(); j++) {
                        if (genre.getUsers().get(j).getEmail().equals(email)) {
                            genreNames.add(String.valueOf(genre.getName()));
                        }
                    }
                }
            }

            List<GenreResponse> genreResponseList = genreNames.stream().map(p -> new GenreResponse(String.valueOf(p))).collect(Collectors.toList());
            return genreResponseList;
        }
        catch (Exception e) {
            throw new GenreNotFoundException("Genre not found for user: " + email);
        }

    };

    //
    public void postGenresToContributor(String email, GenreListRequest request){
        try {
            User user = getUser(email);
            Genre genre;

            for (int i = 0; i < request.getGenreNames().size(); i++) {
                genre = genreRepository.findByName(GenreType.valueOf(request.getGenreNames().get(i))).orElse(null);
                user.getGenres().add(genre);
                genre.getUsers().add(user);
                genreRepository.save(genre);
            }

            userRepository.save(user);
        }
        catch (Exception e) {
            throw new GenrePostException("Genre not found for user: " + email, e);
        }

    }

    // update the genres of the contributor
    public void updateGenresOfContributors(String email, GenreListRequest request){
        try {
            User user = getUser(email);
            user.getGenres().clear();
            userRepository.save(user);

            List<Genre> genreList = genreRepository.findAll();

            for (int i = 0; i < genreList.size(); i++) {

                if (genreList.get(i).getUsers() != null) {

                    for (int j = 0; j < genreList.get(i).getUsers().size(); j++) {

                        if (user == genreList.get(i).getUsers().get(j)) {
                            genreList.get(i).getUsers().remove(user);
                            genreRepository.save(genreList.get(i));
                            break;
                        }

                    }

                }


            }

            postGenresToContributor(email, request);

//        User user = getUser(email);
//        Genre genre;
//
//        List<String> genreNames = new ArrayList<>();
//        List<Genre> genreList = genreRepository.findAll();
//
//        // user in genre but not in updated genre list, then remove
//        for (int i = 0; i < genreList.size(); i++) {
//            boolean isInList = false;
//
//            genre = genreRepository.findById(Long.valueOf(i)).orElse(null);
//
//            if(genre != null && genre.getUsers() != null){
//                for (int j = 0; j < genre.getUsers().size(); j++) {
//                    // genre nin kullanıcısı ise
//                    if(genre.getUsers().get(j).getEmail().equals(email)){
//
//                        for (int k = 0; k < request.getGenreNames().size(); k++) {
//                            if(request.getGenreNames().get(k).equals(String.valueOf(genre.getName()))){
//                                isInList = true;
//                                break;
//                            }
//                        }
//
//                        if(!isInList){
//                            genre.getUsers().remove(user);
//                            genreRepository.save(genre);
//                            userRepository.save(user);
//                        }
//
//                    }
//                }
//
//            }
//
//
//
//        }
//
//        // user not in genre but in updated genre list, add
//        for (int i = 0; i < request.getGenreNames().size(); i++) {
//
//            for (int j = 0; j < genreList.size(); j++) {
//                genre = genreRepository.findById(Long.valueOf(j)).orElse(null);
//                boolean isInGenre = false;
//
//                if(genre != null && genre.getUsers() != null){
//                    for (int k = 0; k < genre.getUsers().size(); k++) {
//
//                        if(email.equals(genre.getUsers().get(k).getEmail())){
//                            isInGenre = true;
//                            break;
//                        }
//
//                    }
//
//                    if(!isInGenre){
//                        genre.getUsers().add(user);
//                        genreRepository.save(genre);
//                        userRepository.save(user);
//                    }
//                }
//
//            }
//
//        }
//
//        List<GenreResponse> genreResponseList = request.getGenreNames().stream().map(p -> new GenreResponse(p)).collect(Collectors.toList());
//        return genreResponseList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BranchResponse postBranchToStory(String email, Long parentBranchId, Long storyId, BranchAddRequest request){
        try {
            // Create a new branch
            Branch newBranch = new Branch();

            Story story = storyRepository.findById(storyId).orElse(null);
            newBranch.setStory(story);

            Contributor contributor = ((Contributor) userRepository.findByEmail(email).orElse(null));
            newBranch.setContributor(contributor);

            newBranch.setText(request.getText());

            if (parentBranchId == 0L) {
                if (newBranch.getParentBranch() != null) {
                    newBranch.getParentBranch().setId(null);
                }
                //newBranch.setParentBranchId(null);
            } else {
                newBranch.getParentBranch().setId(parentBranchId);
                //newBranch.setParentBranchId(parentBranchId);
            }

            newBranch.setTitle(request.getTitle());
            newBranch.setRate(0);


            branchRepository.save(newBranch);
            story.setTotalContributorsNumber(story.getTotalContributorsNumber() + 1);
            storyRepository.save(story);

            // Branch Response
            BranchResponse branchResponse = new BranchResponse(newBranch);
            return branchResponse;
        }
        catch (Exception e) {
            throw new BranchPostException("Failed to post branch for story with id: " + storyId, e);
        }
    }

    public User getUser(String email){
        return userRepository.findByEmail(email).orElse(null);
    }

    public Genre getGenre(GenreType name){
        return genreRepository.findByName(name).orElse(null);
    }
}
