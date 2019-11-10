package backend.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import backend.converters.RegistrationConverter;
import backend.dto.RegistrationDTO;
import backend.model.Administrator;
import backend.model.RegisteredUser;
import backend.model.User;
import backend.repository.UserRepository;
import backend.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	
	@Autowired
	private EmailService emailService;
	
	

	@Override
	public User findByUsername(String username) throws UsernameNotFoundException {
		User u = userRepository.findByUsername(username);
		return u;
	}


	
	@Override
	public User findById(Long id) throws AccessDeniedException{
		return userRepository.getOne(id);
	}

	public List<User> findAll() throws AccessDeniedException {
		List<User> result = userRepository.findAll();
		return result;
	}
	
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public RegisteredUser registerUser(RegistrationDTO registrationDTO) {
		RegisteredUser registeredUser = RegistrationConverter.RegistrationDTOToRegisteredUser(registrationDTO);
		userRepository.save(registeredUser);
		try {
			emailService.sendRegistrationConfirmationEmail(registeredUser);
		} catch (MailException | InterruptedException e) {
			System.out.printf("Error sending mail:",e.getMessage());
			e.printStackTrace();
		} 
		return registeredUser;
	}

	@Override
	public Administrator registerAdmin(RegistrationDTO registrationDTO) {
		Administrator administrator = RegistrationConverter.RegistrationDTOToAdministrator(registrationDTO);
		userRepository.save(administrator);
		return administrator;
		
	}
}
