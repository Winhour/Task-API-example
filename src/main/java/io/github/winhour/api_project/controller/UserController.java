package io.github.winhour.api_project.controller;

import io.github.winhour.api_project.model.Task;
import io.github.winhour.api_project.model.TaskRepository;
import io.github.winhour.api_project.model.User;
import io.github.winhour.api_project.model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RepositoryRestController
public class UserController {

    /*User functionality*/


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository repository;
    private final TaskRepository task_repository;

    public UserController(UserRepository repository, TaskRepository task_repository) {
        this.repository = repository;
        this.task_repository = task_repository;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/users")
    ResponseEntity<List<User>> readAllUsers(){
        logger.warn("Exposing all the users!");
        return ResponseEntity.ok(repository.findAll());
    }

    /*
    @GetMapping
    ResponseEntity<List<User>> readAllUsers(Pageable page){
        logger.warn("Custom pageable");
        return ResponseEntity.ok(repository.findAll(page).getContent());
    }
     */

    @GetMapping("/users/{user_id}")
    ResponseEntity<User> readUser(@PathVariable int user_id)
    {
        return repository.findById(user_id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @RequestMapping(method = RequestMethod.POST, path = "/users")
    ResponseEntity<?> postUser(@RequestBody @Valid User toPost){

        repository.save(toPost);

        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @PutMapping("users/{user_id}")
    ResponseEntity<?> updateUser(@PathVariable int user_id, @RequestBody @Valid User toUpdate) {

        if (!repository.existsById(user_id)){
            return ResponseEntity.notFound().build();
        }
        repository.findById(user_id)
                .ifPresent(user -> {
                    user.updateFrom((toUpdate));
                    repository.save(user);
                });
        //return ResponseEntity.noContent().build();
        return ResponseEntity.ok("User nr " + user_id + " updated");

    }


    @DeleteMapping("/users/{user_id}")
    @ResponseBody
    ResponseEntity<?> deleteUser(@PathVariable int user_id) throws Exception {
        User user = repository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on :: " + user_id));
        for(Task t : user.getAssigned_tasks()){                                                                             //Necessary to get rid of relational problems while deleting
            t.removeUser(user);
        }
        repository.delete(user);
        //return ResponseEntity.noContent().build();
        return ResponseEntity.ok("User nr " + user_id + " deleted");
    }





}
