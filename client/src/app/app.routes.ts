/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-04 14:21:12                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-20 14:35:51                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * Views
*/

/* Imports */
import { Routes } from '@angular/router';
/***/

/* Views */
import { LoginView } from './views/login/login.view';
import { DashboardView } from './views/dashboard/dashboard.view';
import { ProjectDashboardView } from './views/project-dashboard/project-dashboard.view';
/***/

export const routes: Routes = [
  { path: 'login', component: LoginView },
  { path: 'dashboard', component: DashboardView },
  { path: 'projects/:id', component: ProjectDashboardView },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: '**', redirectTo: '/login' }
];
