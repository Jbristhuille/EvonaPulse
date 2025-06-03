/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-03 17:26:48                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-03 17:27:13                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
*/

/* Imports */
import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app';
/***/

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
    }).compileComponents();
  });
});
