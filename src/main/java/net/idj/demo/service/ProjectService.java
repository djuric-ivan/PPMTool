package net.idj.demo.service;

import net.idj.demo.domain.Project;
import net.idj.demo.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public Project saveOrUpdateProject(Project project){
        //Logic
        
       return projectRepository.save(project);
    }
}