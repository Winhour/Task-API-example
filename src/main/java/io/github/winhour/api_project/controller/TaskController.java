package io.github.winhour.api_project.controller;

import io.github.winhour.api_project.model.Task;
import io.github.winhour.api_project.model.TaskRepository;
import io.github.winhour.api_project.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RepositoryRestController
public class TaskController {

    /*Task functionality*/

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    private final TaskRepository repository;

    private final UserRepository user_repository;

    public TaskController(TaskRepository repository, UserRepository user_repository) {
        this.repository = repository;
        this.user_repository = user_repository;
    }

    //@GetMapping(params = {"!sort", "!page", "!size"})
    @RequestMapping(method = RequestMethod.GET, path = "/tasks", params = {"!sort", "!page", "!size"})
    ResponseEntity<List<Task>> readAllTasks(){
        logger.warn("Exposing all the tasks!");
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/tasks")
    ResponseEntity<List<Task>> readAllTasks(Pageable page){
        logger.warn("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }

    @GetMapping("/tasks/{id}")
    ResponseEntity<Task> readTask(@PathVariable int id)
    {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("tasks/search/done")
    ResponseEntity<List<Task>>  readDoneTasks(@RequestParam(defaultValue = "done") String state){
        return ResponseEntity.ok(
                repository.findByStatus(state)
        );
    }

    @PostMapping
    ResponseEntity<?> postTask(@RequestBody @Valid Task toPost){

        repository.save(toPost);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateTask(@PathVariable int id, @RequestBody @Valid Task toUpdate) {

        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> {
                    task.updateFrom((toUpdate));
                    repository.save(task);
                });
        //return ResponseEntity.noContent().build();
        return ResponseEntity.ok("Task nr " + id + " updated");

    }

    @Transactional
    @PatchMapping("/tasks/{id}/status/{state}")
    public ResponseEntity<?> toggleTask(@PathVariable int id, @PathVariable String state) {

        if (!repository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(id)
                .ifPresent(task -> task.setStatus(state));
        return ResponseEntity.ok("Task nr " + id + " state changed to " + state);

    }

    @DeleteMapping("/tasks/{id}")
    @ResponseBody
    ResponseEntity<?> deleteUser(@PathVariable int id) throws Exception {
        Task task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found on :: " + id));
        repository.delete(task);
        //return ResponseEntity.noContent().build();
        return ResponseEntity.ok("Task nr " + id + " deleted");
    }

    @Transactional
    @PatchMapping("/tasks/{id}/add/{user_id}")
    ResponseEntity<?> assignUser(@PathVariable int id, @PathVariable int user_id){

        if (!repository.existsById(id) || !user_repository.existsById(user_id)){
            return ResponseEntity.notFound().build();
        }

        repository.findById(id)
                .ifPresent(task -> {
                    task.addUser(user_repository.findById(user_id).get());
                    repository.save(task);
                });

        /*
        user_repository.findById(user_id)
                .ifPresent(user -> {
                    user.addTask(repository.findById(id).get());
                    user_repository.save(user);
                });
         */


        return ResponseEntity.ok("User with user_id " + user_id + " assigned to Task with id " + id);
    }

    @Transactional
    @PatchMapping("/tasks/{id}/remove/{user_id}")
    ResponseEntity<?> removeUser(@PathVariable int id, @PathVariable int user_id){

        if (!repository.existsById(id) || !user_repository.existsById(user_id)){
            return ResponseEntity.notFound().build();
        }

        repository.findById(id)
                .ifPresent(task -> {
                    task.removeUser(user_repository.findById(user_id).get());
                    repository.save(task);
                });

        return ResponseEntity.ok("User with user_id " + user_id + " removed from Task with id " + id);
    }




}
