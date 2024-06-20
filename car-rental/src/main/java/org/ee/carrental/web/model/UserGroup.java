package org.ee.carrental.web.model;

import jakarta.jws.soap.SOAPBinding;
import jakarta.persistence.*;

@Entity
public class UserGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(nullable = false)
    private String name;

    public UserGroup(){}

    public UserGroup(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}
