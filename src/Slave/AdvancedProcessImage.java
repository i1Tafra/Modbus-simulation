package Slave;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.*;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.ModbusCoupler;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class AdvancedProcessImage extends SimpleProcessImage {         


    @Override
    public Register getRegister(int ref) {        
        return (Register)m_Registers.get(ref); //_Registers.get(ref);
    }
    
    @Override
    public int getRegisterCount() {
        return m_Registers.size(); //_Registers.size();
    }
    
     @Override
    public Register[] getRegisterRange(int ref, int count) {        
        Register[] rez =new Register[count];
           
          for (int i = 0; i < count; i++) {
            rez[i] = (Register)m_Registers.get(i + ref);
        }    
       return rez;
    }    

}
