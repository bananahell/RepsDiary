package com.bj.repsdiary.Utils;

import android.content.Context;

import com.bj.repsdiary.R;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import java.util.Objects;

public class FirebaseHandling {

  public static String errorHandling(Context context, Exception exception) {
    String errorMessage = "";
    try {
      if (exception != null) {
        throw exception;
      }
    } catch (FirebaseAuthWeakPasswordException e) {
      return context.getString(R.string.error_small_password);
    } catch (FirebaseAuthInvalidUserException e) {
      return context.getString(R.string.error_invalid_user);
    } catch (FirebaseAuthInvalidCredentialsException e) {
      switch (Objects.requireNonNull(e.getMessage())) {
        case "The email address is badly formatted.":
          errorMessage = context.getString(R.string.error_bad_email_format);
          break;
        case "The password is invalid or the user does not have a password.":
          errorMessage = context.getString(R.string.error_invalid_password);
          break;
        default:
          errorMessage = e.getMessage();
      }
      return errorMessage;
    } catch (FirebaseAuthUserCollisionException e) {
      return context.getString(R.string.error_email_already_taken);
    } catch (Exception e) {
      return e.getMessage();
    }
    return errorMessage;
  }

}
