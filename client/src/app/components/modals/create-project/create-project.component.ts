/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-10 16:50:06                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-20 14:19:33                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
*/

/* Imports */
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NzInputModule } from 'ng-zorro-antd/input';
/***/

@Component({
  selector: 'component-create-project',
  imports: [
    NzInputModule,
    FormsModule
  ],
  templateUrl: './create-project.component.html',
  styleUrl: './create-project.component.scss'
})
export class CreateProjectComponent {
  public name: string = "";
  public onError: "error" | "warning" | "" = "";
}
