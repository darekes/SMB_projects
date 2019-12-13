package pl.dsamsel.mp1.Services;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.concurrent.Executor;

import pl.dsamsel.mp1.Activities.LoginRegisterActivity;
import pl.dsamsel.mp1.Activities.MainActivity;
import pl.dsamsel.mp1.Models.User;

public class AuthenticationService {

    private FirebaseAuth firebaseAuth;
    private LoginRegisterActivity context;

    public AuthenticationService(LoginRegisterActivity context) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
    }

    public void signIn(String email, String password) {
        List<String> validationErrors = Lists.newArrayList();
        validateEmail(email, validationErrors);
        validatePassword(password, validationErrors);

        if (!validationErrors.isEmpty()) {
            context.setErrorMessage(String.join(", ", validationErrors));
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && firebaseAuth.getCurrentUser() != null) {
                            navigateToMainActivity();
                        } else {
                            context.clearTextFields();
                            String error = "Authentication failed! ";
                            if (task.getException() != null && !Strings.isNullOrEmpty(task.getException().getMessage())) {
                                error += task.getException().getMessage();
                            }
                            context.setErrorMessage(error);
                        }
                    }
                });
    }

    public void signUpAndInsertUser(final String firstName, final String lastName, final String email, String password, String confirmedPassword) {
        List<String> validationErrors = Lists.newArrayList();
        validateFirstName(firstName, validationErrors);
        validateLastName(lastName, validationErrors);
        validateEmail(email, validationErrors);
        validatePassword(password, validationErrors);
        validatePasswordsIdentity(password, confirmedPassword, validationErrors);

        if (!validationErrors.isEmpty()) {
            String errorMessage = String.join(", ", validationErrors);
            context.setErrorMessage(errorMessage);
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            context.clearTextFields();
                            insertUser(firstName, lastName, email);
                            context.setSignInMode();
                        } else {
                            context.clearTextFields();
                            String error = "Account creation failed! ";
                            if (task.getException() != null && !Strings.isNullOrEmpty(task.getException().getMessage())) {
                                error = task.getException().getMessage();
                            }
                            context.setErrorMessage(error);
                        }
                    }
                });
    }

    private void validateFirstName(String firstName, List<String> validationErrors) {
        if (Strings.isNullOrEmpty(firstName)) {
            validationErrors.add("First name can't be empty!");
        }
    }

    private void validateLastName(String lastName, List<String> validationErrors) {
        if (Strings.isNullOrEmpty(lastName)) {
            validationErrors.add("Last name can't be empty!");
        }
    }

    private void validateEmail(String email, List<String> validationErrors) {
        if (Strings.isNullOrEmpty(email)) {
            validationErrors.add("Email can't be empty!");
            return;
        }

        if (email.length() <= 4) {
            validationErrors.add("Email should be longer than 3!");
        }
    }

    private void validatePassword(String password, List<String> validationErrors) {
        if (Strings.isNullOrEmpty(password)) {
            validationErrors.add("Password can't be empty!");
            return;
        }

        if (password.length() <= 5) {
            validationErrors.add("Password should be longer than 4!");
        }
    }

    private void validatePasswordsIdentity(String password, String confirmedPassword, List<String> validationErrors) {
        if (!password.equals(confirmedPassword)) {
            validationErrors.add("Passwords not identity!");
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    private void insertUser(String firstName, String lastName, String email) {
        User user = new User(firstName, lastName, email);
        FirestoreDatabaseService databaseService = new FirestoreDatabaseService();
        databaseService.insertUser(user);
    }
}
