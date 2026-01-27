package com.directoTelmarkFormacion.plataforma_lms.model;
import jakarta.persistence.*;

@Entity
@Table(name = "lessons")

public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "order_index")
    private Integer orderIndex;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;

    public Lesson(){}

    public Long getId(){
        return id;
    }

    public void setId (Long id){
        this.id = id;
    }
    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getContentType(){
        return contentType;
    }

    public void setContentType(String contentType){
        this.contentType = contentType;
    }

    public String getVideoUrl(){
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl){
        this.videoUrl = videoUrl;
    }

    public Integer getOrderIndex(){
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex){
        this.orderIndex = orderIndex;
    }

    public Module getModule(){
        return module;
    }

    public void setModule(Module module){
        this.module = module;
    }
}
