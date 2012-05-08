package gmerg.entities.submission;

public class Person {

    private String name;
    private String email;
    private String lab;
    private String address;
    private String city;
    private String postcode;
    private String country;
    private String phone;
    private String fax;
    private String id;
    
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return this.email;
    }

    public void setLab(String lab) {
        this.lab = lab;
    }

    public String getLab() {
        return this.lab;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }
    
    public String getCity() {
      return this.city;
    }
    
    public void setCity(String city) {
    	this.city = city;
    }
    
    public void setPostcode(String postcode) {
      this.postcode = postcode;
    }
    
    public String getPostcode() {
      return this.postcode;
    }
    
    public void setCountry(String country) {
      this.country = country;
    }
    
    public String getCountry() {
      return this.country;
    }
    
    public void setPhone(String phone) {
      this.phone = phone;
    }
    
    public String getPhone() {
      return this.phone;
    }
    
    public void setFax(String fax) {
      this.fax = fax;
    }
    
    public String getFax() {
      return this.fax;
    }
    
	public void setId(String id) {
	   this.id = id;
	}
      
	public String getId() {
	   return this.id;
	}
	
}
