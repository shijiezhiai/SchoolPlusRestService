package com.websystique.springmvc;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;

import com.websystique.springmvc.model.Admin;
import com.websystique.springmvc.util.Md5Calculator;
import org.springframework.web.client.RestTemplate;

public class SpringRestTestClient {

	public static final String REST_SERVICE_URI = "http://localhost:8080";
	
	/* GET */
	@SuppressWarnings("unchecked")
	private static void listAllAdmins(){
		System.out.println("Testing listAllAdmins API-----------");
		
		RestTemplate restTemplate = new RestTemplate();
		List<LinkedHashMap<String, Object>> adminsMap = restTemplate.getForObject(
				REST_SERVICE_URI + "/admin/", List.class);
		
		if(adminsMap!=null){
			for(LinkedHashMap<String, Object> map : adminsMap){
	            System.out.println("Admin: id=" + map.get("id")
                        + ", Name=" + map.get("username")
                        + ", email=" + map.get("emailAddress") +
                        ", mobile=" + map.get("mobilePhoneNumber"));
	        }
		} else {
			System.out.println("No admin exist----------");
		}
	}
	
	/* GET */
	private static void getAdmin(){
		System.out.println("Testing getAdmin API----------");
		RestTemplate restTemplate = new RestTemplate();
        Admin admin = restTemplate.getForObject(REST_SERVICE_URI + "/admin/1", Admin.class);
        System.out.println(admin);
	}
	
	/* POST */
    private static void createAdmin() throws Exception {
		System.out.println("Testing create Admin API----------");
    	RestTemplate restTemplate = new RestTemplate();
        Admin admin = new Admin(
				"jimmy", Md5Calculator.calMd5("123456"), "15022906687", "jimmy@126.com", 0);
        URI uri = restTemplate.postForLocation(REST_SERVICE_URI + "/admin/", admin, Admin.class);
        System.out.println("Location : " + uri.toASCIIString());
    }

    /* PUT */
    private static void updateAdmin() throws Exception {
		System.out.println("Testing update Admin API----------");
        RestTemplate restTemplate = new RestTemplate();
        Admin admin  = new Admin(
				"Tommy", Md5Calculator.calMd5("123456"), "12345678901", "alabo@163.com", 1);
        restTemplate.put(REST_SERVICE_URI+"/admin/1", admin);
        System.out.println(admin);
    }

    /* DELETE */
    private static void deleteAdmin() {
		System.out.println("Testing delete Admin API----------");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(REST_SERVICE_URI+"/admin/3");
    }


    /* DELETE */
    private static void deleteAllAdmins() {
		System.out.println("Testing all delete Admins API----------");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(REST_SERVICE_URI + "/admin/");
    }

    public static void main(String args[]) throws Exception {
		listAllAdmins();
		getAdmin();
		createAdmin();
		listAllAdmins();
		updateAdmin();
		listAllAdmins();
		deleteAdmin();
		listAllAdmins();
		deleteAllAdmins();
		listAllAdmins();
    }
}