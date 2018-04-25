package application.entity;

import java.io.Serializable;

public class EventEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private String description;

    public EventEntity() {}

    public EventEntity(String description) {

        this.description = description ;

    }

    public String getDescription() {

        return this.description;

    }

    public void setDescription(String description) {

        this.description = description;

    }




}
