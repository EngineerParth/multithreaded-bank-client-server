
package bankclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Parth
 */
public class BankClient implements Runnable{

    /**
     * @param args the command line arguments
     */
    int reqType, clientId;
    double amount;
    BankClient(int reqType, int clientId, double amount){
        this.reqType=reqType;
        this.clientId=clientId;
        this.amount=amount;
        Thread t=new Thread(this);
        t.start();
    }
    public static void main(String[] args) throws InterruptedException {
        
        //Simulating 5 concurrent requests to same account
        new BankClient(1, 1, 200);
        new BankClient(2,1,400);
        new BankClient(1,1,100);
        new BankClient(2,1,700);
        new BankClient(1,1,100);
    }

    @Override
    public void run() {
        try {
            Socket s=new Socket(InetAddress.getLocalHost(),8080);
            DataOutputStream dos=new DataOutputStream(s.getOutputStream());
            DataInputStream dis=new DataInputStream(s.getInputStream());
            
            dos.writeInt(this.reqType);
            dos.writeInt(this.clientId);
            dos.writeDouble(this.amount);
            
            String serverMessage=dis.readUTF();
            System.out.println(serverMessage);
            
        } catch (IOException ex) {
            Logger.getLogger(BankClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
