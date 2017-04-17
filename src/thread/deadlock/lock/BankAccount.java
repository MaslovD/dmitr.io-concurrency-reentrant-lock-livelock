package thread.deadlock.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author dmaslov
 */
public class BankAccount {

    double balance;
    final int id;
    final Lock lock = new ReentrantLock();

    BankAccount(int id, double balance) {
        this.id = id;
        this.balance = balance;
    }

    void withdraw(double amount) {
        // Wait to simulate io like database access ...
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
        }
        balance -= amount;
    }

    void deposit(double amount) {
        // Wait to simulate io like database access ...
        try {
            Thread.sleep(10l);
        } catch (InterruptedException e) {
        }
        balance += amount;
    }

    static void transfer(BankAccount from, BankAccount to, double amount) {
        from.lock.lock();
        from.withdraw(amount);
        to.lock.lock();
        to.deposit(amount);
        to.lock.unlock();
        from.lock.unlock();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final BankAccount fooAccount = new BankAccount(1, 100d);
        final BankAccount barAccount = new BankAccount(2, 100d);

        new Thread() {
            public void run() {
                BankAccount.transfer(fooAccount, barAccount, 10d);
            }
        }.start();

        new Thread() {
            public void run() {
                BankAccount.transfer(barAccount, fooAccount, 10d);
            }
        }.start();
    }

}
