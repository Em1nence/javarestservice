CREATE TABLE IF NOT EXISTS Instructor (
                                          id INT NOT NULL AUTO_INCREMENT,
                                          name VARCHAR(255) NOT NULL,
                                          email VARCHAR(255) NOT NULL,
                                          PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Course (
                                      id INT NOT NULL AUTO_INCREMENT,
                                      title VARCHAR(255) NOT NULL,
                                      description TEXT,
                                      instructor_id INT DEFAULT NULL,
                                      PRIMARY KEY (id),
                                      KEY instructor_id (instructor_id),
                                      CONSTRAINT fk_instructor_id FOREIGN KEY (instructor_id) REFERENCES Instructor (id)
);

CREATE TABLE IF NOT EXISTS student (
                                       id INT NOT NULL AUTO_INCREMENT,
                                       name VARCHAR(255) NOT NULL,
                                       email VARCHAR(255) NOT NULL,
                                       PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS studentcourse (
                                             student_id INT NOT NULL,
                                             course_id INT NOT NULL,
                                             PRIMARY KEY (student_id, course_id),
                                             KEY fk_course_id (course_id),
                                             CONSTRAINT fk_course_id FOREIGN KEY (course_id) REFERENCES Course (id) ON DELETE CASCADE,
                                             CONSTRAINT fk_student_id FOREIGN KEY (student_id) REFERENCES student (id)
);