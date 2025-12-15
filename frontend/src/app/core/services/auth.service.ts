import { Injectable, inject } from '@angular/core';
import { AuthService as Auth0Service } from '@auth0/auth0-angular';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private auth0Service = inject(Auth0Service);

  login(): void {
    this.auth0Service.loginWithRedirect();
  }

  logout(): void {
    this.auth0Service.logout({
      logoutParams: {
        returnTo: window.location.origin
      }
    });
  }

  getUser(): Observable<User | null | undefined> {
    return this.auth0Service.user$;
  }

  isAuthenticated(): Observable<boolean> {
    return this.auth0Service.isAuthenticated$;
  }

  isLoading(): Observable<boolean> {
    return this.auth0Service.isLoading$;
  }

  getAccessToken(): Observable<string> {
    return this.auth0Service.getAccessTokenSilently();
  }

  handleCallback(): void {
    // Auth0 handles the callback automatically
    // This method can be used for custom logic after authentication
  }
}
