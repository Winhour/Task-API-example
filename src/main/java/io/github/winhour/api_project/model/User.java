package io.github.winhour.api_project.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    /*User model*/

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;
    private String name;
    private String surname;
    private String email;
    private int phone_number;

    @ManyToMany(mappedBy = "assigned_users")
    @JsonIgnore                                                     //prevents infinite loop in response json
    private Set<Task> assigned_tasks;

    public User() {
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int id) {
        this.user_id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(int phone_number) {
        this.phone_number = phone_number;
    }

    public Set<Task> getAssigned_tasks() {
        return assigned_tasks;
    }

    public void setAssigned_tasks(Set<Task> assigned_tasks) {
        this.assigned_tasks = assigned_tasks;
    }

    public void addTask(Task task){
        this.assigned_tasks.add(task);
    }

    public void removeTask(Task task){
        this.assigned_tasks.remove(task);
    }

    public void updateFrom(final User source){

        this.setName(source.getName());
        this.setSurname(source.getSurname());
        this.setEmail(source.getEmail());
        this.setPhone_number(source.getPhone_number());
        this.setAssigned_tasks(source.getAssigned_tasks());

    }

}
