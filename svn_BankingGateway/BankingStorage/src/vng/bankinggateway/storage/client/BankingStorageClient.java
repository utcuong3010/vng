/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package vng.bankinggateway.storage.client;

import com.vng.zing.common.thriftutil.TClientPoolConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.log4j.Logger;
import org.apache.thrift.TException;
import vng.bankinggateway.thrift.*;
import vng.zingme.util.LogUtil;

/**
 *
 * @author vinhcq@vng.com.vn
 */
public class BankingStorageClient implements TStorage.Iface {

    private ReentrantLock locker = new ReentrantLock();
    private static BankingStorageClient _mainInstance;
    private static Object lockObj = new Object();
    private static GenericObjectPool _bankingClient;
    private final Logger log = Logger.getLogger("appActions");

    private BankingStorageClient(String host, int port) {
        _bankingClient = new GenericObjectPool(new BankingStorageFactory(host, port),
                TClientPoolConfig.ClonePoolConfig(TClientPoolConfig.DefaultPoolConfig));
    }

    public static BankingStorageClient getMainInstance() {
        if (_mainInstance == null) {
            synchronized (lockObj) {
                if (_mainInstance == null) {
                    String adHost = System.getProperty("adminHost", "localhost");
                    int adPort = Integer.parseInt(System.getProperty(
                            "adminPort", "10114"));
                    _mainInstance = new BankingStorageClient(adHost, adPort);
                }
            }
        }
        return _mainInstance;
    }

    public static BankingStorageClient getMainInstance(String host, int port) {
        if (_mainInstance == null) {
            synchronized (lockObj) {
                if (_mainInstance == null) {
                    _mainInstance = new BankingStorageClient(host, port);
                }
            }
        }
        return _mainInstance;
    }

    private TStorage.Client getClient() throws Exception {
        TStorage.Client client = null;
        locker.lock();
        try {
            client = (TStorage.Client) _bankingClient.borrowObject();
        } finally {
            locker.unlock();
        }
        return client;
    }

    public <T, K> T exec(Command<T, TStorage.Client> command, T defaultValue) {
        TStorage.Client client = null;
        try {
            client = this.getClient();
            T o = command.exec(client);
            _bankingClient.returnObject(client);
            return o;
        } catch (Exception e) {
            log.warn(LogUtil.stackTrace(e));
            System.out.println(LogUtil.stackTrace(e));
            try {
                _bankingClient.invalidateObject(client);
                int invalidCount = _bankingClient.getNumActive() - 1;
                for (int i = 0; i < invalidCount; ++i) {
                    client = this.getClient();
                    _bankingClient.invalidateObject(client);
                }
            } catch (Exception ex1) {
                log.info(ex1.getMessage());
            }
        }
        return defaultValue;

    }

    @Override
    public int storeTranx(final T_Transaction tx) throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {

            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                return client.storeTranx(tx);
            }
        };
        return this.exec(command, new Integer(0)).intValue();
    }

    @Override
    public int updateTranxStatus(final T_TranStat tranxStat) throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {

            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                return client.updateTranxStatus(tranxStat);
            }
        };
        return this.exec(command, new Integer(0)).intValue();
    }

    @Override
    public int updateTranxAndStatus(final T_Transaction tx, final short txStatus, final String responseCode) throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {

            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                return client.updateTranxAndStatus(tx, txStatus, responseCode);
            }
        };
        return this.exec(command, new Integer(0)).intValue();
    }

    @Override
    public List<T_TransactionReport> getTranxs(final String day, final short txStatus, final boolean allStatus) throws TException {
        Command<List<T_TransactionReport>, TStorage.Client> command = new Command<List<T_TransactionReport>, TStorage.Client>() {

            @Override
            public List<T_TransactionReport> exec(TStorage.Client client) throws Exception {
                return client.getTranxs(day, txStatus, allStatus);
            }
        };
        return this.exec(command, new ArrayList<T_TransactionReport>());
    }

    @Override
    public T_Transaction getTransaction(final int txID, final String time) throws TException {
        Command<T_Transaction, TStorage.Client> command = new Command<T_Transaction, TStorage.Client>() {

            @Override
            public T_Transaction exec(TStorage.Client client) throws Exception {
                return client.getTransaction(txID, time);
            }
        };
        return this.exec(command, new T_Transaction());
    }

    @Override
    public synchronized int generateTransID(final String day) throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {

            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                return client.generateTransID(day);
            }
        };
        return this.exec(command, new Integer(0)).intValue();
    }

    @Override
    public T_TranStat getTranxStatus(final String refID, final String day) throws TException {
        Command<T_TranStat, TStorage.Client> command = new Command<T_TranStat, TStorage.Client>() {

            @Override
            public T_TranStat exec(TStorage.Client client) throws Exception {
                return client.getTranxStatus(refID, day);
            }
        };
        return this.exec(command, new T_TranStat());
    }

    @Override
    public List<T_TransactionReport> getTranxsWithConfirmStatus(final String day, final short confirmStatus) throws TException {
        Command<List<T_TransactionReport>, TStorage.Client> command = new Command<List<T_TransactionReport>, TStorage.Client>() {

            @Override
            public List<T_TransactionReport> exec(TStorage.Client client) throws Exception {
                return client.getTranxsWithConfirmStatus(day, confirmStatus);
            }
        };
        return this.exec(command, new ArrayList<T_TransactionReport>());
    }

    @Override
    public synchronized int generateQueryID(final String day) throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {

            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                return client.generateQueryID(day);
            }
        };
        return this.exec(command, new Integer(0)).intValue();
    }

    @Override
    public int updateTransaction(final T_Transaction tx) throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {

            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                return client.updateTransaction(tx);
            }
        };
        return this.exec(command, new Integer(0)).intValue();
    }

    @Override
    public int insertTask() throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {
            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                client.insertTask();
                return 0;
            }
        };
        return this.exec(command, new Integer(0)).intValue();
    }

    @Override
    public int updateTaskStart(final T_Task task) throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {
            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                client.updateTaskStart(task);
                return 0;
            }
        };
        return this.exec(command, new Integer(0)).intValue();

    }

    @Override
    public int updateTaskStop(final T_Task task) throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {
            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                client.updateTaskStop(task);
                return 0;
            }
        };
        return this.exec(command, new Integer(0)).intValue();
    }

    @Override
    public List<T_Task> getListTaskToday() throws TException {

        Command<List<T_Task>, TStorage.Client> command = new Command<List<T_Task>, TStorage.Client>() {
            @Override
            public List<T_Task> exec(TStorage.Client client) throws Exception {

                return client.getListTaskToday();
            }
        };
        List<T_Task> listTask = new ArrayList<T_Task>();
        listTask = this.exec(command, new ArrayList<T_Task>());
        return listTask;
    }

    @Override
    public int updateTaskEveryDay() throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {
            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                client.updateTaskEveryDay();
                return 0;
            }
        };
        return this.exec(command, new Integer(0)).intValue();

    }

    @Override
    public T_Task getTaskById(int id) throws TException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public T_Task getTaskByTaskId(final String taskId) throws TException {
        Command<T_Task, TStorage.Client> command = new Command<T_Task, TStorage.Client>() {
            @Override
            public T_Task exec(TStorage.Client client) throws Exception {
                return client.getTaskByTaskId(taskId);
            }
        };

        return this.exec(command, new T_Task());
    }

    @Override
    public int deleteTaskByTaskId(String taskId) throws TException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<T_Task> getListTaskByStatus(short status) throws TException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<T_Task> getListTaskByStartDate(String startDate) throws TException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized int generateTransIdByBankCode(final String bankCode) throws TException {
        Command<Integer, TStorage.Client> command = new Command<Integer, TStorage.Client>() {

            @Override
            public Integer exec(TStorage.Client client) throws Exception {
                return client.generateTransIdByBankCode(bankCode);
            }
        };
        return this.exec(command, new Integer(0)).intValue();
    }

    @Override
    public List<T_TransactionReport> getTranxsWithBankCode(final String day, final short txStatus, final boolean allStatus, final String bankCode) throws TException {
        Command<List<T_TransactionReport>, TStorage.Client> command = new Command<List<T_TransactionReport>, TStorage.Client>() {

            @Override
            public List<T_TransactionReport> exec(TStorage.Client client) throws Exception {
                return client.getTranxsWithBankCode(day, txStatus, allStatus, bankCode);
            }
        };
        return this.exec(command, new ArrayList<T_TransactionReport>());
    }

    @Override
    public List<T_TransactionReport> getTranxsWithConfirmStatusAndBankCode(final String day, final short confirmStatus, final String bankCode) throws TException {
        Command<List<T_TransactionReport>, TStorage.Client> command = new Command<List<T_TransactionReport>, TStorage.Client>() {

            @Override
            public List<T_TransactionReport> exec(TStorage.Client client) throws Exception {
                return client.getTranxsWithConfirmStatusAndBankCode(day, confirmStatus, bankCode);
            }
        };
        return this.exec(command, new ArrayList<T_TransactionReport>());
    }

    @Override
    public T_Transaction getTransactionWithBankCode(final int txID, final String time, final String bankCode) throws TException {
        Command<T_Transaction, TStorage.Client> command = new Command<T_Transaction, TStorage.Client>() {

            @Override
            public T_Transaction exec(TStorage.Client client) throws Exception {
                return client.getTransactionWithBankCode(txID, time, bankCode);
            }
        };
        return this.exec(command, new T_Transaction());
    }

}
