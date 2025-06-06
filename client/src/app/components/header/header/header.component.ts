/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-06 16:46:33                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-06 16:46:33                              *
 ****************************************************************************/

import { Component } from '@angular/core';
import { NzPageHeaderModule } from 'ng-zorro-antd/page-header';

@Component({
  selector: 'component-header',
  imports: [NzPageHeaderModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {

}
