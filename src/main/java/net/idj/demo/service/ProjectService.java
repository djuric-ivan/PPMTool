package net.idj.demo.service;

import net.idj.demo.domain.Backlog;
import net.idj.demo.domain.Project;
import net.idj.demo.exceptions.ProjectIdException;
import net.idj.demo.repositories.BacklogRepository;
import net.idj.demo.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project){
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

    public Project findProjectByIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project == null) {
            throw new ProjectIdException("Project ID "+projectId+" does not exists");
        }
        return project;
    }

    public Iterable<Project> findAllProjects(){
        return projectRepository.findAll();
    }

    public void deleteByProjectIdentifier(String projectId){
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(project==null){ throw new ProjectIdException("Cannot delete Project with ID '"+projectId+"'. This project does not exist");}
        projectRepository.delete(project);
    }
}