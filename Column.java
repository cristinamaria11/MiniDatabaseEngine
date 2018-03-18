import java.util.ArrayList;

public class Column {
    String columnName;
    String columnType;
    ArrayList content;

    public Column(String columnName, String columnType, ArrayList<Object> content) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.content = content;
    }

    public Column() {

        this(new String(), new String(), new ArrayList<Object>());
    }

    public Column(String columnName, String columnType) {

        this(columnName, columnType, new ArrayList<Object>());
    }

    /*
    * Receives a comparator and a string value to be compared
    * with the elements of the current column. Returns the indexes
    * of the matching elements.
    * */
    public ArrayList<Integer> filterbyCondition(String comparator, String value) {
        ArrayList<Integer> indexes =  new ArrayList<Integer>();

        for(int i = 0; i < this.content.size(); i++) {
            if(comparator.equals("==")) {
                if(this.content.get(i).equals(value)) {
                    indexes.add(i);
                }
            }
        }

        return indexes;
    }

    /*
    * Receives a comparator and an int value to be compared
    * with the elements of the current column. Returns the indexes
    * of the matching elements.
    * */
    public ArrayList<Integer> filterbyCondition(String comparator, int value) {
        ArrayList<Integer> indexes =  new ArrayList<Integer>();

        for(int i = 0; i < this.content.size(); i++) {
            if(comparator.equals("==")) {
                if(this.content.get(i).equals(value)) {
                    indexes.add(i);
                }
            }
            else {
                if(comparator.equals("<")) {
                    if((int)this.content.get(i) < value) {
                        indexes.add(i);
                    }
                }
                else {
                    if(comparator.equals(">")) {
                        if((int)this.content.get(i) > value) {
                            indexes.add(i);
                        }
                    }
                }
            }
        }
        return indexes;
    }

    /*
    * Returns a list with the needed result
    * depending on the given operation.
    * */
    public ArrayList<Object> filterbyOperation(String operation) {
        ArrayList<Object> result = new ArrayList<Object>();

        if(operation.equals("count")) {
            result.add(this.content.size());
        }
        else {
            if (operation.equals("min")) {
                if(this.content.size() > 0) {
                    int min = (int) this.content.get(0);
                    for (Object o : this.content) {
                        if ((int) o < min) {
                            min = (int) o;
                        }
                    }
                    result.add(min);
                }
            } else {
                if (operation.equals("max")) {
                    if(this.content.size() > 0) {
                        int max = (int) this.content.get(0);
                        for (Object o : this.content) {
                            if ((int) o > max) {
                                max = (int) o;
                            }
                        }
                        result.add(max);
                    }
                }
                else {
                    int sum = 0;
                    for (int i = 0; i < this.content.size(); i++) {
                        Object o = this.content.get(i);
                        sum += (int) o;
                    }
                    if (operation.equals("sum")) {
                        result.add(sum);
                    } else {
                        if (operation.equals("avg")) {
                            result.add((float) sum / (this.content.size()));
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    /*
    * Overrides the toString() implicit method
    * for a better output format.
    * */
    public String toString() {

        return this.columnName + ", " + this.columnType;
    }
}
