package com.directoTelmarkFormacion.plataforma_lms.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")

public class Course {
@Id
@GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

@Column(nullable = false)
    private String title;

@Column(columnDefinition = "TEXT")
   private String description;
    private boolean published = false;

@Column(name = "created_at")
    private LocalDateTime createdAt;

@OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Module> modules = new ArrayList<>();

@ManyToOne
@JoinColumn (name = "created_by")
private User createdBy;

public Course(){

}

@PrePersist
    protected void onCreate(){
    createdAt = LocalDateTime.now();
}

public long getId(){
    return id;
}

public void setId(Long id){
    this.id = id;
}

public String getTitle(){
    return title;
}

public void setTitle(String title){
    this.title = title;
}

public String getDescription(){
    return description;
}

public void setDescription(String description){
    this.description = description;
}
    public boolean getPublished(){
    return published;
    }

    public void setPublished(boolean published){
    this.published = published;
    }

    public List<Module> getModules(){
    return modules;
    }

    public void setModules(List<Module> modules){
    this.modules = modules;
    }
}
