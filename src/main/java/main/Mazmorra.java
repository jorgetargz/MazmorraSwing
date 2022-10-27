package main;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "dungeon")
@XmlAccessorType(XmlAccessType.FIELD)
public class Mazmorra {

    @XmlElement(name = "room")
    private List<Room> rooms;

    public Mazmorra() {
    }

    public Mazmorra(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return rooms;
    }

}
