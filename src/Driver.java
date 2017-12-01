import java.util.*; // Need Random
import java.io.*; //Need File IO Tools

//NOTE TO TUTORS!  SEARCH FOR: "CHANGED TO" to find all changes from orignal code!
public class Driver {

    //create the distances between ports in miles
    //winnipeg to dublin = 5151
    //winnipeg to beverly hills = 1547
    //winnipeg to london = 3903
    //winnipeg to barcelona = 8385
    //winnipeg to norway = 9730
    //dublin to beverly hills = 5138
    //dublin to london = 290
    //dublin to barcelona = 917
    //dublin to norway = 738
    //beverly hills to london = 5477
    //beverly hills to barcelona = 6024
    //beverly hills to norway = 5237
    //london to barcelona = 708
    //london to norway = 697
    //barcelona to norway = 1338
    public static int[][] distances = {
            //winnipeg = 0, dublin = 1, beverly hills = 2, london = 3, barcelona = 4, norway = 5
            {0, 5151, 1547, 3903, 8385, 9730},
            {5151, 0, 5138, 290, 917, 738},
            {1547, 5138, 0, 5477, 6024, 5237},
            {3903, 290, 5477, 0, 708, 697},
            {8385, 917, 6024, 708, 0 , 1338},
            {9730, 738, 5237, 697, 1338, 0},
            {1567, 5185, 117, 5483, 6032, 5292, 0}
    };

    /**
     * the ledger keeping track of where the ships are headed
     */
    public static int[] ledger = {0, 0, 0, 0, 0, 0};

    public static ArrayList<Port> myPorts = new ArrayList<>();
    public static ArrayList<Ship> myShips = new ArrayList<Ship>();


    public static void main(String[] args) throws FileNotFoundException {
        //Create a link to an input file
        File portFile = new File("Ports.txt");
        File shipFile = new File("Ships.txt");

        //Open input file
        Scanner inputPorts = new Scanner(portFile);
        Scanner inputShips = new Scanner(shipFile);

        //Setup rest of reading tools and Array Lists
        String line;
        String[] parts;

        //Okay, now lets get the loop going
        //We can use Scanner's hasNext() method to let us know when we run out of data
        while (inputPorts.hasNext()) {
            //Read line
            line = inputPorts.nextLine();
            //Split line into parts
            parts = line.split("%");
            //Store Data
            myPorts.add(new Port(parts[0]));
        }
        //Close input file, we are done with the input file
        inputPorts.close();

        while (inputShips.hasNext()) {
            //Read line
            line = inputShips.nextLine();
            //Split line into parts
            parts = line.split("%");
            //Store Data
            myShips.add(new Ship(parts[0], Double.parseDouble(parts[1]), Integer.parseInt(parts[2]), 6));
            //portId 6 is the construction port where the ships are built
        }
        //Close input file, we are done with the input file
        inputShips.close();

        //Add some cargo to each port
        Random rng = new Random();
        for (Port eachPort : myPorts) {
            if (eachPort.getID() == 6)
                continue;
            //Generate between 1 and 10 cargo objects per Port
            int objects = rng.nextInt(10);
            for (int x = 0; x <= objects; x++) {
                //Generate tonnage
                double tonnage = rng.nextDouble() * 100; //0 and 100ish

                //Generate destination
                //A Port's outbound cargo can't be for that same Port
                int dest = rng.nextInt((myPorts.size() - 1));
                while (eachPort.getName().equals(myPorts.get(dest).getName())) {
                    dest = rng.nextInt((myPorts.size() - 1));
                }
                String target = myPorts.get(dest).getName();

                //Generate Cargo
                //(will need to use full parameter constructor once you write that method)
                Cargo temp = new Cargo(target, tonnage);


                //Add Cargo to Port's Outbound load.
                //You'll need a method in Port to do this
                eachPort.addOutbound(temp);
            }

        }


        //after all the ships are created and there is cargo at the ports, we are ready to
        //start the simulation by setting the destination of each ship.
        for (Ship ship : myShips) {
            ship.setDistanceToDestination();
        }

        //So, I've got:
        // A List of Ships (without cargo)
        //   and
        // A List of Ports (with cargo)

        //CHANGED TO test my code
        //Let's move cargo!
        boolean notDone = true;
        int day = 0;
        do {

            //Move Ships
            while (notDone) {
                for (Ship s : myShips) {
                    if (s.getDestPortId() == s.getCurrentPortId())
                        continue;
                    s.travel();
                    for (Port p : myPorts) {
                        if (s.getDistance() == 0 && s.getDestPortId() == p.getID())
                            p.dock(s);
                    }
                }

                int count = 0;
                for (int i : ledger)
                    count += i;
                if (count == 0)
                    notDone = false;
            }
            System.out.println("Ships reached their destinations");

            //Proccess Ships in the Port
            for (Port p : myPorts) {
                if (p.getID() == 6)
                    continue;
                p.process();
            }


            //Print out current status
            day++;
            System.out.println("DAY " + day + "\n");
            for (Ship s : myShips)
                System.out.println(s);

            for (Port p : myPorts)
                System.out.println(p);


            System.out.println("--------------------------\n\n\n");

            //Done yet?
            //Check if all ports have NO outbound Cargo
            notDone = false;
            for (Port p : myPorts) {
                if (p.getOutboundTonnage() > 0.0)
                    notDone = true;
            }

            //Check if all Ships are unloaded
            for (Ship s : myShips) {
                if (s.getCurrentCargoTonnage() > 0.0)
                    notDone = true;
            }

        } while (notDone);
        System.out.println("Amount of days: " + day);
        System.out.println("DONE!");

    }


}