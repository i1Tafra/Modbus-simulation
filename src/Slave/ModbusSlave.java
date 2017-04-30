package Slave;

// https://code.google.com/archive/p/json-simple/downloads

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.*;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.ModbusCoupler;
import java.lang.String;
import java.net.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import  org.json.simple.parser.ParseException;

public class ModbusSlave {
    
     // Variables
    public AdvancedProcessImage api = null;
    private ModbusTCPListener listener = null;    
    private ThreadLocalRandom rand;
    private Timer timer = new Timer();
    private int registerNumber;
    
        public ModbusSlave(String configLocation) throws UnknownHostException, ParseException {
        rand = ThreadLocalRandom.current();
        api = new AdvancedProcessImage();
        ModbusCoupler.getReference().setProcessImage(api);
        ModbusCoupler.getReference().setMaster(false);        
        listener = new ModbusTCPListener(3);
        
        // obrada JSON objekta
        JSONParser parser = new JSONParser();

        try{
            Object obj = parser.parse(new FileReader(configLocation));
            JSONObject jsonObject = (JSONObject) obj;
            JSONObject serverData = (JSONObject) jsonObject.get("serverData");
            
            // SERVER DATA 
            String ipAddres = (String) serverData.get("ip");
            long port =  (long) serverData.get("port");
            long unitId = (long) serverData.get("unitId");
            
            // SETIRANJE SERVER PODATAKA
            listener.setAddress(InetAddress.getByName(ipAddres));
            listener.setPort((int)port);
            ModbusCoupler.getReference().setUnitID((int)unitId);
          
        
            
            //REGISTER DATA
            JSONArray registers = (JSONArray) jsonObject.get("registers");
            int regNumber = 0;
            for (int i = 0; i < registers.size(); i++) {
                JSONObject reg = (JSONObject) registers.get(i);
                int location = (int)((long) reg.get("adress"));
                long min = (long) reg.get("min");
                long max = (long) reg.get("max");
                
                JSONArray values = (JSONArray) reg.get("values");
                int [] validValues = new int[values.size()];                  
                for (int j = 0; j < values.size(); j++) {
                    validValues[j] = (int)((long) values.get(j));
                }
                
                //ADD REGISTER ON ADRESS LOCATION
                while (regNumber < location) {                    
                    api.addRegister(new SRegister(0, 0, null));
                    regNumber++;
                }
                if(regNumber == location){
                api.addRegister(new SRegister((int)min, (int)max, validValues));
                    regNumber++;                    
                }                                       
            }           
            
            
        } catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}                
    }       
        
        public void Start() {
        try {           
                Randomize();        
            listener.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void Randomize() {
         TimerTask task = new TimerTask() {
            @Override
            public void run() {              
                for (int i = 0; i < api.getRegisterCount(); i++) {
                    SRegister sim = (SRegister)api.getRegister(i);                    
                    System.err.println("REG " + i + " " + sim.simulateValue(rand)); 
                }
            }            
         };
        timer.schedule(task, 5000L, 5000L);
    }
         
}
