package net.idj.demo.web;

import net.idj.demo.domain.Project;
import net.idj.demo.service.MapValidatorErrorService;
import net.idj.demo.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;


@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private MapValidatorErrorService mapValidatorErrorService;

    @PostMapping("")
    public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = mapValidatorErrorService.mapValidatorService(result);
        if(errorMap!=null) return errorMap;
        return new ResponseEntity<Project>(projectService.saveOrUpdateProject(project, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){
        return new ResponseEntity<Project>(projectService.findProjectByIdentifier(projectId, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> getAllProjects(Principal principal){ return projectService.findAllProjects(principal.getName());}

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
       projectService.deleteByProjectIdentifier(projectId, principal.getName());
       return new ResponseEntity<String>("Successfully deleted project by id: "+projectId,HttpStatus.OK);
    }

}
