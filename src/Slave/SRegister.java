package Slave;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.net.*;
import net.wimpi.modbus.procimg.*;
import net.wimpi.modbus.ModbusCoupler;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.concurrent.ThreadLocalRandom;

public class SRegister implements Register {

      private byte[] value = new byte[2];
    
    private int min;
    private int max;
    private int[] validValues;
    private boolean special;
    
    public SRegister(int minValue, int maxValue, int [] validValues ){   
        value[0] = (byte)0x00;
        value[1] = (byte)0xff;
        this.min = minValue;
        this.max = maxValue;               
        this.validValues = validValues;
        try {
            if(this.validValues.length == 0)
            this.special = false;
        else
            this.special = true;
        } catch (NullPointerException e) {
            this.special = false;
        }
        
    }
    
    public String simulateValue(ThreadLocalRandom rand){
        
        
        if(this.min == 0 && this.max == 0 && this.special == false){
            value[0] = (byte)0x00;
            value[1] = (byte)0x00;
        }
        
        if(special){
            int r = rand.nextInt(0, validValues.length);
            int random = validValues[r];
            //System.err.println(random + " length: " + validValues.length );
            short s_rand = (short)random;
            
            value[0] = (byte) (0xff & (s_rand >> 8));
            value[1] = (byte) (0xff & s_rand);
        } else {
            int random = rand.nextInt(min, max + 1);
            short s_rand = (short)random;
            
            value[0] = (byte) (0xff & (s_rand >> 8));
            value[1] = (byte) (0xff & s_rand);
        }
        return " value :" + getValue();
    }
   

    @Override
    public void setValue(int i) {
        setValue((short) i);
    }

    @Override
    public void setValue(short s) {
        if (value == null) {
            value = new byte[2];
        }
        value[0] = (byte) (0xff & (s >> 8));
        value[1] = (byte) (0xff & s);
    }

    @Override
    public void setValue(byte[] bytes) {
        if (bytes.length < 2) {
            throw new IllegalArgumentException();
        } else {
            value[0] = bytes[0];
            value[1] = bytes[1];
        }
    }

    @Override
    public int getValue() {
        return ((value[0] & 0xff) << 8 | (value[1] & 0xff));
    }

    @Override
    public int toUnsignedShort() {
        return ((value[0] & 0xff) << 8 | (value[1] & 0xff));
    }

    @Override
    public short toShort() {
        return (short) ((value[0] << 8) | (value[1] & 0xff));
    }

    @Override
    public byte[] toBytes() {
        return value;
    }

    
}
