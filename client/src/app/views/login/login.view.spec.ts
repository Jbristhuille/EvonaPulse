/*****************************************************************************
 * @Author                : Jbristhuille<jbristhuille@gmail.com>             *
 * @CreatedDate           : 2025-06-04 15:12:41                              *
 * @LastEditors           : Jbristhuille<jbristhuille@gmail.com>             *
 * @LastEditDate          : 2025-06-04 18:54:41                              *
 ****************************************************************************/

/* SUMMARY
  * Imports
*/
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { LoginView } from './login.view';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';
import { provideHttpClient } from '@angular/common/http';
/***/

describe('LoginView', () => {
  let component: LoginView;
  let fixture: ComponentFixture<LoginView>;
  let httpMock: HttpTestingController;
  let messageServiceSpy: jasmine.SpyObj<NzMessageService>;
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    messageServiceSpy = jasmine.createSpyObj('NzMessageService', ['success', 'error']);
    routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [LoginView],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: NzMessageService, useValue: messageServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginView);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  it('should send login request and redirect on success', fakeAsync(() => {
    component.loginForm.setValue({
      email: 'test@example.com',
      password: 'password123'
    });

    expect(component.loginForm.valid).toBeTrue();
    component.submitForm();

    httpMock.match(req => req.url.includes('assets/')); // Ignore asset requests

    const req = httpMock.expectOne(r => r.url.includes("/api/auth/login"));
    expect(req.request.method).toBe('POST');
    req.flush({ token: 'abc123' });

    expect(messageServiceSpy.success).toHaveBeenCalledWith('Login successful!');
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/dashboard']);

    httpMock.verify();
  }));

  it('should show error if login fails', fakeAsync(() => {
    component.loginForm.setValue({ email: 'test@example.com', password: 'wrong1234567' });

    expect(component .loginForm.valid).toBeTrue();
    component.submitForm();

    httpMock.match(req => req.url.includes('assets/')); // Ignore asset requests

    const req = httpMock.expectOne(r => r.url.includes('/api/auth/login'));
    req.flush({}, { status: 401, statusText: 'Unauthorized' });

    expect(messageServiceSpy.error).toHaveBeenCalled();

    httpMock.verify();
  }));

  it('should show error on invalid form submission', () => {
    component.loginForm.setValue({ email: '', password: '' });
    component.submitForm();

    expect(component.loginForm.invalid).toBeTrue();
    Object.values(component.loginForm.controls).forEach(control => {
      expect(control.dirty).toBeTrue();
      expect(control.valid).toBeFalse();
    });
  });
});
