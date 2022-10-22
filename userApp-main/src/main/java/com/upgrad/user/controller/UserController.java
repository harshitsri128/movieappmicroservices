package com.upgrad.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.upgrad.user.dto.UserDTO;
import com.upgrad.user.entities.User;
import com.upgrad.user.service.UserService;
import com.upgrad.user.utils.POJOConvertor;

@RestController
@RequestMapping(value="/user_app/v1")
public class UserController {
	
	@Autowired
	private UserService userService;

	
	//POST API
	@PostMapping(value = "/users",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity createUser(@RequestBody UserDTO userDto) {
		User requestUser = POJOConvertor.covertUserDTOToEntity(userDto);
		User savedUser = userService.createUser(requestUser);
		return new ResponseEntity(POJOConvertor.covertUserEntityToDTO(savedUser),HttpStatus.CREATED);
		
	}
	
	@GetMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getAllUsers() {
		
		List<User> users = userService.getAllUsers();
		List<UserDTO> userDtos = new ArrayList<>();
		
		users.forEach(user -> userDtos.add(POJOConvertor.covertUserEntityToDTO(user)));
		
		return new ResponseEntity(userDtos,HttpStatus.OK);
		
	}
	
	@GetMapping(value = "/users/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getUserById(@PathVariable(name ="id") int id) {
		
		User user = userService.getUserBasedOnId(id);
		return new ResponseEntity(POJOConvertor.covertUserEntityToDTO(user),HttpStatus.OK);
		
	}
	
	@PutMapping(value = "/users",produces = MediaType.APPLICATION_JSON_VALUE, consumes =  MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity updateUser(@RequestBody UserDTO userDto) {
		
		User updatedUser = userService.updateUser(POJOConvertor.covertUserDTOToEntity(userDto));
		
		return new ResponseEntity(POJOConvertor.covertUserEntityToDTO(updatedUser),HttpStatus.OK);
		
	}
	
	@DeleteMapping(value = "/users/{id}")
	public ResponseEntity deleteUserById(@PathVariable(name ="id") int id) {
		
		User user = userService.getUserBasedOnId(id);
		userService.deleteUser(user);
		return new ResponseEntity(null,HttpStatus.OK);
		
	}

}