import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Database implements MyDatabase{
    ArrayList<Table> db;
    int numWorkerThreads;
    ExecutorService tpe;

    public Database() {
        db = new ArrayList<Table>();
    }

    @Override
    /*
    * Initialises the database with numWorkerThreads workers.
    * */
    public void initDb(int numWorkerThreads) {
        this.numWorkerThreads = numWorkerThreads;
        tpe = Executors.newFixedThreadPool(numWorkerThreads);
    }

    @Override
    /*
    * It stops the database service.
    * */
    public void stopDb() {
        tpe.shutdown();
    }

    @Override
    /*
    * Creates a table with the given name that contains
    * the columns with the names and types from the given lists.
    * */
    public void createTable(String tableName, String[] columnNames, String[] columnTypes) {
        db.add(new Table(tableName, columnNames, columnTypes));
    }

    @Override
    /*
    * Reads from the table that has the given name and returns
    * a list of columns that has the length of the conditions list.
    * */
    public synchronized ArrayList<ArrayList<Object>> select(String tableName, String[] operations, String condition) {
        ArrayList<ArrayList<Object>> res = new ArrayList<>();
        Table table = getTable(tableName);

        try {
            table.operationSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Column> list = new ArrayList<Column>();
        list = table.firstFilter(condition);
        table.columns = list;
        res = table.secondFilter(operations);

        table.operationSem.release();
        return res;
    }

    @Override
    /*
    * Writes in the table with the given name, only in the locations where
    * the condition is true, values from the input list.
    * */
    public synchronized void update(String tableName, ArrayList<Object> values, String condition) {
        Table table = getTable(tableName);

        try {
            table.operationSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        table.updateColumn(condition, values);

        table.operationSem.release();
    }

    @Override
    /*
    * Adds at the end of the table a new line with the given values.
    * */
    public synchronized void insert(String tableName, ArrayList<Object> values) {
        Table table = getTable(tableName);

        if(table.columns.size() > 0) {
            for (int i = 0; i < table.columns.size(); i++) {
                table.columns.get(i).content.add(values.get(i));
            }
        }

        table.operationSem.release();
    }

    @Override
    /*
    * Starts a transaction.
    * */
    public void startTransaction(String tableName) {
        Table table = this.getTable(tableName);
        try {
            table.transactionSem.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    /*
    * Ends a transaction already started.
    * */
    public void endTransaction(String tableName) {
        Table table = this.getTable(tableName);
        table.transactionSem.release();
    }

    /*
    * Returns the table with the given name from the current database.
    * */
    public Table getTable(String tableName) {
        Table table = null;
        for(Table x : db) {
            if(x.tableName.equals(tableName)) {
                table = x;
                break;
            }
        }
        return table;
    }

}
