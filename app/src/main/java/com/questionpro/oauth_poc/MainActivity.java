package com.questionpro.oauth_poc;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton btnGoogleSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EdgeToEdge.enable(this);
        btnGoogleSignIn = findViewById(R.id.btnGoogleSignIn);

        // 1. Configure sign-in to request the user’s ID, email, and basic profile
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("391922013901-sribddnvg85cgl8mdkge4s9bg6vcapoe.apps.googleusercontent.com")
                // If you want an ID token, add .requestIdToken("YOUR_WEB_CLIENT_ID")
                .build();

        // 2. Build a GoogleSignInClient with the options
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // 3. Set onClick listener
        btnGoogleSignIn.setOnClickListener(view -> signIn());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
        Log.d("sign", "Inside try");
            if (account != null) {
                String displayName = account.getDisplayName();
                String email = account.getEmail();
                Uri profilePic = account.getPhotoUrl();

                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("name", displayName);
                intent.putExtra("email", email);
                if (profilePic != null) {
                    intent.putExtra("photoUrl", profilePic.toString());
                }
                startActivity(intent);
                finish();
            }
        } catch (ApiException e) {
            // Handle failure
            Log.w("SignIn", "signInResult:failed code=" + e.getStatusCode());
        }
    }
}

