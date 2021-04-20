package edu.wpi.teamname.Database.socketListeners;

import edu.wpi.teamname.Database.UserRegistration;

import java.util.ArrayList;
import java.util.List;

public class Initiator extends Thread {
    private static final Initiator instance = new Initiator();
    private List<RegistrationListener> registrationListeners = new ArrayList<RegistrationListener>();

    private Initiator() {

    }

    public static synchronized Initiator getInstance() {
        return instance;
    }

    public void addRegistrationListener(RegistrationListener _toAdd) {
        registrationListeners.add(_toAdd);
    }

    public void triggerRegistration(UserRegistration _obj) {
        for (RegistrationListener l : registrationListeners) {
            try {
                l.registrationAdded(_obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}