/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-04 19:28:51                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-04 19:31:15                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
*/

/* Imports */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app';
import { RouterTestingModule } from '@angular/router/testing';
import { NavigationEnd, Router } from '@angular/router';
import { Subject } from 'rxjs';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { CommonModule } from '@angular/common';
/***/

describe('AppComponent', () => {
  let fixture: ComponentFixture<AppComponent>;
  let component: AppComponent;
  let router: Router;
  let routerEvents$: Subject<any>;

  beforeEach(async () => {
    routerEvents$ = new Subject();

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        SidebarComponent,
        CommonModule,
        AppComponent
      ],
      providers: [
        {
          provide: Router,
          useValue: {
            events: routerEvents$.asObservable()
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
  });

  it('should hide sidebar on /login', () => {
    routerEvents$.next(new NavigationEnd(1, '/login', '/login'));
    expect(component.isMenuVisible).toBeFalse();
  });

  it('should hide sidebar on /signup', () => {
    routerEvents$.next(new NavigationEnd(1, '/signup', '/signup'));
    expect(component.isMenuVisible).toBeFalse();
  });

  it('should show sidebar on /dashboard', () => {
    routerEvents$.next(new NavigationEnd(1, '/dashboard', '/dashboard'));
    expect(component.isMenuVisible).toBeTrue();
  });
});
