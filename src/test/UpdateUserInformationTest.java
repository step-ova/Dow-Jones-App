package test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import StandAlone.UserProfile;

public class UpdateUserInformationTest {

	/**
	 * Updates User profile information
	 *
	 * @param username that the user wishes to change
	 * @param email to change
	 * @param password to change
	 * @throws NullFieldsException
	 * @throws InvalidUsernameException
	 * @throws InvalidEmailException
	 * @throws InvalidPasswordException
	 */
	public UserProfile updateUserProfile(String username, String email, String password)
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		// If all fields are not null else throw exception
		if (username != null && !username.isEmpty() && email != null && !email.isEmpty() && password != null
				&& !password.isEmpty()) {

			// Regular expression string to check for each parameter
			String usernameRegExp = "^[a-zA-Z0-9]{1,30}$";
			String emailRegExp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
			String passwordRegExp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{6,50}$";

			// Regular expression pattern, send the regex string to it
			Pattern pattern = Pattern.compile(usernameRegExp);

			// Send the pattern to the matcher to see if they match to see if
			// the username matches the regex
			Matcher matcher = pattern.matcher(username);

			// If doesn't match the regex throw exception else continue
			if (!matcher.find())
				throw new InvalidUsernameException(
						"Invalid Username. You can use Alphabetic Characters and Numbers but no special characters. Maximum length of 30 characters");

			// Username is valid now for the Email. Change objects to contain
			// email variables
			pattern = Pattern.compile(emailRegExp, Pattern.CASE_INSENSITIVE);
			matcher = pattern.matcher(email);

			if (!matcher.find())
				throw new InvalidEmailException("Invalid email address: Please provide a valid email address");

			// Email is valid now lastly for the password. Change objects to
			// contain password variables
			pattern = Pattern.compile(passwordRegExp);
			matcher = pattern.matcher(password);

			if (!matcher.find())
				throw new InvalidPasswordException(
						"Invalid Password: Password must contain at least one number, one uppercase, one lowercase, no spaces, at least 6 characters and at the most 50 characters");

		} else {
			throw new NullFieldsException("At least one field is empty");
		}

		// Return valid user profile since no exceptions were thrown
		return new UserProfile(username, password, email);

	}

	/************************************
	 * Tests************
	 ************************/

	/**
	 * Username Null Exception Test Condition Test where username = null, email
	 * not null and password not null ( F T T )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernameNull()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);
	}

	/**
	 * Email Null Exception Test Condition Test where username Not null, email =
	 * null and password Not null ( T F T )
	 */
	@Test(expected = NullFieldsException.class)
	public void emailNull()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "Boberino";
		String email = "";
		String password = "Password1";

		updateUserProfile(username, email, password);
	}

	/**
	 * Password Null Exception Test Condition Test where username Not null,
	 * email Not null and password = null ( T T F )
	 */
	@Test(expected = NullFieldsException.class)
	public void passwordNull()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "Boberino";
		String email = "Me@gmail.com";
		String password = "";

		updateUserProfile(username, email, password);
	}

	/**
	 * Username and email Null Exception Test Condition Test where username =
	 * null, email = null and password Not null ( F F T )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernameAndEmailNull()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "";
		String email = "";
		String password = "Password1";

		updateUserProfile(username, email, password);
	}

	/**
	 * Email and password Null Exception Test Condition Test where username Not
	 * null, email = null and password = null ( T F F )
	 */
	@Test(expected = NullFieldsException.class)
	public void emailAndPasswordNull()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "Boberino";
		String email = "";
		String password = "";

		updateUserProfile(username, email, password);
	}

	/**
	 * Username and password Null Exception Test Condition Test where username =
	 * null, email Not null and password = null ( F T F )
	 */
	@Test(expected = NullFieldsException.class)
	public void usernameAndPasswordNull()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "Boberino";
		String email = "";
		String password = "";

		updateUserProfile(username, email, password);
	}

	/**
	 * All parameters Null Exception Test Condition Test where username = null,
	 * email Not null and password = null ( F F F )
	 */
	@Test(expected = NullFieldsException.class)
	public void allParametersNull()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "";
		String email = "";
		String password = "";

		updateUserProfile(username, email, password);
	}

	/**
	 * Username with special Characters (InvalidUsernameException) Condition
	 * Test
	 */
	@Test(expected = InvalidUsernameException.class)
	public void userNameWithSpecialChars()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "Boberino!";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);
	}

	/**
	 * Username with = 31 characters (InvalidUsernameException) Condition Test
	 */
	@Test(expected = InvalidUsernameException.class)
	public void userNameWith30Chars()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);
	}

	/**
	 * Username with > 31 characters (InvalidUsernameException) Condition Test
	 */
	@Test(expected = InvalidUsernameException.class)
	public void userNameWithOver30Chars()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);
	}

	/**
	 * Username with 30 characters (Valid) Condition Test
	 */
	@Test
	public void userNameWith29Chars()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Username with 15 characters (Valid) Condition Test
	 *
	 */
	@Test
	public void userNameWith15Chars()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "bbbbbbbbbbbbbbb";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Username with 1 characters (Valid) Condition Test
	 *
	 */
	@Test
	public void userNameWith1Chars()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "b";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Username with numbers only (Valid) Condition Test
	 *
	 */
	@Test
	public void userNameWithNumbersOnly()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "12223";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Username with numbers before alphabetic characters (Valid) Condition Test
	 *
	 */
	@Test
	public void userNameWithNumbersBeforeAlphabetic()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "12223AAAA";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Username with alphabetic characters after numbers(Valid) Condition Test
	 *
	 */
	@Test
	public void userNameWithNumbersAfterAlphabetic()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Email without the @ symbol InvalidEmailException Condition Test
	 *
	 */
	@Test(expected = InvalidEmailException.class)
	public void emailWithoutAtSymbol()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Megmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Email without the .com InvalidEmailException Condition Test
	 *
	 */
	@Test(expected = InvalidEmailException.class)
	public void emailWithoutDotCom()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmailcom";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Email without anything before the @ InvalidEmailException Condition Test
	 *
	 */
	@Test(expected = InvalidEmailException.class)
	public void emailWithoutAnythingBeforeTheAtSymbol()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "@gmailcom";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Email with invalid special characters InvalidEmailException Condition
	 * Test
	 *
	 */
	@Test(expected = InvalidEmailException.class)
	public void email()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "me/@gmailcom";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Email with Hotmail extention (Valid)
	 */
	@Test
	public void hotmailEmail()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@hotmail.com";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Email with Concordia extention With .qc.ca (Valid)
	 */
	@Test
	public void concordiaWithRegional()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@concordia.qc.ca";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Email with Canadian extention (Valid)
	 */
	@Test
	public void concordiaWithCanadian()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.ca";
		String password = "Password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password does not contain at least one number InvalidPasswordException
	 * Condition Test
	 *
	 */
	@Test(expected = InvalidPasswordException.class)
	public void passwordDoesntContainAtLeastOneNumber() throws NullFieldsException, InvalidEmailException,
			InvalidUsernameException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.com";
		String password = "Password";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password does not contain at least one UpperCase letter
	 * InvalidPasswordException Condition Test
	 *
	 */
	@Test(expected = InvalidPasswordException.class)
	public void passwordDoesntContainAtLeastOneUpperCase() throws NullFieldsException, InvalidEmailException,
			InvalidUsernameException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.com";
		String password = "password1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password does not contain at least one LowerCase letter
	 * InvalidPasswordException Condition Test
	 *
	 */
	@Test(expected = InvalidPasswordException.class)
	public void passwordDoesntContainAtLeastOneLowerCase() throws NullFieldsException, InvalidEmailException,
			InvalidUsernameException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.com";
		String password = "PASSWORD1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password contains a space InvalidPasswordException Condition Test
	 *
	 */
	@Test(expected = InvalidPasswordException.class)
	public void passwordContainsASpace() throws NullFieldsException, InvalidEmailException, InvalidUsernameException,
			InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.com";
		String password = "Pass word1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password does not contain at least 6 characters InvalidPasswordException
	 * Condition Test
	 *
	 */
	@Test(expected = InvalidPasswordException.class)
	public void passwordLessThan6Chars() throws NullFieldsException, InvalidEmailException, InvalidUsernameException,
			InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.com";
		String password = "Pass1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password contains 1 character InvalidPasswordException Condition Test
	 *
	 */
	@Test(expected = InvalidPasswordException.class)
	public void password1Char() throws NullFieldsException, InvalidEmailException, InvalidUsernameException,
			InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.com";
		String password = "P";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password contains more than 50 characters InvalidPasswordException
	 * Condition Test
	 *
	 */
	@Test(expected = InvalidPasswordException.class)
	public void passwordMoreThan50Chars() throws NullFieldsException, InvalidEmailException, InvalidUsernameException,
			InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.com";
		String password = "Pass1aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password numbers only InvalidPasswordException Condition Test
	 *
	 */
	@Test(expected = InvalidPasswordException.class)
	public void passwordWithNumbersOnly() throws NullFieldsException, InvalidEmailException, InvalidUsernameException,
			InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.com";
		String password = "19298382";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password with 1 upper, at least 1 lower,1 number and 6 characters (Valid)
	 */
	@Test
	public void passwordMinimumValid()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.ca";
		String password = "Helloo1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password with multiple upper, at least 1 lower,1 number and 6 characters
	 * (Valid)
	 */
	@Test
	public void passwordMultipleUpper()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.ca";
		String password = "HELLoo1";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password with 1 upper, at least 1 lower,multiple number and 6 characters
	 * (Valid)
	 */
	@Test
	public void passwordMultipleNumbers()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.ca";
		String password = "Helloo1232";

		updateUserProfile(username, email, password);

	}

	/**
	 * Password with 50 characters,1 upper, at least 1 lower,1 number (Valid)
	 */
	@Test
	public void password50Chars()
			throws NullFieldsException, InvalidEmailException, InvalidUsernameException, InvalidPasswordException {

		String username = "AAAA1233";
		String email = "Me@gmail.ca";
		String password = "Hel0yyyyyyttttttttttvvvvvvvvvvppppppppppaaaaaaaaaa";

		updateUserProfile(username, email, password);

	}

	/**
	 * User Profile Successfully returned a profile Branch Test
	 */
	@Test
	public void userProfileSuccessFullReturn()
			throws NullFieldsException, InvalidUsernameException, InvalidEmailException, InvalidPasswordException {

		String username = "Garry";
		String email = "Me@gmail.com";
		String password = "Password1";

		UserProfile profile = updateUserProfile(username, email, password);

		assertEquals(username, profile.getUsername());
		assertEquals(email, profile.getEmail());
	}

	/*
	 * ************************* Exceptions *************************
	 */
	public class NullFieldsException extends Exception {

		public NullFieldsException(String message) {
			super(message);
		}
	}

	public class InvalidUsernameException extends Exception {

		public InvalidUsernameException(String message) {
			super(message);
		}
	}

	public class InvalidEmailException extends Exception {

		public InvalidEmailException(String message) {
			super(message);
		}
	}

	public class InvalidPasswordException extends Exception {

		public InvalidPasswordException(String message) {
			super(message);
		}
	}

}