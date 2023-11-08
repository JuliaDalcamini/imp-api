package com.imp.project;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends MongoRepository<Project, String> {
    @Query("{'creatorId': ?0}")
    List<Project> findProjectsByCreatorId(String creatorId);

    @Query("{'members.id': ?0}")
    List<Project> findProjectsByMemberId(String memberId);
}
