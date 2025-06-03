/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-03 17:25:33                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-03 17:41:39                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
*/

/* Imports */
import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
/***/

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrls: ['./app.scss']
})
export class AppComponent {
}
