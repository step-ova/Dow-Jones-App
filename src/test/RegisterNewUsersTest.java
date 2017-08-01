package test;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import StandAlone.UserProfile;

public class RegisterNewUsersTest {

	List<String> alreadyTakenUsernames = Arrays.asList("Admin", "Bob", "Jefrey", "Tony180", "1247John", "henry1");

	/**
	 * Registers a new User if the username doesn't already exist, both password
	 * fields match and no null fields.
	 *
	 * @param username
	 *            to create
	 * @param email
	 * @param password1
	 *            password1 must match password2
	 * @param password2
	 * @throws UsernameUnavailableException
	 * @throws PasswordsDontMatchException
	 * @throws NullFieldsException
	 */
	public UserProfile registerNewUser(String username, String email, String password1, String password2)
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		// If all fields are not null else throw exception
		if (username != null && !username.isEmpty() && email != null && !email.isEmpty() && password1 != null
				&& !password1.isEmpty() && password2 != null && !password2.isEmpty()) {

			// Check if Username is available
			if (!alreadyTakenUsernames.contains(username)) {
				// Username is available
			} else {
				throw new UsernameUnavailableException(
						"Sorry, the Username you have entered is already taken, please chose another");
			}

			// Check if both password fields match else throw exception
			if (password1.equals(password2)) {
				// Both passwords match
			} else {
				throw new PasswordsDontMatchException("The passwords you have entered don't match, please try again");
			}

		} else {
			throw new NullFieldsException("At least one field is empty");
		}

		// Return valid user profile since no exceptions were thrown
		return new UserProfile(username, password1, email);

	}

	/**
	 * Username Already Taken Test (InvalidUsernameException) Branch Test
	 */
	@Test(expected = UsernameUnavailableException.class)
	public void usernameAlreadyTaken()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Admin";
		String email = "Me@gmail.com";
		String password1 = "Password1";
		String password2 = "Password1";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * Username Available Branch Test
	 */
	@Test
	public void usernameAvailable()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Goby";
		String email = "Me@gmail.com";
		String password1 = "Password1";
		String password2 = "Password1";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * Passwords don't match (PasswordsDontMatchException) Branch Test
	 */
	@Test(expected = PasswordsDontMatchException.class)
	public void passwordsDontMatch()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Lucy";
		String email = "Me@gmail.com";
		String password1 = "Password1";
		String password2 = "Password";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * Passwords match Branch Test
	 */
	@Test
	public void passwordsMatch() throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Lucy2";
		String email = "Me@gmail.com";
		String password1 = "Max1231";
		String password2 = "Max1231";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * User Profile Successfully returned a profile Branch Test
	 */
	@Test
	public void userProfileSuccessFullReturn()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Garry";
		String email = "Me@gmail.com";
		String password1 = "Password1";
		String password2 = "Password1";

		UserProfile profile = registerNewUser(username, email, password1, password2);

		assertEquals(username, profile.getUsername());
		assertEquals(email, profile.getEmail());
	}

	/**
	 * Username Null Exception Test Condition Test where username = null, email
	 * not null, password1 not null, password2 not null ( F T T T )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernameNull() throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "";
		String email = "Me@gmail.com";
		String password1 = "Max1231";
		String password2 = "Max1231";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * Email Null Exception Test Condition Test where username Not null, email =
	 * null, password1 not null, password2 not null ( T F T T )
	 */
	@Test(expected = NullFieldsException.class)
	public void emailNull() throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Bob33";
		String email = "";
		String password1 = "Max1231";
		String password2 = "Max1231";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * Password1 Null Exception Test Condition Test where username Not null,
	 * email Not null, password1 = null, password2 not null ( T T F T )
	 */
	@Test(expected = NullFieldsException.class)
	public void password1Null() throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Bob33";
		String email = "Dude@hotmail.com";
		String password1 = "";
		String password2 = "Max1231";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * Password2 Null Exception Test Condition Test where username Not null,
	 * email Not null, password1 Not null, password2 = null ( T T T F )
	 */
	@Test(expected = NullFieldsException.class)
	public void password2Null() throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Bob33";
		String email = "Dude@hotmail.com";
		String password1 = "Max1231";
		String password2 = "";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * username and email Null Exception Test Condition Test where username =
	 * null, email = null, password1 Not null, password2 Not null ( F F T T )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernameAndEmailNull()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "";
		String email = "";
		String password1 = "Max1231";
		String password2 = "Max1231";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * username and password1 Null Exception Test Condition Test where username
	 * = null, email Not null, password1 = null, password2 Not null ( F T F T )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernameAndPassword1Null()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "";
		String email = "hans@hotmail.com";
		String password1 = "";
		String password2 = "Max1231";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * username and password2 Null Exception Test Condition Test where username
	 * = null, email Not null, password1 Not null, password2 = null ( F T T F )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernameAndPassword2Null()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "";
		String email = "hans@hotmail.com";
		String password1 = "Max1231";
		String password2 = "";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * email and password1 Null Exception Test Condition Test where username Not
	 * null, email = null, password1 = null, password2 Not null ( T F F T )
	 */
	@Test(expected = NullFieldsException.class)
	public void emailAndPassword1Null()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Garry1";
		String email = "";
		String password1 = "";
		String password2 = "Max1231";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * email and password2 Null Exception Test Condition Test where username Not
	 * null, email = null, password1 Not null, password2 = null ( T F T F )
	 */
	@Test(expected = NullFieldsException.class)
	public void emailAndPassword2Null()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Hello1";
		String email = "";
		String password1 = "Max1231";
		String password2 = "";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * password1 and password2 Null Exception Test Condition Test where username
	 * Not null, email Not null, password1 = null, password2 = null ( T T F F )
	 */
	@Test(expected = NullFieldsException.class)
	public void password1AndPassword2Null()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Hello1";
		String email = "Hello1@gmail.com";
		String password1 = "";
		String password2 = "";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * username, email and password1 Null Exception Test Condition Test where
	 * username = null, email = null, password1 = null, password2 Not null ( F F
	 * F T )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernameEmailAndpassword1Null()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "";
		String email = "";
		String password1 = "";
		String password2 = "Hello1223";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * username, email and password2 Null Exception Test Condition Test where
	 * username = null, email = null, password1 Not null, password2 = null ( F F
	 * T F )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernameEmailAndpassword2Null()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "";
		String email = "";
		String password1 = "Hello1223";
		String password2 = "";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * username, password1 and password2 Null Exception Test Condition Test
	 * where username = null, email Not null, password1 = null, password2 = null
	 * ( F T F F )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernamePassword1Andpassword2Null()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "";
		String email = "Hello1223@Yahoo.com";
		String password1 = "";
		String password2 = "";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * email, password1 and password2 Null Exception Test Condition Test where
	 * username Not null, email = null, password1 = null, password2 = null ( T F
	 * F F )
	 */
	@Test(expected = NullFieldsException.class)
	public void emailPassword1Andpassword2Null()
			throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "Hello1223";
		String email = "";
		String password1 = "";
		String password2 = "";

		registerNewUser(username, email, password1, password2);
	}

	/**
	 * All Null Exception Test Condition Test where username = null, email =
	 * null, password1 = null, password2 = null ( F F F F )
	 */
	@Test(expected = NullFieldsException.class)
	public void allNull() throws UsernameUnavailableException, PasswordsDontMatchException, NullFieldsException {

		String username = "";
		String email = "";
		String password1 = "";
		String password2 = "";

		registerNewUser(username, email, password1, password2);
	}

	/*********** Exceptions ************/

	public class UsernameUnavailableException extends Exception {

		public UsernameUnavailableException(String message) {
			super(message);
		}
	}

	public class PasswordsDontMatchException extends Exception {

		public PasswordsDontMatchException(String message) {
			super(message);
		}
	}

	public class NullFieldsException extends Exception {

		public NullFieldsException(String message) {
			super(message);
		}
	}

}