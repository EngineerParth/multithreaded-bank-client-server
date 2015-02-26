
package bankserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Parth
 */
public class BankServer {
       
    //public static AccountsData accData=new AccountsData();

    public static Account account1=new Account(1,1200);
    
    //Dispatcher
    public static void main(String[] args) throws IOException {
        //BankServer.accData.deposit(0, 500);
        //BankServer.accData.deposit(1, 200);
        
        ServerSocket ss=new ServerSocket(8080);
        System.out.println("Initialized & waiting for connections...");
        while(true){
            Socket s=ss.accept();
            System.out.println("new connection established...");
            WorkerThread wt=new WorkerThread(s,account1);
            System.out.println("Worker thread created...");
        }
    }
}

class WorkerThread implements Runnable{
    Socket s;
    Account account;
    
    WorkerThread(Socket s){
        this.s=s;
        Thread t=new Thread(this);
        t.start();
    }
    
    WorkerThread(Socket s, Account account){
        this.account=account;
        this.s=s;
        Thread t=new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            DataInputStream din=new DataInputStream(s.getInputStream());
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            
            int reqType=din.readInt();
            int clientId=din.readInt();
            double amount=din.readDouble();
            
            switch(reqType){
                case 1:{
                    //double newBalance=BankServer.accData.deposit(clientId, amount);
                    //dout.writeUTF(amount+" Deposited in "+clientId+", new balance: "+newBalance);
                    
                    double newBalance1=account.deposite(amount);
                    dout.writeUTF(amount+" Deposited in "+account.accNo+", new balance: "+newBalance1);
                    break;
                }
                case 2:{                    
                    //double result=BankServer.accData.withdraw(clientId, amount);
                    
                    double result1=account.withdraw(amount);
                    
                    if(result1<=-1){
                        //dout.writeUTF("Unable to withdraw, not enough balance in."+clientId);
                        
                        dout.writeUTF("Unable to withdraw, not enough balance in."+account.accNo);
                    }
                    else{
                        //dout.writeUTF(amount+" withdrawn from "+clientId+", new balance: "+result);
                        
                        dout.writeUTF(amount+" withdrawn from "+account.accNo+", new balance: "+result1);
                    }
                    break;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(WorkerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

}

class AccountsData{
    public double[] amounts=new double[200];
    
    public synchronized double deposit(int clientId,double amount){
        //synchronized(this){
            if(amounts[clientId]==0.0d){
                amounts[clientId]=amount;
            }
            amounts[clientId]+=amount;
            return amounts[clientId];
        //}
    }
    
    public synchronized double withdraw(int clientId,double amount){
        //synchronized(this){
            if(amounts[clientId]<amount){return -1;}
            else{
                amounts[clientId]-=amount;
                return amounts[clientId];
            }
        //}
    }
}

class Account{
    int accNo;
    double balance;
    
    public Account(int accNo, double balance){
        this.accNo=accNo;
        this.balance=balance;
    }
    
    public synchronized double deposite(double amount){
        balance+=amount;
        return balance;
    }
    
    public synchronized double withdraw(double amount){
        if(balance<amount){
            return -1;
        }
        else{
            balance -= amount;
            return balance;
        }
    }
}

