package io.github.winhour.api_project.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tasks")
public class Task {

    /*Task model*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int task_id;
    private String title;
    private String description;
    private String status;              //maybe enum?
    private Date deadline;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(
            name = "assigned_tasks",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id"))
    private Set<User> assigned_users;

    public Task() {
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int id) {
        this.task_id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Set<User> getAssigned_users() {
        return assigned_users;
    }

    public void setAssigned_users(Set<User> assigned_users) {
        this.assigned_users = assigned_users;
    }

    public void addUser(User user){
        this.assigned_users.add(user);
    }

    public void removeUser(User user){
        this.assigned_users.remove(user);
    }

    public void updateFrom(final Task source){

        this.setTitle(source.getTitle());
        this.setDescription(source.getDescription());
        this.setStatus(source.getStatus());
        this.setDeadline(source.getDeadline());
        this.setAssigned_users(source.getAssigned_users());

    }



}