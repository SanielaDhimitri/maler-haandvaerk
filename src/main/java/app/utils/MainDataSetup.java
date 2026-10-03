//package app.utils;                    //TEST DER opretter kun testdata og gemmer dem i databasen,
//
//import app.dao.*;
//import app.entities.*;
//import app.enums.RequestStatus;
//import app.enums.Rolle;
//import app.enums.ServiceType;
//import app.services.BookingService;
//import app.services.OfferService;
//import jakarta.persistence.EntityManagerFactory;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//public class MainDataSetup {
//
//    public static void setup(EntityManagerFactory emf) {
//
//        PersonDAO personDAO = new PersonDAO(emf);
//        BookingDAO bookingDAO = new BookingDAO(emf);
//        ServiceDAO serviceDAO = new ServiceDAO(emf);
//        WorkRequestDAO workRequestDAO = new WorkRequestDAO(emf);
//        OfferDAO offerDAO = new OfferDAO(emf);
//
//        if (!personDAO.findAll().isEmpty()) {
//            return;
//        }
//
//        OfferService offerService =
//                new OfferService(offerDAO, workRequestDAO);
//
//        BookingService bookingService =
//                new BookingService(bookingDAO);
//
//
//        // PERSONER
//
//        Bruger bruger = new Bruger(
//                "Anna",
//                "anna@mail.dk",
//                "1234",
//                "12345678"
//        );
//
//        Admin admin = new Admin(
//                "Peter",
//                "peter@mail.dk",
//                "1234",
//                "87654321",
//                Rolle.ADMIN
//        );
//
//        Medarbejder medarbejder = new Medarbejder(
//                "Mikkel",
//                "mikkel@mail.dk",
//                "1234",
//                "11223344"
//        );
//
//        personDAO.create(bruger);
//        personDAO.create(admin);
//        personDAO.create(medarbejder);
//
//
//        // SERVICES
//
//        Service maling = new Service(
//                ServiceType.MALING,
//                "Indvendig og udvendig maling"
//        );
//
//        Service elektriker = new Service(
//                ServiceType.ELEKTRIKER,
//                "Elektriske installationer"
//        );
//
//        Service vvs = new Service(
//                ServiceType.VVS,
//                "VVS arbejde"
//        );
//
//        Service toomrer = new Service(
//                ServiceType.TOOMRER,
//                "Tømrerarbejde"
//        );
//
//        Service murer = new Service(
//                ServiceType.MURER,
//                "Murerarbejde"
//        );
//
//        Service reparationer = new Service(
//                ServiceType.REPARATIONER,
//                "Reparationer i boligen"
//        );
//
//        serviceDAO.create(maling);
//        serviceDAO.create(elektriker);
//        serviceDAO.create(vvs);
//        serviceDAO.create(toomrer);
//        serviceDAO.create(murer);
//        serviceDAO.create(reparationer);
//
//
//        // WORK REQUEST MED LOGIN
//
//        WorkRequest requestMedLogin = new WorkRequest(
//                "Anna",
//                "Jensen",
//                bruger.getEmail(),
//                bruger.getTelefon(),
//                "Gentofte",
//                "Maling af lejlighed",
//                bruger
//        );
//
//        requestMedLogin.addService(
//                maling,
//                "Maling af stue",
//                40.0,
//                "m²"
//        );
//
//        workRequestDAO.create(requestMedLogin);
//
//
//        // WORK REQUEST UDEN LOGIN
//
//        WorkRequest request = new WorkRequest(
//                "Lars",
//                "Hansen",
//                "lars@mail.dk",
//                "22334455",
//                "Gentofte",
//                "Renovering af bolig",
//                null
//        );
//
//        request.addService(
//                maling,
//                "Maling af vægge",
//                120.0,
//                "m²"
//        );
//
//        request.addService(
//                elektriker,
//                "Montering af stikkontakter",
//                4.0,
//                "stk"
//        );
//
//        request.addService(
//                vvs,
//                "Udskiftning af armatur",
//                1.0,
//                "stk"
//        );
//
//        workRequestDAO.create(request);
//
//        workRequestDAO.updateStatus(
//                request.getId(),
//                RequestStatus.UNDER_BEHANDLING
//        );
//
//
//        // OFFER
//
//        Offer offer = new Offer(
//                32500.0,
//                "Tilbud på renovering af bolig",
//                LocalDate.now(),
//                LocalDate.of(2026, 9, 30),
//                request
//        );
//
//        offerDAO.create(offer);
//
//        workRequestDAO.updateStatus(
//                request.getId(),
//                RequestStatus.TILBUD_SENDT
//        );
//
//        offerService.godkendTilbud(offer.getId());
//
//
//        // BOOKING MED LOGIN
//
//        Booking bookingMedLogin = new Booking(
//                LocalDate.of(2026, 9, 10),
//                LocalTime.of(10, 30),
//                "Maling af stue",
//                bruger.getNavn(),
//                bruger.getEmail(),
//                bruger.getTelefon(),
//                bruger
//        );
//
//        bookingMedLogin.addService(maling);
//
//        bookingDAO.create(bookingMedLogin);
//
//        bookingService.bekraeftBooking(
//                bookingMedLogin.getId()
//        );
//
//
//        // BOOKING UDEN LOGIN
//
//        Booking bookingUdenLogin = new Booking(
//                LocalDate.of(2026, 9, 12),
//                LocalTime.of(13, 0),
//                "Maling af soveværelse",
//                "Lars Hansen",
//                "lars@mail.dk",
//                "22334455",
//                null
//        );
//
//        bookingUdenLogin.addService(maling);
//        bookingUdenLogin.addService(elektriker);
//        bookingUdenLogin.addService(vvs);
//
//        bookingDAO.create(bookingUdenLogin);
//    }
//}

