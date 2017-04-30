package Slave;

//http://jamod.sourceforge.net/api/index.html

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.*;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.ModbusCoupler;
import java.net.*;

public class Vjezba04Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         try {
            ModbusSlave modbusSlave2 = new ModbusSlave("modbus502.json");
            ModbusSlave modbusSlave3 = new ModbusSlave("modbus503.json");
            ModbusSlave modbusSlave4 = new ModbusSlave("modbus504.json");
            ModbusSlave modbusSlave5 = new ModbusSlave("modbus505.json");
            ModbusSlave modbusSlave6 = new ModbusSlave("modbus506.json");
            ModbusSlave modbusSlave7 = new ModbusSlave("modbus507.json");
            ModbusSlave modbusSlave8 = new ModbusSlave("modbus508.json");
            modbusSlave2.Start();
            modbusSlave3.Start();
            modbusSlave4.Start();
            modbusSlave5.Start();
            modbusSlave6.Start();
            modbusSlave7.Start();
            modbusSlave8.Start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }         
    }
    
}
