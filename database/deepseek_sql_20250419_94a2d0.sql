CREATE TABLE exchanges (
    exchange_id INT PRIMARY KEY AUTO_INCREMENT,
    skill_provider_id INT NOT NULL,
    skill_learner_id INT NOT NULL,
    skill_id INT NOT NULL,
    status ENUM('pending', 'accepted', 'completed', 'cancelled') DEFAULT 'pending',
    start_date DATETIME,
    end_date DATETIME,
    rating DECIMAL(3,1),
    feedback TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (skill_provider_id) REFERENCES users(user_id),
    FOREIGN KEY (skill_learner_id) REFERENCES users(user_id),
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id)
);