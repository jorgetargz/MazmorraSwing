package main;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "door")
@XmlAccessorType(XmlAccessType.FIELD)
public class Door {

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "dest")
    private String destination;

    public Door() {
    }

    public Door(String name, String destination) {
        this.name = name;
        this.destination = destination;
    }

    public String getName() {
        return name;
    }

    public String getDestination() {
        return destination;
    }

}
