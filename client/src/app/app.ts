/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-03 17:25:33                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-04 19:32:55                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
  * Constructor - Initializes the component and sets up the router event listener
*/

/* Imports */
import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs';
/***/

/* Components */
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { HeaderComponent } from './components/header/header/header.component';
/***/

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    CommonModule,
    SidebarComponent,
    HeaderComponent
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class AppComponent {
  private noMenuView = ['login', 'signup'];
  public isMenuVisible = true;

  /**
  * Constructor - Initializes the component and sets up the router event listener
  */
  constructor(private router: Router) {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe(event => {
        const currentUrl = (event as NavigationEnd).urlAfterRedirects;
        const base = currentUrl.split('/')[1];
        this.isMenuVisible = !this.noMenuView.includes(base);
      });
  }
  /***/
}
