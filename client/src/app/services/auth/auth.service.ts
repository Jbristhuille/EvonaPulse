/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-04 18:58:51                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-04 19:02:38                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * isLogged - Check if the user is authenticated by verifying the presence of a token.
  * getToken - Get the authentication token from session storage.
  * saveToken - Save the authentication token to session storage.
  * logout - Clear session storage and navigate to the login page.
*/

/* Imports */
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
/***/

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  constructor(private router: Router) {}

  /**
  * isLogged - Check if the user is authenticated by verifying the presence of a token.
  * @returns True if the token exists, otherwise false.
  */
  isLogged(): boolean {
    return !!this.getToken();
  }
  /***/

  /**
  * getToken - Get the authentication token from session storage.
  * @returns The token string if it exists, otherwise null.
  */
  getToken(): string | null {
    return sessionStorage.getItem('token');
  }
  /***/

  /**
  * saveToken - Save the authentication token to session storage.
  * @param token The token string to save.
  */
  saveToken(token: string): void {
    sessionStorage.setItem('token', token);
  }
  /***/

  /**
  * logout - Check if the user is authenticated by verifying the presence of a token.
  * @return True if the token exists, otherwise false.
  */
  logout(): void {
    sessionStorage.clear();
    this.router.navigate(['/login']);
  }
  /***/
}
