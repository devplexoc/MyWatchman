package com.plexoc.mywatchman.Model;

public class PendingContactModel {
    String Name,Contact;

    public PendingContactModel(String name, String contact) {
        Name = name;
        Contact = contact;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }
}
