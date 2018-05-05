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


			// List of interface options for the user. You can add more here.
			System.out.println("Choose an option below: ");
			System.out.println("1: View the data of a specific student (Name, Surname, School, UCT Score)");
			System.out.println("2: View course averages");
			System.out.println("3: View ");
			System.out.println("0: Quit");
			Scanner keyboard = new Scanner(System.in);

			int select = 0; 
			select = keyboard.nextInt();

			while(select!=0)
			{
				//Option 1
				if (select==1)
				{
					conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);  //Open a connection to the database
					stment = conn.createStatement(); //Create a statement object

					System.out.println("Enter Student ID: ");
					String IDs = keyboard.next();
					
					String sql = "SELECT StuID, FirstName, LastName, SchoolName, UCTScore FROM Matric WHERE StuID="; // SQL Statement
					String query = sql + IDs; // We add the user input to the SQL statement

					ResultSet result = stment.executeQuery(query);  // We excecute the query and it returnd a resultSet object

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
						System.out.print("Student ID: "+ StuID + " | Name: "+ FirstName
							+ " | Surname: "+ LastName+ " | School Name: "+ SchoolName +
							 "| UCT Score: "+ UCTScore);


						System.out.println("\n");

					}

					// We must now close the Database objects
					result.close();
					stment.close();
					conn.close();
				}
				
				if (select==2)
				{
					conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
					stment = conn.createStatement();
					System.out.println("Enter Course code: ");
					String code = keyboard.next();
					if (code.equals("0"))
					{
						break;
					}
					String sql = "SELECT avg(Percent) AS Average FROM University WHERE Course=";
					String query = sql + "'" + code + "'";
					//System.out.println(query);


					ResultSet result = stment.executeQuery(query);

					while(result.next())
					{
						
						String Average = result.getString("Average");
						
						System.out.print("Average: " + Average);
						System.out.println();
						System.out.println();

					}
					result.close();
					stment.close();
					conn.close();
				}

				if(select==3)
				{
					System.out.println("TBC");
					
				}
				System.out.println("Choose an option below: ");
				System.out.println("1: View the data of a specific student (Name, Surname, School, UCT Score)");
				System.out.println("2: View course averages");
				System.out.println("3: View top 3 students in a particular course");
				System.out.println("0: Quit");
				select = keyboard.nextInt();


			}
			
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

