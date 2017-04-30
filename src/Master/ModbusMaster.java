/*
 * https://tips4java.wordpress.com/2009/06/07/table-cell-listener/
 * http://jamod.sourceforge.net/api/index.html
 * http://stackoverflow.com/questions/12823475/what-is-the-best-way-to-listen-for-changes-in-jtable-cell-values-and-update-data
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Master;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import net.wimpi.modbus.ModbusException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ModbusMaster  extends javax.swing.JFrame{
     //VARIABLES 
    private SlavePanel[] _slavesJPanel;
    private Timer timer = new Timer();

    public ModbusMaster(String jsonSlaves) throws UnknownHostException, ParseException {
        super();
        JPanel panel = new JPanel();
        
        // obrada JSON objekta
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader(jsonSlaves));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray slavesArr = (JSONArray) jsonObject.get("slaves");
            _slavesJPanel = new SlavePanel[slavesArr.size()];
            for (int i = 0; i < slavesArr.size(); i++) {
                JSONObject slave = (JSONObject) slavesArr.get(i);
                String ip = (String) slave.get("adress");
                long port = (long) slave.get("port");
                long maxReg = (long) slave.get("maxReg");
                System.out.println(ip + " : " + port + " : " + maxReg);
                _slavesJPanel[i] = new SlavePanel(ip, (int) port, (int) maxReg);
                _slavesJPanel[i].setBounds(10 + 250 * i, 10, 250, 350);
                panel.add(_slavesJPanel[i]);
                
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JScrollPane mainScrollPane = new JScrollPane(panel);
        //mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        super.add(mainScrollPane);
    }

    public void ConnectAll() throws Exception {
        for (int i = 0; i < _slavesJPanel.length; i++) {
            _slavesJPanel[i].getSlave().connect();
        }
    }

    private void Update() throws ModbusException {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < _slavesJPanel.length; i++) {

                    try {
                        _slavesJPanel[i].Update();
                    } catch (ModbusException ex) {
                        Logger.getLogger(ModbusMaster.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        };
        timer.schedule(task, 5000L, 5000L);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(800, 400);
        super.setResizable(true);
        super.setVisible(true);
    }


    /*   M A I N  M E T O D A   */
    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ModbusMaster master = new ModbusMaster("SlavesTCP.json"); // SlavesTCP SlavesTCPtest
                    master.setVisible(true);
                    master.ConnectAll();
                    master.Update();
                } catch (UnknownHostException ex) {
                    Logger.getLogger(ModbusMaster.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ParseException ex) {
                    Logger.getLogger(ModbusMaster.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    Logger.getLogger(ModbusMaster.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
