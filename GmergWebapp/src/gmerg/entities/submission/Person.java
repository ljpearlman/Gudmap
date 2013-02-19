package gmerg.entities.submission;

public class Person {
    
    protected String name;
    protected String email;
    protected String lab;
    protected String address;
    protected String address2;
    protected String city;
    protected String postcode;
    protected String country;
    protected String phone;
    protected String fax;
    protected String id;
    protected String fullAddress = null;
    protected String displayAddress = null;
    
    public void setName(String input) {
        name = input;
	if (null != name)
	    name = name.trim();
    }
    
    public String getName() {
        return name;
    }
    
    public void setEmail(String input) {
        email = input;
	if (null != email)
	    email = email.trim();
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setLab(String input) {
        lab = input;
	if (null != lab)
	    lab = lab.trim();
    }
    
    public String getLab() {
        return lab;
    }
    
    public void setAddress(String input) {
        address = input;
	if (null != address)
	    address = address.trim();
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress2(String input) {
        address2 = input;
	if (null != address2)
	    address2 = address2.trim();
    }
    
    public String getAddress2() {
        return address2;
    }
    
    
    public String getCity() {
	return city;
    }
    
    public void setCity(String input) {
    	city = input;
	if (null != city)
	    city = city.trim();
    }
    
    public void setPostcode(String input) {
	postcode = input;
	if (null != postcode)
	    postcode = postcode.trim();
    }
    
    public String getPostcode() {
	return postcode;
    }
    
    public void setCountry(String input) {
	country = input;
	if (null != country)
	    country = country.trim();
    }
    
    public String getCountry() {
	return country;
    }
    
    public void setPhone(String input) {
	phone = input;
	if (null != phone)
	    phone = phone.trim();
    }
    
    public String getPhone() {
	return phone;
    }
    
    public void setFax(String input) {
	fax = input;
	if (null != fax)
	    fax = fax.trim();
    }
    
    public String getFax() {
	return fax;
    }
    
    public void setId(String input) {
	id = input;
	if (null != id)
	    id = id.trim();
    }
    
    public String getId() {
	return id;
    }
    
    public String getFullAddress () {
	if (null == fullAddress) {
	     fullAddress = name;
	     if (null != lab && !lab.equals(""))
		 fullAddress  = fullAddress+", "+lab;
	     if (null != address && !address.equals(""))
		 fullAddress  = fullAddress+", "+address;
	     if (null != address2 && !address2.equals(""))
		 fullAddress  = fullAddress+", "+address2;
	     if (null != city && !city.equals(""))
		 fullAddress  = fullAddress+", "+city;
	     if (null != postcode && !postcode.equals(""))
		 fullAddress  = fullAddress+", "+postcode;
	     if (null != country && !country.equals(""))
		 fullAddress  = fullAddress+", "+country;
	}

	return fullAddress;
    }	
    
    public String getDisplayAddress () {
	if (null == displayAddress) {
	     displayAddress = "";
	     if (null != lab && !lab.equals(""))
		 displayAddress = lab;
	     if (null != city && !city.equals("")) {
		 if (displayAddress.equals(""))
		     displayAddress  = city;
		 else
		     displayAddress  = displayAddress+", "+city;
	     }
	     if (null != country && !country.equals("")) {
		 if (displayAddress.equals(""))
		     displayAddress  = country;
		 else
		     displayAddress  = displayAddress+", "+country;
	     }
	     if ((null != email  && !email.equals("")) && !displayAddress.equals(""))
		 displayAddress  = displayAddress+", "+email;
	}

	return displayAddress;
    }	    
}
