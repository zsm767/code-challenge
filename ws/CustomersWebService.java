package fromjava.server;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(serviceName="CustomerService")
public class CustomersWebService {
		/* 
		this is working under the assumption that DBObject is essentially a middleware class that can aid in working as a liason		
		between the DB and the frontend 
		*/
		DBObject dbo = new DBObject();
	
		//primarily for testing purposes, just displaying an inputted message
		@WebMethod(action="testMessage")
		public String testMessage(@WebParam(message="message") String message){
			return "Hello, " + message + "!";
		}
		
		
		@WebMethod(action="createCustomers")
		public void createNewCustomer(String[] customerInfo){
			dbo.createNewEntry(customerInfo)
		}
		
		
		@WebMethod(action="readCustomers")
		// the arg should be potentially changed, depending on what the "read" entails
		public void readCustomers(@WebParam(...) Customer customer){
			//reading data from the db, looking for the arg
			//below is almost entirely pseudocode:
			dbo.lookUp(customer);
			System.out.print("Found: " +customer+ ".\n\n Information as follows: \n\n" +dbo.info());
		}
		
		
		@WebMethod(action="updateCustomers")
		public void updateCustomers(@WebParam(customer="customer") Customer customer){
			if( dbo.exists(customer) ){
				System.out.println("Updating customer " + customer.name ".");
				dbo.update(customer);
			}
			else{
				System.out.println("Customer not found, creating new customer...");
				createCustomers(customer);
			}
		}
		
		
		@WebMethod(action="deleteCustomers")
		public void deleteCustomers(@WebParam(customer="customer") Customer customer){
			// initially, want to search for the param customer. 
			// keep in mind, this works as an individual delete, not necessarily a mass clear of the DB 
			if( dbo.exists(customer) ){
				// realistically, would want a 'for' loop or something to search, blah blah
				System.out.println("Customer " +customer.name+ " found. Removing...");
				dbo.remove(customer);
			}
			else{
				return "Customer " +customer.name+ "not found. Try a different name...";
			}
		}
		
		
		@WebMethod(action="searchCustomers")
		public void searchCustomers(@WebParam(customer="customer") Customer customer){
			if( dbo.exists(customer) ){
				return "Customer " +customer.name+ "found. Information as follows:\n";
				System.out.println(customer.info()) 
			}
			else{
				return "Customer " +customer.name+ " not found...";
			}
		}
}