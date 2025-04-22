CREATE TABLE skill_tag_mapping (
    mapping_id INT PRIMARY KEY AUTO_INCREMENT,
    skill_id INT NOT NULL,
    tag_id INT NOT NULL,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id),
    FOREIGN KEY (tag_id) REFERENCES skill_tags(tag_id),
    UNIQUE KEY (skill_id, tag_id)
);