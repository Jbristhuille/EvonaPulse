/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-04 19:05:24                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-04 19:05:24                              *
 ****************************************************************************/

import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service';
import { Router } from '@angular/router';

describe('AuthService', () => {
  let service: AuthService;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        AuthService,
        { provide: Router, useValue: routerSpy }
      ]
    });
    service = TestBed.inject(AuthService);
    sessionStorage.clear(); // Clear session storage before each test
  });

  it('should return false if no token is present', () => {
    expect(service.isLogged()).toBeFalse();
  });

  it('should return true if a token is present', () => {
    sessionStorage.setItem('token', 'abc123');
    expect(service.isLogged()).toBeTrue();
  });

  it('should retrieve the token from sessionStorage', () => {
    sessionStorage.setItem('token', 'my-token');
    expect(service.getToken()).toBe('my-token');
  });

  it('should retrieve null if token is not in sessionStorage', () => {
    expect(service.getToken()).toBeNull();
  });

   it('should store the token in sessionStorage', () => {
    service.saveToken('secret-token');
    expect(sessionStorage.getItem('token')).toBe('secret-token');
  });

  it('should clear sessionStorage and navigate to login on logout', () => {
    sessionStorage.setItem('token', 'should-be-cleared');

    service.logout();

    expect(sessionStorage.getItem('token')).toBeNull();
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']);
  });
});
