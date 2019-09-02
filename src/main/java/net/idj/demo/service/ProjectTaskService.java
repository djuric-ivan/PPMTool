package net.idj.demo.service;
import net.idj.demo.domain.Backlog;
import net.idj.demo.domain.ProjectTask;
import net.idj.demo.repositories.BacklogRepository;
import net.idj.demo.repositories.ProjectTaskRepository;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private ProjectTaskRepository projectTaskRepository;

    @Autowired
    private BacklogRepository backlogRepository;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){
        //Exceptions project not found

        //PTs to be added to a specific project, project !=null, BL exist
        Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
        //set the Backlog to the Project Task
        projectTask.setBacklog(backlog);
        //we want to our project sequence to be like this IDPRO-1, IDPRO-2
        Integer backlogSequence = backlog.getPISequence();
        //Update BL sequence
        backlogSequence++;
        //add sequence to the projectTask
        projectTask.setProjectSequence(projectIdentifier+"-"+backlogSequence);
        projectTask.setProjectIdentifier(projectIdentifier);
        //Initial priority when priority is null
        if(projectTask.getPriority()==0||projectTask.getPriority()==null){
            projectTask.setPriority(3);
        }
        //Initial status when status is null
        if(projectTask.getStatus()==""||projectTask.getStatus()==null){
            projectTask.setStatus("TO DO");
        }
        return projectTaskRepository.save(projectTask);
    }

}
