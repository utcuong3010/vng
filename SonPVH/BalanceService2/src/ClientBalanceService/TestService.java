/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientBalanceService;

import Common.Balance;
import Common.BalanceDTO;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author root
 */
public class TestService {
    /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */

    private static final int CREATE = 1;
    private static final int DELETE = 2;
    private static final int GET = 3;
    private static final int ADD = 4;
    private static final int DEDUCT = 5;
    private static final int CREATEWATCH = 6;
    public final Monitor readStats = new Monitor();

    public void createBalanceNode(final int mode) throws IOException {
        int runs = 100;
        for (int i = 0; i < runs; i++) {
            try {
                Runnable task = new Runnable() {
                    IBalanceService balanceService = new BalanceService();
                    public int startIndex;

                    public Runnable setStart(int startIndex) throws IOException, InterruptedException {
                        this.startIndex = startIndex;
                        return this;
                    }

                    @Override
                    public void run() {
                        long start = System.nanoTime();
                        int countter = 50000;
                        for (int j = 0; j < countter; j++) {
                            long startTime1 = System.nanoTime();
                            long nodeId = startIndex * 100000 + (j + 1);


                            BalanceDTO dto = new BalanceDTO((int) nodeId, j, 0, j, null, j, j, j, null, null);
                            switch (mode) {
                                case CREATE:
                                    balanceService.createBalance(dto);
                                    break;
                                case DELETE:
                                    balanceService.deleteBalance(dto);
                                    break;
                                case GET:
                                    balanceService.getBalance(dto);
                                    break;
                                case ADD:
                                    balanceService.addMoney(dto);
                                    break;
                                case DEDUCT:
                                    balanceService.deductMoney(dto);
                                    break;
                                default:
                                    break;
                            }
                            readStats.addMicro((System.nanoTime() - startTime1) / 1000);
                        }
                        long endTime = System.nanoTime();

                        System.out.println(
                                "Total ms " + startIndex + " " + (endTime - start) / 1000000d);
                        double avarage = ((endTime - start) / 1000000d) / (double) countter;

                        System.out.println(
                                "Evarage ms " + startIndex + " " + avarage);
                        System.out.println(
                                "total reg for 1s " + startIndex + " " + (1000d / avarage));
                    }
                }.setStart(i);
                new Thread(task).start();
            } catch (IOException ex) {
                Logger.getLogger(Balance.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Balance.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
