/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-03 17:23:12                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-03 17:27:50                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
*/

/* Imports */
import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { AppComponent } from './app/app';
/***/

bootstrapApplication(AppComponent, appConfig)
  .catch((err) => console.error(err));
