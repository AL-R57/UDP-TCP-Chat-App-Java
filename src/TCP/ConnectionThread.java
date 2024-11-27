package TCP;

public class ConnectionThread extends Thread{
    @Override
    public void run() {
        int counter =0;
        while ( true){
            System. out . println (getName() + ": " + counter++);
            try {
                sleep(100) ;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args){
        ConnectionThread test1 = new ConnectionThread();

        ConnectionThread test2 = new ConnectionThread();
        ConnectionThread test3 = new ConnectionThread();
        ConnectionThread test4 = new ConnectionThread();
        ConnectionThread test5 = new ConnectionThread();


        test1.start();
        test2.start();
        test3.start();
    }
}
