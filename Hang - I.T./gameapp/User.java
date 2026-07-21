package com.example.mobilecomp;

import java.util.List;
import java.util.Map;

public class User {
    public String fullname;
    public String password;
    public String role;
    public String studentId;
    public Map<String, Map<String, String>> sections; // e.g., { "BSIT1A": { "subject1": ..., ... } }

    public User() {}

    public User(String fullname, String username, String password, String role, Map<String, Map<String, String>> sections) {
        this.fullname = fullname;
        this.studentId = username;
        this.password = password;
        this.role = role;
        this.sections = sections;
    }
}
