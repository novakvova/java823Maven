import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Connection conn = ConnectionToDB(2);
        if(conn!=null)
        {
            try {
                Employee employee= new Employee();
                employee.setName("shampur3");
                employee.setSalary(new BigDecimal(11111));
                AddEmployee(employee, conn);
                List<Employee> list =  GetEmployees(conn);
                System.out.println("-------List employees---------");
                for (Employee item : list) {
                    System.out.println(item);
                }
            } catch(Exception ex) {
                System.out.println("Problem "+ex.getMessage());
            }

        }
    }
    private static Connection ConnectionToDB(int db) {
        if(db==1) {
            String hostName = "192.168.0.176";
            String dbName = "montanadb";
            String userName = "root";
            String password = "Qwerty-88*ffsds";
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                String connectionURL = "jdbc:mysql://" + hostName + ":3306/" + dbName;
                Connection conn = DriverManager.getConnection(connectionURL, userName, password);
                if (conn != null) {
                    System.out.println("Connected to the database!");
                    return conn;
                } else {
                    System.out.println("Failed to make connection!");
                }
            } catch (Exception ex) {
                System.out.println("Error " + ex.getMessage());
            }
        }
        else if(db==2) {
            String hostName = "192.168.0.176";
            String dbName = "rainbow_database";
            String userName = "unicorn_user";
            String password = "Qwi*7c673*^$ds";
            try {
                //Class.forName("com.mysql.cj.jdbc.Driver");
                String connectionURL = "jdbc:postgresql://" + hostName + ":5432/" + dbName;
                Connection conn = DriverManager.getConnection(connectionURL, userName, password);
                if (conn != null) {
                    System.out.println("Connected to the database!");
                    return conn;
                } else {
                    System.out.println("Failed to make connection!");
                }
            } catch (Exception ex) {
                System.out.println("Error " + ex.getMessage());
            }
        }
        return null;
    }

    public static void AddEmployee(Employee emp, Connection conn) throws SQLException {
        String SQL_INSERT = "INSERT INTO tbl_employees "
                + "(NAME, SALARY, CREATED_DATE)"
                + " VALUES (?, ?, ?);";

        PreparedStatement statement = conn.prepareStatement(SQL_INSERT);
        statement.setString(1,emp.getName());
        statement.setBigDecimal(2, emp.getSalary());
        statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("A new user was inserted successfully!");
        }

    }

    private static List<Employee> GetEmployees(Connection conn)
            throws SQLException {
        List<Employee> result = new ArrayList<Employee>();

        String SQL_SELECT = "Select * from tbl_employees";

        PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {

            long id = resultSet.getLong("ID");
            String name = resultSet.getString("NAME");
            BigDecimal salary = resultSet.getBigDecimal("SALARY");
            Timestamp createdDate = resultSet.getTimestamp("CREATED_DATE");

            Employee obj = new Employee();
            obj.setId(id);
            obj.setName(name);
            obj.setSalary(salary);

            obj.setCreatedDate(createdDate.toLocalDateTime());

            result.add(obj);

        }
        return result;
    }
}
