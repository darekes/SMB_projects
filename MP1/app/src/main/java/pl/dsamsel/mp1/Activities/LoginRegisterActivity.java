package pl.dsamsel.mp1.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.dsamsel.mp1.R;
import pl.dsamsel.mp1.Services.AuthenticationService;
import pl.dsamsel.mp1.Services.GeofenceBroadcastService;
import pl.dsamsel.mp1.Services.PreferredGuiOptionsService;
import pl.dsamsel.mp1.Services.SharedPreferencesService;

public class LoginRegisterActivity extends AppCompatActivity {

    private Button signInButton;
    private Button signUpButton;
    private Button changeToSignUpButton;
    private Button changeToSignInButton;

    private TextView firstNameField;
    private TextView lastNameField;
    private TextView emailField;
    private TextView passwordField;
    private TextView confirmPasswordField;

    private AuthenticationService authenticationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_register_activity);

        authenticationService = new AuthenticationService(this);
        handlePreferredColorOptions();
        registerButtonsListeners();
        setSignInMode();
    }

    private void startGeofenceBroadcastService() {
        startService(new Intent(LoginRegisterActivity.this, GeofenceBroadcastService.class));
    }

    private void handlePreferredColorOptions() {
        firstNameField = findViewById(R.id.first_name);
        lastNameField = findViewById(R.id.last_name);
        emailField = findViewById(R.id.email_address);
        passwordField = findViewById(R.id.password);
        confirmPasswordField = findViewById(R.id.confirm_password);

        signInButton = findViewById(R.id.sign_in_button);
        changeToSignInButton = findViewById(R.id.change_to_sign_in_button);
        signUpButton = findViewById(R.id.sign_up_button);
        changeToSignUpButton = findViewById(R.id.change_to_sign_up_button);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferencesService
                .COLOR_PREFERENCES, Context.MODE_PRIVATE);
        PreferredGuiOptionsService preferredGuiOptionsService = new PreferredGuiOptionsService(sharedPreferences);
        preferredGuiOptionsService.setPreferredColorForButton(signInButton);
        preferredGuiOptionsService.setPreferredColorForButton(changeToSignInButton);
        preferredGuiOptionsService.setPreferredColorForButton(signUpButton);
        preferredGuiOptionsService.setPreferredColorForButton(changeToSignUpButton);
    }

    private void registerButtonsListeners() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                authenticationService.signIn(email, password);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameField.getText().toString();
                String lastName = lastNameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String confirmedPassword = confirmPasswordField.getText().toString();

                authenticationService.signUpAndInsertUser(firstName, lastName, email, password, confirmedPassword);
            }
        });

        changeToSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignInMode();
            }
        });

        changeToSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignUpMode();
            }
        });
    }

    public void setSignInMode() {
        clearErrorMessage();
        clearTextFields();
        firstNameField.setVisibility(View.GONE);
        lastNameField.setVisibility(View.GONE);
        confirmPasswordField.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
        signInButton.setVisibility(View.VISIBLE);
        changeToSignInButton.setVisibility(View.GONE);
        changeToSignUpButton.setVisibility(View.VISIBLE);
    }

    public void setSignUpMode() {
        clearErrorMessage();
        clearTextFields();
        firstNameField.setVisibility(View.VISIBLE);
        lastNameField.setVisibility(View.VISIBLE);
        confirmPasswordField.setVisibility(View.VISIBLE);
        signUpButton.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.GONE);
        changeToSignInButton.setVisibility(View.GONE);
        changeToSignUpButton.setVisibility(View.GONE);
    }

    public void setErrorMessage(String text) {
        TextView errorMessageField = findViewById(R.id.error_message);
        errorMessageField.setText(text);
        errorMessageField.setHighlightColor(Color.RED);
    }

    public void clearErrorMessage() {
        TextView errorMessageField = findViewById(R.id.error_message);
        errorMessageField.setText("");
    }

    public void clearTextFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}