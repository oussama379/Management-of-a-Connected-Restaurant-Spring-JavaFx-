package com.miola.mcr;

import com.miola.mcr.Dao.RoleRepository;
import com.miola.mcr.Dao.ZoneRepository;
import com.miola.mcr.Entities.Role;
import com.miola.mcr.Entities.Sensor;
import com.miola.mcr.Entities.Zone;
import com.miola.mcr.Services.RoleService;
import com.miola.mcr.Services.UserService;
import com.miola.mcr.Services.ZoneService;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class McrApplication implements CommandLineRunner {

    private final UserService userService;
    private final ZoneService zoneService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final ZoneRepository zoneRepository;

    @Autowired
    public McrApplication(UserService userService, ZoneService zoneService, RoleService roleService, RoleRepository roleRepository, ZoneRepository zoneRepository) {
        this.userService = userService;
        this.zoneService = zoneService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.zoneRepository = zoneRepository;
    }

    public static void main(String[] args) {

        //SpringApplication.run(McrApplication.class, args);
        Application.launch(JavaFxApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
   /*     User u2 = new User(2L,"oussamaX","oussama","1234");
        userService.editUser(u2);*/
//        User u = new User("ilyas","ilyas3","1234");
//        userService.saveUser(u);
//        User u2 = new User("oussama","oussama","1234");
//        userService.saveUser(u2);

        /*for (int i=0 ; i < 30 ; i++){
            User u = new User("oussama","oussama"+i, "1234" );
            userService.saveUser(u);
        }*/


        //Zone Z = new Zone();
        //Role r = roleRepository.findById(1L).orElse(null);
       /* List<Zone> zones = zoneRepository.getAllZones();
        for(Zone Z : zones) {
            System.out.println(Z.toString());
            for(Role R : Z.getRoles())
                System.out.println(R.toString());
            for(Sensor S : Z.getSensors())
                System.out.println(S.toString());
        }*/

//        roleService.getAllRolesNames();
//        deviceService.getAllDevicesNames();
//        sensorService.getAllSensorsNames();

        /*for (String roleName : roleService.getAllRolesNames())
            System.out.println(roleName);*/


//        List<Zone> zones = zoneRepository.getAllZones();
//        for(Zone Z : zones) {
//            System.out.println("Zone 1");
//            for( String Sensor : zoneService.getZoneSensorsNames(Z))
//                System.out.println(Sensor.toString());
////            for( String Device : zoneService.getZoneDevicesNames(Z))
////                System.out.println(Device.toString());
//            for( String Role : zoneService.getZoneRolesNames(Z))
//                System.out.println(Role.toString());
//
//        }






    }
}
