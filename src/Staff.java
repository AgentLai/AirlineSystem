public class Staff extends User {
    private int age;
    private String gender;
    private String email;
    private double salary;
    private String workedSince;
    private String position;

    public Staff(String username, String password) {
        super(username, password, "Staff");
        this.age = 18; // Default value, update as needed
        this.gender = "Not specified";
        this.email = "Not specified";
        this.salary = 0.0;
        this.workedSince = "Unknown";
    }

    // Add getters and setters for all fields

    public int getAge(){ 
    	return age; 
    }
    
    public void setAge(int age){ 
    	this.age = age; 
    }
    
    public String getGender(){ 
    	return gender; 
    }
    
    public void setGender(String gender){ 
    	this.gender = gender; 
    }
    
    public String getEmail(){ 
    	return email; 
    }
    
    public void setEmail(String email){ 
    	this.email = email; 
    }
    
    public double getSalary(){ 
    	return salary; 
   }
    
    public void setSalary(double salary){ 
    	this.salary = salary; 
    }
    
    public String getWorkedSince(){ 
    	return workedSince; 
    }
    
    public void setWorkedSince(String workedSince){ 
    	this.workedSince = workedSince; 
    }
    
    public String getPosition(){
    	return position;
    }
    
    public void setPosition(String position){
    	this.position = position;
    }
    
}

