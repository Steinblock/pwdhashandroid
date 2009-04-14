package pwdhash.android.com;

import java.security.GeneralSecurityException;

import pwdhash.android.com.sharp2java.ArgumentException;
import pwdhash.android.com.sharp2java.ArgumentNullException;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PwdHashAndroid extends Activity {
	
	private EditText editTextSiteDomain;
	private EditText editTextSitePassword;
	private Button buttonCopyClip;
	private Button buttonCopyAndOpen;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        init();
    }
    
    private void init() {
    	editTextSiteDomain = (EditText)findViewById(R.id.editTextSiteDomain);
    	editTextSitePassword = (EditText)findViewById(R.id.editTextSitePassword);
    	
    	editTextSiteDomain.setText("http://www.example.com/login.php?user=test&pass=bla");
    	editTextSitePassword.setText("geheim");
    	
        buttonCopyClip = (Button)findViewById(R.id.buttonCopyClip);
        buttonCopyAndOpen = (Button)findViewById(R.id.buttonCopyAndOpen);

        buttonCopyClip.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        		// not implemented yet
        	}
        });
        
        buttonCopyAndOpen.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {
        	    
        		// Display hash as a toast for now
				try {
					String domain = DomainExtractor.ExtractDomain(editTextSiteDomain.getText().toString());
					String pwdhash = GenerateHashedPassword(domain, editTextSitePassword.getText().toString());
					
					StringBuilder text = new StringBuilder();
					text.append("Site Domain:\t");
					text.append(domain).append("\n");
					
					text.append("PasswordHash:\t");
					text.append(pwdhash).append("\n");					
					
					Toast toast = Toast.makeText(v.getContext(), text, Toast.LENGTH_LONG);
					toast.show();
				} catch (ArgumentException e) {
					Toast toast = Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_LONG);
					toast.show();
				} catch (ArgumentNullException e) {
					Toast toast = Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_LONG);
					toast.show();
				}
        	    
        	}
        }); 
    }
    
	/// <summary>
	/// Generate a hashed password based on the PwdHash algorithm
	/// </summary>
	/// <param name="siteAddress">
	/// A <see cref="System.String"/> representing the full URL of the site
	/// you would like to use the password for.
	/// </param>
	/// <param name="sitePassword">
	/// A <see cref="System.String"/> with the base password used for this
	/// site
	/// </param>
	/// <returns>
	/// A <see cref="System.String"/> containing the unique, strong password
	/// generated using an MD5 hash and the PwdHash algorithm to ensure a
	/// strong and hard-to-guess password
	/// </returns>
	public String GenerateHashedPassword(String siteAddress, String sitePassword) throws ArgumentNullException, ArgumentException {
		
		// Validate parameters
		if (siteAddress == null)
			throw new ArgumentNullException("siteAddress",
			                                "Please specify a valid Site Address");
		if (siteAddress == "")
			throw new ArgumentException("Please specify a valid Site Address",
			                            "siteAddress");
		if (sitePassword == null)
			throw new ArgumentNullException("sitePassword",
			                                "Please specify a valid Site Password");
		if (sitePassword == "")
			throw new ArgumentException("Please specify a valid Site Password",
			                            "sitePassword");
		
		// Get the base domain from the given URI
		String domain = DomainExtractor.ExtractDomain(siteAddress);
		
		// Make sure the new domain isn't empty
		if (domain == "")
			throw new ArgumentException("Please specify a valid Site Address",
			                            "siteAddress");
		
		// Then, use it along with the site password to apply the PwdHash 
		// algorithm.
		PasswordHasher hasher = new PasswordHasher(domain, sitePassword);
		
		String hashedPassword;
		try {
			hashedPassword = hasher.GetHashedPassword();
		} catch (GeneralSecurityException e) {
			throw new ArgumentException("Generation of Passwordhash failed",
										"sitePassword");
		}
		
		return hashedPassword;
	}
}