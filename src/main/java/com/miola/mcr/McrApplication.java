package com.miola.mcr;

import com.miola.mcr.Dao.*;
import com.miola.mcr.Dao.Order_MenuItemRepository;
import com.miola.mcr.Services.*;
import javafx.application.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@Configuration
@SpringBootApplication
public class McrApplication implements CommandLineRunner {

    private final UserService userService;
    private final ZoneService zoneService;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final ZoneRepository zoneRepository;
    private final DeviceService deviceService;
    private final EnergyMonitorRepository energyMonitorRepository;
    private final DeviceRepository deviceRepository;
    private final SensorRepository sensorRepository;
    private final DBEnergyService dbEnergyService;
    private final DiningTableRepository diningTableRepository;
    private final AlertesService alertesService;
    private final Order_MenuItemRepository order_menuItemRepository;
    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderService orderService;
    @Autowired
    public McrApplication(UserService userService, ZoneService zoneService, RoleService roleService,
                          RoleRepository roleRepository, ZoneRepository zoneRepository, DeviceService deviceService,
                          EnergyMonitorRepository energyMonitorRepository, DeviceRepository deviceRepository,
                          SensorRepository sensorRepository, DBEnergyService dbEnergyService,
                          DiningTableRepository diningTableRepository, AlertesService alertesService, Order_MenuItemRepository order_menuItemRepository, OrderRepository orderRepository, MenuItemRepository menuItemRepository, OrderService orderService) {
        this.userService = userService;
        this.zoneService = zoneService;
        this.roleService = roleService;
        this.roleRepository = roleRepository;
        this.zoneRepository = zoneRepository;
        this.deviceService = deviceService;
        this.energyMonitorRepository = energyMonitorRepository;
        this.deviceRepository = deviceRepository;
        this.sensorRepository = sensorRepository;
        this.dbEnergyService = dbEnergyService;
        this.diningTableRepository = diningTableRepository;
        this.alertesService = alertesService;
        this.order_menuItemRepository = order_menuItemRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderService = orderService;
    }

    public static void main(String[] args) {

        //SpringApplication.run(McrApplication.class, args);
        Application.launch(JavaFxApplication.class, args);



    }

    @Override
    public void run(String... args) throws Exception {
/*        Set<Alerte> L = alertesService.TestAlerts(sensorRepository.findById(1L).orElse(null), 1);
        for(Alerte a : L)
            System.out.println(a.toString());
        System.out.println("==================");
        Set<Alerte> L1 = alertesService.TestAlerts(sensorRepository.findById(1L).orElse(null), 150);
        for(Alerte a : L1)
            System.out.println(a.toString());
        System.out.println("==================");
        Set<Alerte> L2 = alertesService.TestAlerts(sensorRepository.findById(2L).orElse(null), 150);
        for(Alerte a : L2)
            System.out.println(a.toString());
        System.out.println("==================");
        Set<Alerte> L3 = alertesService.TestAlerts(sensorRepository.findById(2L).orElse(null), 9);
        for(Alerte a : L3)
            System.out.println(a.toString());
        System.out.println("==================");*/
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


 /*       List<Zone> zones = zoneRepository.getAllZones();
        for(Zone Z : zones) {
            System.out.println("Zone 1");
            for( String Sensor : zoneService.getZoneSensorsNames(Z))
                System.out.println(Sensor);
            for( String Device : zoneService.getZoneDevicesNames(Z))
                System.out.println(Device);
            for( String Role : zoneService.getZoneRolesNames(Z))
                System.out.println(Role);

        }*/

/*        Zone zone1 = zoneRepository.getById(1L);
        Zone zone2 = zoneRepository.getById(2L);
        for(Sensor S : zone1.getSensors()) {
            System.out.println(S.toString());
            System.out.println(S.getDevice().getName());
        }
        for(Sensor S : zone2.getSensors()) {
            System.out.println(S.toString());
            System.out.println(S.getDevice().getName());
        }*/

        /*List<Device> Ds = deviceService.getAllDevices();

        for (Device D : Ds){
            System.out.println(D.getClass().getSimpleName());
            System.out.println(D.getClass());
        }*/

       /* List<Device> Ds = deviceRepository.findByPower(DevicePower.ON);
        for (Device D : Ds)
            System.out.println(D.getName());

*/
            /*long count = sensorRepository.count("EnergyDB");
        System.out.println(count);*/

        /*double cost = dbEnergyService.getCostYesterday();
        System.out.println(cost);*/

        /*for (Sensor S : sensorRepository.findSensorByTopic("EnergyDB"))
            System.out.println(S.getId());*/

 /*       System.out.println(deviceRepository.findById(1L));
        deviceService.changePower(DevicePower.OFF, 1L);
        System.out.println(deviceRepository.findById(1L));*/


        /*System.out.println(diningTableRepository.findById(1L));
        diningTableRepository.UpdateTableState(1L, "Empty");
        System.out.println(diningTableRepository.findById(1L).toString());*/

        /*Order_MenuItem i =  order_menuItemRepository.findByOrderAndMenuItem(orderRepository.findById(1L).orElse(null),
                menuItemRepository.findById(3L).orElse(null));

        if(i != null)
            System.out.println(i);
        else
            System.out.println("i.toString()");*/

       /* MenuItem M1 = menuItemRepository.findById(1L).orElse(null);
        MenuItem M2 = menuItemRepository.findById(2L).orElse(null);
        Order O = orderRepository.findById(1L).orElse(null);
        ArrayList<MenuItem> M = new ArrayList<>();
        M.add(M1);
        M.add(M1);
        M.add(M2);
        System.out.println(M);
        orderService.saveOrderItems(O,M);*/


    }



}
