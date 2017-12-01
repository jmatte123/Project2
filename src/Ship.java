import java.util.*;

//NOTE TO TUTORS!  SEARCH FOR: "CHANGED TO" to find all changes from orignal code!
public class Ship {

    private String name = "";
    private double mct = 0.0; //Max Cargo Tonnage; using this for compareTo()
    private Stack<Cargo> cct = new Stack<Cargo>(); //CHANGED TO Stack
    private int speed = 0; //Speed of Ship
    private int dist = 0; //Distance between Ship and its current destination
    private Random gen = new Random();
    private int myID; //equals()
    private static int nextID = 0;
    private int currentPortId;
    private int destPortId;

    //CHANGED TO add a TopBox method to show the Cargo currently at the top of the Stack
    public Cargo topBox() {
        if (cct.isEmpty())
            return null;
        else
            return cct.peek();
    }

    public boolean equals(Ship other) {
        if (getID() == other.getID())
            return true;
        return false;
    }

    public int compareTo(Ship other) {
        if (getMax() < other.getMax())
            return -1;
        else if (getMax() > other.getMax())
            return 1;
        else
            return 0;
    }

    public String toString() {
        String output = "";
        output = "This is the good ship " + getName() + ":\n";
        output = output + "\tShip Number: " + getID() + "\n";
        output = output + "\tCurrent Distance to Next Destination: " + getDistance() + "\n";
        output = output + "\tTravelling at a Speed of : " + getSpeed() + "\n";
        output = output + "\tMax Tonnage Able to Carry: " + getMax() + "\n*****CARGO ONBOARD*********\n";
        for (Cargo unit : cct) {
            output = output + unit;
        }
        output = output + "*****END CARGO LIST*********\n\tTotal Tonnage of Cargo: " + getCurrentCargoTonnage() + "\n";
        return output;
    }


    public Ship(String inName, double inMax, int inSpeed, int dest) {
        setName(inName);
        setMax(inMax);
        setSpeed(inSpeed);
        this.currentPortId = dest;
        setID();
    }

    public Ship(String inName, double inMax, int inSpeed) {
        setName(inName);
        setMax(inMax);
        setSpeed(inSpeed);
        int tmp = gen.nextInt(900) + 101;
        setDistance(tmp);
        setID();
    }

    public Ship(String inName, double inMax) {
        setName(inName);
        setMax(inMax);
        int tmp = gen.nextInt(51) + 10;
        setSpeed(tmp);
        tmp = gen.nextInt(900) + 101;
        setDistance(tmp);
        setID();
    }

    public Ship(String inName) {
        setName(inName);
        double temp = (gen.nextDouble() * 700) + 50;
        setMax(temp);
        int tmp = gen.nextInt(51) + 10;
        setSpeed(tmp);
        tmp = gen.nextInt(900) + 101;
        setDistance(tmp);
        setID();
    }

    public Ship() {
        double temp = (gen.nextDouble() * 700) + 50;
        setMax(temp);
        int tmp = gen.nextInt(51) + 10;
        setSpeed(tmp);
        tmp = gen.nextInt(900) + 101;
        setDistance(tmp);
        setID();
    }

    public int getID() {
        return myID;
    }

    private void setID() {
        myID = nextID++;
    }

    public void setSpeed(int inSpeed) {
        if (inSpeed >= 0) {
            speed = inSpeed;
        }
    }

    /**
     * get the destination port id
     * @return id
     */
    public int getDestPortId() {
        return destPortId;
    }

    /**
     * set the destination port of the current ship
     * @param destPortId new id
     */
    public void setDestPortId(int destPortId) {
        this.destPortId = destPortId;
        Driver.ledger[destPortId] += 1;
    }

    /**
     * sets the distance from the current destination to the desired destination
     */
    public void setDistanceToDestination() {
        if (topBox() == null) {
            for (Port port : Driver.myPorts) {
                if (port.getID() == 6)
                    continue;

                if (!port.isEmpty() && Driver.ledger[port.getID()] == 0) {
                    setDistance(Driver.distances[currentPortId][port.getID()]);
                    setDestPortId(port.getID());
                    return;
                }
            }
        } else {
            switch (topBox().getDest()) {
                case "Winnipeg":
                    setDistance(Driver.distances[currentPortId][0]);
                    setDestPortId(0);
                    break;
                case "Dublin":
                    setDistance(Driver.distances[currentPortId][1]);
                    setDestPortId(1);
                    break;
                case "Beverly Hills":
                    setDistance(Driver.distances[currentPortId][2]);
                    setDestPortId(2);
                    break;
                case "London":
                    setDistance(Driver.distances[currentPortId][3]);
                    setDestPortId(3);
                    break;
                case "Barcelona":
                    setDistance(Driver.distances[currentPortId][4]);
                    setDestPortId(4);
                    break;
                case "Norway":
                    setDistance(Driver.distances[currentPortId][5]);
                    setDestPortId(5);
                    break;
            }
        }
    }

    public void setDistance(int inDist) {
        if (inDist >= 0) {
            dist = inDist;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public int getDistance() {
        return dist;
    }

    public Cargo[] getCurrentCargo() //CHANGED TO return an array of cargo since Java's Stack supports that
    {
        return (Cargo[]) cct.toArray(); //CHANGED TO have to remember to cast the Object[] to a Cargo[]
    }

    public double getMax() {
        return mct;
    }

    public String getName() {
        return name;
    }

    /*
      This is stars inside
      it does multiple lines
     */

    public void setMax(double inMax) {
        if (inMax >= 0) {
            mct = inMax;
        }
    }

    public void setName(String inName) {
        name = inName;
    }

    public double getCurrentCargoTonnage() {
        double total = 0.0;
        for (Cargo unit : cct) {
            total += unit.getTonnage();
        }
        return total;
    }

    public boolean load(Cargo inCargo) {
        if (inCargo.getTonnage() < 0)
            return false;
        if (inCargo.getTonnage() + getCurrentCargoTonnage() > getMax())
            return false;

        cct.push(inCargo); //CHANGED TO push()
        return true;

    }

    public Stack<Cargo> unload(String port) //CHANGED TO Return a stack instead
    {
        //Make a second stack of cargo to transfer
        Stack<Cargo> toUnload = new Stack<Cargo>();
        //Make a third stack of cargo that stays on board
        Stack<Cargo> toKeep = new Stack<Cargo>();

        //Go through the cargo and sort it into the 2 new stacks
        while (!cct.empty()) {
            Cargo temp = cct.pop();
            if (temp.getDest().equals(port)) {
                toUnload.push(temp);
            } else {
                toKeep.push(temp);
            }
        }

        //toKeep is now our current cargo
        cct = toKeep;

        //Send the other stack over to the Port
        return toUnload;
    }

    //CHANGED TO return a Stack of all current cargo
    public Stack<Cargo> unloadAll() {
        Stack<Cargo> toUnload = new Stack<Cargo>();
        while (!cct.empty())
            toUnload.push(cct.pop());
        return toUnload;
    }

    public void travel() {
        int traveled = 0;

        if (getDistance() == 0) {
            return;
        } else if (getDistance() > getSpeed()) {
            traveled = getDistance() - getSpeed();
            setDistance(traveled);
            //System.out.println("Ship traveled " + getSpeed() + " units and is now " + getDistance() + " units from destination");
        } else if (getDistance() <= getSpeed()) {
            setDistance(traveled);
            //System.out.println("The Ship: " + getName() + " has arrived at its destination");
        }
    }


}