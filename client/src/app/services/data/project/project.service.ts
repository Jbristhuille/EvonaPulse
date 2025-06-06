/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-06 15:35:50                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-06 15:42:56                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * Services
  * Interfaces
  * getAll - Get all projects
*/

/* Imports */
import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment as env } from '../../../../environments/environment';
/***/

/* Services */
import { AuthService } from '../../auth/auth.service';
/***/

/* Interfaces */
import { IProject } from '../../../interfaces/projects';
import { Observable } from 'rxjs';
/***/

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private httpClient = inject(HttpClient);

  constructor(private authService: AuthService) {}

  /**
   * getAll - Get all projects
   * @returns - Array of projects
   */
  public getAll(): Observable<IProject[]> {
    const token = this.authService.getToken();

    return this.httpClient.get<IProject[]>(`${env.API_URL}/api/projects`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  /***/

  /**
   * getById - Get project by idd
   * @returns - project
   */
  public getById(id: string): Observable<IProject> {
    const token = this.authService.getToken();

    return this.httpClient.get<IProject>(`${env.API_URL}/api/projects${id}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  /***/
}
