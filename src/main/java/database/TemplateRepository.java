package database;

import model.appointment.Template;
import org.springframework.data.repository.CrudRepository;

import java.time.DayOfWeek;
import java.util.Set;

//nach https://spring.io/guides/gs/accessing-data-jpa/#initial
public interface TemplateRepository extends CrudRepository<Template, Integer> {

    Set<Template> findAllByWeekday(DayOfWeek weekday);

    Template findById(int id);



}
