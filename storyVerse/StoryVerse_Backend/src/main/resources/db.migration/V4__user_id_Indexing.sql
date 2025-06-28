-- User id indexing
CREATE INDEX idx_user_id ON User(id);
CREATE INDEX idx_author_id ON Author(id);
CREATE INDEX idx_contributor_id ON Contributor(id);
CREATE INDEX idx_admin_id ON Admin(id);
CREATE INDEX idx_story_id ON Story(id);