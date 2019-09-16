package net.idj.demo.service;
import net.idj.demo.domain.Backlog;
import net.idj.demo.domain.Project;
import net.idj.demo.domain.ProjectTask;
import net.idj.demo.exceptions.ProjectNotFoundException;
import net.idj.demo.repositories.BacklogRepository;
import net.idj.demo.repositories.ProjectTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectTaskService {

    @Autowired
    private BacklogRepository backlogRepository;

    @Autowired
    private ProjectTaskRepository projectTaskRepository;


    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

        try{

            //PTs to be added to a specific project, project != null, BL exists
            Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            //set the bl to pt
            projectTask.setBacklog(backlog);
            //we want our project sequence to be like this: IDPRO-1  IDPRO-2  ...100 101
            Integer BacklogSequence = backlog.getPTSequence();
            // Update the BL SEQUENCE
            BacklogSequence++;

            backlog.setPTSequence(BacklogSequence);
            //Add Sequence to Project Task
            projectTask.setProjectSequence(projectIdentifier+"-"+BacklogSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            //INITIAL priority when priority null
            if(projectTask.getPriority()==null){ // In the future we need projectTask.getPriority()==null to handle the form
                projectTask.setPriority(3);
            }
            //INITIAL status when status is null
            if(projectTask.getStatus()==""|| projectTask.getStatus()==null){
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);

        }catch (Exception ex){
            throw new ProjectNotFoundException("Project ID "+projectIdentifier+" not founded");
        }
    }

    public Iterable<ProjectTask> findBacklogById(String id) {
        Iterable<ProjectTask> projectTasks=  projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
        if(!projectTasks.iterator().hasNext()){
            throw new ProjectNotFoundException("Project with ID: '"+id+"' does not exist");
        }
        return projectTasks;
    }

    public ProjectTask findPTByProjectSequence (String backlog_id,String pt_id){
        Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);
        if(backlog==null){
            throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
        }
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);
        if(projectTask==null){
            throw  new ProjectNotFoundException("Project Task '"+pt_id+"' not found");
        }

        if(!projectTask.getBacklog().getProjectIdentifier().equals(backlog_id)){
            throw new ProjectNotFoundException("Project task '"+pt_id+"' does not exist in project: '"+backlog_id+"'");
        }

        return projectTask;
    }

    public ProjectTask updateByProjectSequence (ProjectTask updatedTask, String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);
        projectTask = updatedTask;

        return  projectTaskRepository.save(projectTask);

    }

    public void deletePTByProjectSequence(String backlog_id, String pt_id){
        ProjectTask projectTask = findPTByProjectSequence(backlog_id,pt_id);
        projectTaskRepository.delete(projectTask);
    }

}

