import java.util.*;

//NOTE TO TUTORS!  SEARCH FOR: "CHANGED TO" to find all changes from orignal code!
public class Port
{
    private String name = ""; //Order by name
    private Stack<Cargo> local = new Stack<Cargo>(); //Local Cargo CHANGED TO Stack
    private Stack<Cargo> outbound = new Stack<Cargo>(); //Outbound Cargo CHANGED TO Stack
    //CHANGE TO a new Queue to hold Ships in the Port
    private Queue<Ship> ships = new LinkedList<Ship>();
    private Random gen = new Random();
    private int myID; //equals()
    private static int nextID = 0;
     
    //CHANGED TO a new method for allowing a ship to dock with the port
    public void dock(Ship newShip)
    {
        if (ships.contains(newShip))
            return;
        ships.add(newShip);
        Driver.ledger[myID] -= 1;
    }
    
    //CHANGED TO a new method for processing ships inside the port
    public void process()
    {
        //If not empty, work with current ship
        if (ships.peek() != null)
        {
            //Unload what we can
            unload(ships.peek());
            //load what we can
            load(ships.peek());
            
            //Send Ship off to its next destination
            ships.remove().setDistanceToDestination();
        }
            
    }

    /**
     * checks to see if there is any cargo at the port that needs to be shipped off
     *
     * @return true if there is cargo otherwise false
     */
    public boolean isEmpty() {
        return outbound.isEmpty();
    }
    
    public boolean equals(Port other)
     {
          if (getID() == other.getID())
               return true;
          return false;
     }
     
     public int compareTo(Port other)
     {
          return getName().compareTo(other.getName()); 
     }
     
    public Port(String inName)
    {
         setName(inName);
         setID();
    }
    
    public Port()
    {
         setID();
    }
    
    private void setID()
    {
         myID = nextID++;
    }
    
    public int getID()
    {
         return myID;
    }
    
    public String toString()
    {
         String output = "";
         output = "This is " +getName()+ ":\n";
         output = output + "\tPort Number: " + getID() + "\n*****LOCAL CARGO*********\n";
         for (Cargo unit : local)
         {
              output = output + unit;
         }
         output = output + "*****END LOCAL CARGO*********\n\tTotal Tonnage of Local Cargo: " + getLocalTonnage() + "\n*****OUTBOUND CARGO*********\n";
         for (Cargo unit : outbound)
         {
              output = output + unit;
         }
         output = output + "*****END OUTBOUND CARGO*********\n\tTotal Tonnage of Outbound Cargo: " + getOutboundTonnage() + "\n";
         return output;
    }
    
    //CHANGED TO Array since that is supported
    public Cargo[] getLocal()
    {
         return (Cargo[]) local.toArray(); 
    }

    //CHANGED TO array since that is supported
    public Cargo[] getOutbound()
    {
         return (Cargo[]) outbound.toArray(); 
    }

    public String getName()
    {
         return name;
    }

    /*
      This is stars inside
      it does multiple lines
     */

    public void addOutbound(Cargo inCargo)
    {
         if (inCargo.getTonnage() >= 0)
         {
              outbound.push(inCargo); //CHANGED TO push()
         }
    }
    
    public double getLocalTonnage()
    {
         double total = 0.0;
         for (Cargo unit : local)
         {
              total += unit.getTonnage();
         }
         return total;
    }
    
    public double getOutboundTonnage()
    {
         double total = 0.0;
         for (Cargo unit : outbound)
         {
              total += unit.getTonnage();
         }
         return total;
    }

    public void setName(String inName)
    {
         name = inName;
    }
    
    public void load(Ship targetShip)
    {
        //CHANGED TO use empty() instead of size()
        boolean shipFull = false;
         while(!outbound.empty() && !shipFull)
         {
             //CHANGED TO use pop() instead of get() and remove()
             Cargo temp = outbound.pop();
              if (!targetShip.load(temp))
              {
                   outbound.push(temp);
                   shipFull = true;
              }
         }
    }

    public void unload(Ship targetShip)
    {
        //CHANGED TO account for getting a Stack back!
         Stack<Cargo> unloaded = targetShip.unload(getName());
         
         //CHANGED TO use Stack tools instead
         while(!unloaded.empty())
         {
             Cargo temp = unloaded.pop();
              if (getName().equals(temp.getDest()))
              {
                   local.push(temp);
              }
              else
              {
                   outbound.push(temp);
              }
         }
         
    }

    
    public void unloadAll(Ship targetShip)
    {
        //CHANGED TO account for using a Stack and to use Stack tools
         Stack<Cargo> unloaded = targetShip.unloadAll();
         while(!unloaded.empty())
         {
             Cargo temp = unloaded.pop();
              if (getName().equals(temp.getDest()))
              {
                   local.push(temp);
              }
              else
              {
                   outbound.push(temp);
              }
         }
         
    }
    
    
}