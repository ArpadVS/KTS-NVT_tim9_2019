package backend.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import backend.exceptions.UserNotFoundException;
import backend.model.User;
import backend.repository.UserRepository;
import backend.service.FileUploadService;
import backend.service.UserService;

@RestController
@RequestMapping(value="api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
    private UserRepository repository;
	
	@Autowired
	private FileUploadService fileUploadService;

	// Za pristup ovoj metodi neophodno je da ulogovani korisnik ima ADMIN ulogu
	// Ukoliko nema, server ce vratiti gresku 403 Forbidden
	// Korisnik jeste autentifikovan, ali nije autorizovan da pristupi resursu
	/*@RequestMapping(method = GET, value = "/user/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public User loadById(@PathVariable Long userId) {
		return this.userService.findById(userId);
	}*/
	
	
	
	
	/*ovo je primer query param*/
	/*@RequestMapping(method = GET, value = "/user")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public User loadById2(@RequestParam Long userId) {
		return this.userService.findById(userId);
	}*/
	

	@RequestMapping(method = GET, value = "/user/all")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<User> loadAll() {
		return this.userService.findAll();
	}

	@RequestMapping("/whoami")
	//@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	public User user(Principal user) {
		return this.userService.findByUsername(user.getName());
	}
	
	
	
	
	/*Prvi nacin greska se hvata na nivou controlera nema propagacije greske navise takodje
	 * ispravan nacin rada samo ima puno redundantnog koda*/
/*	@RequestMapping("/kosamja")
	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	public ResponseEntity<User> kosamja(Principal user) {
		User korisnik;
		try {
			korisnik = userService.pronadjiKorisnika(user.getName());
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		}
		return new ResponseEntity<>(korisnik, HttpStatus.OK);
	}*/
	
	/*@RequestMapping("/kosamja")
	@PreAuthorize("hasRole('ROLE_REGISTERED_USER')")
	public ResponseEntity<User> kosamja(Principal user) throws UserNotFoundException{
		User korisnik;
		try {
			korisnik = userService.pronadjiKorisnika(user.getName());
		} catch (UserNotFoundException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		}
		return new ResponseEntity<>(korisnik, HttpStatus.OK);
	}*/
	
	/*@RequestMapping(method = GET, value = "/user/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> loadById(@PathVariable Long userId) throws UserNotFoundException{
		User korisnik = userService.pronadjiKorisnika(userId);
		
		if (korisnik==null) {
			throw new UserNotFoundException(userId);
		}
		return new ResponseEntity<>(korisnik, HttpStatus.OK);
		
	
		return repository.findById(userId)
                .orElseThrow(() -> new BookNotFoundException(id));
		
		
	}*/
	
	
	/*@GetMapping("/user/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    User findOne(@PathVariable Long id) throws UserNotFoundException{
        return repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }*/
	
	
	
	/*Drugi nacin greska se hvata na nivou  GlobalExceptioinHandler vrsi se propagacija greske navise sa 
	 * controllera na GlobalExceptionHandler + imamo CustomErrorREsponse takodje ispravan nacin rada*/
	//OVO RADI
	@GetMapping("/user/{userId}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	User findOne(@PathVariable Long userId) throws UserNotFoundException{
		return this.userService.pronadjiKorisnika(userId);
	
    }
	
	
	 //update slike usera razbijanje requesta na multipart i json deo
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_REGISTERED_USER')")
	@PutMapping(value = "/image",produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<User> updateImage(@RequestParam("file")MultipartFile file, Principal principal) {
		User user = userService.findByUsername(principal.getName());
		user.setImageUrl(fileUploadService.imageUpload(file));
		userService.save(user);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	
	
	
	
	
	
}
