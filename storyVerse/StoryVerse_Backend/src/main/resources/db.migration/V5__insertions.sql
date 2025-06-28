-- USER

-- Author
INSERT INTO user(id, description, email, first_name, gender, last_name, password, role)
VALUES(100, 'I like to write adventure stories!', 'author@gmail.com', 'Mahmut', 'MALE', 'Yazar', '12345678', 'AUTHOR');

INSERT INTO author(id, follower_count, total_number_of_story)
VALUES(100, 157, 2);

-- STORY of AUTHOR
-- The King of North
INSERT INTO story(id, text, title, total_contributors_number, author_id, description, is_draft, rate, dislike_counter, like_counter)
VALUES(500, 'Long time ago, a man had lived in a northern country. His ambition to rule and hunger to power, made him a ruthless king.
He had started wars with neighbor countries, ride horse across the continent to loot and conquer. His people had followed him for many years, until
a young prince on west had rise and challange the king!
', 'The King of North', 2, 100, 'A historical adventure story about a ruthless northern king!', 0, 100, 0, 576);

-- User and Story Genre
INSERT INTO genre(id, name)
VALUES(600, 'ADVENTURE_FICTION');

INSERT INTO story_genre(story_id, genre_id)
VALUES(500, 600);

INSERT INTO user_genre(user_id, genre_id)
VALUES(100, 600);

-- ------------------------------

-- Contributor
INSERT INTO user(id, description, email, first_name, gender, last_name, password, role)
VALUES(101, 'I am a fiction reader and I like to contribute any kind of stories!', 'contributor@gmail.com', 'Salih', 'MALE', 'Katkıveren', '12345678', 'CONTRIBUTOR');

INSERT INTO contributor(id)
VALUES(101);

INSERT INTO story_contributor(story_id, contributor_id)
VALUES(500, 101);

-- Branch of Contributor
INSERT INTO branch(id, parent_branch_id, text, contributor_id, story_id, rate, title, dislike_counter, like_counter)
VALUES(5000 ,null, 'Young prince was brave and handsome. He became a king after the unexpected death of the last king.

An Unexpected Death
The king of Palindor was not a wise man but his people still loved them because he had not start wars or raise taxes to finance it.
The Palindor Kingdom was on a plain surrounded by mountains left, right and back..
', 101, 500, 90, 'Young Prince', 5, 157);

-- Child Branch
INSERT INTO branch(id, parent_branch_id, text, contributor_id, story_id, rate, title, dislike_counter, like_counter)
VALUES(5001, 5000, ' Child Branch of Young Prince Story!!!!!!!!!!
', 101, 500, 75, 'Child Branch Young Prince', 100, 250);

-- --------------------------------

-- Admin
INSERT INTO user(id, description, email, first_name, gender, last_name, password, role)
VALUES(102, 'I like to rule everything! I am an admin!', 'admin@gmail.com', 'Ahmet', 'MALE', 'Yönetici', '12345678', 'ADMIN');

INSERT INTO admin(id)
VALUES(102);
-- ---------------------------------

-- Review
INSERT INTO review(id, point, text, branch_id, story_id, user_id, title)
VALUES(800, 100, 'Review: Awesome story, wow!', 5000, 500, 101, 'Review: The King of North');

-- Reaction
INSERT INTO reaction(id, status, branch_id, story_id, user_id)
VALUES(900, 'LIKE', 5000, 500, 101);



