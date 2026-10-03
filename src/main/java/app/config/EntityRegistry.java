package app.config;

import app.entities.*;
import app.entities.WorkRequestDetail;
import org.hibernate.cfg.Configuration;

final class EntityRegistry {

    private EntityRegistry() {}

    static void registerEntities(Configuration configuration) {
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Bruger.class);
        configuration.addAnnotatedClass(Admin.class);
        configuration.addAnnotatedClass(Medarbejder.class);
        configuration.addAnnotatedClass(Booking.class);
        configuration.addAnnotatedClass(Service.class);
        configuration.addAnnotatedClass(WorkRequest.class);
        configuration.addAnnotatedClass(WorkRequestDetail.class);
        configuration.addAnnotatedClass(Offer.class);
        configuration.addAnnotatedClass(Project.class);
    }
}