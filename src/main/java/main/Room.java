package main;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "room")
@XmlAccessorType(XmlAccessType.FIELD)
public class Room {

    @XmlAttribute(name = "id")
    private String id;

    @XmlElement(name = "description")
    private String description;

    @XmlElement(name = "door")
    private List<Door> doors;

    public Room(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public Room() {
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<Door> getDoors() {
        return doors;
    }
}


