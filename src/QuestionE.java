import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;




public class QuestionE
{
	static final String DRIVER = "com.mysql.jdbc.Driver"; // The Driver determines the vendor and type of database. In our case it is Mysql. This is from the slides
	static final String URL = "jdbc:mysql://nightmare.cs.uct.ac.za:3306/pndren003";  // Is is connecting to my sql databse on the nightmare server that we did in assignment 4
	static final String USERNAME = "pndren003"; 
	static final String PASSWORD = "thazeeth";

	public static void main (String [] args)
	{
		//Declare Database and statement as null. We will initialise this later
		Connection conn= null;    
		Statement stment = null;

		try
		{
			Class.forName(DRIVER); //The basic idea behind using Class.forName() is to load a JDBC driver implementation . In our case MySQL

			Scanner keyboard = new Scanner(System.in);
			int select; 
			do
			{
                                // List of interface options for the user. You can add more here.
                            	System.out.println("Choose an option below: ");
				System.out.println("1: View the data of a specific student (Name, Surname, School, UCT Score)");
				System.out.println("2: View course averages");
				System.out.println("3: View top 3 students in a particular course");
                                System.out.println("4: View the GPA of a specific student");
				System.out.println("0: Quit");
				select = keyboard.nextInt();
                                
				//Option 1
				if (select==1)
				{
                                    System.out.println("Enter Student ID: ");
                                    String IDs = keyboard.next();
                                    conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);  //Open a connection to the database
                                    stment = conn.createStatement(); //Create a statement object


					
                                    String sql = "SELECT StuID, FirstName, LastName, SchoolName, UCTScore FROM Matric WHERE StuID="; // SQL Statement
                                    String query = sql + IDs; // We add the user input to the SQL statement

                                    ResultSet result = stment.executeQuery(query);  // We excecute the query and it returnd a resultSet object
                                    System.out.println();
                                    System.out.printf("%-15s%-20s%-20s%-20s%-10s","Student ID","Name", "Surname", "School Name", "UCT Score");
                                    System.out.println("");
					//We can loop through a result set in a similar way we use Scanners to loop through files. 
					//This is not an array so there are not indexes and we cant go backwards.
                                    while(result.next())
                                    {
						// We can access individual columns in out database and store them in variables
						int StuID = result.getInt("StuID");
						String FirstName = result.getString("FirstName");
						String LastName = result.getString("LastName");
						String SchoolName = result.getString("SchoolName");
						int UCTScore = result.getInt("UCTScore");

						// We can now print the result line by line
						System.out.printf("%-15d%-20s%-20s%-20s%-10d", StuID , FirstName, LastName, SchoolName, UCTScore);
						System.out.println("\n");

					}

					// We must now close the Database objects
					result.close();
					stment.close();
					conn.close();
				}
				
				if (select==2)
				{
                                        System.out.println("Enter Course code: ");
					String code = keyboard.next();
					conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
					stment = conn.createStatement();
					if (code.equals("0"))
					{
						break;
					}
					String sql = "SELECT avg(Percent) AS Average FROM University WHERE Course=";
					String query = sql + "'" + code + "'";
					//System.out.println(query);


					ResultSet result = stment.executeQuery(query);
                                        System.out.println();
                                        System.out.printf("%-15s","Average");
                                        System.out.println("");
					while(result.next())
					{
						String Average = result.getString("Average");
						System.out.printf("%-15s", Average);
					}
                                        
                                        System.out.println("\n");
					result.close();
					stment.close();
					conn.close();
				}

				if(select==3)
				{
                                        System.out.println("Enter Course code: ");
                                        String code = keyboard.next();
					conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
					stment = conn.createStatement();
                                        String sql = "Select A.StuID, B.FirstName, B.LastName, A.Course, A.Percent\n"
                                                    + "FROM University as A, Matric as B\n"
                                                    + "WHERE A.StuID = B.StuID AND A.Course =";
                                        String query =sql + "'" + code + "'" + "\n" 
                                                        + "ORDER BY A.Percent desc LIMIT 3;";
					//  System.out.println(query);
                                        ResultSet result = stment.executeQuery(query);
                                        System.out.printf("%-15s%-20s%-20s%-10s%-10s","Student ID","Name", "Surname", "Course", "Percent");
                                        System.out.println("");
                                        while(result.next())
                                            {
						
                                                int StuID = result.getInt("StuID");
						String FirstName = result.getString("FirstName");
						String LastName = result.getString("LastName");
                                                String Course = result.getString("Course");
						int Percent = result.getInt("Percent");

						// We can now print the result line by line
                                                //System.out.println("");
						System.out.printf("%-15d%-20s%-20s%-10s%-10d", StuID , FirstName, LastName, Course, Percent);
						System.out.println("");

					}
                                        System.out.println("");
                                    result.close();
                                    stment.close();
                                    conn.close();
                                }
                                if(select == 4){
                                    System.out.println("Enter Student ID: ");
                                    String code = keyboard.next();
                                    
                                    conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);  //Open a connection to the database
                                    stment = conn.createStatement(); //Create a statement object
                                    String sql = "SELECT A.StuID, B.FirstName, B.LastName," 
                                            + "CAST((SUM((A.Percent * A.Credits)) / SUM(A.credits)) AS DECIMAL (4 , 2 )) AS GPA\n"
                                            + "FROM University AS A,Matric AS B\n"
                                            + "WHERE A.StuID = B.StuID AND A.StuID =";
                                    String query =sql + "'" + code + "'" + "\n" 
                                            + "GROUP BY A.StuID\n" 
                                            + "ORDER BY FirstName;";
                                    
                                    System.out.println("");
                                    System.out.printf("%-15s%-20s%-20s%-10s","Student ID","Name", "Surname", "GPA");
                                    System.out.println("");
                                    ResultSet result = stment.executeQuery(query);
                                    while(result.next())
					{
						
                                                int StuID = result.getInt("StuID");
						String FirstName = result.getString("FirstName");
						String LastName = result.getString("LastName");
						String GPA = result.getString("GPA");

                                                // We can now print the result line by line
						System.out.printf("%-15d%-20s%-20s%-10s", StuID , FirstName, LastName, GPA);
						System.out.println("\n");

					}
                                    result.close();
                                    stment.close();
                                    conn.close();
                                }
	


			}while(select!=0);
			
		}

		catch(SQLException se)
		{
			se.printStackTrace();

		}

		catch(ClassNotFoundException k)
		{
			k.printStackTrace();
		}

		finally {
		try {
		      if (conn != null) conn.close();
		      } catch (SQLException se)
				{ se.printStackTrace(); 
		      } 
		} 
	} 
		
}

