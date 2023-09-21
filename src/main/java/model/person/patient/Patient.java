package model.person.patient;

import model.appointment.Appointment;
import model.person.Person;


public class Patient extends Person {
    private String phone_number;
    private Appointment appointment;

    //ctr
    public Patient(String pPhone_number, String pFirst_name, String pSurname){
        this.phone_number =pPhone_number;
        this.setFirst_name(pFirst_name);
        this.setSurname(pSurname);
    }//end ctr

    /*
    Patient is linked to Appointment and contrariwise.
     */
    public Appointment makeAppointment(Appointment pAppointment){
        pAppointment.setPatient(this);
        this.appointment = pAppointment;
        pAppointment.setTaken(true);
        System.out.println("LOG: " + this.toString() + " has an appointment for " + this.appointment.getName());
        return pAppointment;
    }//end makeAppointment()

    @Override
    public String toString() {
        return this.getFirst_name() + " " + this.getSurname() + " (Tel " + phone_number + ")";
    }

    //GETTER, SETTER

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Appointment getAppointment() {
        return appointment;
    }
}
