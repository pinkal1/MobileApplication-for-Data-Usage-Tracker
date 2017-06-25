package com.springapp.mvc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Controller
public class UserController {
	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserRepository userRepository;

	@RequestMapping(value = "/" ,method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world guys look here !");
		return "hello";
	}

	@RequestMapping(value = "/user/allusers", method = RequestMethod.GET)
	public
	@ResponseBody
	String listUsersJson(ModelMap model) throws JSONException {
		JSONArray userArray = new JSONArray();
		for (User user : userRepository.findAll()) {
			JSONObject userJSON = new JSONObject();
			userJSON.put("username ", user.getUsername());
			userJSON.put("EmailID", user.getEmailId());
			userJSON.put("date", user.getRegisterDate());
			userJSON.put("Phone No",user.getPhoneNo());
			userJSON.put("Admin No",user.getAdminNo());
			userArray.put(userJSON);
		}
		return userArray.toString();
	}

	//http://92aecc3d.ngrok.io/user/add?emailId=pinkal@abc.com&password=abc&phoneNo=1234&adminPhone=123&username=pinkal;
	@RequestMapping(value = "/user/add")
	public
	@ResponseBody
	String Add(@RequestParam(value="emailId") String eid, @RequestParam(value="username") String username,@RequestParam(value="password") String password,@RequestParam(value = "phoneNo")String phnno, @RequestParam(value = "adminPhone")String adminPhone) throws JSONException{
		JSONObject status  = new JSONObject();
		User user = new User(eid,username,phnno,adminPhone,password);

		userRepository.save(user);

		status.put("tag", "user");
		status.put("status", new Boolean(true));
		/*if(userRepository.findByPhoneNo(adminPhone)!=null) {
			userRepository.save(user);

			status.put("tag", "user");
			status.put("status", new Boolean(true));
		}
		else{
			status.put("error_msg","Admin not registered yet....please wait");
			status.put("status", new Boolean(false));
		}*/
		return  status.toString();
	}
//http://localhost:8055/login/doLogin?phoneNo=123&password=axc
	@RequestMapping("/login/doLogin")
	@Transactional
	public
	@ResponseBody String DoLogin(@RequestParam(value = "phoneNo")String phno, @RequestParam(value="password") String pwd) throws JSONException
	{
		JSONObject status = new JSONObject();
		/*TypedQuery query = em.createQuery("select u from userLogin u where u.emailId=?1 and u.password=?2", User.class);
		query.setParameter(1, phno);
		query.setParameter(2, pwd);*/

		//if(!query.getResultList().isEmpty()){
		//	status.put("tag", "login");
		User user = userRepository.findByEmailId(phno);
		if(user !=null){
			if(user.getPassword().equals(pwd))
				status.put("status", new Boolean(true));
			else {
				status.put("err_msg", "Enter correct password");
				status.put("status",new Boolean(false));
			}
		}
		else {
			//status.put("tag", "login");
			status.put("status", new Boolean(false));
			status.put("error_msg", "Login unsuccessful");
		}
		return status.toString();
	}

	/*// fetch list of users phnno given admin number
	@RequestMapping("/login/familyUsers")
	public
	@ResponseBody String fetchFamilyUsers(@RequestParam(value="phoneNo") String phnNo)throws JSONException{
		JSONArray userArray = new JSONArray();

		User u = userRepository.findByPhoneNo(phnNo);
		for (User user : userRepository.findByAdminNo(u.getAdminNo())) {
			JSONObject userJSON = new JSONObject();
			userJSON.put("username ", user.getUsername());
			//userJSON.put("EmailID", user.getEmailId());
			//	userJSON.put("date", user.getRegisterDate());
			userJSON.put("Phone No",user.getPhoneNo());
			//userJSON.put("Admin No",user.getAdminNo());
			userArray.put(userJSON);
		}
		return userArray.toString();

	}*/

	//given phone number is admin himself
	//http://92aecc3d.ngrok.io/login/isAdmin?adminNo=12
	@RequestMapping("/login/isAdmin")
	public
	@ResponseBody String isAdmin(@RequestParam(value="adminNo") String adminNo)throws JSONException {
		JSONObject status = new JSONObject();
		if(userRepository.findByPhoneNo(adminNo)!= null) {
			if (!userRepository.findByAdminNo(adminNo).isEmpty()) {
				status.put("msg", "is an Admin");
				status.put("status", new Boolean(true));
			} else {
				status.put("err_msg", "is not an Admin");
				status.put("status", new Boolean(false));
			}
		}else {
			status.put("err_msg", "is not an registered yet..");
			status.put("status", new Boolean(false));
		}

		return status.toString();
	}

	// validate and change password
	//https://0fb937ab.ngrok.io/login/changePwd?phoneNo=7543&password=aaa&newval=asa
	//http://localhost:8055/login/changePwd?phoneNo=7543&password=aaa&newval=asa

	@RequestMapping("/login/changePwd")
	@Transactional
	public
	@ResponseBody
	String ChangePwd(@RequestParam(value = "phoneNo") String phno, @RequestParam(value = "password") String pwd, @RequestParam(value = "newval") String newpwd) throws JSONException {
		JSONObject status = new JSONObject();
		User user = userRepository.findByPhoneNo(phno);
		if (user != null) {
			if (user.getPassword().equals(pwd)) {
				user.setPassword(newpwd);
				userRepository.save(user);
				status.put("result", "password changed successfully");
				status.put("status", new Boolean(true));
			} else {
				status.put("result", "password incorrect");
				status.put("status", new Boolean(false));
			}

		} else {
			status.put("result", "phone number does not exists");
		}

		return status.toString();
	}




	// fetch list of users phnno given admin number

	//http://d36faedd.ngrok.io/data
	@RequestMapping("/list/familyUsers")
 public
 @ResponseBody
 String fetchFamilyUsers(@RequestParam(value ="phoneNo") String phoneNo) throws JSONException {
	JSONArray userArray = new JSONArray();
	JSONObject status = new JSONObject();
	User u = userRepository.findByPhoneNo(phoneNo);
	if (u != null) {
		for (User user : userRepository.findByAdminNo(u.getAdminNo())) {
			JSONObject userJSON = new JSONObject();
			userJSON.put("username",user.getUsername());
			//userJSON.put("EmailID", user.getEmailId());
			//	userJSON.put("date", user.getRegisterDate());
			userJSON.put("Phone No",user.getPhoneNo());
			//userJSON.put("Admin No",user.getAdminNo());
			userArray.put(userJSON);
		}
		if (userArray.length() == 0) {
			status.put("status", new Boolean(false));
			status.put("error_msg", "Phone number does not exists");
		} else {
			status.put("status", new Boolean(true));
			status.put("result", userArray);

			//	status.put("result", "Login successful");
		}

	} else {
		status.put("status", new Boolean(false));
		status.put("error_msg", "Phone number does not exists");
	}
	return status.toString();
}


	//http://92aecc3d.ngrok.io/login/removeUser?phoneNo=7543&adminNo=12
	@RequestMapping("/login/removeUser")
	@Transactional
	public
	@ResponseBody
	String removeUser(@RequestParam(value = "phoneNo") String phno,@RequestParam(value = "adminNo") String adno) throws JSONException {
		JSONObject status = new JSONObject();
		User user = userRepository.findByPhoneNo(phno);
		if (user != null) {
			//em.getTransaction().begin();
			em.remove(user);
			//em.getTransaction().commit();
			status.put("result", "User removed");
			status.put("status",new Boolean(true));
		} else {
			status.put("result", "phone number does not exists");
			status.put("status",new Boolean(false));
		}

		return status.toString();
	}

}


