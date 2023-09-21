package model.generator;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

//Singleton
//Supervisor keeps account of updates of the appointment schedule.
public class Supervisor {
    private static final Supervisor SUPERVISOR = new Supervisor();
    //LastUpdate ist IMMER 00:00:00h eines Wochentages. Damit einhergehend werden alle Appointments dieses Wochentages erzeugt.
    //'due template': template with date and time that is to be used for the creation of an appointment.
    //-Function of @param lastUpdate: all templates with date and time before AND same date and as this transformend instant are 'due templates'
    //and have been used to trigger one appointment each.
    // [date of appointment: template-date + (Clerk.weeks * 7 days)]
    //-@param lastUpdate should be today 00:00:00h.
    //-With today 00:00:00h all templates of todays weekday get the status of 'due templates' and will be triggered into appointments.
    // With appointment-creation @param lastUpdate is shifted to today 00:00:00h.
    private Instant lastUpdate;

    private Supervisor() {
    }

    //GETTER, SETTER
    public static Supervisor getInstance(){
        return SUPERVISOR;
    }

    public Instant getLastUpdate() {
        if (lastUpdate == null){
            ZonedDateTime newZDT = ZonedDateTime.now().
                    with(LocalTime.MIN);
            lastUpdate = newZDT.toInstant();
                    //ZonedDateTime.now().
                    //withSecond(0).withMinute(0).withHour(0).withNano(0).toInstant();
                    //LocalDateTime.now().with(LocalTime.MIN).toInstant(ZoneOffset.of("Europe/Vienna"));
        }
        return lastUpdate;
    }
    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
