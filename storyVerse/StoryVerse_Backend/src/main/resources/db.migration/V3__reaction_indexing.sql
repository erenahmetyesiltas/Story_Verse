-- index in order to check the user how react about the branch and story
CREATE INDEX idx_user_post ON Reaction(user_id, branch_id);
CREATE INDEX idx_user_post ON Reaction(user_id, story_id);

-- index in order to calculate the number of like and dislike number for the branch and story
CREATE INDEX idx_post_reaction_type ON Reaction(branch_id, status);
CREATE INDEX idx_post_reaction_type ON Reaction(story_id, status);