import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

public class Table {
    String tableName;
    String []columnNames;
    String []columnTypes;
    ArrayList<Column> columns;
    Semaphore transactionSem;
    Semaphore operationSem;

    public Table(String tableName, String []columnNames, String []columnTypes) {
        columns = new ArrayList<Column>();

        for(int i = 0; i < columnTypes.length; i++) {
            columns.add(new Column(columnNames[i], columnTypes[i]));
        }

        this.transactionSem = new Semaphore(1);
        this.operationSem = new Semaphore(1);
        this.tableName = tableName;
        this.columnNames = columnNames;
        this.columnTypes =  columnTypes;
    }

    /*
    * Returns the column with the given name.
    * */
    public Column getColumn(String name) {
        ArrayList<Object> column = null;
        for(int i = 0 ; i< this.columns.size(); i++) {
            if(columns.get(i).columnName.equals(name)) {
                Column x = columns.get(i);
                return new Column(x.columnName, x.columnType, x.content);
            }
        }
        return new Column();
    }

    /*
    * Updates values in a column, by checking a condition.
    * */
    public void updateColumn(String condition, ArrayList<Object> values) {
        ArrayList<Column> list=  new ArrayList<Column>();
        String columnName;
        String comparator;
        String v;
        int value = -1;
        String []splitCondition = condition.split(" ");
        columnName = splitCondition[0];
        comparator = splitCondition[1];
        v = new String();

        if(condition.equals(" ")) {
            return;
        }

        if(! this.getColumn(columnName).columnType.equals("int")){
            v = String.valueOf(splitCondition[2]);
        }
        else {
            value = Integer.valueOf(splitCondition[2]);
        }

        Column column = this.getColumn(columnName);
        int index = -1;
        ArrayList<Integer> x;
        if(!this.getColumn(columnName).columnType.equals("int")) {
            x = column.filterbyCondition(comparator, v);
        }
        else {
            x = column.filterbyCondition(comparator, value);
        }
        if(x.size() > 0) {
            index = x.get(0);
        }
        else {
            return;
        }

        int i = 0;
        for(Column c : this.columns) {
            c.content.remove(index);
            c.content.add(index, values.get(i));
            i++;
        }
    }

    /*
    * Eliminates the lines that do not fulfill the given condition
    * and returns a list of the ones that do.
    * */
    public ArrayList<Column> firstFilter(String condition) {
        ArrayList<Column> list=  new ArrayList<Column>();

        if(condition.equals(" ")) {
            list = this.columns;
            return list;
        }

        String columnName;
        String comparator;
        int value = -1;
        String v = new String();

        String []splitCondition = condition.split(" ");
        columnName = splitCondition[0];
        comparator = splitCondition[1];
        if(! this.getColumn(columnName).columnType.equals("int")){
            v = String.valueOf(splitCondition[2]);
        }
        else {
            value = Integer.valueOf(splitCondition[2]);
        }

        Column column = this.getColumn(columnName);
        ArrayList<Integer> indexes = new ArrayList<Integer>();

        if(!this.getColumn(columnName).columnType.equals("int")) {
            indexes = column.filterbyCondition(comparator, v);
        }
        else {
            indexes = column.filterbyCondition(comparator, value);
        }

        if(indexes.size() == 0) {
            return new ArrayList<Column>();
        }

        for(Column c : this.columns) {
            Column x = new Column(c.columnName, c.columnType);
            for(int i = 0; i < indexes.size(); i++) {
                if(c.content.size() > 0) {
                    x.content.add(c.content.get(indexes.get(i)));
                }
            }
            list.add(x);
        }
        return list;
    }

    /*
    * Returns a list containing the columns modified by applying
    * the suitable operation from the operations list.
    * */
    public ArrayList<ArrayList<Object>> secondFilter(String[] operations) {
        ArrayList<ArrayList<Object>> list = new ArrayList<>();

        for (int i = 0; i < operations.length; i++) {
            String currOperation = operations[i];
            if(currOperation.contains(")")) {
                String operation = currOperation.split("\\(")[0];
                String columnName = currOperation.split("\\(")[1].split("\\)")[0];
                Column col = this.getColumn(columnName);
                list.add(col.filterbyOperation(operation));
            }
            else {
                list.add(this.getColumn(currOperation).content);
            }
        }
        return list;
    }

    @Override
    /*
    * Overrides the toString() implicit method
    * for a better output format.
    * */
    public String toString() {

        return this.tableName;
    }
}
