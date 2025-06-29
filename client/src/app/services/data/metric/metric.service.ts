/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-29 12:49:30                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-29 12:55:09                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * Services
  * Interfaces
  * getMetrics - Fetches metrics for a project
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
import { IMetric } from '../../../interfaces/metrics';
import { Observable } from 'rxjs';
/***/

@Injectable({
  providedIn: 'root'
})
export class MetricService {
  private httpClient = inject(HttpClient);

  constructor(private authService: AuthService) {
  }

  /**
  * getMetrics - Fetches metrics for a project
  * @param projectId - The ID of the project to fetch metrics for
  * @returns An observable containing the metrics data
  */
  public getMetrics(projectId: string): Observable<IMetric[]> {
    const token = this.authService.getToken();

    return this.httpClient.get<IMetric[]>(`${env.API_URL}/api/projects/${projectId}/metrics`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }
  /***/
}
