package net.idj.demo.service;

import net.idj.demo.domain.Backlog;
import net.idj.demo.domain.Project;
import net.idj.demo.domain.User;
import net.idj.demo.exceptions.ProjectIdException;
import net.idj.demo.exceptions.ProjectNotFoundException;
import net.idj.demo.repositories.BacklogRepository;
import net.idj.demo.repositories.ProjectRepository;
import net.idj.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BacklogRepository backlogRepository;
    @Autowired
    private UserRepository userRepository;

    public Project saveOrUpdateProject(Project project, String username){
        Optional<User> user = userRepository.findByUsername(username);

        project.setUser(user.get());
        project.setProjectLeader(user.get().getUsername());

        String projectIdentifier = project.getProjectIdentifier().toUpperCase();
        try{
            project.setProjectIdentifier(projectIdentifier);
            if(project.getId()==null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }
            if(project.getId()!=null){
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            }
            return projectRepository.save(project);
        }catch (Exception e){
            throw new ProjectIdException("Project ID '"+projectIdentifier+"' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId, String username){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase()).orElseThrow(() ->
                new ProjectIdException("Project ID " + projectId + " does not exists"));
        if(!project.getProjectLeader().equals(username)){
            throw new ProjectNotFoundException("Project not found in your account");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(String username){
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteByProjectIdentifier(String projectId, String username){
        projectRepository.delete(findProjectByIdentifier(projectId, username));
    }
}